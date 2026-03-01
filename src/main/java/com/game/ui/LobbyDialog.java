package com.game.ui;

import com.game.multi.dating.MultiDatingSound; // ✅ 1. Import ตัวจัดการเสียง
import java.awt.*;
import javax.swing.*;

/**
 * LobbyDialog — Dialog สำหรับให้ผู้เล่นกรอก IP และชื่อก่อนเข้าเกม Multiplayer
 *
 * ผลลัพธ์จะเก็บใน LobbyResult (ip, playerName)
 * ถ้ากด Cancel จะ return null
 */
public class LobbyDialog extends JDialog {

    // ===== Result container =====
    public static class LobbyResult {
        public final String ip;
        public final String playerName;

        public LobbyResult(String ip, String playerName) {
            this.ip = ip;
            this.playerName = playerName;
        }
    }

    private LobbyResult result = null;
    private final JTextField ipField;
    private final JTextField nameField;

    // ======================================================
    // Constructor
    // ======================================================
    public LobbyDialog(Frame parent) {
        super(parent, "เล่นออนไลน์ — เชื่อมต่อ", true);

        setSize(440, 320);
        setResizable(false);
        setLocationRelativeTo(parent);

        // ===== Background Panel =====
        JPanel root = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 220, 235),
                        getWidth(), getHeight(), new Color(220, 200, 255));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setPreferredSize(new Dimension(440, 320));
        setContentPane(root);

        // ===== Title =====
        JLabel title = new JLabel("🎮 เล่นออนไลน์กับเพื่อน");
        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        title.setForeground(new Color(120, 40, 100));
        title.setBounds(60, 20, 340, 36);
        root.add(title);

        JLabel sub = new JLabel("รองรับสูงสุด 3 ผู้เล่น (Max 3 Players)");
        sub.setFont(new Font("Tahoma", Font.PLAIN, 13));
        sub.setForeground(new Color(160, 80, 130));
        sub.setBounds(80, 54, 300, 20);
        root.add(sub);

        // ===== IP Field =====
        JLabel ipLabel = new JLabel("IP ของ Host:");
        ipLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        ipLabel.setForeground(new Color(80, 40, 80));
        ipLabel.setBounds(40, 95, 120, 24);
        root.add(ipLabel);

        ipField = makeField("localhost");
        ipField.setBounds(160, 93, 220, 30);
        root.add(ipField);

        JLabel ipHint = new JLabel("← ถ้าคุณเป็น Host ให้ใส่ localhost");
        ipHint.setFont(new Font("Tahoma", Font.ITALIC, 11));
        ipHint.setForeground(new Color(140, 100, 140));
        ipHint.setBounds(40, 126, 360, 18);
        root.add(ipHint);

        // ===== Name Field =====
        JLabel nameLabel = new JLabel("ชื่อผู้เล่น:");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        nameLabel.setForeground(new Color(80, 40, 80));
        nameLabel.setBounds(40, 158, 120, 24);
        root.add(nameLabel);

        nameField = makeField("");
        nameField.setBounds(160, 156, 220, 30);
        root.add(nameField);

        // ===== Buttons =====
        JButton joinBtn = makeButton("เข้าเกม ▶", new Color(255, 100, 140), Color.WHITE);
        joinBtn.setBounds(60, 220, 150, 42);
        joinBtn.addActionListener(e -> onJoin());
        root.add(joinBtn);

        JButton cancelBtn = makeButton("ยกเลิก", new Color(200, 180, 210), new Color(80, 40, 80));
        cancelBtn.setBounds(230, 220, 150, 42);
        cancelBtn.addActionListener(e -> dispose());
        root.add(cancelBtn);

        // Enter key triggers join
        getRootPane().setDefaultButton(joinBtn);
    }

    // ======================================================
    // Action
    // ======================================================
    private void onJoin() {
        String ip = ipField.getText().trim();
        String name = nameField.getText().trim();

        if (ip.isEmpty()) {
            JOptionPane.showMessageDialog(this, "กรุณากรอก IP ของ Host", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "กรุณากรอกชื่อผู้เล่น", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ 2. สั่งหยุดเพลงหน้าเมนูทันทีก่อนจะปิด Dialog นี้
        // เพื่อให้หน้า MultiplayerScreen เริ่มเพลง Lobby ได้แบบไม่มีเพลงเก่าค้าง
        MultiDatingSound.getInstance().stopBGM();

        result = new LobbyResult(ip, name);
        dispose();
    }

    // ======================================================
    // Show and return result (null = cancelled)
    // ======================================================
    public static LobbyResult show(Frame parent) {
        LobbyDialog dialog = new LobbyDialog(parent);
        dialog.setVisible(true); // blocks until closed (modal)
        return dialog.result;
    }

    // ======================================================
    // Helper — สร้าง styled text field
    // ======================================================
    private JTextField makeField(String defaultText) {
        JTextField f = new JTextField(defaultText);
        f.setFont(new Font("Tahoma", Font.PLAIN, 14));
        f.setForeground(new Color(60, 20, 60));
        f.setBackground(new Color(255, 250, 255));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 160, 200), 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return f;
    }

    // ======================================================
    // Helper — สร้าง styled button
    // ======================================================
    private JButton makeButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        b.setText(text);
        b.setFont(new Font("Tahoma", Font.BOLD, 15));
        b.setForeground(fg);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}