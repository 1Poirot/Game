package com.game.multi.dating;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.function.BiFunction;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MultiDatingShop extends JDialog {
    private JLabel moneyLbl;
    private int currentMoney;
    private JLayeredPane layeredPane;
    private JPanel mainContent;

    private final int IMG_WIDTH = 120;
    private final int IMG_HEIGHT = 120;

    public MultiDatingShop(JFrame parent, int money, BiFunction<String, Integer, Boolean> onBuy) {
        super(parent, true);
        this.currentMoney = money;

        setUndecorated(true);
        setSize(parent.getWidth(), parent.getHeight());
        setLocation(parent.getLocation());

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        mainContent = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 245, 250), 0, getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContent.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(mainContent, JLayeredPane.DEFAULT_LAYER);

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 50, 10, 50));

        JButton btnBack = new JButton(
                "<html><font color='#FF1493' size='6'>◀</font> <font color='#FF1493' size='5'> BACK</font></html>");
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());

        // Hover Effect สำหรับปุ่ม Back
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBack.setText(
                        "<html><font color='#FF69B4' size='6'>◀</font> <font color='#FF69B4' size='5'> <u>BACK</u></font></html>");
            }

            public void mouseExited(MouseEvent e) {
                btnBack.setText(
                        "<html><font color='#FF1493' size='6'>◀</font> <font color='#FF1493' size='5'> BACK</font></html>");
            }
        });

        moneyLbl = new JLabel("<html><b style='font-family:Tahoma; font-size:24pt; color:#FF1493;'>👛 "
                + String.format("%,d", currentMoney) + "</b></html>");

        header.add(btnBack, BorderLayout.WEST);
        header.add(moneyLbl, BorderLayout.EAST);
        mainContent.add(header, BorderLayout.NORTH);

        // --- Grid Items ---
        JPanel grid = new JPanel(new GridLayout(2, 3, 30, 30));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(20, 60, 40, 60));

        String[] items = { "ดอกไม้", "สร้อยคอ", "กาแฟ", "ช็อกโกแลต", "เค้ก", "โดนัท" };
        int[] prices = { 250, 50, 80, 100, 150, 50 };

        // ชื่อไฟล์รูปภาพ (เช็ค cofe.jpg เรียบร้อย)
        String[] itemImages = {
                "ชอดอกไม้.png",
                "สร้อยคอ.png",
                "cofe.jpg",
                "ช็อกโกแลท.png",
                "เค็ก.png",
                "โดนัท.png"
        };

        for (int i = 0; i < items.length; i++) {
            grid.add(createItemCard(items[i], prices[i], itemImages[i], onBuy));
        }

        mainContent.add(grid, BorderLayout.CENTER);
    }

    private JPanel createItemCard(String name, int price, String fileName, BiFunction<String, Integer, Boolean> onBuy) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 192, 203), 2, true),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));

        // ✅ ระบบโหลดรูปภาพแบบ Double Check (Resource + File)
        try {
            Image img = null;
            // ลองหาแบบ Resource ก่อน
            URL imgURL = getClass().getResource("/images/icon/" + fileName);
            if (imgURL != null) {
                img = new ImageIcon(imgURL).getImage();
            } else {
                // ถ้าไม่เจอ ลองหาแบบ File Path ตรงๆ (สำหรับ VS Code)
                File f = new File("src/main/resources/images/icon/" + fileName);
                if (f.exists()) {
                    img = new ImageIcon(f.getAbsolutePath()).getImage();
                }
            }

            if (img != null) {
                imageLabel.setIcon(new ImageIcon(img.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH)));
            } else {
                imageLabel
                        .setText("<html><center><font color='#FFB6C1' size='5'>🖼</font><br>No Image</center></html>");
                System.err.println("🚨 ไม่พบรูป: " + fileName);
            }
        } catch (Exception e) {
            imageLabel.setText("Error");
        }

        JLabel nameLbl = new JLabel(name, SwingConstants.CENTER);
        nameLbl.setFont(new Font("Tahoma", Font.BOLD, 18));
        nameLbl.setForeground(new Color(150, 50, 80));

        JButton buyBtn = new JButton("Buy " + price);
        buyBtn.setBackground(new Color(255, 105, 180));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
        buyBtn.setFocusPainted(false);
        buyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buyBtn.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Hover Effect สำหรับปุ่ม Buy
        buyBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buyBtn.setBackground(new Color(255, 20, 147));
            }

            public void mouseExited(MouseEvent e) {
                buyBtn.setBackground(new Color(255, 105, 180));
            }
        });

        buyBtn.addActionListener(e -> {
            if (onBuy.apply(name, price)) {
                currentMoney -= price;
                updateMoneyDisplay();
                showPopupAnimation("ซื้อ " + name + " สำเร็จ!", true);
            } else {
                showPopupAnimation("เงินไม่พอจ้าา", false);
            }
        });

        card.add(nameLbl, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(buyBtn, BorderLayout.SOUTH);

        return card;
    }

    private void updateMoneyDisplay() {
        moneyLbl.setText("<html><b style='font-family:Tahoma; font-size:24pt; color:#FF1493;'>👛 "
                + String.format("%,d", currentMoney) + "</b></html>");
    }

    private void showPopupAnimation(String text, boolean success) {
        JLabel popup = new JLabel(text, SwingConstants.CENTER);
        popup.setFont(new Font("Tahoma", Font.BOLD, 22));
        popup.setOpaque(true);
        popup.setBackground(success ? new Color(255, 182, 193, 240) : new Color(255, 69, 0, 240));
        popup.setForeground(Color.WHITE);
        popup.setBorder(new LineBorder(Color.WHITE, 2, true));

        popup.setBounds((getWidth() - 350) / 2, getHeight() / 2, 350, 70);
        layeredPane.add(popup, JLayeredPane.POPUP_LAYER);

        Timer timer = new Timer(15, null);
        final int[] count = { 0 };
        timer.addActionListener(e -> {
            popup.setLocation(popup.getX(), popup.getY() - 1);
            count[0]++;
            if (count[0] > 50) {
                layeredPane.remove(popup);
                layeredPane.repaint();
                timer.stop();
            }
        });
        timer.start();
    }
}