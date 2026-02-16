package com.game.systems.dialogue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DialogueSystemAndChoice {
    private static final Font THAI_FONT = new Font("Leelawadee UI", Font.PLAIN, 20);
    private static final Font NAME_FONT = new Font("Leelawadee UI", Font.BOLD, 20);
    private static final Font DAY_FONT = new Font("Leelawadee UI", Font.PLAIN, 16);
    private static final Font BTN_FONT = new Font("Leelawadee UI", Font.PLAIN, 18);

    private JFrame FRAME;

    private BGVIEW BG_VIEW;

    private Image CHAR_ORIG;
    private JLabel LABEL_CHARACTER;

    private DIALOGPANEL DIALOG;

    private JPanel CHOICE_PANEL;
    private JButton BTN_CHOICE1;
    private JButton BTN_CHOICE2;

    private Map<String, SCENE> SCENES = new HashMap<>();
    private String CURRENT_ID = "S1";

    public void CREATEANDSHOWGUI() {
        FRAME = new JFrame("Kim Jae-hyun Route");
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setResizable(true);

        
        
        BG_VIEW = new BGVIEW("bg.png");
        BG_VIEW.setLayout(null);
        FRAME.setContentPane(BG_VIEW);


        DIALOG = new DIALOGPANEL("", "", "");
        BG_VIEW.add(DIALOG);

        CHOICE_PANEL = new JPanel(null);
        CHOICE_PANEL.setOpaque(false);
        BG_VIEW.add(CHOICE_PANEL);

        BTN_CHOICE1 = new JButton("");
        BTN_CHOICE2 = new JButton("");
        STYLE_CHOICE_BUTTON(BTN_CHOICE1);
        STYLE_CHOICE_BUTTON(BTN_CHOICE2);
        BTN_CHOICE1.setUI(new PINKBUTTONUI());
        BTN_CHOICE2.setUI(new PINKBUTTONUI());
        CHOICE_PANEL.add(BTN_CHOICE1);
        CHOICE_PANEL.add(BTN_CHOICE2);

        BTN_CHOICE1.addActionListener(E -> PICK(1));
        BTN_CHOICE2.addActionListener(E -> PICK(2));

        DIALOG.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GOTO_NEXT_BY_CLICK();
            }
        });

        FRAME.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                LAYOUT_UI();
                BG_VIEW.REPAINT_BG();
            }
        });

        BUILD_STORY();

        FRAME.setSize(1024, 600);
        FRAME.setLocationRelativeTo(null);
        FRAME.setVisible(true);

        SHOW_SCENE("S1");
        LAYOUT_UI();
        BG_VIEW.REPAINT_BG();
    }

    private void LAYOUT_UI() {
        int W = FRAME.getContentPane().getWidth();
        int H = FRAME.getContentPane().getHeight();

        int DIALOG_W = (int) (W * 0.93);
        int DIALOG_H = Math.max(140, (int) (H * 0.22));
        int DIALOG_X = (W - DIALOG_W) / 2;
        int DIALOG_Y = H - DIALOG_H - Math.max(20, (int) (H * 0.03));
        DIALOG.setBounds(DIALOG_X, DIALOG_Y, DIALOG_W, DIALOG_H);

        CHOICE_PANEL.setBounds(0, 0, W, H);

        int BTN_W = (int) (W * 0.52);
        int BTN_H = 56;
        int BTN_X = (W - BTN_W) / 2;
        int BTN_Y1 = (H / 2) - 55;
        int BTN_Y2 = BTN_Y1 + 72;
        BTN_CHOICE1.setBounds(BTN_X, BTN_Y1, BTN_W, BTN_H);
        BTN_CHOICE2.setBounds(BTN_X, BTN_Y2, BTN_W, BTN_H);

        int CHAR_TARGET_H = (int) (H * 0.78);
        CHAR_TARGET_H = Math.min(720, Math.max(420, CHAR_TARGET_H));

        Image CHAR_SCALED = CHAR_ORIG.getScaledInstance(-1, CHAR_TARGET_H, Image.SCALE_SMOOTH);
        ImageIcon CHAR_ICON = new ImageIcon(CHAR_SCALED);
        LABEL_CHARACTER.setIcon(CHAR_ICON);

        int CHAR_W = CHAR_ICON.getIconWidth();
        int CHAR_H = CHAR_ICON.getIconHeight();

        int CHAR_X = (W - CHAR_W) / 2;

        int FOOT_GAP = 8;
        int CHAR_Y = (DIALOG_Y - CHAR_H) + FOOT_GAP;
        CHAR_Y = Math.max(10, CHAR_Y);

        LABEL_CHARACTER.setBounds(CHAR_X, CHAR_Y, CHAR_W, CHAR_H);

        BG_VIEW.setComponentZOrder(CHOICE_PANEL, 0);
        BG_VIEW.setComponentZOrder(DIALOG, 0);
        BG_VIEW.setComponentZOrder(LABEL_CHARACTER, 2);

        BG_VIEW.revalidate();
        BG_VIEW.repaint();
    }

    private void STYLE_CHOICE_BUTTON(JButton B) {
        B.setFont(BTN_FONT);
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

    private void BUILD_STORY() {
        SCENES.put("S1", new SCENE("Kim Jae-hyun", "Day1", "“ระวังหน่อย... ตรงนี้คนเดินผ่านเยอะ”", null, null, "S2", null, null));
        SCENES.put("S2", new SCENE("Kim Jae-hyun", "Day1", "เขายื่นมือมาจับแขนเบาๆ เหมือนจะกันไม่ให้ชนคนอื่น", "ถอยออกมานิดนึง", "ปล่อยให้เขาจับไว้", null, "S3A", "S3B"));
        SCENES.put("S3A", new SCENE("คุณ", "Day1", "“ขอโทษนะ เราเดินไม่ทันระวังเอง”", null, null, "S4", null, null));
        SCENES.put("S3B", new SCENE("คุณ", "Day1", "คุณไม่ได้ขยับหนี มือเขาอุ่นกว่าที่คิด…", null, null, "S4", null, null));
        SCENES.put("S4", new SCENE("Kim Jae-hyun", "Day1", "“ไปทางนี้สิ เงียบกว่า”", "ไปด้วย", "บอกว่าขอกลับก่อน", null, "S5A", "S5B"));
        SCENES.put("S5A", new SCENE("Narrator", "Day1", "คุณเดินตามเขาไป เสียงซากุระปลิวไหวเหมือนเริ่มต้นอะไรบางอย่าง…", null, null, null, null, null));
        SCENES.put("S5B", new SCENE("Narrator", "Day1", "คุณปฏิเสธอย่างสุภาพ แต่เขามองตามเหมือนยังอยากพูดอะไรต่อ…", null, null, null, null, null));
    }

    private void SHOW_SCENE(String ID) {
        CURRENT_ID = ID;
        SCENE S = SCENES.get(ID);
        if (S == null) return;

        DIALOG.SETDATA(S.NAME, S.DAY, S.TEXT);
        DIALOG.repaint();

        boolean HAS_CHOICES = S.C1 != null && S.C2 != null;
        CHOICE_PANEL.setVisible(HAS_CHOICES);
        BTN_CHOICE1.setVisible(HAS_CHOICES);
        BTN_CHOICE2.setVisible(HAS_CHOICES);

        if (HAS_CHOICES) {
            BTN_CHOICE1.setText(S.C1);
            BTN_CHOICE2.setText(S.C2);
        }
    }

    private void GOTO_NEXT_BY_CLICK() {
        SCENE S = SCENES.get(CURRENT_ID);
        if (S == null) return;
        boolean HAS_CHOICES = S.C1 != null && S.C2 != null;
        if (HAS_CHOICES) return;
        if (S.NEXT != null) SHOW_SCENE(S.NEXT);
    }

    private void PICK(int INDEX) {
        SCENE S = SCENES.get(CURRENT_ID);
        if (S == null) return;
        if (INDEX == 1 && S.NEXT1 != null) SHOW_SCENE(S.NEXT1);
        if (INDEX == 2 && S.NEXT2 != null) SHOW_SCENE(S.NEXT2);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DialogueSystemAndChoice().CREATEANDSHOWGUI());
    }

    static class SCENE {
        String NAME;
        String DAY;
        String TEXT;
        String C1;
        String C2;
        String NEXT;
        String NEXT1;
        String NEXT2;

        SCENE(String NAME, String DAY, String TEXT, String C1, String C2, String NEXT, String NEXT1, String NEXT2) {
            this.NAME = NAME;
            this.DAY = DAY;
            this.TEXT = TEXT;
            this.C1 = C1;
            this.C2 = C2;
            this.NEXT = NEXT;
            this.NEXT1 = NEXT1;
            this.NEXT2 = NEXT2;
        }
    }

    static class BGVIEW extends JPanel {
        private final Image ORIG;
        private Image SCALED;
        

        BGVIEW(String PATH) {
            ORIG = new ImageIcon(PATH).getImage();
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

    static class DIALOGPANEL extends JPanel {
        private String NAME;
        private String DAY;
        private String TEXT;

        DIALOGPANEL(String NAME, String DAY, String TEXT) {
            this.NAME = NAME;
            this.DAY = DAY;
            this.TEXT = TEXT;
            setOpaque(false);
        }

        void SETDATA(String NAME, String DAY, String TEXT) {
            this.NAME = NAME;
            this.DAY = DAY;
            this.TEXT = TEXT;
        }

        @Override
        protected void paintComponent(Graphics G) {
            Graphics2D G2 = (Graphics2D) G.create();
            G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int W = getWidth();
            int H = getHeight();

            int ARC = 40;

            Color PANEL_BG = new Color(255, 180, 220, 170);
            Color PANEL_BORDER = new Color(255, 255, 255, 220);

            G2.setColor(PANEL_BG);
            G2.fillRoundRect(0, 10, W, H - 10, ARC, ARC);

            G2.setStroke(new BasicStroke(3f));
            G2.setColor(PANEL_BORDER);
            G2.drawRoundRect(0, 10, W - 1, H - 11, ARC, ARC);

            G2.setFont(NAME_FONT);
            FontMetrics FM_NAME = G2.getFontMetrics();
            int TAG_W = FM_NAME.stringWidth(NAME) + 60;
            int TAG_H = 50;
            int TAG_X = 15;
            int TAG_Y = 0;

            int TAG_ARC = 30;

            Color TAG_BG = new Color(255, 255, 255, 235);
            Color TAG_BORDER = new Color(255, 180, 220, 220);

            G2.setColor(TAG_BG);
            G2.fillRoundRect(TAG_X, TAG_Y, TAG_W, TAG_H, TAG_ARC, TAG_ARC);

            G2.setStroke(new BasicStroke(2f));
            G2.setColor(TAG_BORDER);
            G2.drawRoundRect(TAG_X, TAG_Y, TAG_W, TAG_H, TAG_ARC, TAG_ARC);

            G2.setFont(NAME_FONT);
            G2.setColor(new Color(60, 60, 60));
            int NAME_X = TAG_X + 20;
            int NAME_Y = TAG_Y + ((TAG_H - FM_NAME.getHeight()) / 2) + FM_NAME.getAscent();
            G2.drawString(NAME, NAME_X, NAME_Y);

            G2.setFont(DAY_FONT);
            G2.setColor(new Color(40, 40, 40));
            FontMetrics FM_DAY = G2.getFontMetrics();
            int DAY_X = W - 15 - FM_DAY.stringWidth(DAY);
            int DAY_Y = 10 + 25;
            G2.drawString(DAY, DAY_X, DAY_Y);

            G2.setFont(THAI_FONT);
            G2.setColor(new Color(30, 30, 30));
            FontMetrics FM_TEXT = G2.getFontMetrics();
            int TEXT_X = 25;
            int TEXT_Y = 10 + 75;

            String[] LINES = WRAP(TEXT, FM_TEXT, W - 50);
            int LINE_H = FM_TEXT.getHeight();
            for (int I = 0; I < LINES.length; I++) {
                G2.drawString(LINES[I], TEXT_X, TEXT_Y + (I * LINE_H));
            }

            G2.dispose();
        }

        private String[] WRAP(String S, FontMetrics FM, int MAXW) {
            String[] WORDS = S.split(" ");
            StringBuilder LINE = new StringBuilder();
            java.util.List<String> OUT = new java.util.ArrayList<>();
            for (String W : WORDS) {
                String TRY = LINE.length() == 0 ? W : LINE + " " + W;
                if (FM.stringWidth(TRY) <= MAXW) {
                    LINE.setLength(0);
                    LINE.append(TRY);
                } else {
                    OUT.add(LINE.toString());
                    LINE.setLength(0);
                    LINE.append(W);
                }
            }
            if (LINE.length() > 0) OUT.add(LINE.toString());
            return OUT.toArray(new String[0]);
        }
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
}
