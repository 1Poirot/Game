package com.game.network;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;

public class GameServer {
    private static final Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private static final Map<String, PrintWriter> playerWriters = new ConcurrentHashMap<>();
    private static final Map<String, String> connectedIPs = new ConcurrentHashMap<>();

    private static final int MAX_PLAYERS = 3;
    private static volatile boolean gameStarted = false;
    private static volatile boolean roomCreated = false;
    private static final Set<String> finishedPlayers = ConcurrentHashMap.newKeySet();
    private static volatile String currentHostName = "";

    // ✅ เพิ่มส่วน UI Controller
    private static JLabel lbStatus;
    private static JLabel lbPlayerCount;

    public static void main(String[] args) throws Exception {
        String ipAddress = "Unknown";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            ipAddress = "127.0.0.1";
        }

        final String finalIP = ipAddress;

        // ✅ สร้างหน้าต่างควบคุม Server
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Love Game - Server Controller");
            frame.setSize(450, 280);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setResizable(false);

            JPanel mainPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            mainPanel.setBackground(new Color(245, 245, 250));

            lbStatus = new JLabel("● สถานะเซิร์ฟเวอร์: ONLINE", SwingConstants.LEFT);
            lbStatus.setForeground(new Color(34, 139, 34));
            lbStatus.setFont(new Font("Tahoma", Font.BOLD, 16));

            JLabel lbIP = new JLabel("Server IP: " + finalIP, SwingConstants.LEFT);
            lbIP.setFont(new Font("Tahoma", Font.BOLD, 18));

            JLabel lbPort = new JLabel("Port: 9090", SwingConstants.LEFT);
            lbPort.setFont(new Font("Tahoma", Font.PLAIN, 14));

            lbPlayerCount = new JLabel("ผู้เล่นในห้อง: 0 / " + MAX_PLAYERS, SwingConstants.LEFT);
            lbPlayerCount.setFont(new Font("Tahoma", Font.PLAIN, 14));

            mainPanel.add(lbStatus);
            mainPanel.add(lbIP);
            mainPanel.add(lbPort);
            mainPanel.add(lbPlayerCount);

            JButton btnStop = new JButton("ปิดเซิร์ฟเวอร์ (STOP SERVER)");
            btnStop.setFont(new Font("Tahoma", Font.BOLD, 14));
            btnStop.setBackground(new Color(220, 20, 60));
            btnStop.setForeground(Color.WHITE);
            btnStop.setFocusPainted(false);
            btnStop.setPreferredSize(new Dimension(0, 50));
            btnStop.addActionListener(e -> System.exit(0));

            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(btnStop, BorderLayout.SOUTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // --- ส่วนการทำงานของ Server (เดิม) ---
        try (ServerSocket serverSocket = new ServerSocket(9090)) {
            System.out.println("========== ✅ เซิร์ฟเวอร์ ศึกชิงนาง ONLINE ==========");
            System.out.println(">> IP ของเครื่องนี้: " + finalIP + " <<");

            while (true) {
                Socket socket = serverSocket.accept();
                String clientIP = socket.getInetAddress().getHostAddress();
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                boolean isLocal = clientIP.equals("127.0.0.1") || clientIP.equals("0:0:0:0:0:0:0:1")
                        || clientIP.equals(finalIP);

                if (!roomCreated && !isLocal) {
                    out.println("REJECT:ขออภัยครับ เจ้าของห้องยังไม่ได้ทำการ 'สร้างห้อง' กรุณารอสักครู่...");
                    socket.close();
                    continue;
                }

                if (connectedIPs.containsKey(clientIP)) {
                    out.println("REJECT:เครื่องของคุณ (IP: " + clientIP + ") อยู่ในห้องแข่งแล้ว ห้ามเข้าซ้ำ!");
                    socket.close();
                    continue;
                }

                if (playerWriters.size() >= MAX_PLAYERS) {
                    out.println("REJECT:ขออภัยครับ ห้องเต็มแล้ว (" + MAX_PLAYERS + "/" + MAX_PLAYERS + ")");
                    socket.close();
                    continue;
                }

                new Thread(new ClientHandler(socket, out, clientIP, isLocal)).start();
            }
        }
    }

    private static void updatePlayerList() {
        broadcast("PLAYER_LIST:" + String.join(",", playerWriters.keySet()) + "|" + currentHostName);
        // ✅ อัปเดตจำนวนผู้เล่นบน UI
        SwingUtilities.invokeLater(() -> {
            if (lbPlayerCount != null) {
                lbPlayerCount.setText("👥 ผู้เล่นในห้อง: " + playerWriters.size() + " / " + MAX_PLAYERS);
            }
        });
    }

    private static void broadcast(String msg) {
        for (PrintWriter writer : playerWriters.values()) {
            writer.println(msg);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final PrintWriter out;
        private final String clientIP;
        private final boolean isLocal;
        private String playerName;

        public ClientHandler(Socket socket, PrintWriter out, String clientIP, boolean isLocal) {
            this.socket = socket;
            this.out = out;
            this.clientIP = clientIP;
            this.isLocal = isLocal;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                playerName = in.readLine();

                if (playerName == null || playerName.trim().isEmpty()) {
                    socket.close();
                    return;
                }
                playerName = playerName.trim();

                if (playerWriters.containsKey(playerName)) {
                    out.println("REJECT:ชื่อนี้มีคนใช้แล้วในห้องแข่ง!");
                    socket.close();
                    return;
                }

                synchronized (GameServer.class) {
                    if (isLocal && !roomCreated) {
                        roomCreated = true;
                        currentHostName = playerName;
                        System.out.println(">>> [ROOM CREATED]: " + playerName + " เปิดห้องแข่งสำเร็จ!");
                    } else if (currentHostName.isEmpty()) {
                        currentHostName = playerName;
                    }
                }

                connectedIPs.put(clientIP, playerName);
                playerWriters.put(playerName, out);
                playerScores.put(playerName, 0);

                System.out.println(">>> [CONNECTED]: " + playerName + " | IP: " + clientIP);
                broadcast("SYSTEM:" + playerName + " เข้าสู่สนามรักแล้ว"
                        + (playerName.equals(currentHostName) ? " (หัวหน้าห้อง)" : ""));
                updatePlayerList();

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equals("START_GAME")) {
                        if (!playerName.equals(currentHostName)) {
                            out.println("SYSTEM:คุณไม่ใช่ Host ไม่มีสิทธิ์เริ่มเกม!");
                            continue;
                        }
                        if (gameStarted)
                            continue;

                        gameStarted = true;
                        finishedPlayers.clear();
                        playerScores.replaceAll((k, v) -> 0);
                        broadcast("START");
                        System.out.println(">>> [GAME START] โดย " + playerName);

                    } else if (msg.startsWith("FINISH:")) {
                        try {
                            int score = Integer.parseInt(msg.split(":")[1]);
                            playerScores.put(playerName, score);
                            finishedPlayers.add(playerName);
                            System.out.println(">>> [SCORE]: " + playerName + " ได้ " + score);

                            if (gameStarted && finishedPlayers.size() >= playerWriters.size()) {
                                broadcast("FINAL_SCORE");
                                playerScores.forEach((name, s) -> broadcast("SCORE:" + name + ":" + s));
                                gameStarted = false;
                                System.out.println("--- [ROUND FINISHED] ---");
                            }
                        } catch (Exception e) {
                            System.err.println("คะแนนส่งมาผิดรูปแบบ: " + msg);
                        }
                    }
                }
            } catch (Exception e) {
                // หลุด
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            if (playerName != null) {
                playerScores.remove(playerName);
                playerWriters.remove(playerName);
                connectedIPs.remove(clientIP);

                System.out.println("<<< [LEFT]: " + playerName + " (IP: " + clientIP + " คืนสิทธิ์แล้ว)");

                if (playerName.equals(currentHostName)) {
                    System.out.println(">>> [ROOM CLOSED]: Host ออกจากระบบ ห้องถูกยกเลิก");
                    broadcast("REJECT:เจ้าของห้องออกจากเกม ห้องแข่งถูกปิดลงแล้ว");
                    roomCreated = false;
                    currentHostName = "";
                    gameStarted = false;
                    playerWriters.values().forEach(pw -> pw.close());
                    playerWriters.clear();
                    connectedIPs.clear();
                } else {
                    broadcast("SYSTEM:" + playerName + " ออกจากห้อง");
                    updatePlayerList();
                }

                if (playerWriters.isEmpty()) {
                    gameStarted = false;
                    roomCreated = false;
                    currentHostName = "";
                }

                // ✅ อัปเดต UI เมื่อมีคนออก
                updatePlayerList();
            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}