package com.game.multi.dating;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MultiDatingInventory extends JDialog {
    private String selectedItem = null;
    private final Color THEME_PINK = new Color(255, 105, 180);
    private final Color SOFT_PINK = new Color(255, 245, 250);
    private final Color ACCENT_PINK = new Color(255, 182, 193, 180);

    public MultiDatingInventory(JFrame parent, Map<String, Integer> items, Consumer<String> onUse) {
        super(parent, "Inventory", true);
        // ✅ ปรับขนาดให้กะทัดรัดลง (Small & Sleek)
        setSize(480, 580);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // ✅ วาดพื้นหลัง Gradient นวลตา (White to Soft Pink)
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), SOFT_PINK);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);

                // ✅ วาดเส้นขอบนอกแบบบางเฉียบแต่ดูหรู
                g2d.setColor(new Color(255, 105, 180, 60));
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 60, 60);

                g2d.dispose();
            }
        };
        root.setBorder(new EmptyBorder(25, 30, 20, 30));
        root.setOpaque(false);
        setContentPane(root);

        // --- Header Section ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("MY GIFTS", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 28));
        title.setForeground(THEME_PINK);
        // ใช้ไอคอนขนาดพอดีกับตัวหนังสือ
        title.setIcon(loadFixedIcon("ชอดอกไม้", 35));
        title.setIconTextGap(10);
        header.add(title, BorderLayout.CENTER);

        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 36));
        closeBtn.setForeground(new Color(255, 105, 180, 150));
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // --- Center: Grid Layout (3x2 หรือ 3x3) ---
        JPanel gridContainer = new JPanel(new GridLayout(2, 3, 15, 15));
        gridContainer.setOpaque(false);

        String[] keys = items.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        for (int i = 0; i < 6; i++) { // ลดเหลือ 6 ช่องเพื่อให้ดู compact
            if (i < keys.length) {
                gridContainer.add(createModernCard(keys[i], items.get(keys[i])));
            } else {
                gridContainer.add(createElegantEmptySlot());
            }
        }
        root.add(gridContainer, BorderLayout.CENTER);

        // --- Footer Section ---
        JButton useBtn = new JButton("GIVE 🎁");
        useBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        useBtn.setForeground(Color.WHITE);
        useBtn.setFocusPainted(false);
        useBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        useBtn.setPreferredSize(new Dimension(0, 65));

        useBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // ไล่สีปุ่มให้ดูแพง
                GradientPaint btnGp = new GradientPaint(0, 0, new Color(255, 150, 200), 0, c.getHeight(), THEME_PINK);
                g2.setPaint(btnGp);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30);
                super.paint(g2, c);
                g2.dispose();
            }
        });

        useBtn.addActionListener(e -> {
            if (selectedItem != null) {
                onUse.accept(selectedItem);
                dispose();
            } else {
                showToast("Please select a gift 💖");
            }
        });
        root.add(useBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createModernCard(String name, int qty) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isSelected = name.equals(selectedItem);
                if (isSelected) {
                    // เอฟเฟกต์ตอนเลือก (Glow)
                    g2.setColor(new Color(255, 105, 180, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                    g2.setColor(THEME_PINK);
                    g2.setStroke(new BasicStroke(3f));
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                    g2.setColor(new Color(255, 182, 193, 80));
                    g2.setStroke(new BasicStroke(1.5f));
                }
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 35, 35);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Badge จำนวนไอเทม (Circle Style)
        JLabel lbQty = new JLabel(String.valueOf(qty));
        lbQty.setFont(new Font("Arial", Font.BOLD, 12));
        lbQty.setForeground(THEME_PINK);
        lbQty.setHorizontalAlignment(SwingConstants.CENTER);
        lbQty.setBorder(new EmptyBorder(2, 5, 2, 5));

        JPanel badgeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        badgeWrapper.setOpaque(false);
        badgeWrapper.add(lbQty);
        card.add(badgeWrapper, BorderLayout.NORTH);

        // ไอคอนตรงกลาง
        JLabel imgLabel = new JLabel(loadItemIcon(name, 75), SwingConstants.CENTER);
        card.add(imgLabel, BorderLayout.CENTER);

        // ชื่อด้านล่าง
        JLabel lbName = new JLabel(name, SwingConstants.CENTER);
        lbName.setFont(new Font("Tahoma", Font.BOLD, 13));
        lbName.setForeground(name.equals(selectedItem) ? THEME_PINK : new Color(120, 120, 120));
        lbName.setBorder(new EmptyBorder(0, 0, 12, 0));
        card.add(lbName, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedItem = name;
                card.getParent().repaint();
            }
        });
        return card;
    }

    private JPanel createElegantEmptySlot() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.setStroke(
                        new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 6 }, 0));
                g2.setColor(new Color(255, 182, 193, 100));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 35, 35);
                g2.dispose();
            }
        };
    }

    private void showToast(String msg) {
        JOptionPane.showMessageDialog(this, "<html><font face='Tahoma' color='#FF69B4'>" + msg + "</font></html>", "💖",
                JOptionPane.PLAIN_MESSAGE);
    }

    private ImageIcon loadFixedIcon(String name, int size) {
        ImageIcon icon = loadItemIcon(name, size);
        return (icon != null) ? icon : null;
    }

    private ImageIcon loadItemIcon(String itemName, int size) {
        String fileName = itemName.toLowerCase();
        if (fileName.contains("ดอกไม้"))
            fileName = "ชอดอกไม้";
        else if (fileName.contains("สร้อยคอ"))
            fileName = "สร้อยคอ";
        else if (fileName.contains("กาแฟ"))
            fileName = "cofe";
        else if (fileName.contains("ช็อกโกแลต"))
            fileName = "ช็อกโกแลท";
        else if (fileName.contains("เค้ก"))
            fileName = "เค็ก";
        else if (fileName.contains("โดนัท"))
            fileName = "โดนัท";

        String[] exts = { ".png", ".jpg", ".jpeg" };
        for (String ext : exts) {
            URL url = getClass().getResource("/images/icon/" + fileName + ext);
            if (url != null)
                return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
            File f = new File("src/main/resources/images/icon/" + fileName + ext);
            if (f.exists())
                return new ImageIcon(new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(size, size,
                        Image.SCALE_SMOOTH));
        }
        return null;
    }
}