package com.game.systems.choice;


import com.game.systems.affection.AffectionManager;
import com.game.systems.affection.CharacterRoute;
import com.game.ui.AffectionBar;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;


public class Day1 {

    private JButton BTN_SETTINGS;
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

                SCENES.put("S1", new SCENE("ผู้บรรยาย", "Day1", "DAY 1 — วันแรกใต้ท้องฟ้าใหม่", null, null, null, "S2",
                                null,
                                null, null));
                SCENES.put("S2", new SCENE("ผู้บรรยาย", "Day1", "เสียงล้อรถบรรทุกขนของค่อย ๆ เงียบลงหน้าบ้านหลังใหม่",
                                null,
                                null, null, "S3", null, null, null));
                SCENES.put("S3", new SCENE("ผู้บรรยาย", "Day1", "บ้านสองชั้นสีครีมในเมืองที่คุณไม่เคยรู้จักมาก่อน",
                                null, null,
                                null, "S4", null, null, null));
                SCENES.put("S4",
                                new SCENE("คุณ", "Day1", "“ต่อจากนี้…ที่นี่คือบ้านของเรา”", null, null, null, "S5",
                                                null, null, null));
                SCENES.put("S5", new SCENE("ผู้บรรยาย", "Day1", "คำพูดของผู้ปกครองยังดังอยู่ในหัว", null, null, null,
                                "S6",
                                null, null, null));
                SCENES.put("S6",
                                new SCENE("ผู้บรรยาย", "Day1",
                                                "ทุกอย่างเกิดขึ้นเร็วเกินไป—การย้ายงาน การเก็บของ การบอกลาเพื่อน", null,
                                                null, null, "S7", null, null, null));
                SCENES.put("S7", new SCENE("ผู้บรรยาย", "Day1", "คุณไม่มีแม้แต่เวลาจะตั้งตัว", null, null, null, "S8",
                                null,
                                null, null));
                SCENES.put("S8", new SCENE("ผู้บรรยาย", "Day1", "คุณยืนมองป้ายชื่อโรงเรียนที่พรุ่งนี้ต้องเข้าไปเรียน",
                                null,
                                null, null, "S9", null, null, null));
                SCENES.put("S9", new SCENE("คุณ", "Day1", "“โรงเรียนมัธยม เอเวอร์บลู (Everblue High School)”", null,
                                null, null,
                                "S10", null, null, null));
                SCENES.put("S10",
                                new SCENE("คุณ", "Day1", "ชื่อดูสงบ…แต่หัวใจคุณกลับไม่สงบเลยสักนิด", null, null, null,
                                                "S11",
                                                null, null, null));

                SCENES.put("S11",
                                new SCENE("คุณ", "Day1", "เมืองนี้เงียบกว่าที่คิด", null, null, null, "S12", null, null,
                                                null));
                SCENES.put("S12",
                                new SCENE("คุณ", "Day1", "ผู้คนดูสุภาพ แต่ก็เหมือนมีระยะห่างบางอย่าง", null, null, null,
                                                "S13", null, null, null));
                SCENES.put("S13",
                                new SCENE("ผู้บรรยาย", "Day1", "เหมือนทุกคนรู้จักกันหมดแล้ว ยกเว้น “คุณ”", null, null,
                                                null,
                                                "S14", null, null, null));
                SCENES.put("S14",
                                new SCENE("ผู้บรรยาย", "Day1", "คืนนั้นคุณจัดของเข้าห้องใหม่", null, null, null, "S15",
                                                null,
                                                null, null));
                SCENES.put("S15",
                                new SCENE("ผู้บรรยาย", "Day1", "บางคนบอกว่าจะคิดถึง", null, null, null, "S16", null,
                                                null, null));
                SCENES.put("S16",
                                new SCENE("ผู้บรรยาย", "Day1", "บางคนบอกว่าอย่าลืมกัน", null, null, null, "S17", null,
                                                null, null));
                SCENES.put("S17",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณวางโทรศัพท์ลงช้า ๆ", null, null, null, "S18", null,
                                                null, null));
                SCENES.put("S18",
                                new SCENE("คุณ", "Day1", "“เริ่มใหม่อีกครั้งสินะ…”", null, null, null, "S19", null,
                                                null, null));
                SCENES.put("S19", new SCENE("ผู้บรรยาย", "Day1", "เช้าวันเปิดเทอม", null, null, null, "S20", null, null,
                                null));
                SCENES.put("S20", new SCENE("คุณ", "Day1", "ลมเช้าเย็นกว่าที่คาด", null, null, null, "S21", null, null,
                                null));
                SCENES.put("S21",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณยืนอยู่หน้าประตูโรงเรียนในชุดนักเรียนใหม่เอี่ยม",
                                                null,
                                                null, null, "S22", null, null, null));
                SCENES.put("S22",
                                new SCENE("ผู้บรรยาย", "Day1", " เสียงนักเรียนรอบตัวเต็มไปด้วยบทสนทนาและเสียงหัวเราะ",
                                                null,
                                                null, null, "S23", null, null, null));
                SCENES.put("S23",
                                new SCENE("ผู้บรรยาย", "Day1", "แต่ไม่มีเสียงไหนเรียกชื่อคุณ", null, null, null, "S24",
                                                null,
                                                null, null));
                SCENES.put("S24", new SCENE("ผู้บรรยาย", "Day1", "คุณสูดหายใจลึก", null, null, null, "S25", null, null,
                                null));
                SCENES.put("S25",
                                new SCENE("ผู้บรรยาย", "Day1", "ก้าวเท้าเข้าไปในรั้วโรงเรียน", null, null, null, "S26",
                                                null,
                                                null, null));
                SCENES.put("S26",
                                new SCENE("ผู้บรรยาย", "Day1", "และทันทีที่คุณเดินผ่านมุมตึกเรียน", null, null, null,
                                                "S27",
                                                null, null, null));
                SCENES.put("S27", new SCENE("", "Day1", "ปึก!", null, null, null, "S28", null, null, null));
                SCENES.put("S28",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณชนเข้ากับใครบางคนอย่างแรง", null, null, null, "S29",
                                                null,
                                                null, null));
                SCENES.put("S29",
                                new SCENE("ผู้บรรยาย", "Day1", "หนังสือในมือเขาร่วงกระจายบนพื้น", null, null, null,
                                                "S30",
                                                null, null, null));
                SCENES.put("S30",
                                new SCENE("ผู้บรรยาย", "Day1", "ดวงตาคู่หนึ่งเงยขึ้นมามองคุณ", null, null, null, "S31",
                                                null,
                                                null, null));
                SCENES.put("S31",
                                new SCENE("ผู้บรรยาย", "Day1", "แววตานั้นนิ่ง เย็น…แต่มีบางอย่างซ่อนอยู่", null, null,
                                                null,
                                                "S32", null, null, null));
                SCENES.put("S32", new SCENE("???", "Day1", "“นักเรียนใหม่…?”", null, null, null, "S33", null, null,
                                null));
                SCENES.put("S33",
                                new SCENE("ผู้บรรยาย", "Day1", "น้ำเสียงไม่ได้เย็นชา", null, null, null, "S34", null,
                                                null, null));
                SCENES.put("S34",
                                new SCENE("ผู้บรรยาย", "Day1", "แต่ก็ไม่ได้เป็นมิตร", null, null, null, "S35", null,
                                                null, null));
                SCENES.put("S35", new SCENE("ผู้บรรยาย", "Day1", "วินาทีนั้นเอง", null, null, null, "S36", null, null,
                                null));
                SCENES.put("S36",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณยังไม่รู้เลยว่า การชนกันเพียงครั้งเดียว", null, null,
                                                null,
                                                "S37", null, null, null));
                SCENES.put("S37",
                                new SCENE("ผู้บรรยาย", "Day1", "จะเปลี่ยน “7 วันแรก” ของคุณไปตลอดกาล", null, null, null,
                                                "S38", null, null, null));
                SCENES.put("S38",
                                new SCENE("ผู้บรรยาย", "Day1", "“หนังสือยังคงกระจายอยู่บนพื้น", null, null, null, "S39",
                                                null,
                                                null, null));
                SCENES.put("S39",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณรีบก้มลงเก็บทันที", null, null, null, "S40", null,
                                                null, null));
                SCENES.put("S40",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายก็ก้มลงพร้อมกันพอดี", null, null, null, "S41",
                                                null,
                                                null, null));
                SCENES.put("S41",
                                new SCENE("ผู้บรรยาย", "Day1", "ปลายนิ้วของคุณแตะโดนมือเขาโดยบังเอิญ", null, null, null,
                                                "S42", null, null, null));
                SCENES.put("S42",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณชะงักไปเล็กน้อยก่อนจะดึงมือกลับ", null, null, null,
                                                "S43",
                                                null, null, null));
                SCENES.put("S43",
                                new SCENE("ผู้บรรยาย", "Day1", "เขาเงยหน้าขึ้นมองคุณ", null, null, null, "S44", null,
                                                null, null));
                SCENES.put("S44",
                                new SCENE("ผู้บรรยาย", "Day1", "สายตานิ่ง ๆ แต่ไม่ได้ดูน่ากลัว", null, null, null,
                                                "S45",
                                                null, null, null));
                SCENES.put("S45",
                                new SCENE("???", "Day1", "“ระวังหน่อย… ตรงนี้คนเดินผ่านเยอะ”", null, null, null, "S46",
                                                null,
                                                null, null));
                SCENES.put("S46",
                                new SCENE("ผู้บรรยาย", "Day1", "น้ำเสียงเรียบ เหมือนพูดเตือนตามปกติ", null, null, null,
                                                "S47",
                                                null, null, null));
                SCENES.put("S47",
                                new SCENE("ผู้บรรยาย", "Day1", "ตอนนั้นเองที่คุณนึกขึ้นได้", null, null, null, "S48",
                                                null,
                                                null, null));
                SCENES.put("S48",
                                new SCENE("ผู้บรรยาย", "Day1",
                                                "คนตรงหน้าคือหนึ่งในนักเรียนที่คนอื่นพูดถึงบ่อยในโรงเรียนนี้",
                                                null, null, null, "Q1", null, null, null));

                SCENES.put("Q1", new SCENE("คุณ", "Day1", "คำถามที่ 1 - คำพูดแรก",
                                "A) “ขอโทษนะ เรารีบไปหน่อย…”",
                                "B) “ก็คุณเดินไม่ดูทางเหมือนกันนะ”",
                                "C) “เอ่อ… ขอบคุณที่ช่วยเก็บนะ”",
                                null, "Q1_A", "Q1_B", "Q1_C"));

                SCENES.put("Q1_A",
                                new SCENE("คุณ", "Day1", "+ ความประทับใจ (สุภาพ จริงใจ) +5", null, null, null, "S49",
                                                null,
                                                null, null));
                SCENES.put("Q1_B",
                                new SCENE("คุณ", "Day1", "- เล็กน้อย (ปากไว แต่ดูมั่นใจ) /+2", null, null, null, "S49",
                                                null,
                                                null, null));
                SCENES.put("Q1_C", new SCENE("คุณ", "Day1", "+ ความอ่อนโยน +1", null, null, null, "S49", null, null,
                                null));

                SCENES.put("S49",
                                new SCENE("ผู้บรรยาย", "Day1", "เขามองคุณอยู่ครู่หนึ่งก่อนจะลุกขึ้นยืน", null, null,
                                                null,
                                                "S50", null, null, null));
                SCENES.put("S50",
                                new SCENE("ผู้บรรยาย", "Day1", "แล้วส่งหนังสือเล่มสุดท้ายคืนให้", null, null, null,
                                                "S51",
                                                null, null, null));
                SCENES.put("S51",
                                new SCENE("???", "Day1", "“นักเรียนใหม่สินะ… ห้อง 2-B ใช่ไหม?”", null, null, null,
                                                "S52",
                                                null, null, null));
                SCENES.put("S52",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณพยักหน้าอย่างแปลกใจ", null, null, null, "S53", null,
                                                null, null));
                SCENES.put("S53",
                                new SCENE("???", "Day1", "“รู้ได้ยังไง…?”", null, null, null, "S54", null, null, null));
                SCENES.put("S54",
                                new SCENE("ผู้บรรยาย", "Day1", "ก่อนมองหน้าคุณอีกครั้ง", null, null, null, "S55", null,
                                                null, null));
                SCENES.put("S55",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณชะงัก", null, null, null, "S56", null, null, null));
                SCENES.put("S56",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายมองคุณอยู่ครู่หนึ่ง", null, null, null, "S57",
                                                null,
                                                null, null));
                SCENES.put("S57",
                                new SCENE("ผู้บรรยาย", "Day1", "เหมือนกำลังประเมินอะไรบางอย่าง", null, null, null,
                                                "S58",
                                                null, null, null));
                SCENES.put("S58", new SCENE("ผู้บรรยาย", "Day1", "ก่อนตอบสั้น ๆ", null, null, null, "S59", null, null,
                                null));
                SCENES.put("S59", new SCENE("???", "Day1", "“ดูออก”", null, null, null, "S60", null, null, null));
                SCENES.put("S60",
                                new SCENE("ผู้บรรยาย", "Day1", "แล้วเขาก็เดินผ่านคุณไป", null, null, null, "S61", null,
                                                null, null));
                SCENES.put("S61",
                                new SCENE("ผู้บรรยาย", "Day1", "แล้วเขาก็เดินผ่านคุณไป", null, null, null, "S62", null,
                                                null, null));
                SCENES.put("S62",
                                new SCENE("ผู้บรรยาย", "Day1", "ทิ้งไว้แค่ความรู้สึกแปลก ๆ ในอก", null, null, null,
                                                "S63",
                                                null, null, null));
                SCENES.put("S63",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณไม่รู้ว่าทำไม", null, null, null, "S64", null, null,
                                                null));
                SCENES.put("S64", new SCENE("ผู้บรรยาย", "Day1", "ในห้องเรียน 2-B", null, null, null, "S65", null, null,
                                null));
                SCENES.put("S65",
                                new SCENE("ผู้บรรยาย", "Day1", "ครูประจำชั้นเรียกคุณขึ้นแนะนำตัว", null, null, null,
                                                "S66",
                                                null, null, null));
                SCENES.put("S66",
                                new SCENE("ผู้บรรยาย", "Day1", "นักเรียนทั้งห้องมองมาที่คุณ", null, null, null, "S67",
                                                null,
                                                null, null));
                SCENES.put("S67",
                                new SCENE("ผู้บรรยาย", "Day1", "บางคนยิ้ม บางคนกระซิบกัน", null, null, null, "S68",
                                                null, null, null));
                SCENES.put("S68",
                                new SCENE("ผู้บรรยาย", "Day1", "หัวใจคุณเต้นแรงอีกครั้ง", null, null, null, "Q2", null,
                                                null, null));

                SCENES.put("Q2", new SCENE("คุณ", "Day1", "คำถามที่ 2 – แนะนำตัว",
                                "A) แนะนำตัวสั้น ๆ สุภาพ",
                                "B) พูดติดตลกให้ห้องหัวเราะ",
                                "C) พูดจริงจังว่าอยากเริ่มต้นใหม่",
                                null, "Q2_A", "Q2_B", "Q2_C"));

                SCENES.put("Q2_A",
                                new SCENE("คุณ", "Day1", "ได้ภาพลักษณ์เรียบร้อย ", null, null, null, "S69", null, null,
                                                null));
                SCENES.put("Q2_B", new SCENE("คุณ", "Day1", "ได้ความสนใจจากหลายคน", null, null, null, "S69", null, null,
                                null));
                SCENES.put("Q2_C",
                                new SCENE("คุณ", "Day1", "บางคนเริ่มสนใจคุณเป็นพิเศษ", null, null, null, "S69", null,
                                                null, null));

                SCENES.put("S69",
                                new SCENE("ผู้บรรยาย", "Day1", "ห้องเรียน", null, null, null, "S70", null, null, null));
                SCENES.put("S70",
                                new SCENE("ผู้บรรยาย", "Day1", "หลังแนะนำตัวหน้าห้องเสร็จ", null, null, null, "S71",
                                                null, null, null));
                SCENES.put("S71",
                                new SCENE("ผู้บรรยาย", "Day1", "ครูให้คุณไปนั่งที่ว่างด้านหลัง", null, null, null,
                                                "S72",
                                                null, null, null));
                SCENES.put("S72",
                                new SCENE("ผู้บรรยาย", "Day1", "และเมื่อคุณเดินไปถึงโต๊ะเรียน", null, null, null, "S73",
                                                null,
                                                null, null));
                SCENES.put("S73",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณหยุดชะงักเล็กน้อย", null, null, null, "S74", null,
                                                null, null));
                SCENES.put("S74",
                                new SCENE("ผู้บรรยาย", "Day1", "โลกมันกลมเกินไปหรือเปล่า…", null, null, null, "S75",
                                                null, null, null));
                SCENES.put("S75",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายเหลือบมองคุณนิดเดียว", null, null, null, "S76",
                                                null,
                                                null, null));
                SCENES.put("S76", new SCENE("???", "Day1", "“บังเอิญอีกแล้ว”", null, null, null, "S77", null, null,
                                null));
                SCENES.put("S77",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณหัวเราะ", null, null, null, "Q3", null, null, null));

                SCENES.put("Q3", new SCENE("คุณ", "Day1", "คำถามที่ 3 – ความบังเอิญ",
                                "A) “หรือว่าเราโชคชะตาผูกกันนะ”",
                                "B) “โลกมันกลมดีเนอะ”",
                                "C) “ก็แค่บังเอิญแหละ”",
                                null, "Q3_A", "Q3_B", "Q3_C"));

                SCENES.put("Q3_A",
                                new SCENE("คุณ", "Day1", "ความสัมพันธ์ +5 (เขินแรง)", null, null, null, "S117", null,
                                                null, null));
                SCENES.put("Q3_B",
                                new SCENE("คุณ", "Day1", "ความสัมพันธ์ +2", null, null, null, "S78", null, null, null));
                SCENES.put("Q3_C",
                                new SCENE("คุณ", "Day1", "ความสัมพันธ์ 0", null, null, null, "S78", null, null, null));
                SCENES.put("S117",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายยิ้มเล็กน้อย", null, null, null, "S78", null,
                                                null, null));

                SCENES.put("S78", new SCENE("ผู้บรรยาย", "Day1", "พักกลางวัน", null, null, null, "S79", null, null,
                                null));
                SCENES.put("S79",
                                new SCENE("ผู้บรรยาย", "Day1", "โรงอาหารเต็มไปด้วยนักเรียนที่มีเพื่อนอยู่แล้ว", null,
                                                null,
                                                null, "S80", null, null, null));
                SCENES.put("S80",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณยืนลังเลอยู่หน้าโรงอาหาร", null, null, null, "S81",
                                                null,
                                                null, null));
                SCENES.put("S81",
                                new SCENE("ผู้บรรยาย", "Day1", "ทันใดนั้น คุณหันไป", null, null, null, "S82", null,
                                                null, null));
                SCENES.put("S82",
                                new SCENE("???", "Day1", "“ถ้ายังไม่มีที่นั่ง… ไปด้วยกันไหม”", null, null, null, "S83",
                                                null,
                                                null, null));
                SCENES.put("S83",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณแอบแปลกใจเล็กน้อย เพราะอีกฝ่ายดูเป็นคนไม่ค่อยชวนใคร",
                                                null,
                                                null, null, "Q4", null, null, null));

                SCENES.put("Q4", new SCENE("คุณ", "Day1", "คำถามที่ 4 — คุณตอบยังไง?",
                                "A) “ดีเลย เรากำลังไม่รู้จะไปไหนพอดี”",
                                "B) “ไม่เป็นไร เราไปคนเดียวได้”",
                                "C) “นาย ชวนเราเหรอเนี่ย น่าแปลกใจนะ”",
                                null, "Q4_A", "Q4_B", "Q4_C"));

                SCENES.put("Q4_A",
                                new SCENE("คุณ", "Day1", "ความสัมพันธ์ +3", null, null, null, "S84", null, null, null));
                SCENES.put("Q4_B",
                                new SCENE("คุณ", "Day1", "ความสัมพันธ์ -1", null, null, null, "S84", null, null, null));
                SCENES.put("Q4_C",
                                new SCENE("คุณ", "Day1", "ความสัมพันธ์ +1 (ขำ ๆ)", null, null, null, "S84", null, null,
                                                null));

                SCENES.put("S84",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายพยักหน้าเบา ๆ แล้วเดินนำไป", null, null, null,
                                                "S85",
                                                null, null, null));
                SCENES.put("S85", new SCENE("ผู้บรรยาย", "Day1", "ระหว่างกินข้าว", null, null, null, "S86", null, null,
                                null));
                SCENES.put("S86",
                                new SCENE("ผู้บรรยาย", "Day1", "บทสนทนาส่วนใหญ่เงียบ", null, null, null, "S87", null,
                                                null, null));
                SCENES.put("S87",
                                new SCENE("ผู้บรรยาย", "Day1", "จนคุณเป็นฝ่ายถามก่อน", null, null, null, "S88", null,
                                                null, null));
                SCENES.put("S88", new SCENE("คุณ", "Day1", "“นาย ชื่ออะไรเหรอ”", null, null, null, "S89", null, null,
                                null));
                SCENES.put("S89",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายมองคุณครู่หนึ่ง", null, null, null, "S90", null,
                                                null, null));
                SCENES.put("S90",
                                new SCENE("ผู้บรรยาย", "Day1", "เหมือนกำลังตัดสินใจว่าจะตอบไหม", null, null, null,
                                                "S91",
                                                null, null, null));
                SCENES.put("S91", new SCENE("ผู้บรรยาย", "Day1", "ก่อนพูดว่า", null, null, null, "S92", null, null,
                                null));
                SCENES.put("S92",
                                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day1", "คิมแจฮยอน (Kim Jaehyun)", null, null,
                                                null,
                                                "S93", null, null, null));
                SCENES.put("S93",
                                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day1", "“แล้ว เธอ ล่ะ”", null, null, null, "S94",
                                                null,
                                                null, null));
                SCENES.put("S94",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณบอกชื่อตัวเอง", null, null, null, "S95", null, null,
                                                null));
                SCENES.put("S95",
                                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day1", "“ชื่อเข้ากับ เธอ ดี”", null, null, null,
                                                "S96",
                                                null, null, null));
                SCENES.put("S96", new SCENE("ผู้บรรยาย", "Day1", "คำพูดธรรมดา", null, null, null, "S97", null, null,
                                null));
                SCENES.put("S97",
                                new SCENE("ผู้บรรยาย", "Day1", "แต่หัวใจคุณกลับเต้นแรงอย่างประหลาด", null, null, null,
                                                "S98",
                                                null, null, null));
                SCENES.put("S98", new SCENE("ผู้บรรยาย", "Day1", "หลังเลิกเรียน", null, null, null, "S99", null, null,
                                null));
                SCENES.put("S99",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณเก็บของเสร็จช้ากว่าคนอื่น", null, null, null, "S100",
                                                null,
                                                null, null));
                SCENES.put("S100",
                                new SCENE("ผู้บรรยาย", "Day1", "เมื่อเดินออกมาหน้าโรงเรียน", null, null, null, "S101",
                                                null,
                                                null, null));
                SCENES.put("S101",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณเห็นอีกฝ่ายยืนอยู่ใต้ต้นไม้ใหญ่", null, null, null,
                                                "S102", null, null, null));
                SCENES.put("S102",
                                new SCENE("ผู้บรรยาย", "Day1", "ลมเย็นพัดผ่าน ใบไม้ไหวเบา ๆ", null, null, null, "S103",
                                                null,
                                                null, null));
                SCENES.put("S103",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายหันมาเห็นคุณพอดี", null, null, null, "S104",
                                                null, null, null));
                SCENES.put("S104",
                                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day1", "“วันแรกเป็นยังไงบ้าง”", null, null, null,
                                                "S105", null, null, null));
                SCENES.put("S105",
                                new SCENE("ผู้บรรยาย", "Day1", "คุณคิดอยู่ครู่หนึ่งก่อนตอบ", null, null, null, "Q5",
                                                null, null, null));

                SCENES.put("Q5", new SCENE("คุณ", "Day1", "คำถามที่ 5 — สถานะ",
                                "A) “เหนื่อยนิดหน่อย… แต่ดีขึ้นเพราะเธอ”",
                                "B) “ก็โอเคนะ เริ่มชินแล้ว”",
                                "C) “ไม่ค่อยดีเท่าไหร่”",
                                null, "Q5_A", "Q5_B", "Q5_C"));

                SCENES.put("Q5_A", new SCENE("คุณ", "Day6", "ความสัมพันธ์ +6", null, null, null, "S116", null, null,
                                null));
                SCENES.put("Q5_B", new SCENE("คุณ", "Day6", "ความสัมพันธ์ +2", null, null, null, "S106", null, null,
                                null));
                SCENES.put("Q5_C", new SCENE("คุณ", "Day6", "ความสัมพันธ์ +1", null, null, null, "S106", null, null,
                                null));
                SCENES.put("S116",
                                new SCENE("คุณ", "Day6", " อีกฝ่ายจะยิ้มชัดเจนครั้งแรก", null, null, null, "S106", null,
                                                null, null));

                SCENES.put("S106",
                                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day1", "“ถ้ามีอะไรไม่เข้าใจ… มาถามฉันได้”", null,
                                                null,
                                                null, "S107", null, null, null));
                SCENES.put("S107", new SCENE("ผู้บรรยาย", "Day1", "คุณกระพริบตา", null, null, null, "S108", null, null,
                                null));
                SCENES.put("S108",
                                new SCENE("คุณ", "Day1", "“นายใจดีจังนะ”", null, null, null, "S109", null, null, null));
                SCENES.put("S109",
                                new SCENE("ผู้บรรยาย", "Day1", "อีกฝ่ายหลุดยิ้มบาง ๆ", null, null, null, "S110", null,
                                                null, null));
                SCENES.put("S110",
                                new SCENE("ผู้บรรยาย", "Day1", "รอยยิ้มที่ทำให้บรรยากาศรอบตัวเปลี่ยนไปทันที", null,
                                                null,
                                                null, "S111", null, null, null));
                SCENES.put("S111",
                                new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day1", "“ไม่ได้ใจดี… แค่ไม่อยากเห็นเธอหลงทาง”",
                                                null,
                                                null, null, "S112", null, null, null));
                SCENES.put("S112",
                                new SCENE("ผู้บรรยาย", "Day1", "หัวใจคุณเต้นแรงขึ้นอีกครั้ง", null, null, null, "S113",
                                                null,
                                                null, null));
                SCENES.put("S113",
                                new SCENE("ผู้บรรยาย", "Day1", "และคุณเพิ่งรู้ตัวว่า—", null, null, null, "S114", null,
                                                null, null));
                SCENES.put("S114",
                                new SCENE("ผู้บรรยาย", "Day1", "ความรู้สึกโดดเดี่ยวที่มีมาตลอดทั้งวัน", null, null,
                                                null,
                                                "S115", null, null, null));
                SCENES.put("S115",
                                new SCENE("ผู้บรรยาย", "Day1", "มันหายไปตั้งแต่ตอนไหนก็ไม่รู้", null, null, null, "END",
                                                null, null, null));

                SCENES.put("END", new SCENE("ผู้บรรยาย", "Day1", "จบ Day 1", null, null, null, null, null, null, null));
        }

        private void SHOW_SCENE(String ID) {
                CURRENT_ID = ID;
                SCENE S = SCENES.get(ID);
                if (S == null)
                        return;

                int sceneNumber = -1;
                if (ID.startsWith("S")) {
                        try {
                                sceneNumber = Integer.parseInt(ID.substring(1));
                        } catch (Exception ignored) {
                        }
                }
                if (sceneNumber >= 1 && sceneNumber <= 7) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/บ้าน.png");
                } else if (sceneNumber >= 8 && sceneNumber <= 10) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/หน้าโรงเรียน.png");
                } else if (sceneNumber >= 11 && sceneNumber <= 13) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/บ้าน.png");
                } else if (sceneNumber >= 13 && sceneNumber <= 18) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องนอน.jpg");
                } else if (sceneNumber >= 19 && sceneNumber <= 20) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องนอน.jpg");
                } else if (sceneNumber >= 21 && sceneNumber <= 26) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/หน้าโรงเรียน.png");
                } else if (sceneNumber >= 27 && sceneNumber <= 29) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/หน้าโรงเรียน.png");
                } else if ((sceneNumber >= 30 && sceneNumber <= 63) || ID.startsWith("Q1")) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/มุมตึก.png");
                } else if ((sceneNumber >= 64 && sceneNumber <= 68) || ID.startsWith("Q2")) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องเรียน.jpg");
                } else if ((sceneNumber >= 69 && sceneNumber <= 77) || ID.startsWith("Q3")) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องเรียน.jpg");
                } else if ((sceneNumber >= 78 && sceneNumber <= 83) || ID.startsWith("Q4")) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/โรงอาหาร.jpg");
                } else if (sceneNumber >= 84 && sceneNumber <= 97) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/โรงเรียนตอนเย็น.jpg");
                } else if ((sceneNumber >= 98 && sceneNumber <= 105) || ID.startsWith("Q5")) {
                        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/โรงเรียนตอนเย็น.jpg");
                } else if (sceneNumber >= 106 && sceneNumber <= 116) {
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
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 5);
                        if (INDEX == 2)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
                        if (INDEX == 3)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
                }

                // Q2
                if (CURRENT_ID.equals("Q2")) {
                        if (INDEX == 1)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
                        if (INDEX == 2)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
                        if (INDEX == 3)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
                }

                // Q3
                if (CURRENT_ID.equals("Q3")) {
                        if (INDEX == 1)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 5);
                        if (INDEX == 2)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
                        if (INDEX == 3)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 0);
                }

                // Q4
                if (CURRENT_ID.equals("Q4")) {
                        if (INDEX == 1)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 3);
                        if (INDEX == 2)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
                        if (INDEX == 3)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
                }

                // Q5
                if (CURRENT_ID.equals("Q5")) {
                        if (INDEX == 1)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 6);
                        if (INDEX == 2)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
                        if (INDEX == 3)
                                affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
                }

                // รีเฟรชแถบ

                if (INDEX == 1 && S.NEXT1 != null)
                        SHOW_SCENE(S.NEXT1);
                if (INDEX == 2 && S.NEXT2 != null)
                        SHOW_SCENE(S.NEXT2);
                if (INDEX == 3 && S.NEXT3 != null)
                        SHOW_SCENE(S.NEXT3);
        }

        public static void main(String[] ARGS) {
                SwingUtilities.invokeLater(() -> new Day1().CREATEANDSHOWGUI());
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