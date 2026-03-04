package com.game.ui;

import com.game.controllers.GameController;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MenuGame extends JPanel {

    private GameController controller;
    private JLayeredPane layeredPane;

    public MenuGame(GameController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        if (controller.getAudioSystem() != null) {
            controller.getAudioSystem().playBGM("backgroundhome.wav");
        }

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutComponents();
            }
        });
    }

    private void layoutComponents() {
        int screenW = layeredPane.getWidth();
        int screenH = layeredPane.getHeight();

        if (screenW == 0 || screenH == 0)
            return;

        layeredPane.removeAll();

        // ===== Background =====
        JLabel background = createScaledImage(
                "src/main/resources/images/backgrounds/หน้าโรงเรียน.png",
                screenW, screenH);
        background.setBounds(0, 0, screenW, screenH);
        layeredPane.add(background, Integer.valueOf(0));

        // ===== Characters =====
        int maleW = (int) (screenW * 0.27);
        int maleH = (int) (screenH * 0.80);
        int downOffset = (int) (screenH * 0.03); // ปรับ % ได้
        int femaleW = (int) (screenW * 0.23);
        int femaleH = (int) (screenH * 0.72);

        int femaleX = (int) (screenW * 0.72);
        int femaleY = screenH - femaleH + downOffset;
        int maleY = screenH - maleH;
        int overlap = (int) (screenW * 0.05);

        JLabel female = createFlippedImage(
                "src/main/resources/images/Characters/ผู้หญิง ตัวเอก.png",
                femaleW, femaleH);
        female.setBounds(femaleX, femaleY, femaleW, femaleH);
        layeredPane.add(female, Integer.valueOf(2));

        JLabel male = createScaledImage(
                "src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png",
                maleW, maleH);
        male.setBounds(femaleX - maleW + overlap, maleY, maleW, maleH);
        layeredPane.add(male, Integer.valueOf(1));

        // ===== Sakura Effect =====
        SakuraPanel sakura = new SakuraPanel();
        sakura.setBounds(0, 0, screenW, screenH);
        layeredPane.add(sakura, Integer.valueOf(3));

        // ===== Title =====
        JLabel title = new JLabel("Seven Days, With You", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, screenW / 24));
        title.setForeground(new Color(180, 60, 140));
        title.setBounds((screenW - (screenW / 2)) / 2,
                screenH / 18,
                screenW / 2,
                screenH / 10);
        layeredPane.add(title, Integer.valueOf(4));

        // ===== Menu Buttons =====
        String[] menuText = { "เริ่มเกม", "โหลดเซฟ", "เล่นออนไลน์", "ตั้งค่า", "ออกเกม" };

        int btnW = screenW / 6;
        int btnH = screenH / 12;
        int startY = (int) (screenH * 0.35);
        int leftX = screenW / 12;

        for (int i = 0; i < menuText.length; i++) {

            String text = menuText[i];

            boolean isPurple = text.equals("เล่นออนไลน์");

            RoundedButton btn = new RoundedButton(text, isPurple, Color.WHITE);
            btn.setFont(new Font("Tahoma", Font.BOLD, screenW / 65));

            btn.setBounds(leftX,
                    startY + (i * (btnH + 22)),
                    btnW,
                    btnH);

            btn.addActionListener(e -> {

                if (controller.getAudioSystem() != null) {
                    controller.getAudioSystem().playSFX("click.wav");
                }

                switch (text) {
                    case "เริ่มเกม" -> controller.showChangescene();
                    case "โหลดเซฟ" -> controller.showSaveScreen();
                    case "เล่นออนไลน์" -> controller.showMultiplayer();
                    case "ตั้งค่า" -> controller.showSettings();
                    case "ออกเกม" -> controller.exitGame();
                }
            });

            layeredPane.add(btn, Integer.valueOf(4));
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // ================== Image Utils ==================
    private JLabel createScaledImage(String path, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            return new JLabel("Missing Image");
        }
    }

    private JLabel createFlippedImage(String path, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon(path);
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.drawImage(icon.getImage(), w, 0, 0, h,
                    0, 0, icon.getIconWidth(), icon.getIconHeight(), null);
            g2.dispose();
            return new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            return new JLabel("Missing Image");
        }
    }

    // ================== Rounded Button ==================
    class RoundedButton extends JButton {

        private final Color textColor;
        private final boolean isOnlineButton;

        public RoundedButton(String text, boolean isOnline, Color fg) {
            super(text);
            this.isOnlineButton = isOnline;
            this.textColor = fg;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 28;

            Color top;
            Color bottom;

            if (isOnlineButton) {
                // 💜 ม่วง (เล่นออนไลน์)
                top = new Color(190, 150, 255);
                bottom = new Color(140, 95, 230);

                if (getModel().isRollover()) {
                    top = new Color(205, 165, 255);
                    bottom = new Color(160, 115, 240);
                }

                if (getModel().isPressed()) {
                    top = new Color(170, 130, 240);
                    bottom = new Color(120, 80, 210);
                }

            } else {
                // 🌸 ชมพูพอดี ไม่อ่อนเกิน ไม่เข้มเกิน

                top = new Color(255, 185, 220); // ชมพูพาสเทลชัด
                bottom = new Color(255, 125, 175); // ชมพูสดพอดี

                if (getModel().isRollover()) {
                    top = new Color(255, 200, 230);
                    bottom = new Color(255, 140, 185);
                }

                if (getModel().isPressed()) {
                    top = new Color(240, 165, 200);
                    bottom = new Color(235, 105, 155);
                }

            }

            // เงา
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(3, 6, getWidth() - 3, getHeight() - 3, arc, arc);

            // Gradient 2 สี
            GradientPaint gp = new GradientPaint(
                    0, 0, top,
                    0, getHeight(), bottom);

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 6, arc, arc);

            // ขอบบาง
            g2.setColor(new Color(255, 255, 255, 110));
            g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 6, arc, arc);

            // ข้อความ
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(textColor);

            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    // ================== Sakura Effect ==================
    class SakuraPanel extends JPanel {
        private ArrayList<SakuraParticle> particles = new ArrayList<>();

        public SakuraPanel() {
            setOpaque(false);
            for (int i = 0; i < 50; i++)
                particles.add(new SakuraParticle());

            new Timer(30, e -> {
                for (SakuraParticle p : particles)
                    p.fall(getWidth(), getHeight());
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 182, 193, 160));
            for (SakuraParticle p : particles)
                g2.fill(p.getShape());
        }
    }

    class SakuraParticle {
        float x, y, size, speed;

        public SakuraParticle() {
            Random r = new Random();
            x = r.nextInt(1920);
            y = r.nextInt(1080);
            size = 6 + r.nextInt(8);
            speed = 1.5f + r.nextFloat() * 2f;
        }

        public void fall(int w, int h) {
            y += speed;
            x += Math.sin(y / 40.0) * 1.5;
            if (y > h) {
                y = -20;
                x = new Random().nextInt(Math.max(1, w));
            }
        }

        public Shape getShape() {
            return new Ellipse2D.Float(x, y, size, size / 1.6f);
        }
    }
}