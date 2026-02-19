package com.game.systems.choice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChoiceSystem extends JFrame {
    private JPanel MAINPANEL;
    private JPanel CHOICECONTAINER;

    private int INDEX = 0;
    private List<String[]> PAGES = new ArrayList<>();

    public ChoiceSystem() {
        setTitle("Kim Jae-hyun Route - Choice System");
        setSize(1024, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MAINPANEL = new JPanel(null);
        MAINPANEL.setBackground(new Color(240, 240, 240));
        setContentPane(MAINPANEL);

        CHOICECONTAINER = new JPanel(null);
        CHOICECONTAINER.setOpaque(false);
        MAINPANEL.add(CHOICECONTAINER);

        JButton B1 = new JButton();
        JButton B2 = new JButton();

        STYLE_CHOICE_BUTTON(B1);
        STYLE_CHOICE_BUTTON(B2);

        B1.setUI(new PINKBUTTONUI());
        B2.setUI(new PINKBUTTONUI());

        CHOICECONTAINER.add(B1);
        CHOICECONTAINER.add(B2);

        PAGES.add(new String[]{"ถอยออกมานิดนึง", "ปล่อยให้เขาจับไว้"});
        PAGES.add(new String[]{"เงยหน้ามองเขา", "หลบสายตาแล้วเงียบ"});
        PAGES.add(new String[]{"ถามว่า “จะพาไปไหน?”", "เดินตามไปเงียบๆ"});
        PAGES.add(new String[]{"ยิ้มให้เขา", "ทำเป็นไม่สนใจ"});

        B1.addActionListener(E -> NEXT_PAGE());
        B2.addActionListener(E -> NEXT_PAGE());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                LAYOUT_UI(B1, B2);
            }
        });

        LAYOUT_UI(B1, B2);
        SHOW_PAGE(B1, B2);
    }

    private void LAYOUT_UI(JButton B1, JButton B2) {
        int W = getContentPane().getWidth();
        int H = getContentPane().getHeight();

        CHOICECONTAINER.setBounds(0, 0, W, H);

        int BTN_W = (int) (W * 0.52);
        int BTN_H = 56;
        int BTN_X = (W - BTN_W) / 2;
        int BTN_Y1 = (H / 2) - 55;
        int BTN_Y2 = BTN_Y1 + 72;

        B1.setBounds(BTN_X, BTN_Y1, BTN_W, BTN_H);
        B2.setBounds(BTN_X, BTN_Y2, BTN_W, BTN_H);
    }

    private void SHOW_PAGE(JButton B1, JButton B2) {
        String[] T = PAGES.get(INDEX);
        B1.setText(T[0]);
        B2.setText(T[1]);
        repaint();
    }

    private void NEXT_PAGE() {
        INDEX++;
        if (INDEX >= PAGES.size()) INDEX = 0;
        JButton B1 = (JButton) CHOICECONTAINER.getComponent(0);
        JButton B2 = (JButton) CHOICECONTAINER.getComponent(1);
        SHOW_PAGE(B1, B2);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChoiceSystem().setVisible(true));
    }
}
