package com.game.multi.dating;

import java.awt.*;
import java.util.function.BiFunction;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MultiDatingShop extends JDialog {
    private JLabel moneyLbl;
    private int currentMoney;
    private JLayeredPane layeredPane;
    private JPanel mainContent;

    // ขนาดมาตรฐานของรูปภาพไอเทมในแว็ก (ปรับได้ตามต้องการ)
    private final int IMG_WIDTH = 100;
    private final int IMG_HEIGHT = 100;

    public MultiDatingShop(JFrame parent, int money, BiFunction<String, Integer, Boolean> onBuy) {
        super(parent, true);
        this.currentMoney = money;

        setUndecorated(true);
        setBounds(parent.getBounds());

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        mainContent = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 240, 245), 0, getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContent.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(mainContent, JLayeredPane.DEFAULT_LAYER);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 50, 10, 50));

        JButton btnBack = new JButton(
                "<html><font color='#FF69B4' size='6'>‹</font> <font color='#FF1493' size='5'> BACK</font></html>");
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());

        moneyLbl = new JLabel("<html><b style='font-family:Tahoma; font-size:22pt; color:#FF1493;'>👛 "
                + String.format("%,d", currentMoney) + "</b></html>");

        header.add(btnBack, BorderLayout.WEST);
        header.add(moneyLbl, BorderLayout.EAST);
        mainContent.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 40, 40));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(30, 80, 50, 80));

        // ข้อมูลไอเทม
        String[] items = { "ดอกไม้", "ช็อกโกแลต", "กาแฟ", "ทิวลิป", "เค้กส้ม", "ครัวซองต์" };
        int[] prices = { 500, 150, 80, 200, 200, 200 };

        // ✅ เปลี่ยนจาก Emoji เป็น Path ของไฟล์รูปภาพ
        // สมมติว่าเก็บรูปไว้ที่ src/main/resources/images/items/
        String[] itemImages = {
                "images/icon/flower.png",
                "images/icon/chocolate.png",
                "images/icon/coffee.png",
                "images/icon/tulip.png",
                "images/icon/cake.png",
                "images/icon/croissant.png"
        };

        for (int i = 0; i < items.length; i++) {
            // ส่ง Path รูปภาพเข้าไปแทน String Emoji
            grid.add(createItemCard(items[i], prices[i], itemImages[i], onBuy));
        }

        mainContent.add(grid, BorderLayout.CENTER);
    }

    // ✅ ปรับ Method รับ Path ของรูปภาพ (imgPath)
    private JPanel createItemCard(String name, int price, String imgPath, BiFunction<String, Integer, Boolean> onBuy) {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 182, 193), 2, true),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT)); // กำหนดขนาดพื้นที่รูป

        try {
            // ✅ วิธีที่ 1: พยายามโหลดผ่าน ClassLoader (มาตรฐานสูงสุด)
            java.net.URL imgURL = getClass().getClassLoader().getResource(imgPath);

            // ✅ วิธีที่ 2: ถ้าวิธีแรกไม่เจอ ให้ลองเติม / ข้างหน้า (Absolute Path)
            if (imgURL == null) {
                imgURL = getClass().getResource("/" + imgPath);
            }

            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImg = originalIcon.getImage().getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                // ❌ กรณีหาไฟล์ไม่เจอจริงๆ: โชว์ข้อความ Error สีแดงแทนรูป
                imageLabel.setText(
                        "<html><center><font color='red' size='3'>✘</font><br><font size='2'>Not Found</font></center></html>");
                System.err.println("🚨 ยังหาไม่เจอที่: " + imgPath);
            }
        } catch (Exception e) {
            imageLabel.setText("Error");
            e.printStackTrace();
        }

        JLabel nameLbl = new JLabel(name, SwingConstants.CENTER);
        nameLbl.setForeground(new Color(199, 21, 133));
        nameLbl.setFont(new Font("Tahoma", Font.BOLD, 20));

        JButton buyBtn = new JButton("ซื้อเลย 💰 " + price);
        buyBtn.setBackground(new Color(255, 105, 180));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        buyBtn.setFocusPainted(false);
        buyBtn.setBorder(new EmptyBorder(12, 0, 12, 0));
        buyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buyBtn.addActionListener(e -> {
            if (onBuy.apply(name, price)) {
                currentMoney -= price;
                updateMoneyDisplay();
                showPopupAnimation("✨ ซื้อ " + name + " สำเร็จ! ✨", true);
            } else {
                showPopupAnimation("❌ เงินไม่พอจ้าา ❌", false);
            }
        });

        // จัดวาง: ชื่อบน - รูปกลาง - ปุ่มล่าง
        card.add(nameLbl, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(buyBtn, BorderLayout.SOUTH);

        return card;
    }

    private void updateMoneyDisplay() {
        moneyLbl.setText("<html><b style='font-family:Tahoma; font-size:22pt; color:#FF1493;'>👛 "
                + String.format("%,d", currentMoney) + "</b></html>");
    }

    private void showPopupAnimation(String text, boolean success) {
        JLabel popup = new JLabel(text, SwingConstants.CENTER);
        popup.setFont(new Font("Tahoma", Font.BOLD, 24));
        popup.setOpaque(true);
        popup.setBackground(success ? new Color(255, 192, 203, 230) : new Color(255, 99, 71, 230));
        popup.setForeground(Color.WHITE);
        popup.setBorder(new LineBorder(Color.WHITE, 3, true));

        int pWidth = 400;
        int pHeight = 80;
        int startX = (getWidth() - pWidth) / 2;
        int startY = (getHeight() - pHeight) / 2 + 50;

        popup.setBounds(startX, startY, pWidth, pHeight);
        layeredPane.add(popup, JLayeredPane.DRAG_LAYER);

        Timer timer = new Timer(10, null);
        final int[] y = { startY };

        timer.addActionListener(e -> {
            y[0] -= 2;
            popup.setLocation(startX, y[0]);

            if (y[0] < startY - 60) {
                layeredPane.remove(popup);
                layeredPane.repaint();
                timer.stop();
            }
        });

        timer.start();
    }
}