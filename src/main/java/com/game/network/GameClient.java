package com.game.network;

import java.io.*;
import java.net.*;

/**
 * GameClient — จัดการการเชื่อมต่อ Socket กับ GameServer
 * ใช้ร่วมกับ MessageListener เพื่อส่ง event กลับไปยัง UI
 */
public class GameClient {

    private final String serverIP;
    private final int port;
    private final String playerName;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private MessageListener listener;
    private volatile boolean running = false;

    // ======================================================
    // Interface สำหรับ UI ฟัง event จาก server
    // ======================================================
    public interface MessageListener {
        /** ถูกเตะออกเพราะห้องเต็ม */
        void onRejected(String reason);

        /** ข้อความระบบ เช่น "X เข้าร่วมแล้ว" */
        void onSystemMessage(String message);

        /** อัปเดตคะแนน */
        void onScoreUpdate(String message);

        /** มีคนชนะ */
        void onWinner(String winnerName);

        /** เชื่อมต่อไม่สำเร็จ */
        void onConnectionFailed(String ip);
    }

    // ======================================================
    // Constructor
    // ======================================================
    public GameClient(String serverIP, int port, String playerName) {
        this.serverIP = serverIP;
        this.port = port;
        this.playerName = playerName;
    }

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    // ======================================================
    // เชื่อมต่อ Server และเริ่ม Background Thread อ่าน messages
    // ======================================================
    public boolean connect() {
        try {
            socket = new Socket(serverIP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            // ส่งชื่อผู้เล่นให้ server รู้จัก
            out.println(playerName);
            running = true;

            // Thread สำหรับอ่าน messages จาก server ตลอดเวลา
            Thread readerThread = new Thread(this::readLoop, "GameClient-Reader");
            readerThread.setDaemon(true);
            readerThread.start();

            return true;

        } catch (Exception e) {
            if (listener != null)
                listener.onConnectionFailed(serverIP);
            return false;
        }
    }

    // ======================================================
    // Loop อ่าน messages จาก server
    // ======================================================
    private void readLoop() {
        try {
            String line;
            while (running && (line = in.readLine()) != null) {
                final String msg = line;
                dispatch(msg);
            }
        } catch (IOException e) {
            if (running && listener != null) {
                listener.onSystemMessage("หลุดการเชื่อมต่อจาก server");
            }
        }
    }

    private void dispatch(String msg) {
        if (listener == null)
            return;

        if (msg.startsWith("REJECT:")) {
            listener.onRejected(msg.substring(7));
        } else if (msg.startsWith("SYSTEM:")) {
            listener.onSystemMessage(msg.substring(7));
        } else if (msg.startsWith("UPDATE:")) {
            listener.onScoreUpdate(msg.substring(7));
        } else if (msg.startsWith("WINNER:")) {
            listener.onWinner(msg.substring(7));
        }
    }

    // ======================================================
    // ส่ง action ไปยัง server ("TALK" หรือ "GIFT")
    // ======================================================
    public void sendAction(String action) {
        if (out != null && running) {
            out.println(action);
        }
    }

    // ======================================================
    // ปิดการเชื่อมต่อ
    // ======================================================
    public void disconnect() {
        running = false;
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException ignored) {
        }
    }

    // ======================================================
    // Getter
    // ======================================================
    public String getPlayerName() {
        return playerName;
    }

    public String getServerIP() {
        return serverIP;
    }
}
