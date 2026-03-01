package com.game.systems.choice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Day7 {
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
    private JButton BTN_CHOICE3;

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

        LABEL_CHARACTER = new JLabel();
        LABEL_CHARACTER.setOpaque(false);
        BG_VIEW.add(LABEL_CHARACTER);

        CHAR_ORIG = LOAD_IMAGE_SAFE("char.png");

        CHOICE_PANEL = new JPanel(null);
        CHOICE_PANEL.setOpaque(false);
        BG_VIEW.add(CHOICE_PANEL);

        BTN_CHOICE1 = new JButton("");
        BTN_CHOICE2 = new JButton("");
        BTN_CHOICE3 = new JButton("");
        STYLE_CHOICE_BUTTON(BTN_CHOICE1);
        STYLE_CHOICE_BUTTON(BTN_CHOICE2);
        STYLE_CHOICE_BUTTON(BTN_CHOICE3);
        BTN_CHOICE1.setUI(new PINKBUTTONUI());
        BTN_CHOICE2.setUI(new PINKBUTTONUI());
        BTN_CHOICE3.setUI(new PINKBUTTONUI());
        CHOICE_PANEL.add(BTN_CHOICE1);
        CHOICE_PANEL.add(BTN_CHOICE2);
        CHOICE_PANEL.add(BTN_CHOICE3);

        BTN_CHOICE1.addActionListener(E -> PICK(1));
        BTN_CHOICE2.addActionListener(E -> PICK(2));
        BTN_CHOICE3.addActionListener(E -> PICK(3));

        DIALOG.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent E) {
                GOTO_NEXT_BY_CLICK();
            }
        });

        FRAME.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent E) {
                LAYOUT_UI();
                BG_VIEW.REPAINT_BG();
            }
        });

        BUILD_STORY();

        FRAME.setSize(1024, 600);
        FRAME.setLocationRelativeTo(null);
        FRAME.setVisible(true);

        SHOW_SCENE(CURRENT_ID);
        LAYOUT_UI();
        BG_VIEW.REPAINT_BG();
    }

    private Image LOAD_IMAGE_SAFE(String PATH) {
        try {
            Image IMG = new ImageIcon(PATH).getImage();
            if (IMG == null) return MAKE_EMPTY_IMAGE();
            if (IMG.getWidth(null) <= 0 || IMG.getHeight(null) <= 0) return MAKE_EMPTY_IMAGE();
            return IMG;
        } catch (Exception EX) {
            return MAKE_EMPTY_IMAGE();
        }
    }

    private Image MAKE_EMPTY_IMAGE() {
        BufferedImage BI = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        return BI;
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

        int BTN_W = (int) (W * 0.62);
        int BTN_H = 56;
        int BTN_X = (W - BTN_W) / 2;

        int GAP = 18;

        int V = 0;
        if (BTN_CHOICE1.isVisible()) V++;
        if (BTN_CHOICE2.isVisible()) V++;
        if (BTN_CHOICE3.isVisible()) V++;

        if (V <= 0) {
            BTN_CHOICE1.setBounds(BTN_X, (H / 2) - 70, BTN_W, BTN_H);
            BTN_CHOICE2.setBounds(BTN_X, (H / 2), BTN_W, BTN_H);
            BTN_CHOICE3.setBounds(BTN_X, (H / 2) + 70, BTN_W, BTN_H);
        } else {
            int STACK_H = (BTN_H * V) + (GAP * (V - 1));
            int TOP_Y = (H / 2) - (STACK_H / 2);

            int Y = TOP_Y;
            if (BTN_CHOICE1.isVisible()) {
                BTN_CHOICE1.setBounds(BTN_X, Y, BTN_W, BTN_H);
                Y += BTN_H + GAP;
            }
            if (BTN_CHOICE2.isVisible()) {
                BTN_CHOICE2.setBounds(BTN_X, Y, BTN_W, BTN_H);
                Y += BTN_H + GAP;
            }
            if (BTN_CHOICE3.isVisible()) {
                BTN_CHOICE3.setBounds(BTN_X, Y, BTN_W, BTN_H);
            }
        }

        int CHAR_TARGET_H = (int) (H * 0.78);
        CHAR_TARGET_H = Math.min(720, Math.max(420, CHAR_TARGET_H));

        if (CHAR_ORIG != null && LABEL_CHARACTER != null) {
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
        }

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
            public void mouseEntered(MouseEvent E) {
                B.putClientProperty("BTN_STATE", "HOVER");
                B.repaint();
            }

            @Override
            public void mouseExited(MouseEvent E) {
                B.putClientProperty("BTN_STATE", "NORMAL");
                B.repaint();
            }

            @Override
            public void mousePressed(MouseEvent E) {
                B.putClientProperty("BTN_STATE", "PRESS");
                B.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent E) {
                B.putClientProperty("BTN_STATE", "HOVER");
                B.repaint();
            }
        });
    }

    private void BUILD_STORY() {
        SCENES.clear();

        SCENES.put("S1", new SCENE("Narrator", "Day7", "DAY 7 — วันที่คำตอบชัดกว่าความเงียบ", null, null, null, "S2", null, null, null));
        SCENES.put("S2", new SCENE("Narrator", "Day7", "เมื่อคืนคุณนอนไม่หลับเลย", null, null, null, "S3", null, null, null));
        SCENES.put("S3", new SCENE("Narrator", "Day7", "ทุกภาพย้อนกลับมาในหัว", null, null, null, "S4", null, null, null));
        SCENES.put("S4", new SCENE("Narrator", "Day7", "วันที่เจอกันครั้งแรก", null, null, null, "S5", null, null, null));
        SCENES.put("S5", new SCENE("Narrator", "Day7", "วันที่มือแตะกันบนชิงช้าสวรรค์", null, null, null, "S6", null, null, null));
        SCENES.put("S6", new SCENE("Narrator", "Day7", "วันที่เขาถามว่า “เรากำลังจีบกันอยู่ใช่ไหม”", null, null, null, "S7", null, null, null));
        SCENES.put("S7", new SCENE("Narrator", "Day7", "และคำพูดล่าสุด “ให้เราได้จีบเธอแบบจริงจังได้ไหม”", null, null, null, "S8", null, null, null));

        SCENES.put("S8", new SCENE("Narrator", "Day7", "วันนี้เขานัดคุณที่เดิม ริมแม่น้ำที่พระอาทิตย์ตกสวยที่สุด", null, null, null, "S9", null, null, null));
        SCENES.put("S9", new SCENE("Narrator", "Day7", "คุณมาถึงก่อน ลมพัดเบา ๆ กลิ่นน้ำลอยอ่อน ๆ", null, null, null, "S10", null, null, null));
        SCENES.put("S10", new SCENE("Narrator", "Day7", "หัวใจเต้นแรงอย่างควบคุมไม่ได้", null, null, null, "S11", null, null, null));
        SCENES.put("S11", new SCENE("Narrator", "Day7", "ไม่นานเขาก็เดินมา", null, null, null, "S12", null, null, null));
        SCENES.put("S12", new SCENE("Narrator", "Day7", "วันนี้เขาไม่ได้แต่งตัวพิเศษ แต่ดูตั้งใจมากกว่าทุกวัน", null, null, null, "S13", null, null, null));
        SCENES.put("S13", new SCENE("Narrator", "Day7", "สายตาที่มองคุณ…ต่างออกไป มันไม่ใช่สายตาเล่น ๆ แล้ว", null, null, null, "S14", null, null, null));
        SCENES.put("S14", new SCENE("Narrator", "Day7", "เขาหยุดตรงหน้าคุณ", null, null, null, "S15", null, null, null));
        SCENES.put("S15", new SCENE("Kim Jae-hyun", "Day7", "“ขอบคุณนะที่มา”", null, null, null, "S16", null, null, null));
        SCENES.put("S16", new SCENE("Narrator", "Day7", "เสียงเบา แต่จริงจัง", null, null, null, "S17", null, null, null));

        SCENES.put("S17", new SCENE("Narrator", "Day7", "ฉากที่ 1 — ก่อนคำตอบ", null, null, null, "S18", null, null, null));
        SCENES.put("S18", new SCENE("Narrator", "Day7", "คุณสองคนนั่งลงที่ม้านั่งไม้ตัวเดิม ท้องฟ้าเริ่มเปลี่ยนเป็นสีส้มทอง", null, null, null, "S19", null, null, null));
        SCENES.put("S19", new SCENE("Narrator", "Day7", "เขานิ่งไปครู่หนึ่ง เหมือนกำลังรวบรวมความกล้า", null, null, null, "S20", null, null, null));
        SCENES.put("S20", new SCENE("Kim Jae-hyun", "Day7", "“เราคิดมาทั้งคืนเลยนะ”", null, null, null, "S21", null, null, null));
        SCENES.put("S21", new SCENE("Narrator", "Day7", "คุณเงียบ ฟังทุกคำ", null, null, null, "S22", null, null, null));
        SCENES.put("S22", new SCENE("Kim Jae-hyun", "Day7", "“ตลอดอาทิตย์ที่ผ่านมา เราไม่เคยมองเธอเป็นแค่เพื่อนเลย”", null, null, null, "S23", null, null, null));
        SCENES.put("S23", new SCENE("Narrator", "Day7", "ลมหายใจคุณสะดุด", null, null, null, "S24", null, null, null));
        SCENES.put("S24", new SCENE("Kim Jae-hyun", "Day7", "“เราชอบเธอจริง ๆ”", null, null, null, "S25", null, null, null));
        SCENES.put("S25", new SCENE("Narrator", "Day7", "คำพูดนั้นชัดเจน ไม่มีหลบ ไม่มีเลี่ยง", null, null, null, "S26", null, null, null));
        SCENES.put("S26", new SCENE("Narrator", "Day7", "เขาหันมามองตรง ๆ", null, null, null, "S27", null, null, null));
        SCENES.put("S27", new SCENE("Kim Jae-hyun", "Day7", "“เราอยากรู้ว่าเธอรู้สึกยังไง”", null, null, null, "S28", null, null, null));
        SCENES.put("S28", new SCENE("Narrator", "Day7", "บรรยากาศเงียบจนได้ยินเสียงคลื่นกระทบฝั่ง", null, null, null, "S29", null, null, null));
        SCENES.put("S29", new SCENE("Narrator", "Day7", "นี่คือช่วงเวลาที่ทุกอย่างจะเปลี่ยน", null, null, null, "Q1", null, null, null));

        SCENES.put("Q1", new SCENE("คุณ", "Day7", "คำตอบของคุณ",
                "A) “เราก็ชอบเธอ… มากกว่าเพื่อนเหมือนกัน”",
                "B) “เรารู้สึกดีนะ… แต่เรายังไม่พร้อมเป็นแฟน”",
                null,
                null, "HE1", "FE1", null));

        SCENES.put("HE1", new SCENE("Narrator", "Day7", "ฉากจบที่ 1 — Happy Ending (เป็นแฟนกัน)", null, null, null, "HE2", null, null, null));
        SCENES.put("HE2", new SCENE("Narrator", "Day7", "คุณสูดหายใจลึก ก่อนจะพูดออกไป", null, null, null, "HE3", null, null, null));
        SCENES.put("HE3", new SCENE("คุณ", "Day7", "“เราชอบเธอ”", null, null, null, "HE4", null, null, null));
        SCENES.put("HE4", new SCENE("Narrator", "Day7", "เสียงคุณสั่นเล็กน้อย แต่ชัดเจนที่สุด", null, null, null, "HE5", null, null, null));
        SCENES.put("HE5", new SCENE("Narrator", "Day7", "เขานิ่งไปหนึ่งวินาที ก่อนรอยยิ้มจะค่อย ๆ ปรากฏ", null, null, null, "HE6", null, null, null));
        SCENES.put("HE6", new SCENE("Narrator", "Day7", "รอยยิ้มแบบที่เก็บไว้ไม่อยู่", null, null, null, "HE7", null, null, null));
        SCENES.put("HE7", new SCENE("Kim Jae-hyun", "Day7", "“จริงนะ…”", null, null, null, "HE8", null, null, null));
        SCENES.put("HE8", new SCENE("Narrator", "Day7", "ดวงตาเขาเป็นประกาย", null, null, null, "HE9", null, null, null));
        SCENES.put("HE9", new SCENE("Narrator", "Day7", "เขาหัวเราะเบา ๆ อย่างโล่งใจ", null, null, null, "HE10", null, null, null));
        SCENES.put("HE10", new SCENE("Kim Jae-hyun", "Day7", "“งั้น…เราขอเป็นแฟนเธอได้ไหม”", null, null, null, "HE11", null, null, null));
        SCENES.put("HE11", new SCENE("คุณ", "Day7", "“ได้สิ”", null, null, null, "HE12", null, null, null));
        SCENES.put("HE12", new SCENE("Narrator", "Day7", "มือเขาจับมือคุณอย่างมั่นคง", null, null, null, "SUM", null, null, null));

        SCENES.put("FE1", new SCENE("Narrator", "Day7", "ฉากจบที่ 2 — Friend Ending (คนสำคัญที่ไม่ใช่แฟน)", null, null, null, "FE2", null, null, null));
        SCENES.put("FE2", new SCENE("คุณ", "Day7", "“เรารู้สึกดีมากนะ แต่เรายังไม่พร้อมเป็นแฟนใครตอนนี้”", null, null, null, "FE3", null, null, null));
        SCENES.put("FE3", new SCENE("Kim Jae-hyun", "Day7", "“ขอบคุณนะที่พูดตรง ๆ อย่างน้อยเราก็ไม่ได้เสียเธอไป”", null, null, null, "SUM", null, null, null));

        SCENES.put("SUM", new SCENE("Narrator", "Day7", "บทสรุป DAY 7 | 7 วัน ที่เปลี่ยนหัวใจของคุณ", null, null, null, "END", null, null, null));
        SCENES.put("END", new SCENE("Narrator", "Day7", "จบตอน", null, null, null, null, null, null, null));
    }

    private void SHOW_SCENE(String ID) {
        CURRENT_ID = ID;
        SCENE S = SCENES.get(ID);
        if (S == null) return;

        DIALOG.SETDATA(S.NAME, S.DAY, S.TEXT);
        DIALOG.repaint();

        int COUNT = 0;
        if (S.C1 != null) COUNT++;
        if (S.C2 != null) COUNT++;
        if (S.C3 != null) COUNT++;

        boolean HAS_CHOICES = COUNT >= 2;

        CHOICE_PANEL.setVisible(HAS_CHOICES);

        BTN_CHOICE1.setVisible(HAS_CHOICES && S.C1 != null);
        BTN_CHOICE2.setVisible(HAS_CHOICES && S.C2 != null);
        BTN_CHOICE3.setVisible(HAS_CHOICES && S.C3 != null);

        if (HAS_CHOICES) {
            if (S.C1 != null) BTN_CHOICE1.setText(S.C1);
            if (S.C2 != null) BTN_CHOICE2.setText(S.C2);
            if (S.C3 != null) BTN_CHOICE3.setText(S.C3);
        }

        LAYOUT_UI();
    }

    private void GOTO_NEXT_BY_CLICK() {
        SCENE S = SCENES.get(CURRENT_ID);
        if (S == null) return;

        int COUNT = 0;
        if (S.C1 != null) COUNT++;
        if (S.C2 != null) COUNT++;
        if (S.C3 != null) COUNT++;

        if (COUNT >= 2) return;
        if (S.NEXT != null) SHOW_SCENE(S.NEXT);
    }

    private void PICK(int INDEX) {
        SCENE S = SCENES.get(CURRENT_ID);
        if (S == null) return;
        if (INDEX == 1 && S.NEXT1 != null) SHOW_SCENE(S.NEXT1);
        if (INDEX == 2 && S.NEXT2 != null) SHOW_SCENE(S.NEXT2);
        if (INDEX == 3 && S.NEXT3 != null) SHOW_SCENE(S.NEXT3);
    }

    public static void main(String[] ARGS) {
        SwingUtilities.invokeLater(() -> new Day7().CREATEANDSHOWGUI());
    }

    static class SCENE {
        String NAME;
        String DAY;
        String TEXT;
        String C1;
        String C2;
        String C3;
        String NEXT;
        String NEXT1;
        String NEXT2;
        String NEXT3;

        SCENE(String NAME, String DAY, String TEXT, String C1, String C2, String C3, String NEXT, String NEXT1, String NEXT2, String NEXT3) {
            this.NAME = NAME;
            this.DAY = DAY;
            this.TEXT = TEXT;
            this.C1 = C1;
            this.C2 = C2;
            this.C3 = C3;
            this.NEXT = NEXT;
            this.NEXT1 = NEXT1;
            this.NEXT2 = NEXT2;
            this.NEXT3 = NEXT3;
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