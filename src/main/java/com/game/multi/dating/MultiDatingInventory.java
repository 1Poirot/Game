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
    private final Color THEME_PINK = new Color(255, 20, 147);
    private final Color SOFT_PINK = new Color(255, 245, 250);
    private final Color GRID_EMPTY = new Color(255, 255, 255, 140); // ปรับให้ชัดขึ้นนิดนึง

    public MultiDatingInventory(JFrame parent, Map<String, Integer> items, Consumer<String> onUse) {
        super(parent, "Storage", true);
        setSize(520, 680);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), SOFT_PINK);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2d.setColor(THEME_PINK);
                g2d.setStroke(new BasicStroke(6)); // เพิ่มความหนาเส้นขอบ
                g2d.drawRoundRect(3, 3, getWidth() - 7, getHeight() - 7, 40, 40);
                g2d.dispose();
            }
        };
        root.setBorder(new EmptyBorder(25, 30, 25, 30));
        setContentPane(root);

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // ✅ แก้สี่เหลี่ยม: ใช้ฟอนต์ Segoe UI Emoji หรือใส่เป็นข้อความธรรมดาแต่เน้นฟอนต์
        JLabel title = new JLabel("STORAGE", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 32));
        title.setForeground(THEME_PINK);
        // เพิ่มไอคอนกระเป๋าจากรูปภาพ (ถ้าไม่มีจะโชว์แค่ข้อความ)
        title.setIcon(loadFixedIcon("flower", 40));
        header.add(title, BorderLayout.CENTER);

        // ✅ แก้สี่เหลี่ยมปุ่มปิด: ใช้ตัว "X" แทนสัญลักษณ์พิเศษ
        JButton closeBtn = new JButton("X");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 24));
        closeBtn.setForeground(THEME_PINK);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // --- Center: Grid 3x3 ---
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        gridPanel.setOpaque(false);

        String[] keys = items.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        for (int i = 0; i < 9; i++) {
            if (i < keys.length) {
                gridPanel.add(createItemCard(keys[i], items.get(keys[i])));
            } else {
                gridPanel.add(createEmptySlot());
            }
        }
        root.add(gridPanel, BorderLayout.CENTER);

        // --- Footer ---
        // ✅ แก้สี่เหลี่ยมปุ่มให้ของ: ใช้ข้อความเน้นๆ และรูปประกอบ
        JButton useBtn = new JButton("GIVE GIFT");
        useBtn.setFont(new Font("Tahoma", Font.BOLD, 28));
        useBtn.setBackground(THEME_PINK);
        useBtn.setForeground(Color.WHITE);
        useBtn.setFocusPainted(false);
        useBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        useBtn.setPreferredSize(new Dimension(0, 85));

        useBtn.addActionListener(e -> {
            if (selectedItem != null) {
                int currentQty = items.get(selectedItem);
                items.put(selectedItem, currentQty - 1);
                onUse.accept(selectedItem);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "เลือกของขวัญก่อนนะจ๊ะ! 💖");
            }
        });
        root.add(useBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createItemCard(String name, int qty) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (name.equals(selectedItem)) {
                    g2.setColor(THEME_PINK);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    g2.setColor(new Color(255, 182, 193, 150));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);
                }
                g2.dispose();
            }
        };
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lbQty = new JLabel("x" + qty);
        lbQty.setFont(new Font("Arial", Font.BOLD, 15));
        lbQty.setForeground(name.equals(selectedItem) ? Color.WHITE : THEME_PINK);
        lbQty.setHorizontalAlignment(SwingConstants.RIGHT);
        lbQty.setBorder(new EmptyBorder(8, 0, 0, 10));
        card.add(lbQty, BorderLayout.NORTH);

        JLabel lbName = new JLabel(name, SwingConstants.CENTER);
        lbName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbName.setForeground(name.equals(selectedItem) ? Color.WHITE : Color.DARK_GRAY);
        lbName.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(lbName, BorderLayout.SOUTH);

        JLabel imgLabel = new JLabel(loadItemIcon(name), SwingConstants.CENTER);
        card.add(imgLabel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedItem = name;
                card.getParent().repaint();
            }
        });
        return card;
    }

    private JPanel createEmptySlot() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRID_EMPTY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                // วาดเส้นประจางๆ ให้ดูเป็นช่องเก็บของ
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 5 }, 0));
                g2.setColor(new Color(255, 20, 147, 50));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 25, 25);
                g2.dispose();
            }
        };
    }

    // Helper สำหรับโหลดไอคอนพรีเมียม (แก้ปัญหาสี่เหลี่ยม)
    private ImageIcon loadFixedIcon(String name, int size) {
        ImageIcon icon = loadItemIcon(name);
        if (icon != null) {
            return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
        }
        return null;
    }

    private ImageIcon loadItemIcon(String itemName) {
        String fileName = itemName.toLowerCase();
        if (fileName.contains("ดอกไม้"))
            fileName = "ชอดอกไม้";
        if (fileName.contains("สร้อยคอ"))
            fileName = "สร้อยคอ";
        if (fileName.contains("กาแฟ"))
            fileName = "cofe";
        if (fileName.contains("ช็อกโกแลต"))
            fileName = "ช็อกโกแลท";
        if (fileName.contains("เค้ก"))
            fileName = "เค็ก";
        if (fileName.contains("โดนัท"))
            fileName = "โดนัท";

        try {
            Image img = null;
            String[] exts = { ".png", ".jpg", ".jpeg" };
            for (String ext : exts) {
                URL url = getClass().getResource("/images/icon/" + fileName + ext);
                if (url != null) {
                    img = new ImageIcon(url).getImage();
                    break;
                }
                File f = new File("src/main/resources/images/icon/" + fileName + ext);
                if (f.exists()) {
                    img = new ImageIcon(f.getAbsolutePath()).getImage();
                    break;
                }
            }
            if (img != null) {
                return new ImageIcon(img.getScaledInstance(85, 85, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * เมธอดสำหรับอัปเดตข้อมูลใน List
     */
    private void updateListModel(DefaultListModel<String> model, Map<String, Integer> items) {
        model.clear();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            if (entry.getValue() > 0) {
                model.addElement(entry.getKey() + " (มี " + entry.getValue() + " ชิ้น)");
            }
        }
    }
}