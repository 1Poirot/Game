package com.game.systems.choice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChoiceSystem extends JFrame {
    private BGVIEW MAINPANEL;
    private JPanel CHOICECONTAINER;
    private JLabel TITLELABEL;

    private int INDEX = 0;
    private final List<PAGE> PAGES = new ArrayList<>();

    public ChoiceSystem() {
        setTitle("Kim Jae-hyun Route - Choice System");
        setSize(1024, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MAINPANEL = new BGVIEW("/images/backgrounds/bg.jpg");
        MAINPANEL.setLayout(null);
        setContentPane(MAINPANEL);

        CHOICECONTAINER = new JPanel(null);
        CHOICECONTAINER.setOpaque(false);
        MAINPANEL.add(CHOICECONTAINER);

        TITLELABEL = new JLabel("", SwingConstants.CENTER);
        TITLELABEL.setFont(new Font("Leelawadee UI", Font.BOLD, 22));
        TITLELABEL.setForeground(new Color(40, 40, 40));
        CHOICECONTAINER.add(TITLELABEL);

        JButton B1 = new JButton();
        JButton B2 = new JButton();
        JButton B3 = new JButton();

        STYLE_CHOICE_BUTTON(B1);
        STYLE_CHOICE_BUTTON(B2);
        STYLE_CHOICE_BUTTON(B3);

        B1.setUI(new PINKBUTTONUI());
        B2.setUI(new PINKBUTTONUI());
        B3.setUI(new PINKBUTTONUI());

        CHOICECONTAINER.add(B1);
        CHOICECONTAINER.add(B2);
        CHOICECONTAINER.add(B3);

        BUILD_PAGES();

        B1.addActionListener(E -> NEXT_PAGE());
        B2.addActionListener(E -> NEXT_PAGE());
        B3.addActionListener(E -> NEXT_PAGE());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                LAYOUT_UI(B1, B2, B3);
                MAINPANEL.REPAINT_BG();
            }
        });

        LAYOUT_UI(B1, B2, B3);
        SHOW_PAGE(B1, B2, B3);
        MAINPANEL.REPAINT_BG();
    }

    private void BUILD_PAGES() {
        PAGES.clear();

        PAGES.add(new PAGE(
                " ตัวเลือกที่ 1  คำพูดแรก",
                "A) “ขอโทษนะ เรารีบไปหน่อย…”",
                "B) “ก็คุณเดินไม่ดูทางเหมือนกันนะ”",
                "C) “เอ่อ… ขอบคุณที่ช่วยเก็บนะ”"
        ));

        PAGES.add(new PAGE(
                " ตัวเลือกที่ 2  แนะนำตัว",
                "A) แนะนำตัวสั้น ๆ สุภาพ",
                "B) พูดติดตลกให้อีกฝ่ายหัวเราะ",
                "C) พูดจริงจังว่าอยากเริ่มต้นใหม่"
        ));

        PAGES.add(new PAGE(
                " ตัวเลือกที่ 3  ความบังเอิญ",
                "A) “หรือว่าเราโชคชะตาผูกกันนะ”",
                "B) “โลกมันกลมดีเนอะ”",
                "C) “ก็แค่บังเอิญแหละ”"
        ));

        PAGES.add(new PAGE(
                " คำถามที่ 4  คุณตอบยังไง?",
                "A) “ดีเลย เรากำลังไม่รู้จะไปไหนพอดี”",
                "B) “ไม่เป็นไร เราไปคนเดียวได้”",
                "C) “เธอ/นาย ชวนเราเหรอเนี่ย น่าแปลกใจนะ”"
        ));

        PAGES.add(new PAGE(
                " คำถามที่ 5 คุณตอบยังไง?",
                "A) “เหนื่อยนิดหน่อย… แต่ดีขึ้นเพราะเธอ”",
                "B) “ก็โอเคนะ เริ่มชินแล้ว”",
                "C) “ไม่ค่อยดีเท่าไหร่”"
        ));
    }

    private void LAYOUT_UI(JButton B1, JButton B2, JButton B3) {
        int W = getContentPane().getWidth();
        int H = getContentPane().getHeight();

        CHOICECONTAINER.setBounds(0, 0, W, H);

        int TITLE_W = (int) (W * 0.74);
        int TITLE_H = 60;
        int TITLE_X = (W - TITLE_W) / 2;
        int TITLE_Y = (int) (H * 0.18);
        TITLELABEL.setBounds(TITLE_X, TITLE_Y, TITLE_W, TITLE_H);

        int BTN_W = (int) (W * 0.62);
        int BTN_H = 60;
        int BTN_X = (W - BTN_W) / 2;

        int START_Y = TITLE_Y + TITLE_H + 30;
        int GAP = 18;

        B1.setBounds(BTN_X, START_Y, BTN_W, BTN_H);
        B2.setBounds(BTN_X, START_Y + (BTN_H + GAP), BTN_W, BTN_H);
        B3.setBounds(BTN_X, START_Y + 2 * (BTN_H + GAP), BTN_W, BTN_H);
    }

    private void SHOW_PAGE(JButton B1, JButton B2, JButton B3) {
        if (PAGES.isEmpty()) return;

        PAGE P = PAGES.get(INDEX);
        TITLELABEL.setText(P.TITLE);

        B1.setText(P.A);
        B2.setText(P.B);
        B3.setText(P.C);

        repaint();
    }

    private void NEXT_PAGE() {
        INDEX++;
        if (INDEX >= PAGES.size()) INDEX = 0;

        JButton B1 = (JButton) CHOICECONTAINER.getComponent(1);
        JButton B2 = (JButton) CHOICECONTAINER.getComponent(2);
        JButton B3 = (JButton) CHOICECONTAINER.getComponent(3);

        SHOW_PAGE(B1, B2, B3);
    }

    private void STYLE_CHOICE_BUTTON(JButton B) {
        B.setFont(new Font("Leelawadee UI", Font.PLAIN, 18));
        B.setFocusPainted(false);
        B.setContentAreaFilled(false);
        B.setOpaque(false);
        B.setForeground(new Color(40, 40, 40));
        B.setHorizontalAlignment(SwingConstants.CENTER);
        B.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        B.putClientProperty("BTN_BG", new Color(255, 180, 220, 200));
        B.putClientProperty("BTN_BORDER", new Color(255, 255, 255, 230));
        B.putClientProperty("BTN_HOVER", new Color(255, 200, 235, 220));
        B.putClientProperty("BTN_PRESS", new Color(255, 160, 210, 220));

        B.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                B.putClientProperty("BTN_STATE", "HOVER");
                B.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                B.putClientProperty("BTN_STATE", "NORMAL");
                B.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                B.putClientProperty("BTN_STATE", "PRESS");
                B.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                B.putClientProperty("BTN_STATE", "HOVER");
                B.repaint();
            }
        });
    }

    static class PINKBUTTONUI extends javax.swing.plaf.basic.BasicButtonUI {
        @Override
        public void paint(Graphics G, JComponent C) {
            AbstractButton B = (AbstractButton) C;
            Graphics2D G2 = (Graphics2D) G.create();
            G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int W = B.getWidth();
            int H = B.getHeight();

            String STATE = (String) B.getClientProperty("BTN_STATE");
            if (STATE == null) STATE = "NORMAL";

            Color BG = (Color) B.getClientProperty("BTN_BG");
            Color BR = (Color) B.getClientProperty("BTN_BORDER");
            Color HOVER = (Color) B.getClientProperty("BTN_HOVER");
            Color PRESS = (Color) B.getClientProperty("BTN_PRESS");

            Color USE_BG = BG;
            if ("HOVER".equals(STATE)) USE_BG = HOVER;
            if ("PRESS".equals(STATE)) USE_BG = PRESS;

            int ARC = 26;

            G2.setColor(USE_BG);
            G2.fillRoundRect(0, 0, W, H, ARC, ARC);

            G2.setStroke(new BasicStroke(3f));
            G2.setColor(BR);
            G2.drawRoundRect(0, 0, W - 1, H - 1, ARC, ARC);

            G2.dispose();
            super.paint(G, C);
        }
    }

    static class PAGE {
        String TITLE;
        String A;
        String B;
        String C;

        PAGE(String TITLE, String A, String B, String C) {
            this.TITLE = TITLE;
            this.A = A;
            this.B = B;
            this.C = C;
        }
    }

    static class BGVIEW extends JPanel {
        private final Image ORIG;
        private Image SCALED;

        BGVIEW(String RESOURCE_PATH) {
            Image IMG = null;

            try {
                URL U = ChoiceSystem.class.getResource(RESOURCE_PATH);
                if (U == null && RESOURCE_PATH != null && RESOURCE_PATH.startsWith("/")) {
                    U = ChoiceSystem.class.getResource(RESOURCE_PATH.substring(1));
                }
                if (U != null) IMG = new ImageIcon(U).getImage();
            } catch (Exception E) {
                IMG = null;
            }

            if (IMG == null) {
                try {
                    String P1 = "src/main/resources" + RESOURCE_PATH;
                    String P2 = "src/main/resources/" + (RESOURCE_PATH != null && RESOURCE_PATH.startsWith("/") ? RESOURCE_PATH.substring(1) : RESOURCE_PATH);

                    java.io.File F1 = new java.io.File(P1);
                    java.io.File F2 = new java.io.File(P2);

                    if (F1.exists()) IMG = new ImageIcon(F1.getAbsolutePath()).getImage();
                    else if (F2.exists()) IMG = new ImageIcon(F2.getAbsolutePath()).getImage();
                } catch (Exception E) {
                    IMG = null;
                }
            }

            if (IMG == null) {
                ORIG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        null,
                        "หาไฟล์รูปไม่เจอ:\n" + RESOURCE_PATH +
                                "\n\nลองแล้วทั้ง:\n" +
                                "1) resources (classpath)\n" +
                                "2) src/main/resources" + RESOURCE_PATH,
                        "Image Not Found",
                        JOptionPane.ERROR_MESSAGE
                ));
            } else {
                ORIG = IMG;
            }
        }

        void REPAINT_BG() {
            if (getWidth() <= 0 || getHeight() <= 0) return;
            SCALED = ORIG.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics G) {
            super.paintComponent(G);
            if (SCALED == null) REPAINT_BG();
            if (SCALED != null) G.drawImage(SCALED, 0, 0, getWidth(), getHeight(), this);
        }
    }
}