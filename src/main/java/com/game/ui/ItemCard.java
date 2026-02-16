package com.game.ui;

import com.game.models.Item;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class ItemCard extends JPanel {
    private Item item;
    private Image itemImage;

    // ในไฟล์ ItemCard.java ส่วน Constructor
    public ItemCard(Item item, Runnable onClick) {
        this.item = item;
        setPreferredSize(new Dimension(160, 210));
        setOpaque(false);

        try {
            // 1. ระบุโฟลเดอร์หลัก (ต้องมี / ปิดท้าย)
            String folderPath = "src/main/resources/images/ui/items/";

            // 2. ดึงชื่อไฟล์จาก Item (เช่น "546546546.jpg")
            String fileName = item.getImagePath();

            // 3. รวมร่างเป็น Path เต็ม
            String fullPath = folderPath + fileName;

            // ลองพิมพ์ออกมาดูใน Console ว่า Path ถูกไหม
            System.out.println("กำลังโหลดรูปไอเทมจาก: " + fullPath);

            this.itemImage = new ImageIcon(fullPath).getImage();

            // เช็คว่าโหลดสำเร็จไหม (ถ้า width เป็น -1 คือหาไฟล์ไม่เจอ)
            if (this.itemImage.getWidth(null) == -1) {
                System.err.println("!!! หาไฟล์ไม่เจอที่: " + fullPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onClick.run();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 40;

        // 1. วาดตัวการ์ดขาว
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

        // 2. วาดรูปไอเทม (ถ้ามี)
        if (itemImage != null) {
            g2.drawImage(itemImage, (w - 100) / 2, 20, 100, 100, this);
        }

        // 3. วาดแถบดำด้านล่าง
        g2.setColor(Color.BLACK);
        Shape oldClip = g2.getClip();
        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
        g2.fillRect(0, h - 50, w, 50);
        g2.setClip(oldClip);

        // 4. วาดชื่อไอเทม และ ราคา
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();

        g2.setColor(Color.BLACK);
        g2.drawString("ITEM", (w - fm.stringWidth("ITEM")) / 2, h - 65);

        g2.setColor(Color.WHITE);
        String price = String.valueOf(item.getPrice());
        g2.drawString(price, (w - fm.stringWidth(price)) / 2, h - 18);

        // 5. เส้นขอบ
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new RoundRectangle2D.Float(2, 2, w - 4, h - 4, arc, arc));

        g2.dispose();
    }
}