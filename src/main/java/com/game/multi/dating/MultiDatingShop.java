package com.game.multi.dating;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.function.BiFunction;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MultiDatingShop extends JDialog {
    private JLabel moneyLbl;
    private int currentMoney;
    private JLayeredPane layeredPane;
    private JPanel mainContent;

    private final Color THEME_PINK = new Color(255, 20, 147);
    private final Color SOFT_PINK = new Color(255, 240, 245);
    private final Color PREMIUM_WHITE = new Color(255, 255, 255, 245);

    private final int IMG_WIDTH = 130;
    private final int IMG_HEIGHT = 130;

    public MultiDatingShop(JFrame parent, int money, BiFunction<String, Integer, Boolean> onBuy) {
        super(parent, true);
        this.currentMoney = money;

        setUndecorated(true);
        setSize(parent.getWidth(), parent.getHeight());
        setLocation(parent.getLocation());

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // --- พื้นหลังไล่เฉดสีนุ่มนวล ---
        mainContent = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(255, 220, 235));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainContent.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(mainContent, JLayeredPane.DEFAULT_LAYER);

        // --- 🌸 Header (Back & Money) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(35, 60, 20, 60));

        JButton btnBack = new JButton(
                "<html><b style='font-family:Segoe UI Emoji; font-size:20pt; color:#FF1493;'>◀ BACK</b></html>");
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());

        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBack.setForeground(new Color(255, 105, 180));
            }

            public void mouseExited(MouseEvent e) {
                btnBack.setForeground(THEME_PINK);
            }
        });

        moneyLbl = new JLabel(
                "<html><div style='background: white; padding: 12px 30px; border-radius: 25px; border: 3px solid #FFB6C1;'>"
                        + "<b style='font-family:Segoe UI Emoji; font-size:22pt; color:#FF1493;'>👛 "
                        + String.format("%,d", currentMoney) + "</b></div></html>");

        header.add(btnBack, BorderLayout.WEST);
        header.add(moneyLbl, BorderLayout.EAST);
        mainContent.add(header, BorderLayout.NORTH);

        // --- 🎁 Grid Items ---
        JPanel grid = new JPanel(new GridLayout(2, 3, 40, 40));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(20, 100, 60, 100));

        String[] items = { "ดอกไม้", "สร้อยคอ", "กาแฟ", "ช็อกโกแลต", "เค้ก", "โดนัท" };
        int[] prices = { 250, 500, 80, 120, 180, 60 };
        String[] itemImages = { "ชอดอกไม้.png", "สร้อยคอ.png", "cofe.jpg", "ช็อกโกแลท.png", "เค็ก.png", "โดนัท.png" };

        for (int i = 0; i < items.length; i++) {
            grid.add(createPremiumCard(items[i], prices[i], itemImages[i], onBuy));
        }

        mainContent.add(grid, BorderLayout.CENTER);
    }

    private JPanel createPremiumCard(String name, int price, String fileName,
            BiFunction<String, Integer, Boolean> onBuy) {
        JPanel card = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw Card Shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 45, 45);
                // Draw Card Body
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 45, 45);
                // Draw Border
                g2d.setColor(new Color(255, 182, 193, 150));
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 45, 45);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        loadItemImage(imageLabel, fileName);

        JLabel nameLbl = new JLabel(name, SwingConstants.CENTER);
        nameLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        nameLbl.setForeground(new Color(120, 50, 80));

        // ปุ่มซื้อสไตล์โมเดิร์น
        JButton buyBtn = new JButton("BUY - " + price) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isPressed = getModel().isPressed();
                boolean isOver = getModel().isRollover();

                GradientPaint gp = new GradientPaint(0, 0,
                        isPressed ? THEME_PINK.darker()
                                : (isOver ? new Color(255, 105, 180) : new Color(255, 140, 190)),
                        0, getHeight(), THEME_PINK);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        buyBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setContentAreaFilled(false);
        buyBtn.setBorderPainted(false);
        buyBtn.setFocusPainted(false);
        buyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buyBtn.setPreferredSize(new Dimension(0, 50));

        buyBtn.addActionListener(e -> {
            if (onBuy.apply(name, price)) {
                currentMoney -= price;
                updateMoneyDisplay();
                showCenteredHeroPopup("✨ ซื้อ " + name + " สำเร็จ! ✨", true);
            } else {
                showCenteredHeroPopup("❌ เงินไม่พอจ้าา", false);
            }
        });

        card.add(nameLbl, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(buyBtn, BorderLayout.SOUTH);

        return card;
    }

    private void loadItemImage(JLabel label, String fileName) {
        try {
            Image img = null;
            URL imgURL = getClass().getResource("/images/icon/" + fileName);
            if (imgURL != null)
                img = new ImageIcon(imgURL).getImage();
            else {
                File f = new File("src/main/resources/images/icon/" + fileName);
                if (f.exists())
                    img = new ImageIcon(f.getAbsolutePath()).getImage();
            }
            if (img != null)
                label.setIcon(new ImageIcon(img.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH)));
            else
                label.setText("<html><font color='#FFB6C1' size='5'>🖼</font></html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMoneyDisplay() {
        moneyLbl.setText(
                "<html><div style='background: white; padding: 12px 30px; border-radius: 25px; border: 3px solid #FFB6C1;'>"
                        + "<b style='font-family:Segoe UI Emoji; font-size:22pt; color:#FF1493;'>👛 "
                        + String.format("%,d", currentMoney) + "</b></div></html>");
    }

    // ✅ ฟังก์ชันแจ้งเตือนแบบพรีเมียม ขึ้นตรงกลางหน้าจอ
    private void showCenteredHeroPopup(String text, boolean success) {
        final JLabel popup = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background ของป๊อปอัพ
                g2d.setColor(success ? new Color(255, 20, 147, 230) : new Color(220, 20, 60, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                // Border ของป๊อปอัพ
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 40, 40);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        popup.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        popup.setForeground(Color.WHITE);

        // จัดตำแหน่งให้อยู่กึ่งกลางหน้าจอพอดี
        int pWidth = 450;
        int pHeight = 100;
        int px = (getWidth() - pWidth) / 2;
        int py = (getHeight() - pHeight) / 2;
        popup.setBounds(px, py, pWidth, pHeight);

        layeredPane.add(popup, JLayeredPane.DRAG_LAYER); // นำมาไว้หน้าสุด
        layeredPane.revalidate();
        layeredPane.repaint();

        // Animation: ลอยขึ้นนิดหน่อยแล้วหายไป
        Timer timer = new Timer(25, null);
        final int[] frame = { 0 };
        timer.addActionListener(e -> {
            frame[0]++;
            popup.setLocation(popup.getX(), popup.getY() - 1);
            if (frame[0] > 40) {
                layeredPane.remove(popup);
                layeredPane.repaint();
                timer.stop();
            }
        });
        timer.setInitialDelay(1200); // แสดงค้างไว้ 1.2 วินาทีก่อนเริ่มหาย
        timer.start();
    }
}