import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    // เก็บข้อมูลคะแนนแยกตามชื่อผู้เล่น เช่น {"Neko": 50, "John": 20}
    private static Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private static List<PrintWriter> clientWriters = new CopyOnWriteArrayList<>();
    
    // ตั้งเป้าหมาย: ใครถึง 100 คะแนนก่อน ชนะ!
    private static final int WINNING_SCORE = 100;
    private static boolean gameEnded = false;

    public static void main(String[] args) throws Exception {
        System.out.println("เปิดเซิร์ฟเวอร์ ศึกชิงนาง...");
        ServerSocket serverSocket = new ServerSocket(9090);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            clientWriters.add(out);

            // สร้าง Thread ให้ผู้เล่นที่เข้ามาใหม่
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
                
                // 1. รับชื่อผู้เล่นทันทีที่เชื่อมต่อ
                playerName = in.readLine();
                playerScores.put(playerName, 0); // เริ่มต้นที่ 0 คะแนน
                
                System.out.println(playerName + " เข้าร่วมการแข่งขัน!");
                broadcast("SYSTEM:" + playerName + " มาร่วมวงจีบสาวแล้ว!");

                String message;
                // 2. รอรับคำสั่ง (TALK หรือ GIFT)
                while ((message = in.readLine()) != null) {
                    if (gameEnded) continue; // ถ้ามีคนชนะแล้ว ห้ามกดเพิ่ม

                    int currentScore = playerScores.get(playerName);
                    
                    if (message.equals("TALK")) {
                        currentScore += 5;
                        broadcast("UPDATE:" + playerName + " ชวนคุยเล่น (คะแนนของ " + playerName + " = " + currentScore + ")");
                    } else if (message.equals("GIFT")) {
                        currentScore += 10;
                        broadcast("UPDATE:" + playerName + " เปย์ของขวัญ! (คะแนนของ " + playerName + " = " + currentScore + ")");
                    }

                    // อัปเดตคะแนนกลับเข้า Map
                    playerScores.put(playerName, currentScore);

                    // 3. เช็คว่าชนะหรือยัง?
                    if (currentScore >= WINNING_SCORE) {
                        gameEnded = true;
                        broadcast("WINNER:" + playerName);
                    }
                }
            } catch (IOException e) {
                System.out.println(playerName + " หลุดออกจากเกม");
            }
        }

        // ส่งข้อความไปหาผู้เล่นทุกคน
        private void broadcast(String msg) {
            for (PrintWriter writer : clientWriters) {
                writer.println(msg);
            }
        }
    }
}