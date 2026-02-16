package com.game.ui;

import com.game.models.Item;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BuyConfirmDialog extends JDialog {
    private boolean confirmed = false;

    public BuyConfirmDialog(JFrame parent, Item item) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        // ในไฟล์ BuyConfirmDialog.java แก้ตรงส่วน paintComponent ใน mainPanel
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#f3dff7"));
                // วาดแค่สี่เหลี่ยมขอบมนสีชมพู (ไม่ต้องมีคำสั่ง g2.draw)
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setOpaque(false);

        // ส่วนแสดงรูปไอเทม (ใช้ ItemCard ที่มีอยู่มาโชว์)
        ItemCard bigCard = new ItemCard(item, () -> {
        });
        bigCard.setPreferredSize(new Dimension(180, 240));

        // แถบปุ่ม ยืนยัน และ ยกเลิก
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        JButton confirmBtn = createPinkButton("ยืนยัน");
        JButton cancelBtn = createPinkButton("ยกเลิก");

        confirmBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancelBtn.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(bigCard, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(parent);
    }

    private JButton createPinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 50));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // เอฟเฟกต์เมื่อเอาเมาส์ชี้
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(255, 240, 245));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}