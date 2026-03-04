package com.game.ui;

import com.game.controllers.GameController;
import com.game.multi.dating.MultiDatingScreen;
import com.game.network.GameClient;
import java.awt.*; // ✅ เพิ่ม import
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * MultiplayerScreen — หน้า Lobby และจัดการการเริ่มเกม
 */
public class MultiplayerScreen extends JFrame implements GameClient.MessageListener {

    private final GameClient client;
    private final GameController controller; // ✅ เพิ่มตัวแปรเก็บ Controller
    private MultiDatingScreen gameUI; // ✅ เพิ่มตัวแปรสำหรับอ้างอิงหน้าจอเกม
    private final Map<String, Integer> finalScoresMap = new HashMap<>(); // ✅ ตัวเก็บคะแนนที่รับจาก Server
    private int expectedPlayers = 0;

    // ===== UI Components =====
    private JTextArea logArea;
    private JPanel scoreboardPanel;
    private JButton btnTalk; // ใช้เป็นปุ่มเริ่มเกม
    private JButton btnGift; // ใช้เป็นปุ่มออกจากห้อง

    // ======================================================
    // Constructor (รับ 3 พารามิเตอร์เพื่อให้หายแดงใน GameController)
    // ======================================================
    public MultiplayerScreen(GameClient client, boolean isHost, GameController controller) {
        this.client = client;
        this.controller = controller; // ✅ เก็บค่าไว้ส่งต่อ
        client.setMessageListener(this);

        setTitle("ศึกชิงนาง — Lobby  |  ผู้เล่น: " + client.getPlayerName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(800, 580);
        setMinimumSize(new Dimension(640, 480));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitRoom();
            }
        });

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(new Color(30, 15, 30));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // ===== TOP — Title =====
        JLabel title = new JLabel("ศึกชิงนาง — Lobby", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(new Color(255, 180, 210));
        root.add(title, BorderLayout.NORTH);

        // ===== CENTER — Scoreboard + Log =====
        JPanel center = new JPanel(new BorderLayout(10, 0));
        center.setOpaque(false);
        root.add(center, BorderLayout.CENTER);

        // -- Scoreboard (left) --
        scoreboardPanel = new JPanel();
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
        scoreboardPanel.setBackground(new Color(50, 20, 50));
        scoreboardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 100, 150), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        scoreboardPanel.setPreferredSize(new Dimension(220, 0));

        JLabel sbTitle = new JLabel("รายชื่อผู้เล่น");
        sbTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        sbTitle.setForeground(new Color(255, 200, 220));
        sbTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scoreboardPanel.add(sbTitle);
        center.add(scoreboardPanel, BorderLayout.WEST);

        // -- Log Area (right) --
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        logArea.setBackground(new Color(40, 18, 40));
        logArea.setForeground(new Color(240, 200, 220));
        logArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        logArea.setText("กำลังรอผู้เล่นคนอื่น...\n");

        JScrollPane scroll = new JScrollPane(logArea);
        center.add(scroll, BorderLayout.CENTER);

        // ===== BOTTOM — Buttons =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnPanel.setOpaque(false);

        // ✅ ใช้ตัวแปรคลาส btnTalk มาทำเป็นปุ่มเริ่มเกม
        btnTalk = makeActionButton("เริ่มเกม", new Color(50, 200, 100));
        btnTalk.setEnabled(true);
        btnTalk.addActionListener(e -> {
            client.sendAction("START_GAME");
            appendLog("[ระบบ] ส่งคำสั่งเริ่มเกม...");
        });

        // ✅ ใช้ตัวแปรคลาส btnGift มาทำเป็นปุ่มออกจากห้อง
        btnGift = makeActionButton("ออกจากห้อง", new Color(220, 50, 50));
        btnGift.addActionListener(e -> exitRoom());

        btnPanel.add(btnTalk);
        btnPanel.add(btnGift);
        root.add(btnPanel, BorderLayout.SOUTH);
    }

    private void exitRoom() {
        int choice = JOptionPane.showConfirmDialog(this, "ต้องการออกจากห้องใช่หรือไม่?", "ยืนยัน",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            client.disconnect();
            dispose();
            if (controller != null)
                controller.showMainMenu(); // กลับหน้าหลัก
        }
    }

    // ======================================================
    // GameClient.MessageListener
    // ======================================================
    @Override
    public void onRejected(String reason) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, reason, "ห้องเต็ม", JOptionPane.ERROR_MESSAGE);
            client.disconnect();
            dispose();
        });
    }

    @Override
    public void onSystemMessage(String message) {
        SwingUtilities.invokeLater(() -> appendLog("[ระบบ] " + message));
    }

    @Override
    public void onScoreUpdate(String message) {
        SwingUtilities.invokeLater(() -> appendLog(message));
    }

    @Override
    public void onWinner(String winnerName) {
    }

    @Override
    public void onConnectionFailed(String ip) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "เชื่อมต่อล้มเหลว", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        });
    }

    @Override
    public void onPlayerListUpdate(java.util.List<String> players) {
        SwingUtilities.invokeLater(() -> {
            scoreboardPanel.removeAll();
            JLabel title = new JLabel("รายชื่อผู้เล่น");
            title.setFont(new Font("Tahoma", Font.BOLD, 16));
            title.setForeground(new Color(255, 200, 220));
            scoreboardPanel.add(title);
            this.expectedPlayers = players.size();

            for (String name : players) {
                JLabel lbl = new JLabel("👤 " + name);
                lbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
                lbl.setForeground(name.equals(client.getPlayerName()) ? Color.YELLOW : Color.WHITE);
                scoreboardPanel.add(lbl);
            }
            scoreboardPanel.revalidate();
            scoreboardPanel.repaint();
        });
    }

    @Override
    public void onGameStart() {
        SwingUtilities.invokeLater(() -> {
            appendLog("[ระบบ] เกมกำลังเริ่ม...");
            this.setVisible(false);
            // ✅ เก็บหน้าจอที่สร้างใหม่ไว้ในตัวแปร gameUI
            this.gameUI = new MultiDatingScreen(client, controller, this);
        });
    }

    @Override
    public void onFinalScore() {
        // 2. เมื่อได้รับสัญญาณ "FINAL_SCORE" จาก Server (แปลว่า Server
        // ส่งคะแนนครบทุกคนแล้ว)
        // ให้รอเสี้ยววินาทีเพื่อให้ Packet สุดท้ายเข้าที่
        Timer delayTimer = new Timer(300, e -> {
            synchronized (finalScoresMap) {
                // เช็คว่าใน Map มีข้อมูลครบตามจำนวนคนจริงๆ ไหม
                if (finalScoresMap.size() >= expectedPlayers) {
                    Map<String, Integer> resultData = new HashMap<>(finalScoresMap);

                    SwingUtilities.invokeLater(() -> {
                        if (gameUI != null && gameUI.isVisible()) {
                            // 🚀 สั่งเปิดหน้าสรุปผลด้วยข้อมูลที่สมบูรณ์ที่สุด
                            gameUI.showFinalResults(resultData);

                            // ล้างเพื่อเล่นรอบใหม่
                            finalScoresMap.clear();
                        }
                    });
                }
            }
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    // ✅ ตรวจสอบเมธอดนี้ใน MultiplayerScreen.java
    @Override
    public void onFinalScoreItem(String p, int s) {
        // 1. เก็บชื่อและคะแนนสะสมไว้เรื่อยๆ
        synchronized (finalScoresMap) {
            finalScoresMap.put(p, s);
            System.out.println("DEBUG >>> รับคะแนนของ [" + p + "] : " + s + " แต้ม");
        }
    }

    private void appendLog(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private JButton makeActionButton(String text, Color accent) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? accent : Color.GRAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Tahoma", Font.BOLD, 16));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(200, 45));
        return b;
    }
}