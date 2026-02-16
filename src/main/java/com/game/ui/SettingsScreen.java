package com.game.ui;

import com.game.controllers.GameController;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SettingsScreen extends JPanel {
    private GameController controller;

    public SettingsScreen(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(255, 209, 220)); // สีพื้นหลังชมพูอ่อน

        // --- Header (ปุ่มย้อนกลับ) ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 20, 0, 0));

        JButton backBtn = new JButton("< ย้อนกลับ");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        backBtn.setBackground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(150, 50));
        backBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // เมื่อกด ย้อนกลับ ให้กลับไปหน้า Shop
        backBtn.addActionListener(e -> controller.showShop());

        header.add(backBtn);
        add(header, BorderLayout.NORTH);

        // --- Center Menu (กรอบเมนูตรงกลาง) ---
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        // กรอบสีขาวมนๆ
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                new EmptyBorder(40, 60, 40, 60)));

        // สร้างปุ่มต่างๆ ตามรูป
        String[] menuItems = { "เซฟเกม", "Profile", "กลับสู่เกม", "ตั้งค่า", "ออกจากเกม" };
        for (String text : menuItems) {
            menuPanel.add(createMenuButton(text));
            menuPanel.add(Box.createRigidArea(new Dimension(0, 15))); // เว้นระยะห่างระหว่างปุ่ม
        }

        centerContainer.add(menuPanel);
        add(centerContainer, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        // สร้าง JButton แบบ Custom เพื่อวาดขอบมนเอง
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // วาดพื้นหลังปุ่มให้โค้ดมน (ค่า 40 คือความกลม ยิ่งเยอะยิ่งกลม)
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                // วาดเส้นขอบสีดำให้โค้งตามปุ่ม
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 40, 40);

                // วาดตัวหนังสือ
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 105, 180)); // สีชมพูเข้ม

        // ตั้งค่าปุ่มให้โปร่งใสเพื่อโชว์ความโค้งที่เราวาดเอง
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.setPreferredSize(new Dimension(250, 60));
        btn.setMaximumSize(new Dimension(250, 60));

        // เอฟเฟกต์เมื่อเอาเมาส์ชี้ (Hover)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(255, 20, 147));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(255, 105, 180));
            }
        });

        if (text.equals("ออกจากเกม")) {
            btn.addActionListener(e -> System.exit(0));
        }

        return btn;
    }
}