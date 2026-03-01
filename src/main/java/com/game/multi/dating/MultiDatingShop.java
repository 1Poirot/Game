package com.game.multi.dating;

import java.awt.*;
import java.util.function.BiFunction;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MultiDatingShop extends JDialog {
    public MultiDatingShop(JFrame parent, int money, BiFunction<String, Integer, Boolean> onBuy) {
        super(parent, true); // Modal dialog

        // ✅ 1. ตั้งค่าให้แสดงผลเต็มจอ (Full Screen)
        setUndecorated(true); // เอาขอบหน้าต่างและปุ่ม X ออก
        setBounds(parent.getBounds()); // ให้ขนาดเท่ากับหน้าจอหลักเป๊ะๆ

        setLayout(new BorderLayout());

        // Background Image
        JLabel bg = new JLabel(new ImageIcon("src/main/resources/images/shop_bg.jpg")); // ใส่ path รูปหลังร้าน
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        // Header (SHOP Title & Money)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        // ปุ่มย้อนกลับ ( < SHOP )
        JButton btnBack = new JButton(
                "<html><b style='font-family:Tahoma; font-size:18pt; color:#FF69B4;'>‹ SHOP</b></html>");
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.addActionListener(e -> dispose());

        JLabel moneyLbl = new JLabel(
                "<html><b style='font-family:Tahoma; font-size:18pt; color:#333;'>💰 " + money + "</b></html>");

        header.add(btnBack, BorderLayout.WEST);
        header.add(moneyLbl, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Item Grid (6 ช่องตามรูป)
        JPanel grid = new JPanel(new GridLayout(2, 3, 30, 30));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(40, 60, 40, 60));

        String[] items = { "ดอกไม้", "ช็อกโกแลต", "กาแฟ", "ทิวลิป", "เค้กส้ม", "ครัวซองต์" };
        int[] prices = { 50, 100, 30, 70, 150, 45 };
        String[] icons = { "💐", "🍫", "☕", "🌷", "🍰", "🥐" };

        for (int i = 0; i < items.length; i++) {
            grid.add(createItemCard(items[i], prices[i], icons[i], onBuy));
        }

        add(grid, BorderLayout.CENTER);
    }

    private JPanel createItemCard(String name, int price, String iconStr, BiFunction<String, Integer, Boolean> onBuy) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 240)); // ขาวนวลโปร่งแสง
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 182, 193), 3, true), // ขอบชมพูโค้ง
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel icon = new JLabel(iconStr, SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));

        // ปรับปุ่มซื้อให้ดูพรีเมียม
        JButton buyBtn = new JButton(
                "<html><center><b>" + name + "</b><br><font color='#FFD700'>💰 " + price + "</font></center></html>");
        buyBtn.setBackground(new Color(50, 50, 50)); // สีเทาเข้มเกือบดำ
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        buyBtn.setFocusPainted(false);
        buyBtn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        buyBtn.addActionListener(e -> {
            if (onBuy.apply(name, price)) {
                // ไม่ต้องใส่ JOptionPane เด้งขวางหน้าจอ ใช้การอัปเดตตัวเลขเงินแทนถ้าเป็นไปได้
                // แต่ถ้าจะใส่ ก็ไม่ต้อง dispose(); หน้าจอจะได้ไม่หาย
                System.out.println("ซื้อ " + name + " สำเร็จ");
            } else {
                JOptionPane.showMessageDialog(this, "เงินไม่พอจ้า!");
            }
        });

        card.add(icon, BorderLayout.CENTER);
        card.add(buyBtn, BorderLayout.SOUTH);
        return card;
    }
}