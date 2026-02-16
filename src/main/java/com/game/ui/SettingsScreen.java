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
        setBackground(new Color(255, 209, 220)); 

        // --- Header (ปุ่มย้อนกลับ) ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(40, 40, 0, 0));

        JButton backBtn = createMenuButton("ย้อนกลับ");
        backBtn.addActionListener(e -> controller.showShop());
        header.add(backBtn);
        add(header, BorderLayout.NORTH);

        // --- Center Menu ---
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                new EmptyBorder(50, 80, 50, 80)));

        // รายการเมนู
        String[] menuItems = { "เซฟเกม", "Profile", "กลับสู่เกม", "ตั้งค่า", "ออกจากเกม" };
        
        for (String text : menuItems) {
            JButton btn = createMenuButton(text);
            
            // --- แก้ปัญหาภาษาไทยเพี้ยนด้วยการตั้ง ActionCommand เป็นภาษาอังกฤษ ---
            if (text.equals("เซฟเกม")) btn.setActionCommand("SAVE_GAME");
            else if (text.equals("ตั้งค่า")) btn.setActionCommand("AUDIO_SETTINGS");
            else if (text.equals("กลับสู่เกม")) btn.setActionCommand("BACK_TO_GAME");
            else if (text.equals("ออกจากเกม")) btn.setActionCommand("EXIT_GAME");
            else btn.setActionCommand(text);

            btn.addActionListener(e -> {
                String cmd = e.getActionCommand();
                System.out.println("Command Triggered: " + cmd); // จะเห็นเป็นภาษาอังกฤษใน Console

                switch (cmd) {
                    case "SAVE_GAME":
                        controller.showSaveScreen(); // ไปหน้าเซฟเกม
                        break;
                    case "AUDIO_SETTINGS":
                        controller.showAudioSettings(); // ไปหน้าตั้งค่าเสียง
                        break;
                    case "BACK_TO_GAME":
                        controller.showShop(); // กลับไปหน้าหลัก
                        break;
                    case "EXIT_GAME":
                        System.exit(0); // ปิดเกม
                        break;
                    default:
                        System.out.println("Clicked other: " + text);
                        break;
                }
            });

            menuPanel.add(btn);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        }

        centerContainer.add(menuPanel);
        add(centerContainer, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // วาดปุ่มมน
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                
                // วาดขอบดำ
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 40, 40);
                
                // วาดข้อความ
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 5;
                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        btn.setFont(new Font("Tahoma", Font.BOLD, 26)); 
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 105, 180));
        btn.setPreferredSize(new Dimension(300, 70));
        btn.setMaximumSize(new Dimension(300, 70));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(255, 20, 147)); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(new Color(255, 105, 180)); }
        });

        return btn;
    }
}