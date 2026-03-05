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

        // ================= BACKGROUND =================
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 230, 240),
                        0, getHeight(), new Color(240, 220, 255)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        background.setLayout(new BorderLayout());
        add(background);

       // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(40, 60, 20, 60));

        JLabel title = new JLabel("🌸  Settings  🌸", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        title.setForeground(new Color(160, 60, 110));

        JButton backBtn = createModernButton("← กลับ");
        backBtn.setPreferredSize(new Dimension(140, 45));
        backBtn.addActionListener(e -> controller.backToPreviousScreen());

        // 👇 สร้างตัวดันด้านขวาให้กว้างเท่าปุ่ม
        JPanel rightSpace = new JPanel();
        rightSpace.setOpaque(false);
        rightSpace.setPreferredSize(new Dimension(140, 45));

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(rightSpace, BorderLayout.EAST);

        background.add(header, BorderLayout.NORTH);

        // ================= CENTER CARD =================
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel card = new ShadowPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(60, 100, 60, 100));
        card.setPreferredSize(new Dimension(520, 520));

        String[] menu = {
                "เซฟเกม",
                "กลับสู่เกม",
                "ตั้งค่าเสียง",
                "ออกจากเกม"
        };

        for (String text : menu) {

            JButton btn = createModernButton(text);

            btn.addActionListener(e -> {
                switch (text) {
                    case "เซฟเกม":
                        controller.showSaveScreen();
                        break;
                    case "ตั้งค่าเสียง":
                        controller.showAudioSettings();
                        break;
                    case "ออกจากเกม":
                        controller.exitGame();
                        break;
                    case "กลับสู่เกม":
                        controller.showGameScene();
                        break;
                }
            });

            card.add(btn);
            card.add(Box.createRigidArea(new Dimension(0, 30)));
        }

        wrapper.add(card);
        background.add(wrapper, BorderLayout.CENTER);
    }

    // ================= MODERN BUTTON =================
    private JButton createModernButton(String text) {

        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp;

                if (getModel().isRollover()) {
                    gp = new GradientPaint(
                            0, 0, new Color(255, 182, 193),
                            0, getHeight(), new Color(255, 140, 170)
                    );
                } else {
                    gp = new GradientPaint(
                            0, 0, new Color(255, 150, 180),
                            0, getHeight(), new Color(255, 105, 150)
                    );
                }

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Tahoma", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(320, 65));
        btn.setMaximumSize(new Dimension(320, 65));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        return btn;
    }

    // ================= SHADOW PANEL =================
    class ShadowPanel extends JPanel {

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(10, 10, getWidth() - 10, getHeight() - 10, 40, 40);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 40, 40);
        }
    }
}