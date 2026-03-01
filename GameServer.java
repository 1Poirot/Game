import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private static Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private static Map<String, PrintWriter> playerWriters = new ConcurrentHashMap<>();
    private static final int MAX_PLAYERS = 3;
    private static boolean gameStarted = false;
    private static Set<String> finishedPlayers = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("========== ✅ เซิร์ฟเวอร์ ศึกชิงนาง ONLINE ==========");
        System.out.println("รองรับผู้เล่นสูงสุด: " + MAX_PLAYERS + " ท่าน");
        String hostIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println(">> IP: " + hostIP + " <<");
        System.out.println("=============================================");

        while (true) {
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            if (playerWriters.size() >= MAX_PLAYERS) {
                out.println("REJECT:ขออภัยครับ ห้องเต็มแล้ว");
                socket.close();
                continue;
            }
            new Thread(new ClientHandler(socket, out)).start();
        }
    }

    private static void updatePlayerList() {
        String listMsg = "PLAYER_LIST:" + String.join(",", playerWriters.keySet());
        broadcast(listMsg);
    }

    private static void broadcast(String msg) {
        for (PrintWriter writer : playerWriters.values()) {
            writer.println(msg);
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String playerName;

        public ClientHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                playerName = in.readLine();

                if (playerName != null) {
                    playerScores.put(playerName, 0);
                    playerWriters.put(playerName, out);
                    System.out.println(">>> [JOIN]: " + playerName);
                    broadcast("SYSTEM:" + playerName + " เข้าร่วมวงจีบสาวแล้ว");
                    updatePlayerList();
                }

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equals("START_GAME")) {
                        gameStarted = true;
                        finishedPlayers.clear();

                        // ล้างคะแนนทุกคนให้เป็น 0 ก่อนเริ่มรอบใหม่
                        for (String name : playerScores.keySet()) {
                            playerScores.put(name, 0);
                        }

                        System.out.println("--- [NEW GAME STARTED] ---");
                        broadcast("SYSTEM:เริ่มการแข่งขันรอบใหม่!");
                        broadcast("START");
                    } else if (msg.startsWith("FINISH:")) {
                        int finalScore = Integer.parseInt(msg.split(":")[1]);
                        playerScores.put(playerName, finalScore);
                        finishedPlayers.add(playerName);
                        broadcast("SYSTEM:" + playerName + " เล่นจบแล้ว");

                        // ถ้าเล่นจบครบทุกคนที่อยู่ในห้องขณะนั้น
                        if (finishedPlayers.size() >= playerWriters.size()) {
                            broadcast("FINAL_SCORE");
                            for (String p : playerScores.keySet()) {
                                broadcast("SCORE:" + p + ":" + playerScores.get(p));
                            }
                            gameStarted = false;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(playerName + " Disconnected.");
            } finally {
                if (playerName != null) {
                    playerScores.remove(playerName);
                    playerWriters.remove(playerName);
                    finishedPlayers.remove(playerName);
                    broadcast("SYSTEM:" + playerName + " ออกจากห้อง");
                    updatePlayerList();

                    // ถ้าในห้องไม่เหลือใครเลย ให้รีเซ็ตสถานะเกม
                    if (playerWriters.isEmpty()) {
                        gameStarted = false;
                        finishedPlayers.clear();
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}