package com.game.systems.choice;

import com.game.systems.affection.AffectionManager;
import com.game.systems.affection.CharacterRoute;
import com.game.ui.AffectionBar;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;


public class Day2 {

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

        SCENES.put("S1", new SCENE("ผู้บรรยาย", "Day2", "DAY 2 — ระยะห่างที่เริ่มเปลี่ยน", null, null, null, "S2", null, null, null));
        SCENES.put("S2", new SCENE("ผู้บรรยาย", "Day2", "เช้าวันถัดมา", null, null, null, "S3", null, null, null));
        SCENES.put("S3", new SCENE("ผู้บรรยาย", "Day2", "เสียงนาฬิกาปลุกดังขึ้น", null, null, null, "S4", null, null, null));
        SCENES.put("S4", new SCENE("ผู้บรรยาย", "Day2", " คุณลืมตาช้า ๆ พร้อมความรู้สึกแปลกใหม่", null, null, null, "S5", null, null, null));
        SCENES.put("S5", new SCENE("ผู้บรรยาย", "Day2", "เมื่อวานยังเป็นวันแรกที่เต็มไปด้วยความกังวล", null, null, null, "S6", null, null, null));
        SCENES.put("S6", new SCENE("ผู้บรรยาย", "Day2", "แต่วันนี้…คุณกลับคิดถึงใครบางคนขึ้นมาเป็นคนแรก", null, null, null, "S7", null, null, null));
        SCENES.put("S7", new SCENE("ผู้บรรยาย", "Day2", "ภาพรอยยิ้มบาง ๆ ใต้ต้นไม้ใหญ่ยังติดอยู่ในหัว", null, null, null, "S8", null, null, null));
        SCENES.put("S8", new SCENE("ผู้บรรยาย", "Day2", "เช้า — หน้าโรงเรียน", null, null, null, "S9", null, null, null));
        SCENES.put("S9", new SCENE("ผู้บรรยาย", "Day2", "คุณมาถึง โรงเรียนมัธยม เอเวอร์บลู (Everblue High School)",	null,null,null,"S10" ,null,null,null));
        SCENES.put("S10", new SCENE("ผู้บรรยาย", "Day2", "เร็วกว่าปกติเล็กน้อย", null, null, null, "S11", null, null, null));
        SCENES.put("S11", new SCENE("ผู้บรรยาย", "Day2", "เมื่อคุณมาถึงรถหรูหลายคันจอดเรียงอยู่หน้าโรงเรียน", null, null, null, "S12", null, null, null));
        SCENES.put("S12", new SCENE("ผู้บรรยาย", "Day2", "นักเรียนบางคนลงจากรถพร้อมคนขับ", null, null, null, "S13", null, null, null));
        SCENES.put("S13", new SCENE("ผู้บรรยาย", "Day2", "ที่นี่คือ โรงเรียนเอกชนชื่อดังของเมือง", null, null, null, "S14", null, null, null));
        SCENES.put("S14", new SCENE("ผู้บรรยาย", "Day2", "และในกลุ่มนักเรียนเหล่านั้น—", null, null, null, "S15", null, null, null));
        SCENES.put("S15", new SCENE("ผู้บรรยาย", "Day2", "คุณเห็นอีกฝ่ายยืนอยู่ แต่งตัวเรียบร้อย", null, null, null, "S16", null, null, null));
        SCENES.put("S16", new SCENE("ผู้บรรยาย", "Day2", "มีออร่าบางอย่างที่ทำให้คนรอบตัวดูจางลงไปทันที", null, null, null, "S17", null, null, null));
        SCENES.put("S17", new SCENE("ผู้บรรยาย", "Day2", "มีนักเรียนหลายคนเข้ามาทัก",	null,null,null,"S18" ,null,null,null));
        SCENES.put("S18", new SCENE("ผู้บรรยาย", "Day2", "แต่เขาตอบเพียงสั้น ๆ", null, null, null, "S19", null, null, null));
        SCENES.put("S19", new SCENE("ผู้บรรยาย", "Day2", "พออีกฝ่ายเห็นคุณ สายตาก็เปลี่ยนไปเล็กน้อย",null,null,null,"S20" ,null,null,null));
        SCENES.put("S20", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“สวัสดีตอนเช้า”", null, null, null, "Q1", null, null, null));
        
        SCENES.put("Q1", new SCENE("คุณ", "Day2", "คำถามที่ 1 — คุณจะทักยังไง?",
                "A) “สวัสดีตอนเช้า… วันนี้มาพร้อมกันเลยนะ”",
                "B) “อืม สวัสดีตอนเช้า”",
                "C) “คนมาทักเธอ/นายเยอะจังเลย”",
                null, "Q1_A", "Q1_B", "Q1_C"));

        SCENES.put("Q1_A", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +3 ", null, null, null, "S21", null, null, null));
        SCENES.put("Q1_B", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +1 ", null, null, null, "S21", null, null, null));
        SCENES.put("Q1_C", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +2 ", null, null, null, "S23", null, null, null));
        
        SCENES.put("S21", new SCENE("ผู้บรรยาย", "Day2", "อีกฝ่ายตอบเรียบ ๆ พร้อมกับพยักหน้าเบาๆ", null, null, null, "S22", null, null, null));
        SCENES.put("S22", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2"	,"“อืม”"	,null,null,null,"S27" ,null,null,null));
        SCENES.put("S23", new SCENE("ผู้บรรยาย","Day2","อีกฝ่ายตอบเรียบ ๆ พร้อมกับพยักหน้าเบาๆ"	,null,null,null,"S24" ,null,null,null));
        SCENES.put("S24", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“ก็ปกติ”", null, null, null, "S25", null, null, null));
        SCENES.put("S25", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“เธอก็ทักฉันได้เหมือนกัน”",	null,null,null,"S26" ,null,null,null));
        SCENES.put("S26", new SCENE("ผู้บรรยาย","Day2" ,"สนใจพิเศษ สำหรับการตอบคำถามสาม +3",null,null,null,"S27" ,null,null,null));
        SCENES.put("S27", new SCENE("ผู้บรรยาย","Day2" ,"คาบเรียนช่วงเช้า",null,null,null,"S28" ,null,null,null));
        SCENES.put("S28", new SCENE("ผู้บรรยาย", "Day2"	,"ระหว่างเรียน คุณเริ่มสังเกตหลายอย่าง",null,null,null,"S29" ,null,null,null));
        SCENES.put("S29", new SCENE("ผู้บรรยาย", "Day2"	,"รูปร่างหน้าทรงผม ปากกาที่อีกฝ่ายใช้",null,null,null,"S30" ,null,null,null));
        SCENES.put("S30", new SCENE("ผู้บรรยาย", "Day2", "หนังสือเรียนใหม่เอี่ยมทุกเล่ม  นาฬิกาข้อมือเรียบ ๆ แต่ดูหรู", null, null, null, "S31", null, null, null));
        SCENES.put("S31", new SCENE("ผู้บรรยาย", "Day2", "และมีนักเรียนบางคนแอบมองเขาอยู่ตลอด", null, null, null, "S32", null, null, null));
        SCENES.put("S32", new SCENE("ผู้บรรยาย", "Day2", "ครูประกาศงานกลุ่มกะทันหัน", null, null, null, "S33", null, null, null));
        SCENES.put("S33", new SCENE("ครู", "Day2", "“นักเรียนใหม่… เธออยู่กลุ่มเดียวกับ คิมแจฮยอน นะ”", null, null, null, "S34", null, null, null));
        SCENES.put("S34", new SCENE("ผู้บรรยาย", "Day2", "คุณชะงักเล็กน้อย", null, null, null, "S35", null, null, null));
        SCENES.put("S35", new SCENE("ผู้บรรยาย", "Day2", "นักเรียนบางคนหันมามองคุณทันที", null, null, null, "S36", null, null, null));
        SCENES.put("S36", new SCENE("ผู้บรรยาย", "Day2", "เหมือนคุณได้สิทธิพิเศษบางอย่าง",null,null,null,"S37" ,null,null,null));
        SCENES.put("S37", new SCENE("ผู้บรรยาย","Day2","ส่วนอีกฝ่ายดูนิ่งเหมือนเดิม"	,null,null,null,"S38" ,null,null,null));
        SCENES.put("S38", new SCENE("ผู้บรรยาย","Day2","หลังเลิกคาบ" ,null,null,null,"S39" ,null,null,null));
        SCENES.put("S39", new SCENE("ผู้บรรยาย", "Day2", "อีกฝ่ายหันมาหาคุณ", null, null, null, "S40", null, null, null));
        SCENES.put("S40", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“เลิกเรียนแล้วทำด้วยกันไหม จะได้เสร็จเร็ว”", null, null, null, "S41", null, null, null));
        SCENES.put("S41", new SCENE("ผู้บรรยาย", "Day2", "น้ำเสียงเหมือนเดิม แต่คุณรู้สึกว่าเขาตั้งใจชวน", null, null, null, "Q2", null, null, null));
        
        SCENES.put("Q2", new SCENE("คุณ", "Day2", "คำถามที่ 2 — คุณตอบยังไง?",
                "A) “ได้เลย เราก็อยากทำกับนายอยู่แล้ว”",
                "B) “โอเค งั้นฝากด้วยนะ”",
                "C) “เดี๋ยวเราทำเองก็ได้”",
                null, "Q2_A", "Q2_B", "Q2_C"));

        SCENES.put("Q2_A", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +5 ", null, null, null, "S42", null, null, null));
        SCENES.put("Q2_B", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +2", null, null, null, "S43", null, null, null));
        SCENES.put("Q2_C", new SCENE("คุณ", "Day2", "ความสัมพันธ์ -2", null, null, null, "S43", null, null, null));
        
        SCENES.put("S42", new SCENE("ผู้บรรยาย", "Day2", "ทั้งสองคนทั้งใจทำงาน", null, null, null, "S43", null, null, null));
        SCENES.put("S43", new SCENE("ผู้บรรยาย", "Day2", "พักกลางวัน", null, null, null, "S44", null, null, null));
        SCENES.put("S44", new SCENE("ผู้บรรยาย", "Day2", "วันนี้คุณไม่ได้ลังเลเหมือนเมื่อวาน", null, null, null, "S45", null, null, null));
        SCENES.put("S45", new SCENE("ผู้บรรยาย", "Day2", "เพราะอีกฝ่ายเดินมาหาคุณก่อน", null, null, null, "S46", null, null, null));
        SCENES.put("S46", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“ไปกินข้าวไหม”",null,null,null,"S47" ,null,null,null));
        SCENES.put("S47", new SCENE("ผู้บรรยาย","Day2","เหมือนเป็นเรื่องปกติไปแล้ว"	,null,null,null,"S48" ,null,null,null));
        SCENES.put("S48", new SCENE("ผู้บรรยาย","Day2","ระหว่างกินข้าว อีกฝ่ายถามคุณขึ้นมา"	,null,null,null,"S49" ,null,null,null));
        SCENES.put("S49", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“ย้ายมาอยู่ที่นี่… ลำบากไหม”", null, null, null, "S50", null, null, null));
        SCENES.put("S50", new SCENE("ผู้บรรยาย", "Day2", "คำถามเรียบ ๆ", null, null, null, "S51", null, null, null));
        SCENES.put("S51", new SCENE("ผู้บรรยาย", "Day2", "แต่แฝงความเป็นห่วง", null, null, null, "S52", null, null, null));
        SCENES.put("S52", new SCENE("ผู้บรรยาย", "Day2", "คุณพยักหน้าอย่างแปลกใจ", null, null, null, "Q3", null, null, null));
        
       SCENES.put("Q3", new SCENE("คุณ", "Day2", "คำถามที่ 3 — คุณตอบยังไง?",
                "A) “ตอนแรกก็ลำบาก… แต่ตอนนี้ดีขึ้นแล้ว”",
                "B) “ก็เฉย ๆ นะ เราปรับตัวเก่ง”",
                "C) “ยังไม่ชินเลย… เหงานิดหน่อย”",
                null, "Q3_A", "Q3_B", "Q3_C"));
        
        SCENES.put("Q3_A", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +3", null, null, null, "S54", null, null, null));
        SCENES.put("Q3_B", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +1", null, null, null, "S54", null, null, null));
        SCENES.put("Q3_C", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +4 (อีกฝ่ายจะอ่อนโยนขึ้น)", null, null, null, "S53", null, null, null)); 
        
        SCENES.put("S53", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“ถ้าเหงา… มาหาฉันก็ได้”", null, null, null, "S54", null, null, null));
        SCENES.put("S54", new SCENE("ผู้บรรยาย", "Day2", "หลังเลิกเรียน — ทำงานกลุ่ม", null, null, null, "S55", null, null, null));
        SCENES.put("S55", new SCENE("ผู้บรรยาย", "Day2", "คุณสองคนนั่งทำงานในห้องเรียนที่เริ่มเงียบลง", null, null, null, "S56", null, null, null));
        SCENES.put("S56", new SCENE("ผู้บรรยาย", "Day2", "แสงเย็นส่องผ่านหน้าต่าง",null,null,null,"S57" ,null,null,null));
        SCENES.put("S57", new SCENE("ผู้บรรยาย", "Day2", "บรรยากาศสงบมาก", null, null, null, "S58", null, null, null));
        SCENES.put("S58", new SCENE("ผู้บรรยาย", "Day2", "ระหว่างที่คุณกำลังเขียน", null, null, null, "S59", null, null, null));
        SCENES.put("S59", new SCENE("ผู้บรรยาย", "Day2", "อีกฝ่ายพูดขึ้นมา", null, null, null, "S60", null, null, null));
        SCENES.put("S60", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“เธอ… ตั้งใจมากกว่าที่คิดนะ”", null, null, null, "S61", null, null, null));
        SCENES.put("S61", new SCENE("ผู้บรรยาย", "Day2", "คุณเงยหน้าขึ้น ระยะห่างใกล้กว่าที่คิด", null, null, null, "S62", null, null, null));
        SCENES.put("S62", new SCENE("ผู้บรรยาย", "Day2", "จนคุณได้ยินเสียงลมหายใจของอีกฝ่าย", null, null, null, "S63", null, null, null));
        SCENES.put("S63", new SCENE("ผู้บรรยาย", "Day2", "หัวใจคุณเต้นแรงขึ้นทันที", null, null, null, "Q4", null, null, null));
        
        SCENES.put("Q4", new SCENE("คุณ", "Day2", "คำถามที่ 4 — คุณตอบยังไง?",
                "A) “ก็… อยากทำให้ดีต่อหน้านาย”",
                "B) “เราก็เป็นแบบนี้แหละ”",
                "C) “ชมเกินไปแล้ว”",
                null, "Q4_A", "Q4_B", "Q4_C"));
        
        SCENES.put("Q4_A", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +6 ", null, null, null, "S64", null, null, null));
        SCENES.put("Q4_B", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +2", null, null, null, "S65", null, null, null));
        SCENES.put("Q4_C", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +1", null, null, null, "S65", null, null, null));
        
        SCENES.put("S64", new SCENE("ผู้บรรยาย", "Day2", "อีกฝ่ายจะยิ้มชัดขึ้น",null,null,null,"S65" ,null,null,null));
        SCENES.put("S65", new SCENE("ผู้บรรยาย", "Day2", "เหตุการณ์พิเศษ — ฝนตก" ,null,null,null,"S67" ,null,null,null));
        SCENES.put("S67", new SCENE("ผู้บรรยาย", "Day2", "วันนี้คุณกับเขาอยู่ทำงานกลุ่มต่อจนเย็น", null, null, null, "S68", null, null, null));
        SCENES.put("S68", new SCENE("ผู้บรรยาย", "Day2", "เมื่อทำงานเสร็จ นักเรียนส่วนใหญ่กลับบ้านไปแล้ว", null, null, null, "S69", null, null, null));
        SCENES.put("S69", new SCENE("ผู้บรรยาย", "Day2", "ทางเดินในอาคารเงียบลงมาก", null, null, null, "S70", null, null, null));
        SCENES.put("S70", new SCENE("ผู้บรรยาย", "Day2", "แสงแดดช่วงเย็นส่องผ่านหน้าต่างยาวของทางเดิน", null, null, null, "S71", null, null, null));
        SCENES.put("S71", new SCENE("ผู้บรรยาย", "Day2", "เกิดเงาสีส้มอ่อนบนพื้นกระเบื้อง", null, null, null, "S72", null, null, null));
        SCENES.put("S72", new SCENE("ผู้บรรยาย", "Day2", "คุณเก็บของใส่กระเป๋า", null, null, null, "S73", null, null, null));
        SCENES.put("S73", new SCENE("ผู้บรรยาย", "Day2", "คุณหยุดชะงักเล็กน้อย", null, null, null, "S74", null, null, null));
        SCENES.put("S74", new SCENE("ผู้บรรยาย", "Day2", "โลกมันกลมเกินไปหรือเปล่า…", null, null, null, "S75", null, null, null));
        SCENES.put("S75", new SCENE("ผู้บรรยาย", "Day2", "เขาก็ยืนรออยู่ข้าง ๆ เหมือนเป็นเรื่องปกติไปแล้ว", null, null, null, "S76", null, null, null));
        SCENES.put("S76", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“เสร็จแล้ว?”",null,null,null,"S77" ,null,null,null));
        SCENES.put("S77", new SCENE("คุณ", "Day2", "“อืม เสร็จแล้ว”", null, null, null, "S78", null, null, null));
        SCENES.put("S78", new SCENE("ผู้บรรยาย", "Day2", "คุณตอบพร้อมรอยยิ้มเล็ก ๆ", null, null, null, "S79", null, null, null));
        SCENES.put("S79", new SCENE("ผู้บรรยาย", "Day2", "ทั้งสองคนเดินออกจากอาคารไปพร้อมกัน", null, null, null, "S80", null, null, null));
        SCENES.put("S80", new SCENE("ผู้บรรยาย", "Day2", "คุณเดินออกจากอาคาร", null, null, null, "S81", null, null, null));
        SCENES.put("S81", new SCENE("ผู้บรรยาย", "Day2", "แต่ฝนตกหนักกะทันหัน", null, null, null, "S82", null, null, null));
        SCENES.put("S82", new SCENE("ผู้บรรยาย", "Day2", "คุณยืนอยู่ใต้ชายคา อีกฝ่ายเดินมาหยุดข้าง ๆ", null, null, null, "S83", null, null, null));
        SCENES.put("S83", new SCENE("ผู้บรรยาย", "Day2", "ก่อนกางร่มออก", null, null, null, "S84", null, null, null));
        SCENES.put("S84", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“ไปด้วยกันไหม”", null, null, null, "S85", null, null, null));
        SCENES.put("S85", new SCENE("ผู้บรรยาย", "Day2", "ใต้ร่มเดียวกัน", null, null, null, "S86", null, null, null));
        SCENES.put("S86", new SCENE("ผู้บรรยาย", "Day2", "คุณก้าวเข้าไปใต้ร่ม", null, null, null, "S87", null, null, null));
        SCENES.put("S87", new SCENE("ผู้บรรยาย", "Day2", "ระยะห่างใกล้มาก", null, null, null, "S88", null, null, null));
        SCENES.put("S88", new SCENE("ผู้บรรยาย", "Day2", "ไหล่แทบชนกัน",null,null,null,"S89" ,null,null,null));
        SCENES.put("S89", new SCENE("ผู้บรรยาย", "Day2", "คุณรับรู้ถึงความอบอุ่นจากตัวอีกฝ่าย", null, null, null, "S90", null, null, null));
        SCENES.put("S90", new SCENE("ผู้บรรยาย", "Day2", "เสียงฝนตกกระทบร่มดังสม่ำเสมอ", null, null, null, "S91", null, null, null));
        SCENES.put("S91", new SCENE("ผู้บรรยาย", "Day2", "โลกภายนอกดูพร่าเลือนไปหมด",	null,null,null,"S92" ,null,null,null));
        SCENES.put("S92", new SCENE("ผู้บรรยาย", "Day2"	,"เหมือนมีเพียงพื้นที่เล็ก ๆ ใต้ร่มคันนี้ที่เป็นของคุณสองคน",null,null,null,"S93" ,null,null,null));
        SCENES.put("S93", new SCENE("ผู้บรรยาย", "Day2", "เขาขยับร่มเข้ามาหาคุณมากขึ้น", null, null, null, "S94", null, null, null));
        SCENES.put("S94", new SCENE("ผู้บรรยาย", "Day2", "จนคุณแทบไม่โดนฝนเลย", null, null, null, "S95", null, null, null));
        SCENES.put("S95", new SCENE("ผู้บรรยาย", "Day2", "แต่ไหล่ของเขากลับเริ่มเปียกแทน", null, null, null, "S96", null, null, null));
        SCENES.put("S96", new SCENE("ผู้บรรยาย", "Day2", "คุณสังเกตเห็นทันที", null, null, null, "S97", null, null, null));
        SCENES.put("S97", new SCENE("ผู้บรรยาย", "Day2", "หัวใจอุ่นขึ้นอย่างประหลาด", null, null, null, "Q5", null, null, null));
        
         SCENES.put("Q5", new SCENE("คุณ", "Day2", "คำถามที่ 5 — ใต้ร่มเดียวกัน",
                "A) “ขอบคุณนะ… ดีจังที่มีนายอยู่”",
                "B) “ฝนตกแรงเนอะ”",
                "C) “เรากลับเองก็ได้”",
                null, "Q5_A", "Q5_B", "Q5_C"));

        SCENES.put("Q5_A", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +6", null, null, null, "S98", null, null, null));
        SCENES.put("Q5_B", new SCENE("คุณ", "Day2", "ความสัมพันธ์ +2", null, null, null, "S123", null, null, null));
        SCENES.put("Q5_C", new SCENE("คุณ", "Day2", "ความสัมพันธ์ -3", null, null, null, "S123", null, null, null));

        SCENES.put("S98", new SCENE("ผู้บรรยาย", "Day2", "คุณพูดออกไปเบา ๆ", null, null, null, "S99", null, null, null));
        SCENES.put("S99", new SCENE("คุณ", "Day2", "“ขอบคุณนะ… ดีจังที่มีเธออยู่”", null, null, null, "S100", null, null, null));
        SCENES.put("S100", new SCENE("ผู้บรรยาย", "Day2", "อีกฝ่ายชะงัก",null,null,null,"S101" ,null,null,null));
        SCENES.put("S101", new SCENE("ผู้บรรยาย","Day2","สายตาที่มองคุณนิ่งไปครู่หนึ่ง"	,null,null,null,"S102" ,null,null,null));
        SCENES.put("S102", new SCENE("ผู้บรรยาย","Day2","ก่อนเขาจะขยับร่มเข้ามาใกล้คุณมากขึ้นอีก"	,null,null,null,"S103" ,null,null,null));
        SCENES.put("S103", new SCENE("ผู้บรรยาย", "Day2", "จนไหล่ชนกันเต็ม ๆ", null, null, null, "S104", null, null, null));
        SCENES.put("S104", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“เปียกเดี๋ยวไม่สบาย”", null, null, null, "S105", null, null, null));
        SCENES.put("S105", new SCENE("ผู้บรรยาย", "Day2", "เสียงเบา…ใกล้มาก", null, null, null, "S106", null, null, null));
        SCENES.put("S106", new SCENE("ผู้บรรยาย", "Day2", "หัวใจคุณเต้นแรงจนควบคุมไม่ได้", null, null, null, "S107", null, null, null));
        SCENES.put("S107", new SCENE("ผู้บรรยาย", "Day2", "ฝนยังคงตกหนัก", null, null, null, "S108", null, null, null));
        SCENES.put("S108", new SCENE("ผู้บรรยาย", "Day2", "เสียงรอบตัวเหมือนหายไป", null, null, null, "S109", null, null, null));
        SCENES.put("S109", new SCENE("ผู้บรรยาย", "Day2", "เหลือเพียงเสียงลมหายใจของกันและกัน", null, null, null, "S110", null, null, null));
        SCENES.put("S110", new SCENE("ผู้บรรยาย", "Day2", "มือของคุณแกว่งไปตามจังหวะเดิน", null, null, null, "S111", null, null, null));
        SCENES.put("S111", new SCENE("ผู้บรรยาย", "Day2", "แล้ว—ปลายนิ้วของอีกฝ่ายแตะมือคุณ", null, null, null, "S112", null, null, null));
        SCENES.put("S112", new SCENE("ผู้บรรยาย", "Day2", "คุณชะงักแต่ครั้งนี้…เขาไม่ได้ขยับหนี", null, null, null, "S113", null, null, null));
        SCENES.put("S113", new SCENE("ผู้บรรยาย", "Day2", "นิ้วเกี่ยวกันเบา ๆเหมือนกำลังลังเล",	null,null,null,"S114" ,null,null,null));
        SCENES.put("S114", new SCENE("ผู้บรรยาย", "Day2", "สุดท้าย—เขาจับมือคุณเบา ๆ"	,null,null,null,"S115" ,null,null,null));
        SCENES.put("S115", new SCENE("ผู้บรรยาย", "Day2", "ไม่แน่นแต่ชัดเจนว่า “ตั้งใจ”", null, null, null, "S116", null, null, null));
        SCENES.put("S116", new SCENE("ผู้บรรยาย", "Day2", "หัวใจคุณแทบหยุดเต้นคุณเงยหน้ามอง", null, null, null, "S117", null, null, null));
        SCENES.put("S117", new SCENE("ผู้บรรยาย", "Day2", "สายตาสบกันใกล้มาก", null, null, null, "S118", null, null, null));
        SCENES.put("S118", new SCENE("คุณ", "Day2", "“……”", null, null, null, "S119", null, null, null));
        SCENES.put("S119", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“อย่าหายไปไหนนะ”", null, null, null, "S120", null, null, null));
        SCENES.put("S120", new SCENE("ผู้บรรยาย", "Day2", "เสียงเบาจนแทบไม่ได้ยิน", null, null, null, "S121", null, null, null));
        SCENES.put("S121", new SCENE("ผู้บรรยาย", "Day2", "แต่คุณได้ยินชัดเจน", null, null, null, "S122", null, null, null));
        SCENES.put("S122", new SCENE("ผู้บรรยาย", "Day2", "ประโยคนั้นทำให้หัวใจคุณอบอุ่นทันที",	null,null,null,"S123" ,null,null,null));
        SCENES.put("S123", new SCENE("ผู้บรรยาย", "Day2", "ตอนจบฉาก"	,null,null,null,"S124" ,null,null,null));
        SCENES.put("S124", new SCENE("ผู้บรรยาย", "Day2", "อีกฝ่ายปล่อยมือช้า ๆ", null, null, null, "S125", null, null, null));
        SCENES.put("S125", new SCENE("ผู้บรรยาย", "Day2", "ก่อนพูดเบา ๆ", null, null, null, "S126", null, null, null));
        SCENES.put("S126", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“พรุ่งนี้…เจอกัน”", null, null, null, "S127", null, null, null));
        SCENES.put("S127", new SCENE("ผู้บรรยาย", "Day2", "คุณพยักหน้า", null, null, null, "S128", null, null, null));
        SCENES.put("S128", new SCENE("คุณ", "Day2", "“อืม”", null, null, null, "S129", null, null, null));
        SCENES.put("S129", new SCENE("ผู้บรรยาย", "Day2", "เขามองคุณอีกครั้งสายตานุ่มกว่าที่เคย", null, null, null, "S130", null, null, null));
        SCENES.put("S130", new SCENE("คิมแจฮยอน (Kim Jaehyun)", "Day2", "“ดี”", null, null, null, "S131", null, null, null));
        SCENES.put("S131", new SCENE("ผู้บรรยาย", "Day2", "เพียงคำเดียว", null, null, null, "S132", null, null, null));
        SCENES.put("S132", new SCENE("ผู้บรรยาย", "Day2", "แต่ทำให้คุณยิ้มออกมาโดยไม่รู้ตัว", null, null, null, "S133", null, null, null));
        SCENES.put("S133", new SCENE("ผู้บรรยาย", "Day2", "“บางคน… ใช้เวลาเป็นปีถึงจะสนิทกันแต่บางคน… แค่สองวันก็เริ่มสำคัญแล้ว”", null, null, null, "END", null, null, null));


        SCENES.put("END", new SCENE("ผู้บรรยาย", "Day2", "จบ Day 2", null, null, null, null, null, null, null));
    }

    private void SHOW_SCENE(String ID) {
    CURRENT_ID = ID;
    SCENE S = SCENES.get(ID);
    if (S == null) return;

    int sceneNumber = -1;
    if (ID.startsWith("S")) {
        try {
            sceneNumber = Integer.parseInt(ID.substring(1));
        } catch (Exception ignored) {}
    }
    if (sceneNumber >= 1 && sceneNumber <= 7) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องนอน.jpg");
    }
    else if ((sceneNumber >= 8 && sceneNumber <= 26) || ID.startsWith("Q1")) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/หน้าโรงเรียน.png");
    }
    else if ((sceneNumber >= 27 && sceneNumber <= 41) || ID.startsWith("Q2")){
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องเรียน.jpg");
    }
    else if (sceneNumber == 42) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ฉากพิเศษตอนทำงานห้องสมุด.png");
    }
    else if ((sceneNumber >= 43 && sceneNumber <= 53) || ID.startsWith("Q3")) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/โรงอาหาร.jpg");
    }
    else if ((sceneNumber >= 54 && sceneNumber <= 79) || ID.startsWith("Q4")){
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ห้องเรียนตอนเย็น.png");
    }
    else if (sceneNumber >= 80 && sceneNumber <= 89) {
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/โรงเรียนฝนตก.png");
    }
    else if ((sceneNumber >= 90 && sceneNumber <= 133) || ID.startsWith("Q5")){
        BG_VIEW.SET_BG("src/main/resources/images/backgrounds/ฝนตก.png");
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
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 3);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
    }

    // Q2
    if (CURRENT_ID.equals("Q2")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 5);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, -2);
    }

    // Q3
    if (CURRENT_ID.equals("Q3")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 3);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 1);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 4);
    }

    // Q4
    if (CURRENT_ID.equals("Q4")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 6);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, -1);
    }

    // Q5
    if (CURRENT_ID.equals("Q5")) {
        if (INDEX == 1) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 6);
        if (INDEX == 2) affection.addAffection(CharacterRoute.KIM_JAEHYUN, 2);
        if (INDEX == 3) affection.addAffection(CharacterRoute.KIM_JAEHYUN, -5);
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
        SwingUtilities.invokeLater(() -> new Day2().CREATEANDSHOWGUI());
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