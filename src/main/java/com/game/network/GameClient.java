package com.game.network;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameClient {

    private final String serverIP;
    private final int port;
    private final String playerName;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private MessageListener listener;
    private volatile boolean running = false;
    private List<String> playerList = new ArrayList<>(); // ✅ เก็บรายชื่อผู้เล่นในห้อง

    private final Queue<String> pendingMessages = new ConcurrentLinkedQueue<>();

    // ======================================================
    public interface MessageListener {
        void onPlayerListUpdate(List<String> players);

        void onGameStart();

        void onRejected(String reason); // ✅ สำหรับเด้งกลับหน้า MenuGame

        void onSystemMessage(String message);

        void onConnectionFailed(String ip);

        void onScoreUpdate(String message);

        void onWinner(String winnerName);

        void onFinalScore();

        void onFinalScoreItem(String playerName, int score);
    }

    // ======================================================
    public GameClient(String serverIP, int port, String playerName) {
        this.serverIP = serverIP;
        this.port = port;
        this.playerName = playerName;
    }

    // ======================================================
    public synchronized void setMessageListener(MessageListener listener) {
        this.listener = listener;
        while (!pendingMessages.isEmpty()) {
            dispatchInternal(pendingMessages.poll());
        }
    }

    // ======================================================
    public boolean connect() {
        try {
            socket = new Socket(serverIP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            out.println(playerName);
            running = true;

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

    private void readLoop() {
        try {
            String line;
            while (running && (line = in.readLine()) != null) {
                dispatch(line);
            }
        } catch (IOException e) {
            if (running && listener != null) {
                listener.onSystemMessage("หลุดการเชื่อมต่อจาก server");
            }
        }
    }

    private void dispatch(String msg) {
        if (listener == null) {
            pendingMessages.add(msg);
            return;
        }
        dispatchInternal(msg);
    }

    // ✅ แก้ไขส่วนนี้เพื่อดักจับการ REJECT และ PLAYER_LIST
    private void dispatchInternal(String msg) {
        if (msg.startsWith("REJECT:")) {
            String reason = msg.substring(7);
            if (listener != null)
                listener.onRejected(reason);
            disconnect(); // ตัดการเชื่อมต่อทันทีเมื่อโดนปฏิเสธ
        } else if (msg.startsWith("SYSTEM:")) {
            listener.onSystemMessage(msg.substring(7));
        } else if (msg.startsWith("PLAYER_LIST:")) {
            String rawData = msg.substring(12);
            // แยกชื่อผู้เล่นและ Host (รูปแบบ: Player1,Player2|HostName)
            String namesPart = rawData.contains("|") ? rawData.split("\\|")[0] : rawData;
            List<String> players = Arrays.asList(namesPart.split(","));

            // ✅ อัปเดตรายชื่อลงในตัวแปร Class
            this.playerList = new ArrayList<>(players);
            listener.onPlayerListUpdate(players);
        } else if (msg.equals("START")) {
            listener.onGameStart();
        } else if (msg.startsWith("UPDATE:")) {
            listener.onScoreUpdate(msg.substring(7));
        } else if (msg.startsWith("WINNER:")) {
            listener.onWinner(msg.substring(7));
        } else if (msg.equals("FINAL_SCORE")) {
            listener.onFinalScore();
        } else if (msg.startsWith("SCORE:")) {
            String[] parts = msg.split(":");
            if (parts.length == 3) {
                String name = parts[1];
                int score = Integer.parseInt(parts[2]);
                listener.onFinalScoreItem(name, score);
            }
        }
    }

    public void sendAction(String action) {
        if (out != null && running)
            out.println(action);
    }

    public void disconnect() {
        running = false;
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException ignored) {
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getServerIP() {
        return serverIP;
    }

    // ✅ เมธอดสำหรับเรียกดูรายชื่อผู้เล่นล่าสุด
    public List<String> getPlayerList() {
        return playerList;
    }
}