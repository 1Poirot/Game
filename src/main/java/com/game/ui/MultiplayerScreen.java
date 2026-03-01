package com.game.ui;

import com.game.controllers.GameController;
import com.game.multi.dating.MultiDatingScreen;
import com.game.multi.dating.MultiDatingSound;
import com.game.network.GameClient; // ✅ เพิ่ม Import Controller
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * MultiplayerScreen (Lobby Home)
 */
public class MultiplayerScreen extends JFrame implements GameClient.MessageListener {

    private final GameClient client;
    private final GameController controller; // ✅ เพิ่มตัวแปรเก็บ Controller เพื่อส่งไม้ต่อ
    private JPanel playerListPanel;
    private JButton btnStart;
    private JButton btnLeave;
    private JTextArea logArea;
    private boolean isHost;
    private Font fontPlain;
    private Font fontBold;

    // ✅ ปรับ Constructor ให้รับ GameController เพิ่มมาด้วย
    public MultiplayerScreen(GameClient client, boolean isHost, GameController controller) {
        this.client = client;
        this.isHost = isHost;
        this.controller = controller; // ✅ เก็บค่าไว้ใช้งาน

        client.setMessageListener(this);

        initFonts();

        setTitle("💖 ศึกชิงนาง — Lobby");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();
                dispose();
            }
        });

        buildUI();
        setVisible(true);
    }

    private void initFonts() {
        fontPlain = new Font("Tahoma", Font.PLAIN, 16);
        fontBold = new Font("Tahoma", Font.BOLD, 16);

        if (!fontPlain.getFamily().equalsIgnoreCase("Tahoma")) {
            fontPlain = new Font("Dialog", Font.PLAIN, 16);
            fontBold = new Font("Dialog", Font.BOLD, 16);
        }
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(new Color(30, 15, 30));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JLabel title = new JLabel("💖 ศึกชิงนาง — Lobby", SwingConstants.CENTER);
        title.setFont(fontBold.deriveFont(22f));
        title.setForeground(new Color(255, 180, 210));
        root.add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.4);
        split.setDividerSize(4);
        split.setBorder(null);

        playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setBackground(new Color(50, 20, 50));
        playerListPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 100, 150), 1),
                "ผู้เล่นในห้อง",
                0, 0,
                fontBold,
                new Color(255, 180, 210)));

        JScrollPane playerScroll = new JScrollPane(playerListPanel);
        split.setLeftComponent(playerScroll);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(fontPlain);
        logArea.setBackground(new Color(40, 15, 40));
        logArea.setForeground(new Color(255, 210, 230));
        logArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 100, 150), 1),
                "ข้อความจากระบบ",
                0, 0,
                fontBold,
                new Color(255, 180, 210)));

        JScrollPane logScroll = new JScrollPane(logArea);
        split.setRightComponent(logScroll);

        root.add(split, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnPanel.setOpaque(false);

        btnStart = makeButton("🚀 เริ่มเกม");
        btnStart.setEnabled(isHost);
        btnStart.addActionListener(e -> client.sendAction("START_GAME"));

        btnLeave = makeButton("❌ ออกจากห้อง");
        btnLeave.addActionListener(e -> {
            client.disconnect();
            dispose();
        });

        btnPanel.add(btnStart);
        btnPanel.add(btnLeave);
        root.add(btnPanel, BorderLayout.SOUTH);
    }

    @Override
    public void onPlayerListUpdate(List<String> players) {
        SwingUtilities.invokeLater(() -> {
            playerListPanel.removeAll();
            if (players == null || players.isEmpty()) {
                playerListPanel.revalidate();
                playerListPanel.repaint();
                return;
            }
            String hostName = players.get(0);
            for (String name : players) {
                JLabel lbl = new JLabel(
                        name.equals(hostName) ? "👑 " + name : "👤 " + name);
                lbl.setFont(fontPlain);
                lbl.setForeground(new Color(240, 200, 220));
                lbl.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
                lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                playerListPanel.add(lbl);
            }
            playerListPanel.revalidate();
            playerListPanel.repaint();
        });
    }

    @Override
    public void onSystemMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("📢 " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    // ======================================================
    // 🔥 แก้ไขจุดที่เป็นปัญหา: ส่ง Client และ Controller ไปยังหน้าจีบสาว
    // ======================================================
    @Override
    public void onGameStart() {
        SwingUtilities.invokeLater(() -> {
            MultiDatingSound.getInstance().stopBGM();

            this.setVisible(false);
            this.dispose();

            // ✅ ส่งไปทั้ง client และ controller ตัวแดงจะหาย และบัค NullPointer จะหมดไป!
            new MultiDatingScreen(client, controller);
        });
    }

    @Override
    public void onFinalScore() {
        SwingUtilities.invokeLater(() -> logArea.append("\n🏁 ทุกคนเล่นเสร็จแล้ว! กำลังสรุปคะแนน...\n"));
    }

    @Override
    public void onFinalScoreItem(String playerName, int score) {
        SwingUtilities.invokeLater(() -> logArea.append("🏆 " + playerName + " ได้ " + score + " คะแนน\n"));
    }

    @Override
    public void onRejected(String reason) {
    }

    @Override
    public void onConnectionFailed(String ip) {
    }

    @Override
    public void onScoreUpdate(String message) {
    }

    @Override
    public void onWinner(String winnerName) {
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFont(fontBold);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(200, 80, 150));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(200, 45));
        return b;
    }
}