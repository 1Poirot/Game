package com.game.network;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * ========================================================
 * GameServer — เซิร์ฟเวอร์สำหรับโหมดเล่นออนไลน์ (Multiplayer)
 * ========================================================
 *
 * วิธีรัน (จาก root ของโปรเจกต์):
 * javac -d out src/main/java/com/game/network/GameServer.java
 * java -cp out com.game.network.GameServer
 *
 * หรือใน VS Code: คลิกขวาไฟล์นี้ → Run Java
 *
 * โปรโตคอลการสื่อสาร (Client → Server):
 * START_GAME — Host กดเริ่มเกม (รีเซ็ตคะแนนและ broadcast START)
 * FINISH:<คะแนน> — ผู้เล่นส่งคะแนนสุดท้ายเมื่อเล่นจบ
 *
 * โปรโตคอลการสื่อสาร (Server → Client):
 * REJECT:<เหตุผล> — ห้องเต็ม ไม่รับเข้า
 * PLAYER_LIST:<ชื่อ1,ชื่อ2,...> — อัปเดตรายชื่อผู้เล่นในห้อง
 * SYSTEM:<ข้อความ> — ข้อความแจ้งเตือนระบบ
 * START — เริ่มเกมได้
 * FINAL_SCORE — ทุกคนเล่นจบ กำลังประกาศผล
 * SCORE:<ชื่อ>:<คะแนน> — คะแนนของแต่ละคน
 */
public class GameServer {

    /** คะแนนของผู้เล่นแต่ละคน (ชื่อ → คะแนน) */
    private static final Map<String, Integer> playerScores = new ConcurrentHashMap<>();

    /** PrintWriter สำหรับส่งข้อความหาแต่ละผู้เล่น (ชื่อ → Writer) */
    private static final Map<String, PrintWriter> playerWriters = new ConcurrentHashMap<>();

    /** รองรับผู้เล่นสูงสุด 3 คน */
    private static final int MAX_PLAYERS = 3;

    /**
     * สถานะเกม: true = กำลังเล่นอยู่, false = รอผู้เล่น / จบแล้ว
     * ใช้เพื่อป้องกันการ START ซ้อนกัน
     */
    private static volatile boolean gameStarted = false;

    /** รายชื่อผู้เล่นที่เล่นจบแล้วในรอบนี้ */
    private static final Set<String> finishedPlayers = ConcurrentHashMap.newKeySet();

    // ================================================================
    // จุดเริ่มต้นของโปรแกรม
    // ================================================================
    public static void main(String[] args) throws Exception {

        // try-with-resources เพื่อปิด serverSocket อัตโนมัติเมื่อโปรแกรมหยุด
        try (ServerSocket serverSocket = new ServerSocket(9090)) {

            System.out.println("========== ✅ เซิร์ฟเวอร์ ศึกชิงนาง ONLINE ==========");
            System.out.println("รองรับผู้เล่นสูงสุด: " + MAX_PLAYERS + " ท่าน");
            System.out.println(">> IP: " + InetAddress.getLocalHost().getHostAddress() + " <<");
            System.out.println("=============================================");

            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                // ห้องเต็ม → ปฏิเสธทันที
                if (playerWriters.size() >= MAX_PLAYERS) {
                    out.println("REJECT:ขออภัยครับ ห้องเต็มแล้ว");
                    socket.close();
                    continue;
                }

                new Thread(new ClientHandler(socket, out)).start();
            }
        }
    }

    // ================================================================
    // ส่งรายชื่อผู้เล่นปัจจุบันให้ทุกคนในห้อง
    // ================================================================
    private static void updatePlayerList() {
        broadcast("PLAYER_LIST:" + String.join(",", playerWriters.keySet()));
    }

    // ================================================================
    // ส่งข้อความเดียวกันหาทุกคนในห้อง
    // ================================================================
    private static void broadcast(String msg) {
        for (PrintWriter writer : playerWriters.values()) {
            writer.println(msg);
        }
    }

    // ================================================================
    // Thread สำหรับจัดการผู้เล่นแต่ละคน
    // ================================================================
    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private final PrintWriter out;
        private String playerName;

        public ClientHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                // บรรทัดแรกที่ Client ส่งมาคือชื่อผู้เล่น
                playerName = in.readLine();

                if (playerName != null) {
                    playerScores.put(playerName, 0);
                    playerWriters.put(playerName, out);
                    System.out.println(">>> [JOIN]: " + playerName);
                    broadcast("SYSTEM:" + playerName + " เข้าร่วมวงจีบสาวแล้ว");
                    updatePlayerList();
                }

                // รับคำสั่งจาก Client วนซ้ำจนกว่าจะ disconnect
                String msg;
                while ((msg = in.readLine()) != null) {

                    if (msg.equals("START_GAME")) {
                        // ป้องกัน Host กด Start ซ้อนกัน
                        if (gameStarted) {
                            out.println("SYSTEM:เกมกำลังดำเนินอยู่แล้ว!");
                            continue;
                        }
                        gameStarted = true;
                        finishedPlayers.clear();

                        // รีเซ็ตคะแนนทุกคนก่อนเริ่มรอบใหม่
                        playerScores.replaceAll((k, v) -> 0);

                        System.out.println("--- [NEW GAME STARTED] ---");
                        broadcast("SYSTEM:เริ่มการแข่งขันรอบใหม่!");
                        broadcast("START");

                    } else if (msg.startsWith("FINISH:")) {
                        int finalScore = Integer.parseInt(msg.split(":")[1]);
                        playerScores.put(playerName, finalScore);
                        finishedPlayers.add(playerName);
                        broadcast("SYSTEM:" + playerName + " เล่นจบแล้ว");

                        // ประกาศผลเมื่อทุกคนในห้องเล่นจบแล้ว
                        if (gameStarted && finishedPlayers.size() >= playerWriters.size()) {
                            broadcast("FINAL_SCORE");
                            playerScores.forEach((name, score) -> broadcast("SCORE:" + name + ":" + score));
                            gameStarted = false;
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("[Disconnect] " + playerName + " — " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        /** ทำความสะอาดเมื่อผู้เล่นออกจากห้อง */
        private void cleanup() {
            if (playerName != null) {
                playerScores.remove(playerName);
                playerWriters.remove(playerName);
                finishedPlayers.remove(playerName);
                broadcast("SYSTEM:" + playerName + " ออกจากห้อง");
                updatePlayerList();

                // รีเซ็ตสถานะเกมถ้าไม่มีใครเหลืออยู่
                if (playerWriters.isEmpty()) {
                    gameStarted = false;
                    finishedPlayers.clear();
                }
            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
