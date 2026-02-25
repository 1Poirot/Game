package com.game.systems.dialogue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;

public class DialogueSystem {
    private static final Font THAI_FONT = new Font("Leelawadee UI", Font.PLAIN, 20);
    private static final Font NAME_FONT = new Font("Leelawadee UI", Font.BOLD, 20);
    private static final Font DAY_FONT = new Font("Leelawadee UI", Font.PLAIN, 16);

    private JFrame FRAME;
    private BGVIEW BG_VIEW;

    private Image CHAR_ORIG;
    private JLabel LABEL_CHARACTER;

    private DIALOGPANEL DIALOG;

    private Map<String, SCENE> SCENES = new HashMap<>();
    private String CURRENT_ID = "S1";

    public void CREATEANDSHOWGUI() {
        FRAME = new JFrame("Kim Jae-hyun Route");
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setResizable(true);

        BG_VIEW = new BGVIEW("/images/backgrounds/bg.jpg");
        BG_VIEW.setLayout(null);
        FRAME.setContentPane(BG_VIEW);

        if (LABEL_CHARACTER == null) {
            LABEL_CHARACTER = new JLabel();
            BG_VIEW.add(LABEL_CHARACTER);
        }
        if (CHAR_ORIG == null) {
            CHAR_ORIG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        DIALOG = new DIALOGPANEL("", "", "");
        BG_VIEW.add(DIALOG);

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

        if (CHAR_ORIG != null && LABEL_CHARACTER != null) {
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
        }

        BG_VIEW.setComponentZOrder(DIALOG, 0);
        BG_VIEW.setComponentZOrder(LABEL_CHARACTER, 1);

        BG_VIEW.revalidate();
        BG_VIEW.repaint();
    }

    private void ADD_SCENE(String ID, String NAME, String DAY, String TEXT, String NEXT) {
        SCENES.put(ID, new SCENE(NAME, DAY, TEXT, NEXT));
    }

    private void BUILD_STORY() {
        SCENES.clear();

        ADD_SCENE("S1", "ผู้บรรยาย", "Day 1", "Prologue – วันแรกใต้ท้องฟ้าใหม่", "S2");
        ADD_SCENE("S2", "ผู้บรรยาย", "Day 1", "เสียงล้อรถบรรทุกขนของค่อย ๆ เงียบลงหน้าบ้านหลังใหม่", "S3");
        ADD_SCENE("S3", "ผู้บรรยาย", "Day 1", "บ้านสองชั้นสีครีมในเมืองที่คุณไม่เคยรู้จักมาก่อน", "S4");
        ADD_SCENE("S4", "ผู้ปกครอง", "Day 1", "“ต่อจากนี้…ที่นี่คือบ้านของเรา”", "S5");
        ADD_SCENE("S5", "ผู้บรรยาย", "Day 1", "คำพูดของผู้ปกครองยังดังอยู่ในหัว", "S6");
        ADD_SCENE("S6", "ผู้บรรยาย", "Day 1", "ทุกอย่างเกิดขึ้นเร็วเกินไป—การย้ายงาน การเก็บของ การบอกลาเพื่อน", "S7");
        ADD_SCENE("S7", "ผู้บรรยาย", "Day 1", "คุณไม่มีแม้แต่เวลาจะตั้งตัว", "S8");

        ADD_SCENE("S8", "ผู้บรรยาย", "Day 1", "คุณยืนมองป้ายชื่อโรงเรียนที่พรุ่งนี้ต้องเข้าไปเรียน", "S9");
        ADD_SCENE("S9", "คุณ", "Day 1", "“โรงเรียนมัธยม เอเวอร์บลู (Everblue High School)”", "S10");
        ADD_SCENE("S10", "ผู้บรรยาย", "Day 1", "ชื่อดูสงบ…แต่หัวใจคุณกลับไม่สงบเลยสักนิด", "S11");
        ADD_SCENE("S11", "ผู้บรรยาย", "Day 1", "เมืองนี้เงียบกว่าที่คิด", "S12");
        ADD_SCENE("S12", "ผู้บรรยาย", "Day 1", "ผู้คนดูสุภาพ แต่ก็เหมือนมีระยะห่างบางอย่าง", "S13");
        ADD_SCENE("S13", "ผู้บรรยาย", "Day 1", "เหมือนทุกคนรู้จักกันหมดแล้ว ยกเว้น “คุณ”", "S14");

        ADD_SCENE("S14", "ผู้บรรยาย", "Day 1", "คืนนั้นคุณจัดของเข้าห้องใหม่", "S15");
        ADD_SCENE("S15", "ผู้บรรยาย", "Day 1", "โทรศัพท์เต็มไปด้วยข้อความจากเพื่อนเก่า", "S16");
        ADD_SCENE("S16", "ผู้บรรยาย", "Day 1", "บางคนบอกว่าจะคิดถึง", "S17");
        ADD_SCENE("S17", "ผู้บรรยาย", "Day 1", "บางคนบอกว่าอย่าลืมกัน", "S18");
        ADD_SCENE("S18", "ผู้บรรยาย", "Day 1", "คุณวางโทรศัพท์ลงช้า ๆ", "S19");
        ADD_SCENE("S19", "คุณ", "Day 1", "“เริ่มใหม่อีกครั้งสินะ…”", "S20");

        ADD_SCENE("S20", "ผู้บรรยาย", "Day 1", "เช้าวันเปิดเทอม", "S21");
        ADD_SCENE("S21", "ผู้บรรยาย", "Day 1", "ลมเช้าเย็นกว่าที่คาด", "S22");
        ADD_SCENE("S22", "ผู้บรรยาย", "Day 1", "คุณยืนอยู่หน้าประตูโรงเรียนในชุดนักเรียนใหม่เอี่ยม", "S23");
        ADD_SCENE("S23", "ผู้บรรยาย", "Day 1", "เสียงนักเรียนรอบตัวเต็มไปด้วยบทสนทนาและเสียงหัวเราะ", "S24");
        ADD_SCENE("S24", "ผู้บรรยาย", "Day 1", "แต่ไม่มีเสียงไหนเรียกชื่อคุณ", "S25");
        ADD_SCENE("S25", "ผู้บรรยาย", "Day 1", "คุณสูดหายใจลึก", "S26");
        ADD_SCENE("S26", "ผู้บรรยาย", "Day 1", "ก้าวเท้าเข้าไปในรั้วโรงเรียน", "S27");
        ADD_SCENE("S27", "ผู้บรรยาย", "Day 1", "และทันทีที่คุณเดินผ่านมุมตึกเรียน", "S28");
        ADD_SCENE("S28", "ผู้บรรยาย", "Day 1", "ปึก!", "S29");
        ADD_SCENE("S29", "ผู้บรรยาย", "Day 1", "คุณชนเข้ากับใครบางคนอย่างแรง", "S30");
        ADD_SCENE("S30", "ผู้บรรยาย", "Day 1", "หนังสือในมือเขา/เธอร่วงกระจายบนพื้น", "S31");
        ADD_SCENE("S31", "ผู้บรรยาย", "Day 1", "ดวงตาคู่หนึ่งเงยขึ้นมามองคุณ", "S32");
        ADD_SCENE("S32", "ผู้บรรยาย", "Day 1", "แววตานั้นนิ่ง เย็น…แต่มีบางอย่างซ่อนอยู่", "S33");
        ADD_SCENE("S33", "เขา/เธอ", "Day 1", "“นักเรียนใหม่…?”", "S34");
        ADD_SCENE("S34", "ผู้บรรยาย", "Day 1", "น้ำเสียงไม่ได้เย็นชา", "S35");
        ADD_SCENE("S35", "ผู้บรรยาย", "Day 1", "แต่ก็ไม่ได้เป็นมิตร", "S36");
        ADD_SCENE("S36", "ผู้บรรยาย", "Day 1", "วินาทีนั้นเอง", "S37");
        ADD_SCENE("S37", "ผู้บรรยาย", "Day 1", "คุณยังไม่รู้เลยว่า การชนกันเพียงครั้งเดียว", "S38");
        ADD_SCENE("S38", "ผู้บรรยาย", "Day 1", "จะเปลี่ยน “7 วันแรก” ของคุณไปตลอดกาล", "S39");

        ADD_SCENE("S39", "ผู้บรรยาย", "Day 1", "DAY 1 – คนแปลกหน้าใต้ต้นไม้ใหญ่", "S40");
        ADD_SCENE("S40", "ผู้บรรยาย", "Day 1", "หนังสือยังคงกระจายอยู่บนพื้น", "S41");
        ADD_SCENE("S41", "ผู้บรรยาย", "Day 1", "คุณรีบก้มลงเก็บทันที", "S42");
        ADD_SCENE("S42", "ผู้บรรยาย", "Day 1", "อีกฝ่ายก็ก้มลงพร้อมกันพอดี", "S43");
        ADD_SCENE("S43", "ผู้บรรยาย", "Day 1", "ปลายนิ้วของคุณแตะโดนมือเขา/เธอโดยบังเอิญ", "S44");
        ADD_SCENE("S44", "ผู้บรรยาย", "Day 1", "คุณชะงักไปเล็กน้อยก่อนจะดึงมือกลับ", "S45");
        ADD_SCENE("S45", "ผู้บรรยาย", "Day 1", "เขา/เธอเงยหน้าขึ้นมองคุณ", "S46");
        ADD_SCENE("S46", "ผู้บรรยาย", "Day 1", "สายตานิ่ง ๆ แต่ไม่ได้ดูน่ากลัว", "S47");
        ADD_SCENE("S47", "เขา/เธอ", "Day 1", "“ระวังหน่อย… ตรงนี้คนเดินผ่านเยอะ”", "S48");
        ADD_SCENE("S48", "ผู้บรรยาย", "Day 1", "น้ำเสียงเรียบ เหมือนพูดเตือนตามปกติ", "S49");

        ADD_SCENE("S49", "ผู้บรรยาย", "Day 1", "ตอนนั้นเองที่คุณนึกขึ้นได้", "S50");
        ADD_SCENE("S50", "ผู้บรรยาย", "Day 1", "คนตรงหน้าคือหนึ่งในนักเรียนที่คนอื่นพูดถึงบ่อยในโรงเรียนนี้", "S51");
        ADD_SCENE("S51", "ผู้บรรยาย", "Day 1", "เขา/เธอมองคุณอยู่ครู่หนึ่งก่อนจะลุกขึ้นยืน", "S52");
        ADD_SCENE("S52", "ผู้บรรยาย", "Day 1", "แล้วส่งหนังสือเล่มสุดท้ายคืนให้", "S53");
        ADD_SCENE("S53", "เขา/เธอ", "Day 1", "“นักเรียนใหม่สินะ… ห้อง 2-B ใช่ไหม?”", "S54");
        ADD_SCENE("S54", "ผู้บรรยาย", "Day 1", "คุณพยักหน้าอย่างแปลกใจ", "S55");
        ADD_SCENE("S55", "คุณ", "Day 1", "“รู้ได้ยังไง…?”", "S56");
        ADD_SCENE("S56", "คุณ", "Day 1", "ก่อนมองหน้าคุณอีกครั้ง", "S57");
        ADD_SCENE("S57", "คุณ", "Day 1", "คุณชะงัก", "S58");
        ADD_SCENE("S58", "คุณ", "Day 1", "“รู้ได้ยังไง…?”", "S59");
        ADD_SCENE("S59", "ผู้บรรยาย", "Day 1", "อีกฝ่ายมองคุณอยู่ครู่หนึ่ง", "S60");
        ADD_SCENE("S60", "ผู้บรรยาย", "Day 1", "เหมือนกำลังประเมินอะไรบางอย่าง", "S61");
        ADD_SCENE("S61", "เขา/เธอ", "Day 1", "ก่อนตอบสั้น ๆ", "S62");
        ADD_SCENE("S62", "เขา/เธอ", "Day 1", "“ดูออก”", "S63");
        ADD_SCENE("S63", "ผู้บรรยาย", "Day 1", "แล้วเขา/เธอก็เดินผ่านคุณไป", "S64");
        ADD_SCENE("S64", "ผู้บรรยาย", "Day 1", "ทิ้งไว้แค่ความรู้สึกแปลก ๆ ในอก", "S65");
        ADD_SCENE("S65", "ผู้บรรยาย", "Day 1", "คุณไม่รู้ว่าทำไม", "S66");
        ADD_SCENE("S66", "ผู้บรรยาย", "Day 1", "แต่ภาพของคนคนนั้นยังติดอยู่ในหัว", "S67");

        ADD_SCENE("S67", "ผู้บรรยาย", "Day 1", "ในห้องเรียน 2-B", "S68");
        ADD_SCENE("S68", "ผู้บรรยาย", "Day 1", "ครูประจำชั้นเรียกคุณขึ้นแนะนำตัว", "S69");
        ADD_SCENE("S69", "ผู้บรรยาย", "Day 1", "นักเรียนทั้งห้องมองมาที่คุณ", "S70");
        ADD_SCENE("S70", "ผู้บรรยาย", "Day 1", "บางคนยิ้ม บางคนกระซิบกัน", "S71");
        ADD_SCENE("S71", "ผู้บรรยาย", "Day 1", "หัวใจคุณเต้นแรงอีกครั้ง", "S72");

        ADD_SCENE("S72", "ผู้บรรยาย", "Day 1", "ห้องเรียน", "S73");
        ADD_SCENE("S73", "ผู้บรรยาย", "Day 1", "หลังแนะนำตัวหน้าห้องเสร็จ", "S74");
        ADD_SCENE("S74", "ผู้บรรยาย", "Day 1", "ครูให้คุณไปนั่งที่ว่างด้านหลัง", "S75");
        ADD_SCENE("S75", "ผู้บรรยาย", "Day 1", "และเมื่อคุณเดินไปถึงโต๊ะเรียน", "S76");
        ADD_SCENE("S76", "ผู้บรรยาย", "Day 1", "อีกฝ่ายก็นั่งอยู่โต๊ะข้าง ๆ", "S77");
        ADD_SCENE("S77", "ผู้บรรยาย", "Day 1", "คุณหยุดชะงักเล็กน้อย", "S78");
        ADD_SCENE("S78", "คุณ", "Day 1", "โลกมันกลมเกินไปหรือเปล่า…", "S79");
        ADD_SCENE("S79", "ผู้บรรยาย", "Day 1", "อีกฝ่ายเหลือบมองคุณนิดเดียว", "S80");
        ADD_SCENE("S80", "เขา/เธอ", "Day 1", "“บังเอิญอีกแล้ว”", "S81");
        ADD_SCENE("S81", "คุณ", "Day 1", "คุณหัวเราะ", "S82");

        ADD_SCENE("S82", "ผู้บรรยาย", "Day 1", "พักกลางวัน", "S83");
        ADD_SCENE("S83", "ผู้บรรยาย", "Day 1", "โรงอาหารเต็มไปด้วยนักเรียนที่มีเพื่อนอยู่แล้ว", "S84");
        ADD_SCENE("S84", "ผู้บรรยาย", "Day 1", "คุณยืนลังเลอยู่หน้าโรงอาหาร", "S85");
        ADD_SCENE("S85", "ผู้บรรยาย", "Day 1", "ทันใดนั้น คุณหันไป", "S86");
        ADD_SCENE("S86", "เขา/เธอ", "Day 1", "“ถ้ายังไม่มีที่นั่ง… ไปด้วยกันไหม”", "S87");
        ADD_SCENE("S87", "ผู้บรรยาย", "Day 1", "คุณแอบแปลกใจเล็กน้อย เพราะอีกฝ่ายดูเป็นคนไม่ค่อยชวนใคร", "S88");
        ADD_SCENE("S88", "ผู้บรรยาย", "Day 1", "อีกฝ่ายพยักหน้าเบา ๆ แล้วเดินนำไป", "S89");
        ADD_SCENE("S89", "ผู้บรรยาย", "Day 1", "ระหว่างกินข้าว", "S90");
        ADD_SCENE("S90", "ผู้บรรยาย", "Day 1", "บทสนทนาส่วนใหญ่เงียบ", "S91");
        ADD_SCENE("S91", "ผู้บรรยาย", "Day 1", "แต่เป็นความเงียบที่ไม่อึดอัด", "S92");
        ADD_SCENE("S92", "ผู้บรรยาย", "Day 1", "จนคุณเป็นฝ่ายถามก่อน", "S93");
        ADD_SCENE("S93", "คุณ", "Day 1", "“เธอ/นาย ชื่ออะไรเหรอ”", "S94");
        ADD_SCENE("S94", "ผู้บรรยาย", "Day 1", "อีกฝ่ายมองคุณครู่หนึ่ง", "S95");
        ADD_SCENE("S95", "ผู้บรรยาย", "Day 1", "เหมือนกำลังตัดสินใจว่าจะตอบไหม", "S96");
        ADD_SCENE("S96", "เขา/เธอ", "Day 1", "ก่อนพูดว่า", "S97");
        ADD_SCENE("S97", "เขา/เธอ", "Day 1", "“ชื่อตัวละคร”", "S98");
        ADD_SCENE("S98", "เขา/เธอ", "Day 1", "“แล้ว เธอ/ นายล่ะ”", "S99");
        ADD_SCENE("S99", "ผู้บรรยาย", "Day 1", "คุณบอกชื่อตัวเอง", "S100");
        ADD_SCENE("S100", "ผู้บรรยาย", "Day 1", "อีกฝ่ายพยักหน้าเล็กน้อย", "S101");
        ADD_SCENE("S101", "เขา/เธอ", "Day 1", "“ชื่อเข้ากับ เธอ/นาย ดี”", "S102");
        ADD_SCENE("S102", "ผู้บรรยาย", "Day 1", "คำพูดธรรมดา", "S103");
        ADD_SCENE("S103", "ผู้บรรยาย", "Day 1", "แต่หัวใจคุณกลับเต้นแรงอย่างประหลาด", "S104");

        ADD_SCENE("S104", "ผู้บรรยาย", "Day 1", "หลังเลิกเรียน", "S105");
        ADD_SCENE("S105", "ผู้บรรยาย", "Day 1", "คุณเก็บของเสร็จช้ากว่าคนอื่น", "S106");
        ADD_SCENE("S106", "ผู้บรรยาย", "Day 1", "เมื่อเดินออกมาหน้าโรงเรียน", "S107");
        ADD_SCENE("S107", "ผู้บรรยาย", "Day 1", "คุณเห็นอีกฝ่ายยืนอยู่ใต้ต้นไม้ใหญ่", "S108");
        ADD_SCENE("S108", "ผู้บรรยาย", "Day 1", "ลมเย็นพัดผ่าน", "S109");
        ADD_SCENE("S109", "ผู้บรรยาย", "Day 1", "ใบไม้ไหวเบา ๆ", "S110");
        ADD_SCENE("S110", "ผู้บรรยาย", "Day 1", "อีกฝ่ายหันมาเห็นคุณพอดี", "S111");
        ADD_SCENE("S111", "เขา/เธอ", "Day 1", "“วันแรกเป็นยังไงบ้าง”", "S112");
        ADD_SCENE("S112", "ผู้บรรยาย", "Day 1", "คุณคิดอยู่ครู่หนึ่งก่อนตอบ", "S113");
        ADD_SCENE("S113", "ผู้บรรยาย", "Day 1", "อีกฝ่ายจะยิ้มชัดเจนครั้งแรก", "S114");
        ADD_SCENE("S114", "เขา/เธอ", "Day 1", "“ถ้ามีอะไรไม่เข้าใจ… มาถามฉันได้”", "S115");
        ADD_SCENE("S115", "คุณ", "Day 1", "คุณกระพริบตา", "S116");
        ADD_SCENE("S116", "คุณ", "Day 1", "“เธอ/นายใจดีจังนะ”", "S117");
        ADD_SCENE("S117", "ผู้บรรยาย", "Day 1", "อีกฝ่ายหลุดยิ้มบาง ๆ", "S118");
        ADD_SCENE("S118", "ผู้บรรยาย", "Day 1", "รอยยิ้มที่ทำให้บรรยากาศรอบตัวเปลี่ยนไปทันที", "S119");
        ADD_SCENE("S119", "เขา/เธอ", "Day 1", "“ไม่ได้ใจดี… แค่ไม่อยากเห็นเธอ/นายหลงทาง”", "S120");
        ADD_SCENE("S120", "ผู้บรรยาย", "Day 1", "หัวใจคุณเต้นแรงขึ้นอีกครั้ง", "S121");
        ADD_SCENE("S121", "ผู้บรรยาย", "Day 1", "และคุณเพิ่งรู้ตัวว่า—", "S122");
        ADD_SCENE("S122", "ผู้บรรยาย", "Day 1", "ความรู้สึกโดดเดี่ยวที่มีมาตลอดทั้งวัน", "S123");
        ADD_SCENE("S123", "ผู้บรรยาย", "Day 1", "มันหายไปตั้งแต่ตอนไหนก็ไม่รู้", "S124");

        ADD_SCENE("S124", "ผู้บรรยาย", "Day 2", "DAY 2 — ระยะห่างที่เริ่มเปลี่ยน", "S125");
        ADD_SCENE("S125", "ผู้บรรยาย", "Day 2", "เช้าวันถัดมา", "S126");
        ADD_SCENE("S126", "ผู้บรรยาย", "Day 2", "เสียงนาฬิกาปลุกดังขึ้น", "S127");
        ADD_SCENE("S127", "ผู้บรรยาย", "Day 2", "คุณลืมตาช้า ๆ พร้อมความรู้สึกแปลกใหม่", "S128");
        ADD_SCENE("S128", "ผู้บรรยาย", "Day 2", "เมื่อวานยังเป็นวันแรกที่เต็มไปด้วยความกังวล", "S129");
        ADD_SCENE("S129", "ผู้บรรยาย", "Day 2", "แต่วันนี้…", "S130");
        ADD_SCENE("S130", "ผู้บรรยาย", "Day 2", "คุณกลับคิดถึงใครบางคนขึ้นมาเป็นคนแรก", "S131");
        ADD_SCENE("S131", "ผู้บรรยาย", "Day 2", "ภาพรอยยิ้มบาง ๆ ใต้ต้นไม้ใหญ่ยังติดอยู่ในหัว", "S132");

        ADD_SCENE("S132", "ผู้บรรยาย", "Day 2", "เช้า — หน้าโรงเรียน", "S133");
        ADD_SCENE("S133", "ผู้บรรยาย", "Day 2", "คุณมาถึง โรงเรียนมัธยม เอเวอร์บลู (Everblue High School)", "S134");
        ADD_SCENE("S134", "ผู้บรรยาย", "Day 2", "เร็วกว่าเดิมเล็กน้อย", "S135");
        ADD_SCENE("S135", "ผู้บรรยาย", "Day 2", "เมื่อคุณมาถึงรถหรูหลายคันจอดเรียงอยู่หน้าโรงเรียน", "S136");
        ADD_SCENE("S136", "ผู้บรรยาย", "Day 2", "นักเรียนบางคนลงจากรถพร้อมคนขับ", "S137");
        ADD_SCENE("S137", "ผู้บรรยาย", "Day 2", "ที่นี่คือ", "S138");
        ADD_SCENE("S138", "ผู้บรรยาย", "Day 2", "โรงเรียนเอกชนชื่อดังของเมือง", "S139");
        ADD_SCENE("S139", "ผู้บรรยาย", "Day 2", "และในกลุ่มนักเรียนเหล่านั้น—", "S140");
        ADD_SCENE("S140", "ผู้บรรยาย", "Day 2", "คุณเห็นอีกฝ่ายยืนอยู่", "S141");
        ADD_SCENE("S141", "ผู้บรรยาย", "Day 2", "แต่งตัวเรียบร้อย", "S142");
        ADD_SCENE("S142", "ผู้บรรยาย", "Day 2", "มีออร่าบางอย่างที่ทำให้คนรอบตัวดูจางลงไปทันที", "S143");
        ADD_SCENE("S143", "ผู้บรรยาย", "Day 2", "มีนักเรียนหลายคนเข้ามาทัก", "S144");
        ADD_SCENE("S144", "ผู้บรรยาย", "Day 2", "แต่เขา/เธอตอบเพียงสั้น ๆ", "S145");
        ADD_SCENE("S145", "ผู้บรรยาย", "Day 2", "พออีกฝ่ายเห็นคุณ", "S146");
        ADD_SCENE("S146", "ผู้บรรยาย", "Day 2", "สายตาก็เปลี่ยนไปเล็กน้อย", "S147");
        ADD_SCENE("S147", "คุณ", "Day 2", "“สวัสดีตอนเช้า”", "S148");
        ADD_SCENE("S148", "ผู้บรรยาย", "Day 2", "เหมือนเสียงนั้นอ่อนลงเฉพาะตอนคุยกับคุณ", "S149");
        ADD_SCENE("S149", "ผู้บรรยาย", "Day 2", "อีกฝ่ายตอบเรียบ ๆ พร้อมกับพยักหน้าเบาๆ", "S150");
        ADD_SCENE("S150", "เขา/เธอ", "Day 2", "“อืม”", "S151");
        ADD_SCENE("S151", "ผู้บรรยาย", "Day 2", "สำหรับการตอบคำถามเเรกและสอง", "S152");
        ADD_SCENE("S152", "เขา/เธอ", "Day 2", "“ก็ปกติ”", "S153");
        ADD_SCENE("S153", "ผู้บรรยาย", "Day 2", "แล้วมองคุณต่อ", "S154");
        ADD_SCENE("S154", "เขา/เธอ", "Day 2", "“เธอ/นายก็ทักฉันได้เหมือนกัน”", "S155");

        ADD_SCENE("S155", "ผู้บรรยาย", "Day 2", "คาบเรียนช่วงเช้า", "S156");
        ADD_SCENE("S156", "ผู้บรรยาย", "Day 2", "ระหว่างเรียน", "S157");
        ADD_SCENE("S157", "ผู้บรรยาย", "Day 2", "คุณเริ่มสังเกตหลายอย่าง", "S158");
        ADD_SCENE("S158", "ผู้บรรยาย", "Day 2", "รูปร่างหน้าทรงผม", "S159");
        ADD_SCENE("S159", "ผู้บรรยาย", "Day 2", "ปากกาที่อีกฝ่ายใช้", "S160");
        ADD_SCENE("S160", "ผู้บรรยาย", "Day 2", "หนังสือเรียนใหม่เอี่ยมทุกเล่ม", "S161");
        ADD_SCENE("S161", "ผู้บรรยาย", "Day 2", "นาฬิกาข้อมือเรียบ ๆ แต่ดูหรู", "S162");
        ADD_SCENE("S162", "ผู้บรรยาย", "Day 2", "และมีนักเรียนบางคนแอบมองเขา/เธออยู่ตลอด", "S163");
        ADD_SCENE("S163", "ผู้บรรยาย", "Day 2", "ครูประกาศงานกลุ่มกะทันหัน", "S164");
        ADD_SCENE("S164", "ครู", "Day 2", "“นักเรียนใหม่… เธออยู่กลุ่มเดียวกับ …(ชื่อตัวละคร) นะ”", "S165");
        ADD_SCENE("S165", "ผู้บรรยาย", "Day 2", "คุณชะงักเล็กน้อย", "S166");
        ADD_SCENE("S166", "ผู้บรรยาย", "Day 2", "นักเรียนบางคนหันมามองคุณทันที", "S167");
        ADD_SCENE("S167", "ผู้บรรยาย", "Day 2", "เหมือนคุณได้สิทธิพิเศษบางอย่าง", "S168");
        ADD_SCENE("S168", "ผู้บรรยาย", "Day 2", "ส่วนอีกฝ่ายดูนิ่งเหมือนเดิม", "S169");

        ADD_SCENE("S169", "ผู้บรรยาย", "Day 2", "หลังเลิกคาบ", "S170");
        ADD_SCENE("S170", "ผู้บรรยาย", "Day 2", "อีกฝ่ายหันมาหาคุณ", "S171");
        ADD_SCENE("S171", "เขา/เธอ", "Day 2", "“เลิกเรียนแล้วทำด้วยกันไหม จะได้เสร็จเร็ว”", "S172");
        ADD_SCENE("S172", "ผู้บรรยาย", "Day 2", "น้ำเสียงเหมือนเดิม", "S173");
        ADD_SCENE("S173", "ผู้บรรยาย", "Day 2", "แต่คุณรู้สึกว่าเขา/เธอตั้งใจชวน", "S174");

        ADD_SCENE("S174", "ผู้บรรยาย", "Day 2", "พักกลางวัน", "S175");
        ADD_SCENE("S175", "ผู้บรรยาย", "Day 2", "วันนี้คุณไม่ได้ลังเลเหมือนเมื่อวาน", "S176");
        ADD_SCENE("S176", "ผู้บรรยาย", "Day 2", "เพราะอีกฝ่ายเดินมาหาคุณก่อน", "S177");
        ADD_SCENE("S177", "เขา/เธอ", "Day 2", "“ไปกินข้าวไหม”", "S178");
        ADD_SCENE("S178", "ผู้บรรยาย", "Day 2", "เหมือนเป็นเรื่องปกติไปแล้ว", "S179");
        ADD_SCENE("S179", "ผู้บรรยาย", "Day 2", "ระหว่างกินข้าว", "S180");
        ADD_SCENE("S180", "เขา/เธอ", "Day 2", "“ย้ายมาอยู่ที่นี่… ลำบากไหม”", "S181");
        ADD_SCENE("S181", "ผู้บรรยาย", "Day 2", "คำถามเรียบ ๆ", "S182");
        ADD_SCENE("S182", "ผู้บรรยาย", "Day 2", "แต่แฝงความเป็นห่วง", "S183");
        ADD_SCENE("S183", "ผู้บรรยาย", "Day 2", "ถ้าเลือก C → อีกฝ่ายจะพูดเบา ๆ", "S184");
        ADD_SCENE("S184", "เขา/เธอ", "Day 2", "“ถ้าเหงา… มาหาฉันก็ได้”", "S185");
        ADD_SCENE("S185", "ผู้บรรยาย", "Day 2", "ความสนิทเพิ่มพิเศษ", "S186");

        ADD_SCENE("S186", "ผู้บรรยาย", "Day 2", "หลังเลิกเรียน — ทำงานกลุ่ม", "S187");
        ADD_SCENE("S187", "ผู้บรรยาย", "Day 2", "คุณสองคนนั่งทำงานในห้องเรียนที่เริ่มเงียบลง", "S188");
        ADD_SCENE("S188", "ผู้บรรยาย", "Day 2", "แสงเย็นส่องผ่านหน้าต่าง", "S189");
        ADD_SCENE("S189", "ผู้บรรยาย", "Day 2", "บรรยากาศสงบมาก", "S190");
        ADD_SCENE("S190", "ผู้บรรยาย", "Day 2", "ระหว่างที่คุณกำลังเขียน", "S191");
        ADD_SCENE("S191", "ผู้บรรยาย", "Day 2", "อีกฝ่ายพูดขึ้นมา", "S192");
        ADD_SCENE("S192", "เขา/เธอ", "Day 2", "“เธอ/นาย… ตั้งใจมากกว่าที่คิดนะ”", "S193");
        ADD_SCENE("S193", "ผู้บรรยาย", "Day 2", "คุณเงยหน้าขึ้น", "S194");
        ADD_SCENE("S194", "ผู้บรรยาย", "Day 2", "ระยะห่างใกล้กว่าที่คิด", "S195");
        ADD_SCENE("S195", "ผู้บรรยาย", "Day 2", "จนคุณได้ยินเสียงลมหายใจของอีกฝ่าย", "S196");
        ADD_SCENE("S196", "ผู้บรรยาย", "Day 2", "หัวใจคุณเต้นแรงขึ้นทันที", "S197");

        ADD_SCENE("S197", "ผู้บรรยาย", "Day 2", "🌧 เหตุการณ์พิเศษ — ฝนตก", "S198");
        ADD_SCENE("S198", "ผู้บรรยาย", "Day 2", "เมื่อทำงานเสร็จ", "S199");
        ADD_SCENE("S199", "ผู้บรรยาย", "Day 2", "คุณเดินออกจากอาคาร", "S200");
        ADD_SCENE("S200", "ผู้บรรยาย", "Day 2", "แต่ฝนตกหนักกะทันหัน", "S201");
        ADD_SCENE("S201", "ผู้บรรยาย", "Day 2", "คุณยืนอยู่ใต้ชายคา", "S202");
        ADD_SCENE("S202", "ผู้บรรยาย", "Day 2", "อีกฝ่ายเดินมาหยุดข้าง ๆ", "S203");
        ADD_SCENE("S203", "ผู้บรรยาย", "Day 2", "ก่อนกางร่มออก", "S204");
        ADD_SCENE("S204", "เขา/เธอ", "Day 2", "“ไปด้วยกันไหม”", "S205");
        ADD_SCENE("S205", "ผู้บรรยาย", "Day 2", "หัวใจคุณเต้นแรงอีกครั้ง", "S206");
        ADD_SCENE("S206", "ผู้บรรยาย", "Day 2", "ระยะใกล้มาก", "S207");
        ADD_SCENE("S207", "ผู้บรรยาย", "Day 2", "จนไหล่เกือบชนกัน", "S208");
        ADD_SCENE("S208", "ผู้บรรยาย", "Day 2", "อีกฝ่ายจะขยับร่มเข้าหาคุณมากขึ้น", "S209");
        ADD_SCENE("S209", "เขา/เธอ", "Day 2", "“เปียกเดี๋ยวไม่สบาย”", "S210");
        ADD_SCENE("S210", "ผู้บรรยาย", "Day 2", "น้ำเสียงอ่อนโยนกว่าทุกครั้งที่ผ่านมา", "S211");

        ADD_SCENE("S211", "เขา/เธอ", "Day 2", "“บางคน…", "S212");
        ADD_SCENE("S212", "เขา/เธอ", "Day 2", "ใช้เวลาเป็นปีถึงจะสนิทกัน", "S213");
        ADD_SCENE("S213", "เขา/เธอ", "Day 2", "แต่บางคน… แค่สองวันก็เริ่มสำคัญแล้ว”", null);
    }

    private void SHOW_SCENE(String ID) {
        CURRENT_ID = ID;
        SCENE S = SCENES.get(ID);
        if (S == null) return;

        DIALOG.SETDATA(S.NAME, S.DAY, S.TEXT);
        DIALOG.repaint();
    }

    private void GOTO_NEXT_BY_CLICK() {
        SCENE S = SCENES.get(CURRENT_ID);
        if (S == null) return;
        if (S.NEXT != null) SHOW_SCENE(S.NEXT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DialogueSystem().CREATEANDSHOWGUI());
    }

    static class SCENE {
        String NAME;
        String DAY;
        String TEXT;
        String NEXT;

        SCENE(String NAME, String DAY, String TEXT, String NEXT) {
            this.NAME = NAME;
            this.DAY = DAY;
            this.TEXT = TEXT;
            this.NEXT = NEXT;
        }
    }

    static class BGVIEW extends JPanel {
    private final Image ORIG;
    private Image SCALED;

    BGVIEW(String RESOURCE_PATH) {
        Image IMG = null;

        try {
            if (RESOURCE_PATH != null && RESOURCE_PATH.startsWith("/")) {
                URL U = DialogueSystem.class.getResource(RESOURCE_PATH);
                if (U != null) IMG = new ImageIcon(U).getImage();
            }
        } catch (Exception E) {
            IMG = null;
        }

        if (IMG == null) {
            try {
                IMG = new ImageIcon("src/main/resources" + RESOURCE_PATH).getImage();
            } catch (Exception E) {
                IMG = null;
            }
        }

        if (IMG == null) {
            ORIG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    "หาไฟล์รูปไม่เจอ: " + RESOURCE_PATH + "\n" +
                            "1) ต้องอยู่ใน resources: src/main/resources" + RESOURCE_PATH + "\n" +
                            "2) หรือเช็คชื่อไฟล์/โฟลเดอร์ให้ตรง",
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
            String SAFE_NAME = NAME == null ? "" : NAME;
            int TAG_W = FM_NAME.stringWidth(SAFE_NAME) + 60;
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
            G2.drawString(SAFE_NAME, NAME_X, NAME_Y);

            G2.setFont(DAY_FONT);
            G2.setColor(new Color(40, 40, 40));
            FontMetrics FM_DAY = G2.getFontMetrics();
            String SAFE_DAY = DAY == null ? "" : DAY;
            int DAY_X = W - 15 - FM_DAY.stringWidth(SAFE_DAY);
            int DAY_Y = 10 + 25;
            G2.drawString(SAFE_DAY, DAY_X, DAY_Y);

            G2.setFont(THAI_FONT);
            G2.setColor(new Color(30, 30, 30));
            FontMetrics FM_TEXT = G2.getFontMetrics();
            int TEXT_X = 25;
            int TEXT_Y = 10 + 75;

            String SAFE_TEXT = TEXT == null ? "" : TEXT;
            String[] LINES = WRAP(SAFE_TEXT, FM_TEXT, W - 50);
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
}