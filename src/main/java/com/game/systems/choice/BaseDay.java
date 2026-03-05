package com.game.systems.choice;

import com.game.controllers.GameController;
import com.game.ui.SettingsScreen;
import java.awt.*;
import javax.swing.*;

public abstract class BaseDay {

    protected JFrame FRAME;
    protected JPanel BG_VIEW;
    protected JButton BTN_SETTINGS;
    protected GameController controller;

    protected void initBaseUI(JFrame frame, JPanel bgView, GameController controller) {

        this.FRAME = frame;
        this.BG_VIEW = bgView;
        this.controller = controller;

        createSettingsButton();
    }

    private void createSettingsButton() {

    BTN_SETTINGS = new JButton("🌸") {

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // ===== เงานุ่ม =====
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(4, 6, w - 4, h - 4, 35, 35);

            // ===== ไล่เฉดชมพูพาสเทล =====
            GradientPaint gp;

            if (getModel().isRollover()) {
                gp = new GradientPaint(
                        0, 0, new Color(255, 200, 220),
                        0, h, new Color(255, 170, 200)
                );
            } else {
                gp = new GradientPaint(
                        0, 0, new Color(255, 225, 235),
                        0, h, new Color(245, 205, 225)
                );
            }

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w - 4, h - 4, 35, 35);

            // ===== ขอบขาวบาง =====
            g2.setColor(new Color(255, 255, 255, 200));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(0, 0, w - 5, h - 5, 35, 35);

            super.paintComponent(g);
        }
    };

    // 🌸 ตั้งค่า font ตรงนี้ (ถูกต้อง)
    BTN_SETTINGS.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
    BTN_SETTINGS.setForeground(new Color(190, 60, 110));
    BTN_SETTINGS.setFocusPainted(false);
    BTN_SETTINGS.setBorderPainted(false);
    BTN_SETTINGS.setContentAreaFilled(false);
    BTN_SETTINGS.setCursor(new Cursor(Cursor.HAND_CURSOR));
    BTN_SETTINGS.setOpaque(false);

    BTN_SETTINGS.addActionListener(e -> {
        FRAME.setContentPane(new SettingsScreen(controller));
        FRAME.revalidate();
        FRAME.repaint();
    });

    BG_VIEW.add(BTN_SETTINGS);
    BG_VIEW.setComponentZOrder(BTN_SETTINGS, 0);

    FRAME.addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
        public void componentResized(java.awt.event.ComponentEvent e) {
            positionButton();
        }
    });

    positionButton();
}

   private void positionButton() {

    int w = FRAME.getContentPane().getWidth();

    // ขยายใหญ่ขึ้น
    BTN_SETTINGS.setBounds(w - 125, 25, 85, 60);
}
protected static JFrame GLOBAL_FRAME;

public static void setGlobalFrame(JFrame frame) {
    GLOBAL_FRAME = frame;
}
}