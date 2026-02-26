import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private static Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private static List<PrintWriter> clientWriters = new CopyOnWriteArrayList<>();

    private static final int WINNING_SCORE = 100;
    private static final int MAX_PLAYERS = 3; // <-- กำหนดผู้เล่นสูงสุด 3 คน
    private static boolean gameEnded = false;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9090);

        System.out.println("========== เปิดเซิร์ฟเวอร์ ศึกชิงนาง ==========");
        System.out.println("รับผู้เล่นได้สูงสุด: " + MAX_PLAYERS + " คน");

        // ดึง IP Address ของเครื่อง Host มาแสดงให้เพื่อนดู
        String hostIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println(">> บอกให้เพื่อนกรอก IP นี้นะ: " + hostIP + " <<");
        System.out.println("=============================================");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // เช็คว่าห้องเต็มหรือยัง?
            if (clientWriters.size() >= MAX_PLAYERS) {
                out.println("REJECT:ห้องเต็มแล้วจ้า (รับได้แค่ 3 คนเท่านั้น!)");
                clientSocket.close(); // เตะออก
                System.out.println("มีคนพยายามเข้า แต่ห้องเต็มแล้ว");
                continue;
            }

            clientWriters.add(out);
            new Thread(new ClientHandler(clientSocket, out)).start();
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

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                playerName = in.readLine();
                playerScores.put(playerName, 0);

                System.out.println(playerName + " เข้าร่วม! (" + clientWriters.size() + "/" + MAX_PLAYERS + " คน)");
                broadcast("SYSTEM:" + playerName + " เข้าร่วมวงจีบสาวแล้ว! (" + clientWriters.size() + "/" + MAX_PLAYERS
                        + ")");

                String message;
                while ((message = in.readLine()) != null) {
                    if (gameEnded)
                        continue;

                    int currentScore = playerScores.get(playerName);
                    if (message.equals("TALK"))
                        currentScore += 5;
                    else if (message.equals("GIFT"))
                        currentScore += 10;

                    playerScores.put(playerName, currentScore);
                    broadcast("UPDATE:" + playerName + " ทำคะแนนได้ " + currentScore);

                    if (currentScore >= WINNING_SCORE) {
                        gameEnded = true;
                        broadcast("WINNER:" + playerName);
                    }
                }
            } catch (IOException e) {
                System.out.println(playerName + " หลุดออกจากเกม");
            } finally {
                // ถ้ามีคนออก ให้ลบข้อมูลออก ห้องจะได้ว่างให้คนอื่นเข้า
                if (playerName != null) {
                    playerScores.remove(playerName);
                    clientWriters.remove(out);
                    broadcast("SYSTEM:" + playerName + " ยอมแพ้และเดินจากไป...");
                }
            }
        }

        private void broadcast(String msg) {
            for (PrintWriter writer : clientWriters) {
                writer.println(msg);
            }
        }
    }
}