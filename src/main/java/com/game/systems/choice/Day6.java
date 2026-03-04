package com.game.systems.choice;

import com.game.systems.affection.AffectionManager;
import com.game.systems.affection.CharacterRoute;
import com.game.ui.AffectionBar;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class Day6 {

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
        CREATEANDSHOWGUI(null);
    }

    public void CREATEANDSHOWGUI(JFrame existingFrame) {
        if (existingFrame == null) {
            FRAME = new JFrame("Kim Jae-hyun Route");
            FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            FRAME.setResizable(true);
        } else {
            FRAME = existingFrame;
            FRAME.getContentPane().removeAll();
        }

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
            int barW = 320; // ขนาดกำลังดี
            int barH = 85; // เตี้ยลง
            affectionBar.setBounds(40, 25, barW, barH);
        }
    }

    private Image LOAD_IMAGE_SAFE(String PATH) {
        try {
            Image IMG = new ImageIcon(PATH).getImage();
            if (IMG == null)
                return MAKE_EMPTY_IMAGE();
            if (IMG.getWidth(null) <= 0 || IMG.getHeight(null) <= 0)
                return MAKE_EMPTY_IMAGE();
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

        SCENES.put("S1", new SCENE("ผู้บรรยาย", "Day6", "DAY 6 — วันที่เหมือนเดท แต่ยังไม่เรียกว่าเดท", null, null,
                null, "S2", null, null, null));
        SCENES.put("S2", new SCENE("ผู้บรรยาย", "Day6", "เช้าวันนี้อากาศสดใสกว่าทุกวัน", null, null, null, "S3", null,
                null, null));
        SCENES.put("S3", new SCENE("ผู้บรรยาย", "Day6", "แสงแดดอ่อน ๆ ลอดผ่านผ้าม่านเข้ามา", null, null, null, "S4",
                null, null, null));
        SCENES.put("S4",
                new SCENE("ผู้บรรยาย", "Day6", "โทรศัพท์ของคุณสั่น", null, null, null, "S5", null, null, null));
        SCENES.put("S5", new SCENE("ผู้บรรยาย", "Day6", "ข้อความจากเขา", null, null, null, "S6", null, null, null));
        SCENES.put("S6",
                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“ตื่นยัง”", null, null, null, "S7", null, null, null));
        SCENES.put("S7", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“วันนี้ว่างไหม…”", null, null, null, "S8", null,
                null, null));
        SCENES.put("S8", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“อยากชวนไปเที่ยวด้วยกันหน่อย”", null, null, null,
                "S9", null, null, null));
        SCENES.put("S9", new SCENE("ผู้บรรยาย", "Day6", "คุณอ่านวนซ้ำสองรอบ ใจเต้นเร็วขึ้นโดยไม่รู้ตัว", null, null,
                null, "S10", null, null, null));
        SCENES.put("S10", new SCENE("ผู้บรรยาย", "Day6", "คำว่า “ไปเที่ยวด้วยกัน” มันไม่ใช่แค่คำธรรมดา", null, null,
                null, "S11", null, null, null));
        SCENES.put("S11", new SCENE("ผู้บรรยาย", "Day6", "คุณพิมพ์ตอบตกลง แม้มือจะสั่นเล็กน้อย", null, null, null,
                "S12", null, null, null));
        SCENES.put("S12", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“งั้นบ่ายสอง เจอกันหน้าสถานีรถไฟนะ :)”", null,
                null, null, "S13", null, null, null));
        SCENES.put("S13",
                new SCENE("ผู้บรรยาย", "Day6", "คุณเผลอยิ้มให้หน้าจอ", null, null, null, "S14", null, null, null));

        SCENES.put("S14", new SCENE("ผู้บรรยาย", "Day6", "ฉากที่ 1 — เจอกันครั้งแรกของวันนี้", null, null, null, "S15",
                null, null, null));
        SCENES.put("S15", new SCENE("ผู้บรรยาย", "Day6", "ตอนคุณเดินไปถึง เขายืนพิงเสาอยู่ก่อนแล้ว", null, null, null,
                "S16", null, null, null));
        SCENES.put("S16", new SCENE("ผู้บรรยาย", "Day6", "ใส่เสื้อที่คุณเคยชมว่าสีนี้เหมาะกับเขา", null, null, null,
                "S17", null, null, null));
        SCENES.put("S17", new SCENE("ผู้บรรยาย", "Day6", "พอเขาเห็นคุณ รอยยิ้มก็ปรากฏทันที", null, null, null, "S18",
                null, null, null));
        SCENES.put("S18", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“วันนี้…ดูดีนะ”", null, null, null, "S19", null,
                null, null));
        SCENES.put("S19", new SCENE("ผู้บรรยาย", "Day6", "หัวใจคุณสะดุด", null, null, null, "Q1", null, null, null));

        SCENES.put("Q1", new SCENE("คุณ", "Day6", "คำถามที่ 1 — จะตอบยังไง",
                "A) “ก็ต้องดูดีหน่อยสิ มากับนายนี่นา”",
                "B) “จริงเหรอ ขอบคุณนะ”",
                "C) “ปกติแหละ”",
                null, "Q1_A", "Q1_B", "Q1_C"));

        SCENES.put("Q1_A", new SCENE("คุณ", "Day6", "ความสัมพันธ์ +7", null, null, null, "S20", null, null, null));
        SCENES.put("Q1_B", new SCENE("คุณ", "Day6", "ความสัมพันธ์ +4", null, null, null, "S20", null, null, null));
        SCENES.put("Q1_C", new SCENE("คุณ", "Day6", "ความสัมพันธ์ +1", null, null, null, "S20", null, null, null));

        SCENES.put("S20", new SCENE("ผู้บรรยาย", "Day6", "ฉากที่ 2 — เดินเล่นในสวนสนุก", null, null, null, "S21", null,
                null, null));
        SCENES.put("S21", new SCENE("ผู้บรรยาย", "Day6", "เสียงเพลงสดใสลอยตามลม กลิ่นขนมหวานลอยมาแตะปลายจมูก", null,
                null, null, "S22", null, null, null));
        SCENES.put("S22", new SCENE("ผู้บรรยาย", "Day6", "คุณสองคนเดินช้า ๆ ไหล่แทบจะชนกัน", null, null, null, "S23",
                null, null, null));
        SCENES.put("S23", new SCENE("ผู้บรรยาย", "Day6", "บางจังหวะมือเกือบแตะ แต่ก็ไม่มีใครกล้าขยับ", null, null, null,
                "S24", null, null, null));
        SCENES.put("S24", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“อยากเล่นอะไรไหม”", null, null, null, "S25",
                null, null, null));
        SCENES.put("S25", new SCENE("ผู้บรรยาย", "Day6", "คุณชี้ไปที่ชิงช้าสวรรค์ มันสูงมาก แต่ก็โรแมนติกมากเหมือนกัน",
                null, null, null, "S26", null, null, null));
        SCENES.put("S26", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "เขายิ้มบาง ๆ “กลัวความสูงไหม”", null, null,
                null, "Q2", null, null, null));

        SCENES.put("Q2", new SCENE("คุณ", "Day6", "คำถามที่ 2 — ก่อนขึ้นชิงช้า",
                "A) “กลัวนิดหน่อย… แต่ถ้ามีเธออยู่ก็โอเค”",
                "B) “ไม่กลัวเลย”",
                "C) “งั้นไปอย่างอื่นแทนไหม”",
                null, "Q2_A", "Q2_B", "Q2_C"));

        SCENES.put("Q2_A", new SCENE("คุณ", "Day6", "“กลัวนิดหน่อย… แต่ถ้ามีเธออยู่ก็โอเค”", null, null, null, "S27",
                null, null, null));
        SCENES.put("Q2_B", new SCENE("คุณ", "Day6", "“ไม่กลัวเลย”", null, null, null, "S27", null, null, null));
        SCENES.put("Q2_C",
                new SCENE("คุณ", "Day6", "“งั้นไปอย่างอื่นแทนไหม”", null, null, null, "S27", null, null, null));

        SCENES.put("S27", new SCENE("ผู้บรรยาย", "Day6", "บนชิงช้าสวรรค์", null, null, null, "S28", null, null, null));
        SCENES.put("S28", new SCENE("ผู้บรรยาย", "Day6", "กระเช้าค่อย ๆ ลอยสูงขึ้น เมืองทั้งเมืองค่อย ๆ เล็กลง", null,
                null, null, "S29", null, null, null));
        SCENES.put("S29", new SCENE("ผู้บรรยาย", "Day6", "ภายในเงียบลงทันที มีแค่เสียงลมหายใจของคุณสองคน", null, null,
                null, "S30", null, null, null));
        SCENES.put("S30",
                new SCENE("ผู้บรรยาย", "Day6", "ระยะห่างแค่เอื้อมมือก็ถึง", null, null, null, "S31", null, null, null));
        SCENES.put("S31", new SCENE("ผู้บรรยาย", "Day6", "ตอนกระเช้าหยุดอยู่จุดสูงสุด ลมพัดแรงเล็กน้อย", null, null,
                null, "S32", null, null, null));
        SCENES.put("S32", new SCENE("ผู้บรรยาย", "Day6", "ตัวคุณเอนนิดหนึ่งโดยไม่ตั้งใจ", null, null, null, "S33", null,
                null, null));
        SCENES.put("S33",
                new SCENE("ผู้บรรยาย", "Day6", "เขาขยับเข้ามาใกล้ทันที", null, null, null, "S34", null, null, null));
        SCENES.put("S34",
                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“ระวังนะ”", null, null, null, "S35", null, null, null));
        SCENES.put("S35", new SCENE("ผู้บรรยาย", "Day6", "มือเขาแตะหลังมือคุณ แค่เบา ๆ แต่ไฟฟ้าเหมือนแล่นผ่านหัวใจ",
                null, null, null, "S36", null, null, null));
        SCENES.put("S36", new SCENE("ผู้บรรยาย", "Day6", "คุณเงยหน้าขึ้น สายตาสบกันพอดี", null, null, null, "S37", null,
                null, null));
        SCENES.put("S37",
                new SCENE("ผู้บรรยาย", "Day6", "โลกเหมือนเงียบไปหมด", null, null, null, "Q3", null, null, null));

        SCENES.put("Q3", new SCENE("คุณ", "Day6", "คำถามที่ 3 — จะทำยังไง",
                "A) ปล่อยให้มือแตะกันแบบนั้น",
                "B) รีบชักมือกลับ",
                "C) หัวเราะกลบเกลื่อน",
                null, "Q3_A", "Q3_B", "Q3_C"));

        SCENES.put("Q3_A", new SCENE("ผู้บรรยาย", "Day6", "คุณปล่อยให้มือแตะกันแบบนั้น", null, null, null, "Q3_A1",
                null, null, null));
        SCENES.put("Q3_A1", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“อยู่ใกล้ ๆ แบบนี้…มันดีจังเลยนะ”", null,
                null, null, "S38", null, null, null));
        SCENES.put("Q3_B", new SCENE("ผู้บรรยาย", "Day6", "คุณรีบชักมือกลับ แล้วทำเป็นมองวิวข้างนอก", null, null, null,
                "S38", null, null, null));
        SCENES.put("Q3_C", new SCENE("ผู้บรรยาย", "Day6", "คุณหัวเราะกลบเกลื่อน เหมือนทำให้ทุกอย่างเบาลง", null, null,
                null, "S38", null, null, null));

        SCENES.put("S38", new SCENE("ผู้บรรยาย", "Day6", "ฉากที่ 3 — ช่วงเย็นริมแม่น้ำ", null, null, null, "S39", null,
                null, null));
        SCENES.put("S39", new SCENE("ผู้บรรยาย", "Day6", "หลังจากเดินเล่นมาทั้งวัน คุณสองคนไปนั่งที่ริมแม่น้ำ", null,
                null, null, "S40", null, null, null));
        SCENES.put("S40", new SCENE("ผู้บรรยาย", "Day6", "พระอาทิตย์กำลังตก ท้องฟ้าเปลี่ยนเป็นสีส้มชมพู", null, null,
                null, "S41", null, null, null));
        SCENES.put("S41",
                new SCENE("ผู้บรรยาย", "Day6", "ลมเย็นพัดผมคุณปลิว", null, null, null, "S42", null, null, null));
        SCENES.put("S42", new SCENE("ผู้บรรยาย", "Day6", "เขาเอื้อมมือมาจัดผมให้เบา ๆ", null, null, null, "S43", null,
                null, null));
        SCENES.put("S43", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“เดี๋ยวเข้าตา”", null, null, null, "S44", null,
                null, null));
        SCENES.put("S44", new SCENE("ผู้บรรยาย", "Day6", "สัมผัสนั้นนุ่มนวลมาก ใกล้จนคุณได้ยินเสียงหัวใจเขา", null,
                null, null, "S45", null, null, null));
        SCENES.put("S45",
                new SCENE("ผู้บรรยาย", "Day6", "เงียบไปครู่หนึ่ง", null, null, null, "S46", null, null, null));
        SCENES.put("S46", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“เราดีใจนะที่วันนี้เธอมาด้วย”", null, null,
                null, "S47", null, null, null));
        SCENES.put("S47", new SCENE("ผู้บรรยาย", "Day6", "เสียงเขาจริงใจกว่าทุกครั้ง", null, null, null, "S48", null,
                null, null));
        SCENES.put("S48", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“ช่วงนี้…เราคิดถึงเธอบ่อยมากเลย”", null, null,
                null, "S49", null, null, null));
        SCENES.put("S49", new SCENE("ผู้บรรยาย", "Day6", "หัวใจคุณเต้นแรงจนแทบควบคุมไม่อยู่", null, null, null, "Q4",
                null, null, null));

        SCENES.put("Q4", new SCENE("คุณ", "Day6", "คำถามที่ 4 — จะตอบยังไง",
                "A) “เราก็คิดถึงเธอเหมือนกัน”",
                "B) “เหรอ” (เขินจนพูดไม่ออก)",
                "C) “อย่าพูดแบบนั้นสิ”",
                null, "Q4_A", "Q4_B", "Q4_C"));

        SCENES.put("Q4_A",
                new SCENE("คุณ", "Day6", "“เราก็คิดถึงเธอเหมือนกัน”", null, null, null, "S50", null, null, null));
        SCENES.put("Q4_B", new SCENE("คุณ", "Day6", "“เหรอ”", null, null, null, "S50", null, null, null));
        SCENES.put("Q4_C", new SCENE("คุณ", "Day6", "“อย่าพูดแบบนั้นสิ”", null, null, null, "S50", null, null, null));

        SCENES.put("S50", new SCENE("ผู้บรรยาย", "Day6", "ฉากที่ 4 — เดินกลับตอนกลางคืน", null, null, null, "S51", null,
                null, null));
        SCENES.put("S51", new SCENE("ผู้บรรยาย", "Day6", "ไฟถนนเปิดสว่าง ผู้คนบางตาลง", null, null, null, "S52", null,
                null, null));
        SCENES.put("S52", new SCENE("ผู้บรรยาย", "Day6", "มือของคุณกับเขาแกว่งไปมาใกล้กัน ใกล้จนแทบจะประสานกันได้",
                null, null, null, "S53", null, null, null));
        SCENES.put("S53", new SCENE("ผู้บรรยาย", "Day6", "จังหวะหนึ่ง มือชนกันจริง ๆ ไม่มีใครชักออก", null, null, null,
                "S54", null, null, null));
        SCENES.put("S54", new SCENE("ผู้บรรยาย", "Day6", "นิ้วก้อยแตะกัน ก่อนที่เขาจะกระซิบ", null, null, null, "S55",
                null, null, null));
        SCENES.put("S55", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“เราถามอะไรหน่อยได้ไหม”", null, null, null,
                "S56", null, null, null));
        SCENES.put("S56", new SCENE("ผู้บรรยาย", "Day6", "คุณหันไปมอง", null, null, null, "S57", null, null, null));
        SCENES.put("S57", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“ตอนนี้…เรากำลังจีบกันอยู่ใช่ไหม”", null, null,
                null, "Q5", null, null, null));

        SCENES.put("Q5", new SCENE("คุณ", "Day6", "คำถามที่ 5 — สถานะ",
                "A) “ใช่…แล้วเธอล่ะคิดว่าไง”",
                "B) “ก็…มั้ง”",
                "C) “ไม่รู้สิ”",
                null, "Q5_A", "Q5_B", "Q5_C"));

        SCENES.put("Q5_A",
                new SCENE("คุณ", "Day6", "“ใช่…แล้วเธอล่ะคิดว่าไง”", null, null, null, "S58", null, null, null));
        SCENES.put("Q5_B", new SCENE("คุณ", "Day6", "“ก็…มั้ง”", null, null, null, "S58", null, null, null));
        SCENES.put("Q5_C", new SCENE("คุณ", "Day6", "“ไม่รู้สิ”", null, null, null, "S58", null, null, null));

        SCENES.put("S58", new SCENE("ผู้บรรยาย", "Day6", "เขาหยุดเดิน หันมาหาคุณตรง ๆ", null, null, null, "S59", null,
                null, null));
        SCENES.put("S59",
                new SCENE("ผู้บรรยาย", "Day6", "แสงไฟสะท้อนดวงตาเขา", null, null, null, "S60", null, null, null));
        SCENES.put("S60", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day6", "“งั้น…ให้เราได้จีบเธอแบบจริงจังเลยได้ไหม”",
                null, null, null, "S61", null, null, null));
        SCENES.put("S61", new SCENE("ผู้บรรยาย", "Day6", "มือเขายังจับมือคุณอยู่ อุ่นมาก มั่นคงมาก", null, null, null,
                "S62", null, null, null));
        SCENES.put("S62",
                new SCENE("ผู้บรรยาย", "Day6", "แต่ยังไม่เรียกว่าแฟน", null, null, null, "S63", null, null, null));
        SCENES.put("S63", new SCENE("ผู้บรรยาย", "Day6", "ทุกอย่างจะถูกตัดสิน ในวันพรุ่งนี้", null, null, null, "S64",
                null, null, null));
        SCENES.put("S64", new SCENE("ผู้บรรยาย", "Day6", "จบ DAY 6", null, null, null, "S65", null, null, null));
        SCENES.put("S65", new SCENE("ผู้บรรยาย", "Day6", "คืนนี้คุณนอนพร้อมรอยยิ้ม และคำถามในหัว", null, null, null,
                "S66", null, null, null));
        SCENES.put("S66", new SCENE("ผู้บรรยาย", "Day6", "พรุ่งนี้… ความสัมพันธ์นี้จะกลายเป็นอะไร", null, null, null,
                "END", null, null, null));

        SCENES.put("END", new SCENE("ผู้บรรยาย", "Day6", "จบ Day 6", null, null, null, null, null, null, null));

    }

    private void SHOW_SCENE(String ID) {
        CURRENT_ID = ID;
        SCENE S = SCENES.get(ID);
        if (S == null)
            return;

        // 🔥 ดึงเลขฉากออกมา
        int sceneNumber = -1;
        if (ID.startsWith("S")) {
            try {
                sceneNumber = Integer.parseInt(ID.substring(1));
            } catch (Exception ignored) {
            }
        }

        // ห้องนอน (S1 - S10)
        if (sceneNumber >= 1 && sceneNumber <= 13) {
            BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องนอน.jpg");
        }

        // หน้าโรงเรียนเช้า (S11 - S20 + Q1)
        else if ((sceneNumber >= 14 && sceneNumber <= 19) || ID.startsWith("Q1")) {
            BG_VIEW.SET_BG("src/main/resources/images/backgrounds/สถานีรถไฟฟ้า.jpg");
        }
        // ห้องเรียน (S21 - S30)
        else if (sceneNumber >= 20 && sceneNumber <= 26) {
            BG_VIEW.SET_BG("src/main/resources/images/backgrounds/สวนสนุก.jpg");
        }
        // พักกลางวัน (S27 - S44)
        else if (sceneNumber >= 27 && sceneNumber <= 37) {
            BG_VIEW.SET_BG("src/main/resources/images/backgrounds/บนชิงช้า.png");
        }
        // ทางเดิน/เย็น (S45 - S50)
        else if (sceneNumber >= 38 && sceneNumber <= 49) {
            BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ตอนเย็น.png");
        } else if (sceneNumber >= 50 && sceneNumber <= 64) {
            BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ทางเดินตอนกลางคืน.jpg");
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
        if (S == null)
            return;
        boolean HAS_CHOICES = S.C1 != null && S.C2 != null && S.C3 != null;
        if (HAS_CHOICES)
            return;

        if (CURRENT_ID.equals("END")) {
            new Day7().CREATEANDSHOWGUI(FRAME);
            return;
        }

        if (S.NEXT != null)
            SHOW_SCENE(S.NEXT);
    }

    private void PICK(int INDEX) {
        SCENE S = SCENES.get(CURRENT_ID);
        if (S == null)
            return;

        AffectionManager affection = AffectionManager.getInstance();

        // Q1
        if (CURRENT_ID.equals("Q1")) {
            if (INDEX == 1)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 7);
            if (INDEX == 2)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 4);
            if (INDEX == 3)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
        }

        // Q2
        if (CURRENT_ID.equals("Q2")) {
            if (INDEX == 1)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 8);
            if (INDEX == 2)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 3);
            if (INDEX == 3)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
        }

        // Q3
        if (CURRENT_ID.equals("Q3")) {
            if (INDEX == 1)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 9);
            if (INDEX == 2)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
            if (INDEX == 3)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 3);
        }

        // Q4
        if (CURRENT_ID.equals("Q4")) {
            if (INDEX == 1)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 10);
            if (INDEX == 2)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 4);
            if (INDEX == 3)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, -2);
        }
        // Q5
        if (CURRENT_ID.equals("Q5")) {
            if (INDEX == 1)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 8);
            if (INDEX == 2)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 3);
            if (INDEX == 3)
                affection.addAffection(CharacterRoute.KIM_JAEHYUN, -3);
        }
        // รีเฟรชแถบ
        if (affectionBar != null) {
            affectionBar.refresh();
        }

        if (INDEX == 1 && S.NEXT1 != null)
            SHOW_SCENE(S.NEXT1);
        if (INDEX == 2 && S.NEXT2 != null)
            SHOW_SCENE(S.NEXT2);
        if (INDEX == 3 && S.NEXT3 != null)
            SHOW_SCENE(S.NEXT3);
    }

    public static void main(String[] ARGS) {
        SwingUtilities.invokeLater(() -> new Day6().CREATEANDSHOWGUI());
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

        SCENE(String NAME, String DAY, String TEXT, String C1, String C2, String C3, String NEXT, String NEXT1,
                String NEXT2, String NEXT3) {
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
            repaint(); // เรียกครั้งเดียวพอ
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
            if (LINE.length() > 0)
                OUT.add(LINE.toString());
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
            if (STATE == null)
                STATE = "NORMAL";

            Color BG = (Color) B.getClientProperty("BTN_BG");
            Color BR = (Color) B.getClientProperty("BTN_BORDER");
            Color HOVER = (Color) B.getClientProperty("BTN_HOVER");
            Color PRESS = (Color) B.getClientProperty("BTN_PRESS");

            Color USE_BG = BG;
            if ("HOVER".equals(STATE))
                USE_BG = HOVER;
            if ("PRESS".equals(STATE))
                USE_BG = PRESS;

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