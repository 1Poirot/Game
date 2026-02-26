package com.game.ui;

import com.game.network.GameClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * MultiplayerScreen — หน้าเกม Multiplayer สำหรับ Max 3 ผู้เล่น
 *
 * ฟัง events จาก GameClient.MessageListener แล้วอัปเดต UI บน EDT
 */
public class MultiplayerScreen extends JFrame implements GameClient.MessageListener {

    private final GameClient client;

    // ===== UI Components =====
    private JTextArea logArea;
    private JPanel scoreboardPanel;
    private JButton btnTalk;
    private JButton btnGift;

    private final Map<String, JLabel> scoreLabels = new LinkedHashMap<>();

    // ======================================================
    // Constructor
    // ======================================================
    public MultiplayerScreen(GameClient client) {
        this.client = client;
        client.setMessageListener(this);

        setTitle("ศึกชิงนาง — Online  |  ผู้เล่น: " + client.getPlayerName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(800, 580);
        setMinimumSize(new Dimension(640, 480));
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

    // ======================================================
    // Build UI
    // ======================================================
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(new Color(30, 15, 30));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // ===== TOP — Title =====
        JLabel title = new JLabel("💖 ศึกชิงนาง — Multiplayer", SwingConstants.CENTER);
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

        JLabel sbTitle = new JLabel("📊 คะแนน");
        sbTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        sbTitle.setForeground(new Color(255, 200, 220));
        sbTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sbTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        scoreboardPanel.add(sbTitle);

        center.add(scoreboardPanel, BorderLayout.WEST);

        // -- Log Area (center-right) --
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        logArea.setBackground(new Color(40, 18, 40));
        logArea.setForeground(new Color(240, 200, 220));
        logArea.setCaretColor(new Color(255, 150, 180));
        logArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        logArea.setText("กำลังรอผู้เล่นคนอื่น...\n");

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(150, 80, 120), 1));
        scroll.getViewport().setBackground(logArea.getBackground());
        center.add(scroll, BorderLayout.CENTER);

        // ===== BOTTOM — Buttons =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnPanel.setOpaque(false);

        btnTalk = makeActionButton("💬 คุย  (+5)", new Color(100, 180, 255));
        btnTalk.addActionListener(e -> {
            client.sendAction("TALK");
            btnTalk.setEnabled(false);
            btnGift.setEnabled(false);
            javax.swing.Timer t = new javax.swing.Timer(600, ev -> {
                btnTalk.setEnabled(true);
                btnGift.setEnabled(true);
            });
            t.setRepeats(false);
            t.start();
        });

        btnGift = makeActionButton("🎁 ให้ของขวัญ  (+10)", new Color(255, 130, 180));
        btnGift.addActionListener(e -> {
            client.sendAction("GIFT");
            btnTalk.setEnabled(false);
            btnGift.setEnabled(false);
            javax.swing.Timer t = new javax.swing.Timer(600, ev -> {
                btnTalk.setEnabled(true);
                btnGift.setEnabled(true);
            });
            t.setRepeats(false);
            t.start();
        });

        btnPanel.add(btnTalk);
        btnPanel.add(btnGift);
        root.add(btnPanel, BorderLayout.SOUTH);
    }

    // ======================================================
    // GameClient.MessageListener — called from background thread
    // ======================================================
    @Override
    public void onRejected(String reason) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, reason, "เข้าเกมไม่ได้ — ห้องเต็ม", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> {
            appendLog(message);
            // Parse "NAME ทำคะแนนได้ SCORE" เพื่ออัปเดต scoreboard
            updateScoreBoard(message);
        });
    }

    @Override
    public void onWinner(String winnerName) {
        SwingUtilities.invokeLater(() -> {
            String myName = client.getPlayerName();
            if (winnerName.equals(myName)) {
                JOptionPane.showMessageDialog(this,
                        "💖 ยินดีด้วย! คุณจีบติดแล้ว!\n\nคุณคือผู้ชนะ!", "คุณชนะ!", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "💔 แห้วแดก...\n\nเธอเลือก " + winnerName, "แพ้แล้ว", JOptionPane.PLAIN_MESSAGE);
            }
            appendLog("🏆 ผู้ชนะ: " + winnerName);
            btnTalk.setEnabled(false);
            btnGift.setEnabled(false);
        });
    }

    @Override
    public void onConnectionFailed(String ip) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "เชื่อมต่อ IP: " + ip + " ไม่สำเร็จ!\nโปรดเช็คว่า Host เปิดเกมหรือยัง",
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        });
    }

    // ======================================================
    // Helper — อัปเดต Scoreboard
    // ======================================================
    private void updateScoreBoard(String message) {
        // Format: "NAME ทำคะแนนได้ SCORE"
        try {
            String[] parts = message.split(" ทำคะแนนได้ ");
            if (parts.length == 2) {
                String name = parts[0].trim();
                String score = parts[1].trim();

                if (scoreLabels.containsKey(name)) {
                    scoreLabels.get(name).setText(name + "  →  " + score + " คะแนน");
                } else {
                    JLabel lbl = new JLabel(name + "  →  " + score + " คะแนน");
                    lbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
                    lbl.setForeground(name.equals(client.getPlayerName())
                            ? new Color(255, 220, 80) // ไฮไลต์ชื่อตัวเอง
                            : new Color(220, 190, 210));
                    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                    lbl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
                    scoreLabels.put(name, lbl);
                    scoreboardPanel.add(lbl);
                    scoreboardPanel.revalidate();
                    scoreboardPanel.repaint();
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void appendLog(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ======================================================
    // Helper — styled action button
    // ======================================================
    private JButton makeActionButton(String text, Color accent) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = isEnabled()
                        ? (getModel().isPressed() ? accent.darker()
                                : getModel().isRollover() ? accent.brighter() : accent)
                        : new Color(80, 60, 80);
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Tahoma", Font.BOLD, 16));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 48));
        return b;
    }
}
