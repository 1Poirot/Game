package com.game.ui;

import com.game.controllers.GameController;
import com.game.systems.save.SaveSystem;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SaveScreen extends JPanel {
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final GameController CONTROLLER;
    private final Runnable ON_BACK;
    private final JLabel[] SLOT_TITLE = new JLabel[5];
    private final JLabel[] SLOT_SUB = new JLabel[5];

    public SaveScreen(GameController CONTROLLER, Runnable ON_BACK) {
        this.CONTROLLER = CONTROLLER;
        this.ON_BACK = ON_BACK;

        setLayout(new BorderLayout());
        setBackground(new Color(255, 209, 220));
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // Top Bar
        JPanel TOP = new JPanel(new BorderLayout(18, 0));
        TOP.setOpaque(false);
        JButton BACK = MAKE_BUTTON("< ย้อนกลับ", Color.WHITE, new Color(30, 30, 30), 200, 56, 22, 26);
        BACK.addActionListener(e -> this.ON_BACK.run());
        TOP.add(BACK, BorderLayout.WEST);
        add(TOP, BorderLayout.NORTH);

        JPanel LIST = new JPanel(new GridLayout(5, 1, 0, 14));
        LIST.setOpaque(false);

        for (int i = 0; i < 5; i++) {
            int SLOT = i + 1;
            JPanel CARD = MAKE_SLOT_CARD();
            SLOT_TITLE[i] = new JLabel();
            SLOT_TITLE[i].setFont(new Font("Tahoma", Font.BOLD, 22));
            SLOT_SUB[i] = new JLabel();
            SLOT_SUB[i].setFont(new Font("Tahoma", Font.PLAIN, 16));

            JPanel TEXT = new JPanel(new GridLayout(2, 1, 0, 2));
            TEXT.setOpaque(false);
            TEXT.add(SLOT_TITLE[i]);
            TEXT.add(SLOT_SUB[i]);

            JButton LOAD = MAKE_BUTTON("โหลด", new Color(92, 145, 235), Color.WHITE, 150, 50, 18, 22);
            JButton SAVE = MAKE_BUTTON("บันทึกทับ", new Color(255, 105, 180), Color.WHITE, 150, 50, 18, 22);
            JButton DELETE = MAKE_BUTTON("ลบเซฟ", new Color(180, 180, 180), Color.WHITE, 150, 50, 18, 22);

            // Logic ปุ่มโหลด
            LOAD.addActionListener(e -> {
                Map<String, String> DATA = SaveSystem.loadFromLocal(SLOT);
                if (DATA == null) { JOptionPane.showMessageDialog(this, "สล็อตนี้ว่างอยู่"); return; }
                int CONFIRM = JOptionPane.showConfirmDialog(this, "โหลด Slot " + SLOT + "?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
                if (CONFIRM != JOptionPane.YES_OPTION) return;

                this.CONTROLLER.getPlayer().setName(DATA.getOrDefault("Name", "Hero"));
                this.CONTROLLER.getPlayer().setMoney(Integer.parseInt(DATA.getOrDefault("Money", "0")));
                int savedIdx = Integer.parseInt(DATA.getOrDefault("DialogueIndex", "0"));
                this.CONTROLLER.loadGameAt(savedIdx); // วาร์ปไปหน้าเนื้อเรื่อง
                JOptionPane.showMessageDialog(this, "โหลดข้อมูลสำเร็จ!");
            });

            // Logic ปุ่มเซฟ
            SAVE.addActionListener(e -> {
                int CONFIRM = JOptionPane.showConfirmDialog(this, "บันทึกทับ Slot " + SLOT + "?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
                if (CONFIRM != JOptionPane.YES_OPTION) return;

                int currentIdx = this.CONTROLLER.getCurrentDialogueIndex();
                SaveSystem.saveToFile(SLOT, this.CONTROLLER.getPlayer().getName(), this.CONTROLLER.getPlayer().getMoney(), DATE_FORMAT.format(new Date()), currentIdx);
                REFRESH_SLOTS();
                JOptionPane.showMessageDialog(this, "บันทึกเรียบร้อย!");
            });

            // Logic ปุ่มลบ
            DELETE.addActionListener(e -> {
                int CONFIRM = JOptionPane.showConfirmDialog(this, "ลบ Slot " + SLOT + "?", "ยืนยันการลบ", JOptionPane.YES_NO_OPTION);
                if (CONFIRM == JOptionPane.YES_OPTION) {
                    SaveSystem.deleteSave(SLOT);
                    REFRESH_SLOTS();
                    JOptionPane.showMessageDialog(this, "ลบข้อมูลสำเร็จ!");
                }
            });

            JPanel ACTIONS = new JPanel(new GridLayout(3, 1, 0, 5)); // ปรับเป็น 3 แถว
            ACTIONS.setOpaque(false);
            ACTIONS.add(LOAD); ACTIONS.add(SAVE); ACTIONS.add(DELETE);

            CARD.add(TEXT, BorderLayout.CENTER);
            CARD.add(ACTIONS, BorderLayout.EAST);
            LIST.add(CARD);
        }
        add(LIST, BorderLayout.CENTER);
        REFRESH_SLOTS();
    }

    private void REFRESH_SLOTS() {
        for (int i = 0; i < 5; i++) {
            Map<String, String> DATA = SaveSystem.loadFromLocal(i + 1);
            if (DATA == null) {
                SLOT_TITLE[i].setText("SLOT " + (i + 1) + " | ว่าง");
                SLOT_SUB[i].setText("ยังไม่มีข้อมูล");
            } else {
                SLOT_TITLE[i].setText("SLOT " + (i + 1) + " | " + DATA.get("Name") + " | ฿" + DATA.get("Money"));
                SLOT_SUB[i].setText("วันที่: " + DATA.get("Date"));
            }
        }
    }

    private JPanel MAKE_SLOT_CARD() {
        JPanel P = new JPanel(new BorderLayout(18, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D G2 = (Graphics2D) g.create();
                G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                G2.setColor(Color.WHITE);
                G2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                G2.dispose();
            }
        };
        P.setOpaque(false);
        P.setBorder(new EmptyBorder(10, 16, 10, 16));
        return P;
    }

    private JButton MAKE_BUTTON(String TEXT, Color BG, Color FG, int W, int H, int FONT_SIZE, int ARC) {
        JButton BTN = new JButton(TEXT) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D G2 = (Graphics2D) g.create();
                G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                G2.setColor(getBackground());
                G2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
                G2.setColor(getForeground());
                FontMetrics FM = G2.getFontMetrics();
                G2.drawString(getText(), (getWidth() - FM.stringWidth(getText())) / 2, (getHeight() + FM.getAscent()) / 2 - 4);
                G2.dispose();
            }
        };
        BTN.setFont(new Font("Tahoma", Font.BOLD, FONT_SIZE)); // ใช้ Tahoma เพื่อภาษาไทย
        BTN.setBackground(BG);
        BTN.setForeground(FG);
        BTN.setPreferredSize(new Dimension(W, H));
        BTN.setContentAreaFilled(false);
        BTN.setBorderPainted(false);
        BTN.setFocusPainted(false);
        return BTN;
    }
}