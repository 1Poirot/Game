package com.game.network;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private static final Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private static final Map<String, PrintWriter> playerWriters = new ConcurrentHashMap<>();

    // ✅ หัวใจหลัก: 1 IP ต่อ 1 ห้อง (IP -> PlayerName)
    private static final Map<String, String> connectedIPs = new ConcurrentHashMap<>();

    private static final int MAX_PLAYERS = 3;
    private static volatile boolean gameStarted = false;
    private static final Set<String> finishedPlayers = ConcurrentHashMap.newKeySet();
    private static volatile String currentHostName = "";

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(9090)) {
            System.out.println("========== ✅ เซิร์ฟเวอร์ ศึกชิงนาง ONLINE (Secure Mode v2) ==========");
            System.out.println(">> สถานะ: 1 IP ต่อ 1 ห้อง (Strict Mode) <<");
            System.out.println(">> IP ของเครื่องนี้: " + InetAddress.getLocalHost().getHostAddress() + " <<");
            System.out.println("===============================================================");

            while (true) {
                Socket socket = serverSocket.accept();
                // ดึง IP ของผู้ที่พยายามเชื่อมต่อเข้ามา
                String clientIP = socket.getInetAddress().getHostAddress();

                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                // 🛑 1. ตรวจสอบเงื่อนไข 1 IP ต่อ 1 การเชื่อมต่อในห้อง
                if (connectedIPs.containsKey(clientIP)) {
                    String existingPlayer = connectedIPs.get(clientIP);
                    System.err.println("!!! [DENIED]: IP " + clientIP + " พยายามเข้าซ้ำ (มีชื่อ " + existingPlayer
                            + " อยู่ในห้องแล้ว)");
                    out.println("REJECT:เครื่องของคุณ (IP: " + clientIP + ") อยู่ในห้องแข่งแล้ว ห้ามเข้าซ้ำ!");
                    socket.close();
                    continue;
                }

                // 🛑 2. เช็คห้องเต็ม
                if (playerWriters.size() >= MAX_PLAYERS) {
                    out.println("REJECT:ขออภัยครับ ห้องเต็มแล้ว (" + MAX_PLAYERS + "/" + MAX_PLAYERS + ")");
                    socket.close();
                    continue;
                }

                // ผ่านการตรวจสอบเบื้องต้น เริ่ม Thread จัดการผู้เล่น
                new Thread(new ClientHandler(socket, out, clientIP)).start();
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
        private String playerName;

        public ClientHandler(Socket socket, PrintWriter out, String clientIP) {
            this.socket = socket;
            this.out = out;
            this.clientIP = clientIP;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                // รับชื่อผู้เล่น
                playerName = in.readLine();

                if (playerName == null || playerName.trim().isEmpty()) {
                    socket.close();
                    return;
                }

                playerName = playerName.trim();

                // 🛑 3. ตรวจสอบชื่อซ้ำ (กรณีคนละ IP แต่ตั้งชื่อเหมือนกัน)
                if (playerWriters.containsKey(playerName)) {
                    out.println("REJECT:ชื่อนี้มีคนใช้แล้วในห้องแข่ง!");
                    socket.close();
                    return;
                }

                // ✅ ยืนยันการจอง IP และชื่อลงในระบบ
                connectedIPs.put(clientIP, playerName);
                playerWriters.put(playerName, out);
                playerScores.put(playerName, 0);

                // ✅ Smart Host Detection
                boolean isLocal = clientIP.equals("127.0.0.1") || clientIP.equals("0:0:0:0:0:0:0:1");
                synchronized (GameServer.class) {
                    if (currentHostName.isEmpty() || isLocal) {
                        currentHostName = playerName;
                        System.out.println(">>> [HOST]: " + playerName + (isLocal ? " (Owner/Localhost)" : ""));
                    }
                }

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
                // ผู้เล่นหลุด
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            if (playerName != null) {
                playerScores.remove(playerName);
                playerWriters.remove(playerName);
                // ✅ คืนสิทธิ์ IP ให้สามารถกลับเข้ามาใหม่ได้
                connectedIPs.remove(clientIP);

                System.out.println("<<< [LEFT]: " + playerName + " (IP: " + clientIP + " คืนสิทธิ์แล้ว)");

                // โอนสิทธิ์ Host ถ้าคนเดิมออก
                if (playerName.equals(currentHostName)) {
                    currentHostName = playerWriters.keySet().stream().findFirst().orElse("");
                    if (!currentHostName.isEmpty()) {
                        broadcast("SYSTEM:Host หลุดไปแล้ว! เปลี่ยนให้ " + currentHostName + " เป็นแทน");
                    }
                }

                broadcast("SYSTEM:" + playerName + " ออกจากห้อง");
                updatePlayerList();

                if (playerWriters.isEmpty()) {
                    gameStarted = false;
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