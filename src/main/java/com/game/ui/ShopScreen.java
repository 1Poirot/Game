package com.game.ui;

import com.game.controllers.GameController;
import com.game.models.Item;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ShopScreen extends JPanel {
    private Image bgImage;
    private GameController controller;
    private boolean isBlurred = false;

    public ShopScreen(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // โหลด Background แบบเช็ค Error
        try {
            bgImage = new ImageIcon("src/main/resources/images/backgrounds/2524254.jpg").getImage();
        } catch (Exception e) {
            System.out.println("Background image not found!");
        }

        // --- 1. Header Section ---
        add(createEnhancedHeader(), BorderLayout.NORTH);

        // --- 2. Center Section (Item Grid) ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // แผงรวมไอเทมแบบกระจกฝ้า (Glass Panel)
        JPanel itemPanel = new JPanel(new GridLayout(0, 3, 25, 25)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // พื้นหลังสีม่วงอ่อนแบบโปร่งใส
                g2.setColor(new Color(243, 223, 247, 180)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                // เส้นขอบสีขาวจางๆ ให้ดูมีมิติ
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(255, 255, 255, 150));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 30, 30);
                g2.dispose();
            }
        };
        itemPanel.setOpaque(false);
        itemPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // ดึงไอเทมมาใส่
        for (Item item : controller.getShopSystem().getAvailableItems()) {
            itemPanel.add(new ItemCard(item, () -> handleItemClick(item)));
        }

        centerWrapper.add(itemPanel);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private JPanel createEnhancedHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(25, 30, 10, 30));

        // ปุ่ม SHOP (สไตล์เก๋ๆ)
        JButton shopBtn = createStyledButton("❤ SHOP MENU", new Color(255, 105, 180));
        
        // ปุ่ม Settings (สไตล์วงกลม)
        JButton settingsBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(new Color(220, 220, 220));
                else if (getModel().isRollover()) g2.setColor(new Color(245, 245, 245));
                else g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(200, 200, 200));
                g2.drawOval(0, 0, getWidth()-1, getHeight()-1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        settingsBtn.setText("⚙");
        settingsBtn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        settingsBtn.setContentAreaFilled(false);
        settingsBtn.setBorderPainted(false);
        settingsBtn.setFocusPainted(false);
        settingsBtn.setPreferredSize(new Dimension(55, 55));
        settingsBtn.addActionListener(e -> controller.showSettings());

        header.add(shopBtn, BorderLayout.WEST);
        header.add(settingsBtn, BorderLayout.EAST);
        return header;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 45));
        return btn;
    }

    private void handleItemClick(Item item) {
        isBlurred = true;
        repaint();

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        BuyConfirmDialog dialog = new BuyConfirmDialog(topFrame, item);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            boolean success = controller.getShopSystem().buyItem(controller.getPlayer(), item);
            if (success) {
                showCustomMessage("ซื้อสำเร็จ!", "เหลือเงิน: " + controller.getPlayer().getMoney() + " Gold", JOptionPane.INFORMATION_MESSAGE);
                controller.showShop();
            } else {
                showCustomMessage("เงินไม่พอ!", "ยอดเงินของคุณไม่เพียงพอ", JOptionPane.ERROR_MESSAGE);
            }
        }

        isBlurred = false;
        repaint();
    }

    private void showCustomMessage(String title, String msg, int type) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
        // วาดพื้นหลัง
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }

        // เอฟเฟกต์เบลอ (ฉาบแผ่นฟิล์มสีขาวนวล)
        if (isBlurred) {
            g2.setColor(new Color(255, 255, 255, 180)); // เพิ่มความขุ่นขึ้นเล็กน้อย
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
    }
}