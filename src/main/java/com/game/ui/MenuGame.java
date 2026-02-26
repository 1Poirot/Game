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

        // ====== เริ่มเล่นเพลง BGM หน้าเมนู ======
        if (controller.getAudioSystem() != null) {
            controller.getAudioSystem().playBGM("audiotest.wav");
        }

        // ฟัง resize ทุกครั้ง
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutComponents();
            }
        });
    }

    // ================== Responsive Layout ==================
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
        int femaleW = (int) (screenW * 0.23);
        int femaleH = (int) (screenH * 0.72);

        int femaleX = (int) (screenW * 0.72);
        int femaleY = screenH - femaleH;
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
        JLabel title = new JLabel("เกมจีบสาว", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, screenW / 35));
        title.setOpaque(true);
        title.setBackground(new Color(255, 255, 255, 220));
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        title.setBounds((screenW - (screenW / 3)) / 2,
                screenH / 15,
                screenW / 3,
                screenH / 9);
        layeredPane.add(title, Integer.valueOf(4));

        // ===== Buttons =====
        String[] menuText = { "เริ่มเกม", "โหลดเซฟ", "ตั้งค่า", "ออกเกม" };
        int btnW = screenW / 8;
        int btnH = screenH / 14;
        int startY = (int) (screenH * 0.35);

        for (int i = 0; i < menuText.length; i++) {
            RoundedButton btn = new RoundedButton(menuText[i]);
            btn.setFont(new Font("Tahoma", Font.BOLD, screenW / 60));
            btn.setBounds(screenW / 10,
                    startY + (i * (btnH + 20)),
                    btnW, btnH);

            String text = menuText[i];
            btn.addActionListener(e -> {
<<<<<<< HEAD
                // ====== เล่นเสียง SFX เมื่อคลิกปุ่ม ======
                if (controller.getAudioSystem() != null) {
                    controller.getAudioSystem().playSFX("click.wav");
                }

=======
>>>>>>> f303d15 (Add new UI item image and initial save slot data)
                if (text.equals("เริ่มเกม"))
                    controller.showGameScene();
                else if (text.equals("โหลดเซฟ"))
                    controller.showSaveScreen(() -> controller.showMainMenu());
                else if (text.equals("ตั้งค่า"))
                    controller.showSettings();
                else if (text.equals("ออกเกม"))
                    controller.exitGame();
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
            // พลิกภาพตัวละคร
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
        public RoundedButton(String label) {
            super(label);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // สีชมพูพาสเทล
            g2.setColor(new Color(255, 215, 230));
            g2.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth(), getHeight(), 45, 45));

            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 182, 193, 160)); // สีกลีบซากุระ
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