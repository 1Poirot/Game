package com.game.network;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private static final Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private static final Map<String, PrintWriter> playerWriters = new ConcurrentHashMap<>();
    private static final Map<String, String> connectedIPs = new ConcurrentHashMap<>();

    private static final int MAX_PLAYERS = 3;
    private static volatile boolean gameStarted = false;
    private static volatile boolean roomCreated = false; // ✅ สถานะการเปิดห้อง
    private static final Set<String> finishedPlayers = ConcurrentHashMap.newKeySet();
    private static volatile String currentHostName = "";

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(9090)) {
            System.out.println("========== ✅ เซิร์ฟเวอร์ ศึกชิงนาง ONLINE (Room Controller v3) ==========");
            System.out.println(">> สถานะ: 1 IP ต่อ 1 ห้อง | ระบบ: บังคับสร้างห้องก่อนเข้าเล่น <<");
            System.out.println(">> IP ของเครื่องนี้: " + InetAddress.getLocalHost().getHostAddress() + " <<");
            System.out.println("===============================================================");

            while (true) {
                Socket socket = serverSocket.accept();
                String clientIP = socket.getInetAddress().getHostAddress();
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                // 🛑 ตรวจสอบความเป็นเจ้าของเครื่อง (Localhost/Host)
                boolean isLocal = clientIP.equals("127.0.0.1") || clientIP.equals("0:0:0:0:0:0:0:1");

                // กรณี Host ยังไม่สร้างห้อง
                if (!roomCreated && !isLocal) {
                    out.println("REJECT:ขออภัยครับ เจ้าของห้องยังไม่ได้ทำการ 'สร้างห้อง' กรุณารอสักครู่...");
                    socket.close();
                    continue;
                }

                // กรณี IP ซ้ำ
                if (connectedIPs.containsKey(clientIP)) {
                    out.println("REJECT:เครื่องของคุณ (IP: " + clientIP + ") อยู่ในห้องแข่งแล้ว ห้ามเข้าซ้ำ!");
                    socket.close();
                    continue;
                }

                // กรณีห้องเต็ม
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

                // 🛑 ตรวจสอบชื่อซ้ำ
                if (playerWriters.containsKey(playerName)) {
                    out.println("REJECT:ชื่อนี้มีคนใช้แล้วในห้องแข่ง!");
                    socket.close();
                    return;
                }

                // ✅ จัดการสถานะห้องและการเป็น Host
                synchronized (GameServer.class) {
                    if (isLocal && !roomCreated) {
                        roomCreated = true;
                        currentHostName = playerName;
                        System.out.println(">>> [ROOM CREATED]: " + playerName + " เปิดห้องแข่งสำเร็จ!");
                    } else if (currentHostName.isEmpty()) {
                        currentHostName = playerName; // กรณีฉุกเฉิน
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

                // ✅ ลอจิกสำคัญ: ถ้า Host ออก ให้ปิดห้องทันที
                if (playerName.equals(currentHostName)) {
                    System.out.println(">>> [ROOM CLOSED]: Host ออกจากระบบ ห้องถูกยกเลิก");
                    broadcast("REJECT:เจ้าของห้องออกจากเกม ห้องแข่งถูกปิดลงแล้ว");
                    roomCreated = false;
                    currentHostName = "";
                    gameStarted = false;
                    // เคลียร์ทุกคนที่เหลือ
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
            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}