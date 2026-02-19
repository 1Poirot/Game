package main;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class MenuGame extends JFrame {
    private int screenW, screenH;

    public MenuGame() {
        // ตั้งค่าเบื้องต้น
        setTitle("เกมจีบสาว");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); 
        setLayout(null);

        // คำนวณขนาดหน้าจอ
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenW = screenSize.width;
        screenH = screenSize.height;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, screenW, screenH);
        add(layeredPane);

        // ====== 1. Background (Layer 0) ======
        JLabel background = createScaledImage("src/main/resources/images/backgrounds/หน้าโรงเรียน.png", screenW, screenH);
        background.setBounds(0, 0, screenW, screenH);
        layeredPane.add(background, Integer.valueOf(0));

        // ====== 2. Characters (Layer 1-2) ======
        // ตั้งค่าขนาด: ผู้ชาย (80% ของจอ), ผู้หญิง (72% ของจอเพื่อให้เตี้ยกว่า)
        int maleW = (int) (screenW * 0.27);
        int maleH = (int) (screenH * 0.80);
        int femaleW = (int) (screenW * 0.23); 
        int femaleH = (int) (screenH * 0.72); 

        // ตำแหน่ง: จัดวางที่มุมขวาล่าง
        int femaleX = (int) (screenW * 0.72);
        int femaleY = screenH - femaleH;
        int maleY = screenH - maleH;
        int overlap = 145; // ระยะหลังชนกัน (ปรับตามความชอบ)

        // วางผู้หญิง (หันหลังชน - พลิกภาพ)
        JLabel female = createFlippedImage( "src/main/resources/images/Characters/ผู้หญิง ตัวเอก.png", femaleW, femaleH);
        female.setBounds(femaleX, femaleY, femaleW, femaleH);
        layeredPane.add(female, Integer.valueOf(2)); // เลเยอร์หน้า

        // วางผู้ชาย (หันหลังชน)
        JLabel male = createScaledImage("src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png", maleW, maleH);
        male.setBounds(femaleX - maleW + overlap, maleY, maleW, maleH);
        layeredPane.add(male, Integer.valueOf(1)); // เลเยอร์หลัง

        // ====== 3. Sakura Effect (Layer 3) ======
        SakuraPanel sakuraEffect = new SakuraPanel(screenW, screenH);
        layeredPane.add(sakuraEffect, Integer.valueOf(3));

        // ====== 4. UI Elements (Layer 4) ======
        // Title Box
        JLabel title = new JLabel("เกมจีบสาว", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, screenW / 35));
        title.setOpaque(true);
        title.setBackground(new Color(255, 255, 255, 220)); 
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        title.setBounds((screenW - (screenW / 3)) / 2, screenH / 15, screenW / 3, screenH / 9);
        layeredPane.add(title, Integer.valueOf(4));

        // ปุ่มเมนู
        String[] menuText = {"เริ่มเกม", "โหลดเซฟ", "ตั้งค่า", "ออกเกม"};
        int btnW = 280;
        int btnH = 65;
        int startY = (int) (screenH * 0.35);

        for (int i = 0; i < menuText.length; i++) {
            RoundedButton btn = new RoundedButton(menuText[i]);
            btn.setFont(new Font("Tahoma", Font.BOLD, 26));
            btn.setBounds(screenW / 10, startY + (i * (btnH + 25)), btnW, btnH);
            if (menuText[i].equals("ออกเกม")) btn.addActionListener(e -> System.exit(0));
            layeredPane.add(btn, Integer.valueOf(4));
        }

        setVisible(true);
    }

    // --- ฟังก์ชันโหลดภาพแบบ Ultra High Quality ---
    private JLabel createScaledImage(String path, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon(path);
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            
            // ตั้งค่าระดับสูงสุดเพื่อความคมชัด
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            
            g2.drawImage(icon.getImage(), 0, 0, w, h, null);
            g2.dispose();
            return new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            return new JLabel("Missing: " + path);
        }
    }

    private JLabel createFlippedImage(String path, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon(path);
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // พลิกแกน X เพื่อให้หันหลังชนกัน
            g2.drawImage(icon.getImage(), w, 0, 0, h, 0, 0, icon.getIconWidth(), icon.getIconHeight(), null);
            g2.dispose();
            return new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            return new JLabel("Missing: " + path);
        }
    }

    // --- ปุ่มดีไซน์มน ---
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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 215, 230)); 
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 45, 45));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, 
                         (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }

    // --- ระบบซากุระ ---
    class SakuraPanel extends JPanel {
        private ArrayList<SakuraParticle> particles = new ArrayList<>();
        public SakuraPanel(int w, int h) {
            setOpaque(false);
            setBounds(0, 0, w, h);
            for (int i = 0; i < 50; i++) particles.add(new SakuraParticle(new Random().nextInt(w), new Random().nextInt(h)));
            new Timer(30, e -> { for (SakuraParticle p : particles) p.fall(getWidth(), getHeight()); repaint(); }).start();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 182, 193, 160));
            for (SakuraParticle p : particles) g2.fill(p.getShape());
        }
    }

    class SakuraParticle {
        float x, y, size, speed;
        public SakuraParticle(int x, int y) {
            this.x = x; this.y = y;
            this.size = 6 + new Random().nextInt(8);
            this.speed = 1.5f + new Random().nextFloat() * 2.0f;
        }
        public void fall(int w, int h) {
            y += speed;
            x += Math.sin(y / 40.0) * 1.5;
            if (y > h) { y = -20; x = new Random().nextInt(w); }
        }
        public Shape getShape() { return new Ellipse2D.Float(x, y, size, size / 1.6f); }
    }

    public static void main(String[] args) {
        // แก้ปัญหาภาพเบลอบน Windows Scaling (High DPI)
        System.setProperty("sun.java2d.uiScale", "1.0"); 
        SwingUtilities.invokeLater(MenuGame::new);
    }
}