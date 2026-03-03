package com.game.systems.choice;

import com.game.systems.affection.AffectionManager;
import com.game.systems.affection.CharacterRoute;
import com.game.ui.AffectionBar;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;


public class Day5 {

    private AffectionBar affectionBar;
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

        affectionBar = new AffectionBar(CharacterRoute.KIM_JAEHYUN);
        BG_VIEW.add(affectionBar);

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
            
            }
        });

        BUILD_STORY();

        FRAME.setSize(1024, 600);
        FRAME.setLocationRelativeTo(null);
        FRAME.setVisible(true);

        SHOW_SCENE(CURRENT_ID);
        LAYOUT_UI();
        
       if (affectionBar != null) {
    int barW = 320;   // ขนาดกำลังดี
    int barH = 85;    // เตี้ยลง
    affectionBar.setBounds(40, 25, barW, barH);
}
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

        int STACK_H = (BTN_H * 3) + 18 + 18;
        int TOP_Y = (H / 2) - (STACK_H / 2);

        int BTN_Y1 = TOP_Y;
        int BTN_Y2 = BTN_Y1 + BTN_H + 18;
        int BTN_Y3 = BTN_Y2 + BTN_H + 18;

        BTN_CHOICE1.setBounds(BTN_X, BTN_Y1, BTN_W, BTN_H);
        BTN_CHOICE2.setBounds(BTN_X, BTN_Y2, BTN_W, BTN_H);
        BTN_CHOICE3.setBounds(BTN_X, BTN_Y3, BTN_W, BTN_H);

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

        BG_VIEW.setComponentZOrder(affectionBar, 0);

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

        SCENES.put("S1", new SCENE("Narrator", "Day5", "DAY 5 — ความรู้สึกที่เริ่มปิดไม่อยู่", null, null, null, "S2", null, null, null));
        SCENES.put("S2", new SCENE("Narrator", "Day5", "เช้าวันที่ห้า", null, null, null, "S3", null, null, null));
        SCENES.put("S3", new SCENE("Narrator", "Day5", "แสงแดดลอดผ่านผ้าม่าน", null, null, null, "S4", null, null, null));
        SCENES.put("S4", new SCENE("Narrator", "Day5", "คุณลืมตาขึ้นช้า ๆ", null, null, null, "S5", null, null, null));
        SCENES.put("S5", new SCENE("Narrator", "Day5", "หัวใจยังเต้นแรงเมื่อนึกถึงเมื่อวาน", null, null, null, "S6", null, null, null));
        SCENES.put("S6", new SCENE("Narrator", "Day5", "คำถามที่เขาเกือบพูดจบ", null, null, null, "S7", null, null, null));
        SCENES.put("S7", new SCENE("Narrator", "Day5", "สายตาที่จริงจัง", null, null, null, "S8", null, null, null));
        SCENES.put("S8", new SCENE("Narrator", "Day5", "ระยะห่างที่ใกล้จนแทบสัมผัสลมหายใจ", null, null, null, "S9", null, null, null));
        SCENES.put("S9", new SCENE("คุณ", "Day5", "คุณพลิกตัวนอนหงาย", null, null, null, "S10", null, null, null));
        SCENES.put("S10", new SCENE("คุณ", "Day5", "“…เรากำลังรอคำบางคำอยู่หรือเปล่า”", null, null, null, "S11", null, null, null));

        SCENES.put("S11", new SCENE("Narrator", "Day5", "หน้าโรงเรียน", null, null, null, "S12", null, null, null));
        SCENES.put("S12", new SCENE("Narrator", "Day5", "คุณมาถึงเร็วกว่าปกติ", null, null, null, "S13", null, null, null));
        SCENES.put("S13", new SCENE("Narrator", "Day5", "รถหรูคันเดิมแล่นเข้ามาจอด", null, null, null, "S14", null, null, null));
        SCENES.put("S14", new SCENE("Narrator", "Day5", "เขาลงจากรถ", null, null, null, "S15", null, null, null));
        SCENES.put("S15", new SCENE("Narrator", "Day5", "แต่วันนี้", null, null, null, "S16", null, null, null));
        SCENES.put("S16", new SCENE("Narrator", "Day5", "สายตาที่มองคุณ… ต่างออกไป", null, null, null, "S17", null, null, null));
        SCENES.put("S17", new SCENE("Narrator", "Day5", "ไม่ใช่แค่สนใจ", null, null, null, "S18", null, null, null));
        SCENES.put("S18", new SCENE("Narrator", "Day5", "แต่เหมือนกำลังตัดสินใจบางอย่างอยู่", null, null, null, "S19", null, null, null));
        SCENES.put("S19", new SCENE("Narrator", "Day5", "เขาเดินเข้ามาใกล้", null, null, null, "S20", null, null, null));
        SCENES.put("S20", new SCENE("Kim Jae-hyun", "Day5", "“…เมื่อคืน นอนหลับไหม”", null, null, null, "Q1", null, null, null));

        SCENES.put("Q1", new SCENE("คุณ", "Day5", "คำถามที่ 1", "A) “หลับ…แต่คิดถึงเธอก่อนนอน”", "B) “ก็หลับปกติ”", "C) “ไม่ค่อยหลับเลย”", null, "Q1_A", "Q1_B", "Q1_C"));
        SCENES.put("Q1_A", new SCENE("คุณ", "Day5", "“หลับ…แต่คิดถึงเธอก่อนนอน”", null, null, null, "Q1_R1", null, null, null));
        SCENES.put("Q1_B", new SCENE("คุณ", "Day5", "“ก็หลับปกติ”", null, null, null, "Q1_R1", null, null, null));
        SCENES.put("Q1_C", new SCENE("คุณ", "Day5", "“ไม่ค่อยหลับเลย”", null, null, null, "Q1_R1", null, null, null));

        SCENES.put("Q1_R1", new SCENE("Narrator", "Day5", "อีกฝ่ายชะงัก", null, null, null, "Q1_R2", null, null, null));
        SCENES.put("Q1_R2", new SCENE("Narrator", "Day5", "ก่อนหลบสายตาเล็กน้อย", null, null, null, "Q1_R3", null, null, null));
        SCENES.put("Q1_R3", new SCENE("Kim Jae-hyun", "Day5", "“…เหมือนกัน”", null, null, null, "Q1_R4", null, null, null));
        SCENES.put("Q1_R4", new SCENE("Narrator", "Day5", "คำตอบเบามาก แต่ชัดเจน", null, null, null, "S21", null, null, null));

        SCENES.put("S21", new SCENE("Narrator", "Day5", "คาบเช้า — สายตาที่ชัดเกินไป", null, null, null, "S22", null, null, null));
        SCENES.put("S22", new SCENE("Narrator", "Day5", "ระหว่างเรียน", null, null, null, "S23", null, null, null));
        SCENES.put("S23", new SCENE("Narrator", "Day5", "คุณรู้สึกถึงสายตาที่มองมาบ่อยกว่าทุกวัน", null, null, null, "S24", null, null, null));
        SCENES.put("S24", new SCENE("Narrator", "Day5", "เมื่อเงยหน้า", null, null, null, "S25", null, null, null));
        SCENES.put("S25", new SCENE("Narrator", "Day5", "เขาไม่ได้หันหนี", null, null, null, "S26", null, null, null));
        SCENES.put("S26", new SCENE("Narrator", "Day5", "กลับมองตรง ๆ", null, null, null, "S27", null, null, null));
        SCENES.put("S27", new SCENE("Narrator", "Day5", "เหมือนกำลังอ่านความรู้สึกคุณ", null, null, null, "S28", null, null, null));
        SCENES.put("S28", new SCENE("Narrator", "Day5", "ระหว่างพักคาบ", null, null, null, "S29", null, null, null));
        SCENES.put("S29", new SCENE("Narrator", "Day5", "มีนักเรียนคนหนึ่งเดินมาคุยกับคุณ", null, null, null, "S30", null, null, null));
        SCENES.put("S30", new SCENE("Narrator", "Day5", "ถามเรื่องงาน", null, null, null, "S31", null, null, null));
        SCENES.put("S31", new SCENE("Narrator", "Day5", "หัวเราะเล็กน้อย", null, null, null, "S32", null, null, null));
        SCENES.put("S32", new SCENE("Narrator", "Day5", "คุณไม่ได้คิดอะไร", null, null, null, "S33", null, null, null));
        SCENES.put("S33", new SCENE("Narrator", "Day5", "แต่พอหันไปอีกที", null, null, null, "S34", null, null, null));
        SCENES.put("S34", new SCENE("Narrator", "Day5", "เห็นเขายืนมองอยู่", null, null, null, "S35", null, null, null));
        SCENES.put("S35", new SCENE("Narrator", "Day5", "สีหน้าเรียบ แต่ดวงตาเย็นลงเล็กน้อย", null, null, null, "S36", null, null, null));
        SCENES.put("S36", new SCENE("Narrator", "Day5", "หลังจากอีกคนเดินไป", null, null, null, "S37", null, null, null));
        SCENES.put("S37", new SCENE("Narrator", "Day5", "เขาเดินมาหาคุณทันที", null, null, null, "S38", null, null, null));
        SCENES.put("S38", new SCENE("Kim Jae-hyun", "Day5", "“…สนิทกันเหรอ”", null, null, null, "Q2", null, null, null));

        SCENES.put("Q2", new SCENE("คุณ", "Day5", "คำถามที่ 2", "A) “ไม่เลย เราไม่ได้คิดอะไร”", "B) “แค่คุยเรื่องงาน”", "C) “ทำไม หึงเหรอ”", null, "Q2_A", "Q2_B", "Q2_C"));
        SCENES.put("Q2_A", new SCENE("คุณ", "Day5", "“ไม่เลย เราไม่ได้คิดอะไร”", null, null, null, "S39", null, null, null));
        SCENES.put("Q2_B", new SCENE("คุณ", "Day5", "“แค่คุยเรื่องงาน”", null, null, null, "S39", null, null, null));
        SCENES.put("Q2_C", new SCENE("คุณ", "Day5", "“ทำไม หึงเหรอ”", null, null, null, "Q2_C1", null, null, null));

        SCENES.put("Q2_C1", new SCENE("Narrator", "Day5", "อีกฝ่ายนิ่งไป", null, null, null, "Q2_C2", null, null, null));
        SCENES.put("Q2_C2", new SCENE("Narrator", "Day5", "ก่อนตอบช้า ๆ", null, null, null, "Q2_C3", null, null, null));
        SCENES.put("Q2_C3", new SCENE("Kim Jae-hyun", "Day5", "“…ถ้าหึงแล้วจะทำไม”", null, null, null, "Q2_C4", null, null, null));
        SCENES.put("Q2_C4", new SCENE("Narrator", "Day5", "หัวใจคุณกระตุกทันที", null, null, null, "S39", null, null, null));

        SCENES.put("S39", new SCENE("Narrator", "Day5", "พักกลางวัน — บทสนทนาตรงไปตรงมา", null, null, null, "S40", null, null, null));
        SCENES.put("S40", new SCENE("Narrator", "Day5", "วันนี้คุณสองคนนั่งกินข้าวกันสองคน", null, null, null, "S41", null, null, null));
        SCENES.put("S41", new SCENE("Narrator", "Day5", "บรรยากาศไม่เหมือนเดิม มันเข้มข้นกว่า", null, null, null, "S42", null, null, null));
        SCENES.put("S42", new SCENE("Narrator", "Day5", "เหมือนมีคำพูดบางอย่างค้างอยู่กลางโต๊ะ", null, null, null, "S43", null, null, null));
        SCENES.put("S43", new SCENE("Narrator", "Day5", "เขาวางช้อนลง แล้วมองคุณตรง ๆ", null, null, null, "S44", null, null, null));
        SCENES.put("S44", new SCENE("Kim Jae-hyun", "Day5", "“…เธอคิดยังไงกับฉัน”", null, null, null, "Q3", null, null, null));

        SCENES.put("Q3", new SCENE("คุณ", "Day5", "คำถามที่ 3 (สำคัญมาก)", "A) “เราชอบเธอ”", "B) “ก็สำคัญนะ”", "C) “ไม่รู้สิ”", null, "Q3_A", "Q3_B", "Q3_C"));
        SCENES.put("Q3_A", new SCENE("คุณ", "Day5", "“เราชอบเธอ”", null, null, null, "Q3_R1", null, null, null));
        SCENES.put("Q3_B", new SCENE("คุณ", "Day5", "“ก็สำคัญนะ”", null, null, null, "Q3_R1", null, null, null));
        SCENES.put("Q3_C", new SCENE("คุณ", "Day5", "“ไม่รู้สิ”", null, null, null, "Q3_R1", null, null, null));

        SCENES.put("Q3_R1", new SCENE("Narrator", "Day5", "อีกฝ่ายเงียบไปหลายวินาที", null, null, null, "Q3_R2", null, null, null));
        SCENES.put("Q3_R2", new SCENE("Narrator", "Day5", "ก่อนสูดหายใจลึก", null, null, null, "Q3_R3", null, null, null));
        SCENES.put("Q3_R3", new SCENE("Kim Jae-hyun", "Day5", "“…ดี”", null, null, null, "Q3_R4", null, null, null));
        SCENES.put("Q3_R4", new SCENE("Kim Jae-hyun", "Day5", "“…เพราะฉันก็ชอบเธอ”", null, null, null, "Q3_R5", null, null, null));
        SCENES.put("Q3_R5", new SCENE("Narrator", "Day5", "คำว่า “ชอบ” ถูกพูดชัดเจนครั้งแรก แต่ยังไม่มีคำว่าแฟน ยังไม่มีคำว่ารัก", null, null, null, "S45", null, null, null));

        SCENES.put("S45", new SCENE("Narrator", "Day5", "หลังเลิกเรียน — ทางเดินเงียบ", null, null, null, "S46", null, null, null));
        SCENES.put("S46", new SCENE("Narrator", "Day5", "วันนี้คุณสองคนไม่ได้รีบกลับ เดินช้ากว่าปกติ", null, null, null, "S47", null, null, null));
        SCENES.put("S47", new SCENE("Narrator", "Day5", "ไม่มีใครพูดอะไรสักพัก แต่บรรยากาศไม่อึดอัด", null, null, null, "S48", null, null, null));
        SCENES.put("S48", new SCENE("Narrator", "Day5", "มือของคุณแกว่งไปตามจังหวะเดิน แล้วปลายนิ้วชนกันอีกครั้ง", null, null, null, "S49", null, null, null));
        SCENES.put("S49", new SCENE("Narrator", "Day5", "คราวนี้เขาเป็นฝ่ายจับไว้เอง มืออุ่นมาก แน่น…แต่ไม่บังคับ", null, null, null, "S50", null, null, null));
        SCENES.put("S50", new SCENE("Kim Jae-hyun", "Day5", "“…อย่าเปิดโอกาสให้ใครก่อนฉันได้ไหม”", null, null, null, "S51", null, null, null));
        SCENES.put("S51", new SCENE("Narrator", "Day5", "เสียงเบา แต่จริงจังที่สุดตั้งแต่เคยได้ยินมา", null, null, null, "Q4", null, null, null));

        SCENES.put("Q4", new SCENE("คุณ", "Day5", "คำถามที่ 4", "A) “ไม่มีใครสำคัญกว่าเธอ”", "B) “พูดแบบนี้ ใจเต้นนะ”", "C) “เรายังไม่ได้คบกันนะ”", null, "Q4_A", "Q4_B", "Q4_C"));
        SCENES.put("Q4_B", new SCENE("คุณ", "Day5", "“พูดแบบนี้ ใจเต้นนะ”", null, null, null, "END", null, null, null));
        SCENES.put("Q4_C", new SCENE("คุณ", "Day5", "“เรายังไม่ได้คบกันนะ”", null, null, null, "END", null, null, null));

        SCENES.put("Q4_A", new SCENE("คุณ", "Day5", "“ไม่มีใครสำคัญกว่าเธอ”", null, null, null, "Q4_A1", null, null, null));
        SCENES.put("Q4_A1", new SCENE("Narrator", "Day5", "อีกฝ่ายหยุดเดิน หันมามองคุณใกล้มาก", null, null, null, "Q4_A2", null, null, null));
        SCENES.put("Q4_A2", new SCENE("Kim Jae-hyun", "Day5", "“…งั้นรอฉันอีกนิด”", null, null, null, "Q4_A3", null, null, null));
        SCENES.put("Q4_A3", new SCENE("Kim Jae-hyun", "Day5", "“…พรุ่งนี้ ฉันจะไม่ให้เธอรอแล้ว”", null, null, null, "END", null, null, null));

        SCENES.put("END", new SCENE("Narrator", "Day5", "จบ Day 5", null, null, null, null, null, null, null));
    }

    private void SHOW_SCENE(String ID) {
    CURRENT_ID = ID;
    SCENE S = SCENES.get(ID);
    if (S == null) return;

    // 🔥 ดึงเลขฉากออกมา
    int sceneNumber = -1;
    if (ID.startsWith("S")) {
        try {
            sceneNumber = Integer.parseInt(ID.substring(1));
        } catch (Exception ignored) {}
    }

    // ห้องนอน (S1 - S10)
    if (sceneNumber >= 1 && sceneNumber <= 10) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องนอน.jpg");
    }

    // หน้าโรงเรียนเช้า (S11 - S20 + Q1)
    else if ((sceneNumber >= 11 && sceneNumber <= 20) || ID.startsWith("Q1")) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/หน้าโรงเรียน.png");
    }

    // ห้องเรียน (S21 - S30)
    else if (sceneNumber >= 21 && sceneNumber <= 38) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องเรียน.jpg");
    }
    // พักกลางวัน (S39 - S50)
    else if (sceneNumber >= 39 && sceneNumber <= 44) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/โรงอาหาร.jpg");
    }
    // ทางเดิน/เย็น (S45 - S50)
    else if (sceneNumber >= 45 && sceneNumber <= 50) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/โรงเรียนตอนเย็น.jpg");
    }

    DIALOG.SETDATA(S.NAME, S.DAY, S.TEXT);
    DIALOG.repaint();

    boolean HAS_CHOICES = S.C1 != null && S.C2 != null && S.C3 != null;
    CHOICE_PANEL.setVisible(HAS_CHOICES);
    BTN_CHOICE1.setVisible(HAS_CHOICES);
    BTN_CHOICE2.setVisible(HAS_CHOICES);
    BTN_CHOICE3.setVisible(HAS_CHOICES);    


    if (HAS_CHOICES) {
        BTN_CHOICE1.setText(S.C1);
        BTN_CHOICE2.setText(S.C2);
        BTN_CHOICE3.setText(S.C3);
    }
}

    private void GOTO_NEXT_BY_CLICK() {
        SCENE S = SCENES.get(CURRENT_ID);
        if (S == null) return;
        boolean HAS_CHOICES = S.C1 != null && S.C2 != null && S.C3 != null;
        if (HAS_CHOICES) return;
        if (S.NEXT != null) SHOW_SCENE(S.NEXT);
    }

   private void PICK(int INDEX) {
    SCENE S = SCENES.get(CURRENT_ID);
    if (S == null) return;

    AffectionManager affection = AffectionManager.getInstance();

    // Q1
    if (CURRENT_ID.equals("Q1")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 10);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 5);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 8);
    }

    // Q2
    if (CURRENT_ID.equals("Q2")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 3);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 7);
    }

    // Q3
    if (CURRENT_ID.equals("Q3")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 10);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 5);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, -3);
    }

    // Q4
    if (CURRENT_ID.equals("Q4")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 8);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 4);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
    }

    // รีเฟรชแถบ
    if (affectionBar != null) {
        affectionBar.refresh();
    }

    if (INDEX == 1 && S.NEXT1 != null) SHOW_SCENE(S.NEXT1);
    if (INDEX == 2 && S.NEXT2 != null) SHOW_SCENE(S.NEXT2);
    if (INDEX == 3 && S.NEXT3 != null) SHOW_SCENE(S.NEXT3);
}

    public static void main(String[] ARGS) {
        SwingUtilities.invokeLater(() -> new Day5().CREATEANDSHOWGUI());
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
    private Image ORIG;

    BGVIEW(String PATH) {
        SET_BG(PATH);
    }

    void SET_BG(String PATH) {
        ORIG = new ImageIcon(PATH).getImage();
        repaint();   // เรียกครั้งเดียวพอ
    }

    @Override
    protected void paintComponent(Graphics G) {
        super.paintComponent(G);

        if (ORIG != null) {
            G.drawImage(ORIG, 0, 0, getWidth(), getHeight(), this);
        }
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