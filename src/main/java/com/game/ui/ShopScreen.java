package com.game.ui;

import com.game.controllers.GameController;
import com.game.models.Item;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ShopScreen extends JPanel {
    private Image bgImage;
    private GameController controller;
    private boolean isBlurred = false; // ตัวแปรคุมสถานะการเบลอ

    public ShopScreen(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        try {
            // โหลด Background
            String path = "src/main/resources/images/backgrounds/2524254.jpg";
            bgImage = new ImageIcon(path).getImage();
            if (bgImage.getWidth(null) == -1) {
                System.out.println("ยังหาไฟล์ไม่เจอที่: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- 1. Header Section (วางไว้ทิศเหนือ ไม่โดนแผ่นฟิล์มบัง) ---
        add(createHeader(), BorderLayout.NORTH);

        // --- 2. Center Section ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel itemPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        itemPanel.setBackground(Color.decode("#f3dff7"));
        itemPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        for (Item item : controller.getShopSystem().getAvailableItems()) {
            itemPanel.add(new ItemCard(item, () -> handleItemClick(item)));
        }

        centerWrapper.add(itemPanel);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 20, 0, 20));

        JButton shopBtn = new JButton("< SHOP");
        shopBtn.setBackground(new Color(255, 105, 180));
        shopBtn.setForeground(Color.WHITE);
        shopBtn.setFont(new Font("Arial", Font.BOLD, 18));
        shopBtn.setPreferredSize(new Dimension(120, 45));

        JButton settingsBtn = new JButton();
        try {
            ImageIcon settingsIcon = new ImageIcon(getClass().getResource("/images/ui/settings_icon.png"));
            Image img = settingsIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            settingsBtn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            settingsBtn.setText("⚙");
        }

        settingsBtn.addActionListener(e -> {
            controller.showSettings(); // เมื่อกดปุ่ม จะสลับไปหน้าตั้งค่าทันที
        });
        
        settingsBtn.setBackground(Color.WHITE);
        settingsBtn.setPreferredSize(new Dimension(50, 50));

        header.add(shopBtn, BorderLayout.WEST);
        header.add(settingsBtn, BorderLayout.EAST);
        return header;

    }

    private void handleItemClick(Item item) {
        // เริ่มการเบลอ (วาดสีขาวขุ่นทับ)
        isBlurred = true;
        repaint();

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        BuyConfirmDialog dialog = new BuyConfirmDialog(topFrame, item);
        dialog.setVisible(true);

        // ตรวจสอบการซื้อ
        if (dialog.isConfirmed()) {
            boolean success = controller.getShopSystem().buyItem(controller.getPlayer(), item);
            if (success) {
                JOptionPane.showMessageDialog(this, "ซื้อสำเร็จแล้ว! เหลือเงิน: " + controller.getPlayer().getMoney());
                controller.showShop();
            } else {
                JOptionPane.showMessageDialog(this, "เงินไม่พอสำหรับการซื้อไอเทมนี้", "ยอดเงินคงเหลือไม่พอ",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // ปิดการเบลอเมื่อ Dialog ปิดลง
        isBlurred = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // 1. วาดพื้นหลังปกติ
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }

        // 2. ถ้าสถานะคือเบลอ ให้ฉาบสีขาวใสทับทั้งหน้าจอ
        if (isBlurred) {
            // ปรับค่า 150 (0-255) ตามความขุ่นที่ต้องการ
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
    }
}