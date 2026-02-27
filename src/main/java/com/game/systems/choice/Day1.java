package com.game.systems.choice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Day1 {

    private static final Font THAI_FONT = new Font("Leelawadee UI", Font.PLAIN, 20);
    private static final Font BTN_FONT  = new Font("Leelawadee UI", Font.PLAIN, 18);

    private JFrame FRAME;
    private BGVIEW BG_VIEW;
    private DIALOGPANEL DIALOG;

    private JPanel CHOICE_PANEL;
    private JButton BTN1, BTN2, BTN3;

    private Map<String, SCENE> SCENES = new HashMap<>();
    private String CURRENT_ID = "S1";

    public void CREATEANDSHOWGUI() {

        FRAME = new JFrame("Everblue Route");
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setResizable(true);

        BG_VIEW = new BGVIEW("src/main/resources/images/บ้าน.png");
        BG_VIEW.setLayout(null);
        FRAME.setContentPane(BG_VIEW);

        // ===== Dialog =====
        DIALOG = new DIALOGPANEL();
        BG_VIEW.add(DIALOG);

        // ===== Choice Panel =====
        CHOICE_PANEL = new JPanel(null);
        CHOICE_PANEL.setOpaque(false);
        BG_VIEW.add(CHOICE_PANEL);

        // ให้ปุ่มอยู่บนสุดเสมอ
        BG_VIEW.setComponentZOrder(CHOICE_PANEL, 0);

        BTN1 = new JButton();
        BTN2 = new JButton();
        BTN3 = new JButton();

        STYLE_BTN(BTN1);
        STYLE_BTN(BTN2);
        STYLE_BTN(BTN3);

        CHOICE_PANEL.add(BTN1);
        CHOICE_PANEL.add(BTN2);
        CHOICE_PANEL.add(BTN3);

        BTN1.addActionListener(e -> PICK(1));
        BTN2.addActionListener(e -> PICK(2));
        BTN3.addActionListener(e -> PICK(3));

        DIALOG.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NEXT();
            }
        });

        FRAME.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                LAYOUT_UI();
            }
        });

        BUILD_STORY();

        FRAME.setSize(1024, 600);
        FRAME.setLocationRelativeTo(null);
        FRAME.setVisible(true);

        SHOW("S1");
        LAYOUT_UI();
    }

    // ================= STORY (ครบของคุณ) =================
    private void BUILD_STORY() {

        SCENES.clear();

        SCENES.put("S1", new SCENE("Narrator","Day 1",
                "ฉากที่ 1 – Prologue – วันแรกใต้ท้องฟ้าใหม่",
                null,null,null,"S2",null,null,null));

        SCENES.put("S2", new SCENE("Narrator","Day 1",
                "เสียงล้อรถบรรทุกขนของค่อย ๆ เงียบลงหน้าบ้านหลังใหม่",
                null,null,null,"S3",null,null,null));

        SCENES.put("S3", new SCENE("Narrator","Day 1",
                "บ้านสองชั้นสีครีมในเมืองที่คุณไม่เคยรู้จักมาก่อน",
                null,null,null,"S4",null,null,null));

        SCENES.put("S4", new SCENE("ผู้ปกครอง","Day 1",
                "“ต่อจากนี้…ที่นี่คือบ้านของเรา”",
                null,null,null,"S5",null,null,null));

        SCENES.put("S5", new SCENE("Narrator","Day 1",
                "ทุกอย่างเกิดขึ้นเร็วเกินไป—การย้ายงาน การเก็บของ การบอกลาเพื่อน",
                null,null,null,"S6",null,null,null));

        SCENES.put("S6", new SCENE("Narrator","Day 1",
                "คุณไม่มีแม้แต่เวลาจะตั้งตัว",
                null,null,null,"S7",null,null,null));

        SCENES.put("S7", new SCENE("Narrator","Day 1",
                "คุณยืนมองป้ายชื่อโรงเรียน “Everblue High School”",
                null,null,null,"S8",null,null,null));

        SCENES.put("S8", new SCENE("Narrator","Day 1",
                "ชื่อดูสงบ…แต่หัวใจคุณกลับไม่สงบเลย",
                null,null,null,"S9",null,null,null));

        SCENES.put("S9", new SCENE("Narrator","Day 1",
                "เช้าวันเปิดเทอม คุณก้าวเข้าสู่โรงเรียนใหม่",
                null,null,null,"S10",null,null,null));

        SCENES.put("S10", new SCENE("Narrator","Day 1",
                "ปึก! คุณชนเข้ากับใครบางคน หนังสือกระจายเต็มพื้น",
                null,null,null,"S11",null,null,null));

        SCENES.put("S11", new SCENE("ปริศนา","Day 1",
                "“นักเรียนใหม่…?”",
                null,null,null,"S12",null,null,null));

        SCENES.put("S12", new SCENE("Narrator","Day 1",
                "สายตานิ่ง ๆ แต่ไม่ได้ดูน่ากลัว",
                null,null,null,"S13",null,null,null));

        SCENES.put("S13", new SCENE("ปริศนา","Day 1",
                "“ระวังหน่อย… ตรงนี้คนเดินผ่านเยอะ”",
                null,null,null,"Q1",null,null,null));

        // ===== Q1 =====
        SCENES.put("Q1", new SCENE("คุณ","Day 1",
                "🎯 ตัวเลือกที่ 1 – คำพูดแรก",
                "A) ขอโทษนะ เรารีบไปหน่อย…",
                "B) ก็คุณเดินไม่ดูทางเหมือนกันนะ",
                "C) เอ่อ… ขอบคุณที่ช่วยเก็บนะ",
                null,"Q1A","Q1B","Q1C"));

        SCENES.put("Q1A", new SCENE("คุณ","Day 1",
                "“ขอโทษนะ…” (+5)",
                null,null,null,"S14",null,null,null));

        SCENES.put("Q1B", new SCENE("คุณ","Day 1",
                "“ก็คุณเดินไม่ดูทางเหมือนกันนะ” (+2)",
                null,null,null,"S14",null,null,null));

        SCENES.put("Q1C", new SCENE("คุณ","Day 1",
                "“เอ่อ… ขอบคุณที่ช่วยเก็บนะ” (+1)",
                null,null,null,"S14",null,null,null));

        SCENES.put("S14", new SCENE("ปริศนา","Day 1",
                "“นักเรียนใหม่สินะ… ห้อง 2-B ใช่ไหม?”",
                null,null,null,"S15",null,null,null));

        SCENES.put("S15", new SCENE("คุณ","Day 1",
                "“รู้ได้ยังไง…?”",
                null,null,null,"S16",null,null,null));

        SCENES.put("S16", new SCENE("ปริศนา","Day 1",
                "“ดูออก”",
                null,null,null,"S17",null,null,null));

        SCENES.put("S17", new SCENE("Narrator","Day 1",
                "แล้วเขาก็เดินจากไป ทิ้งไว้เพียงความรู้สึกแปลก ๆ ในอก",
                null,null,null,"S18",null,null,null));

        SCENES.put("S18", new SCENE("Narrator","Day 1",
                "คุณไม่รู้ว่าทำไม แต่ภาพของคนคนนั้นยังติดอยู่ในหัว",
                null,null,null,"S19",null,null,null));

        SCENES.put("S19", new SCENE("Narrator","Day 1",
                "ในห้องเรียน 2-B ครูเรียกคุณขึ้นแนะนำตัว",
                null,null,null,"Q2",null,null,null));

        SCENES.put("Q2", new SCENE("คุณ","Day 1",
                "🎯 ตัวเลือกที่ 2 – แนะนำตัว",
                "A) แนะนำตัวสั้น ๆ สุภาพ",
                "B) พูดติดตลกให้ห้องหัวเราะ",
                "C) พูดจริงจังว่าอยากเริ่มต้นใหม่",
                null,"Q2A","Q2B","Q2C"));

        SCENES.put("Q2A", new SCENE("Narrator","Day 1",
                "คุณดูเรียบร้อย สุภาพ",
                null,null,null,"S20",null,null,null));

        SCENES.put("Q2B", new SCENE("Narrator","Day 1",
                "เสียงหัวเราะดังขึ้นทั้งห้อง",
                null,null,null,"S20",null,null,null));

        SCENES.put("Q2C", new SCENE("Narrator","Day 1",
                "บางคนเริ่มสนใจคุณเป็นพิเศษ",
                null,null,null,"S20",null,null,null));

        SCENES.put("S20", new SCENE("ปริศนา","Day 1",
                "“บังเอิญอีกแล้ว”",
                null,null,null,"Q3",null,null,null));

        SCENES.put("Q3", new SCENE("คุณ","Day 1",
                "🎯 ตัวเลือกที่ 3 – ความบังเอิญ",
                "A) หรือว่าเราโชคชะตาผูกกันนะ",
                "B) โลกมันกลมดีเนอะ",
                "C) ก็แค่บังเอิญแหละ",
                null,"Q3A","Q3B","Q3C"));

        SCENES.put("Q3A", new SCENE("ปริศนา","Day 1",
                "อีกฝ่ายยิ้มเล็กน้อย… (Rare Reaction 💖 +5)",
                null,null,null,"END",null,null,null));

        SCENES.put("Q3B", new SCENE("ปริศนา","Day 1",
                "“โลกมันกลมจริง ๆ” (+2)",
                null,null,null,"END",null,null,null));

        SCENES.put("Q3C", new SCENE("ปริศนา","Day 1",
                "“งั้นเหรอ…” (0)",
                null,null,null,"END",null,null,null));

        // ================= LUNCH SCENE =================

SCENES.put("END", new SCENE("Narrator","Day 1",
        "พักกลางวัน โรงอาหารเต็มไปด้วยนักเรียนที่มีเพื่อนอยู่แล้ว",
        null,null,null,"LUNCH1",null,null,null));

SCENES.put("LUNCH1", new SCENE("Narrator","Day 1",
        "คุณยืนลังเลอยู่หน้าโรงอาหาร",
        null,null,null,"LUNCH2",null,null,null));

SCENES.put("LUNCH2", new SCENE("ปริศนา","Day 1",
        "“ถ้ายังไม่มีที่นั่ง… ไปด้วยกันไหม”",
        null,null,null,"LUNCH3",null,null,null));

SCENES.put("LUNCH3", new SCENE("Narrator","Day 1",
        "คุณแอบแปลกใจเล็กน้อย เพราะอีกฝ่ายดูเป็นคนไม่ค่อยชวนใคร",
        null,null,null,"Q4",null,null,null));
// ===== Q4 =====
SCENES.put("Q4", new SCENE("คุณ","Day 1",
        "🎯 ตัวเลือกที่ 4 – ตอบรับคำชวน",
        "A) ดีเลย เรากำลังไม่รู้จะไปไหนพอดี",
        "B) ไม่เป็นไร เราไปคนเดียวได้",
        "C) เธอชวนเราเหรอเนี่ย น่าแปลกใจนะ",
        null,"Q4A","Q4B","Q4C"));

SCENES.put("Q4A", new SCENE("Narrator","Day 1",
        "อีกฝ่ายพยักหน้าเบา ๆ แล้วเดินนำไป (+3)",
        null,null,null,"LUNCH4",null,null,null));

SCENES.put("Q4B", new SCENE("Narrator","Day 1",
        "อีกฝ่ายพยักหน้าเบา ๆ ก่อนแยกไปอีกทาง (-1)",
        null,null,null,"LUNCH4",null,null,null));

SCENES.put("Q4C", new SCENE("Narrator","Day 1",
        "อีกฝ่ายหลุดยิ้มบาง ๆ แล้วเดินนำไป (+1)",
        null,null,null,"LUNCH4",null,null,null));


SCENES.put("LUNCH4", new SCENE("Narrator","Day 1",
        "ระหว่างกินข้าว บทสนทนาส่วนใหญ่เงียบ แต่เป็นความเงียบที่ไม่อึดอัด",
        null,null,null,"LUNCH5",null,null,null));

SCENES.put("LUNCH5", new SCENE("คุณ","Day 1",
        "“เธอชื่ออะไรเหรอ”",
        null,null,null,"LUNCH6",null,null,null));

SCENES.put("LUNCH6", new SCENE("ปริศนา","Day 1",
        "“ชื่อตัวละคร”",
        null,null,null,"LUNCH7",null,null,null));

SCENES.put("LUNCH7", new SCENE("Narrator","Day 1",
        "“ชื่อเข้ากับเธอดีนะ” คำพูดธรรมดา แต่หัวใจคุณกลับเต้นแรง",
        null,null,null,"AFTER1",null,null,null));
// ================= หลังเลิกเรียน =================

SCENES.put("AFTER1", new SCENE("Narrator","Day 1",
        "หลังเลิกเรียน คุณเห็นอีกฝ่ายยืนอยู่ใต้ต้นไม้ใหญ่",
        null,null,null,"AFTER2",null,null,null));

SCENES.put("AFTER2", new SCENE("ปริศนา","Day 1",
        "“วันแรกเป็นยังไงบ้าง”",
        null,null,null,"Q5",null,null,null));


// ===== Q5 =====
SCENES.put("Q5", new SCENE("คุณ","Day 1",
        "🎯 คำถามที่ 5 – วันแรกเป็นยังไงบ้าง",
        "A) เหนื่อยนิดหน่อย… แต่ดีขึ้นเพราะเธอ",
        "B) ก็โอเคนะ เริ่มชินแล้ว",
        "C) ไม่ค่อยดีเท่าไหร่",
        null,"Q5A","Q5B","Q5C"));


// ===== เส้นทางพิเศษ (A เท่านั้น) =====
SCENES.put("Q5A", new SCENE("ปริศนา","Day 1",
        "อีกฝ่ายนิ่งไปเล็กน้อย ก่อนจะยิ้มชัดเจนเป็นครั้งแรก (+6 💖)",
        null,null,null,"SPECIAL1",null,null,null));

SCENES.put("SPECIAL1", new SCENE("Narrator","Day 1",
        "รอยยิ้มนั้นต่างจากก่อนหน้านี้—ไม่ใช่แค่ยิ้มบาง ๆ แต่เป็นรอยยิ้มที่อบอุ่น",
        null,null,null,"SPECIAL2",null,null,null));

SCENES.put("SPECIAL2", new SCENE("ปริศนา","Day 1",
        "“ถ้างั้น… พรุ่งนี้ฉันจะรอเธอหน้าโรงเรียนนะ”",
        null,null,null,"DAY1_END",null,null,null));


// ===== ทางปกติ =====
SCENES.put("Q5B", new SCENE("ปริศนา","Day 1",
        "“ดีแล้วล่ะ ค่อย ๆ ปรับตัวไปนะ” (+2)",
        null,null,null,"DAY1_END",null,null,null));

SCENES.put("Q5C", new SCENE("ปริศนา","Day 1",
        "อีกฝ่ายมองคุณอย่างเป็นห่วงเล็กน้อย (+1)",
        null,null,null,"DAY1_END",null,null,null));
// ===== จบจริง =====
SCENES.put("DAY1_END", new SCENE("Narrator","Day 1",
        "🌙 วันแรกจบลง และความโดดเดี่ยวในใจคุณ…หายไปตั้งแต่ตอนไหนก็ไม่รู้",
        null,null,null,null,null,null,null));
    }

    // ================= ENGINE =================
    private void SHOW(String id){
        CURRENT_ID=id;
        SCENE s=SCENES.get(id);
        if(s==null)return;

        DIALOG.SETDATA(s.NAME,s.DAY,s.TEXT);

        boolean hasChoice=s.C1!=null;

        BTN1.setVisible(hasChoice);
        BTN2.setVisible(hasChoice);
        BTN3.setVisible(hasChoice);

        if(hasChoice){
            BTN1.setText(s.C1);
            BTN2.setText(s.C2);
            BTN3.setText(s.C3);
        }
    }

    private void NEXT(){
        SCENE s=SCENES.get(CURRENT_ID);
        if(s!=null&&s.NEXT!=null)SHOW(s.NEXT);
    }

    private void PICK(int i){
        SCENE s=SCENES.get(CURRENT_ID);
        if(s==null)return;
        if(i==1)SHOW(s.NEXT1);
        if(i==2)SHOW(s.NEXT2);
        if(i==3)SHOW(s.NEXT3);
    }

    private void STYLE_BTN(JButton b){
        b.setFont(BTN_FONT);
        b.setBackground(new Color(255,220,235));
        b.setFocusPainted(false);
        b.setOpaque(true);
    }

    private void LAYOUT_UI(){
        int w=FRAME.getContentPane().getWidth();
        int h=FRAME.getContentPane().getHeight();

        DIALOG.setBounds(40,h-200,w-80,160);
        CHOICE_PANEL.setBounds(0,0,w,h);

        int bw=420,bh=50,g=15;
        int y=h-200-(bh+g)*3-20;

        BTN1.setBounds(w/2-bw/2,y,bw,bh);
        BTN2.setBounds(w/2-bw/2,y+bh+g,bw,bh);
        BTN3.setBounds(w/2-bw/2,y+(bh+g)*2,bw,bh);
    }

    public static void main(String[]args){
        SwingUtilities.invokeLater(() -> new Day1().CREATEANDSHOWGUI());
    }

    static class SCENE{
        String NAME,DAY,TEXT;
        String C1,C2,C3;
        String NEXT,NEXT1,NEXT2,NEXT3;
        SCENE(String n,String d,String t,
              String c1,String c2,String c3,
              String next,String n1,String n2,String n3){
            NAME=n;DAY=d;TEXT=t;
            C1=c1;C2=c2;C3=c3;
            NEXT=next;NEXT1=n1;NEXT2=n2;NEXT3=n3;
        }
    }

    static class BGVIEW extends JPanel{
        private Image bg;
        BGVIEW(String path){ bg=new ImageIcon(path).getImage(); }
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            if(bg!=null) g.drawImage(bg,0,0,getWidth(),getHeight(),this);
        }
    }

    static class DIALOGPANEL extends JPanel{
        String NAME="",DAY="",TEXT="";
        DIALOGPANEL(){ setOpaque(false); }
        void SETDATA(String n,String d,String t){
            NAME=n;DAY=d;TEXT=t; repaint();
        }
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g;
            g2.setColor(new Color(255,200,230,200));
            g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
            g2.setColor(Color.BLACK);
            g2.setFont(THAI_FONT);
            g2.drawString(NAME+"  "+DAY,20,30);
            g2.drawString(TEXT,20,70);
        }
    }
}