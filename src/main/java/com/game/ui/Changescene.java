package com.game.ui;

import com.game.controllers.GameController;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Changescene extends JPanel {

    private GameController controller;
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    public Changescene(GameController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        mainPanel.add(new SceneRoom(), "room");
        mainPanel.add(new SceneNext(), "next");

        add(mainPanel, BorderLayout.CENTER);
    }

    // ========================== SCENE BASE ==========================
    // คลาสพื้นฐานสำหรับทุก Scene — ฟัง resize แล้ว rebuild layout อัตโนมัติ
    abstract class ResponsiveScene extends JLayeredPane {

        public ResponsiveScene() {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    layoutComponents();
                }
            });
        }

        // แต่ละ Scene implement method นี้เพื่อจัด layout ตามขนาดจริง
        protected abstract void layoutComponents();
    }

    // ========================== SCENE 1 ==========================
    class SceneRoom extends ResponsiveScene {

        @Override
        protected void layoutComponents() {
            int w = getWidth();
            int h = getHeight();
            if (w == 0 || h == 0)
                return;

            removeAll();

            // Background
            JLabel bg = createBackground("src/main/resources/images/backgrounds/มุมตึก.png", w, h);
            bg.setBounds(0, 0, w, h);
            add(bg, Integer.valueOf(0));

            // Character
            JLabel character = createCharacter(w, h);
            add(character, Integer.valueOf(1));

            // Top UI
            add(createTopLeftUI(w, h), Integer.valueOf(5));
            add(createTopRightUI(w, h), Integer.valueOf(5));

            // Dialog
            add(createDialogUI1(w, h), Integer.valueOf(10));

            revalidate();
            repaint();
        }
    }

    // ========================== SCENE 2 ==========================
    class SceneNext extends ResponsiveScene {

        @Override
        protected void layoutComponents() {
            int w = getWidth();
            int h = getHeight();
            if (w == 0 || h == 0)
                return;

            removeAll();

            // Background
            JLabel bg = createBackground("src/main/resources/images/backgrounds/ห้องเรียน.jpg", w, h);
            bg.setBounds(0, 0, w, h);
            add(bg, Integer.valueOf(0));

            // Character
            JLabel character = createCharacter(w, h);
            add(character, Integer.valueOf(1));

            // Top UI
            add(createTopLeftUI(w, h), Integer.valueOf(5));
            add(createTopRightUI(w, h), Integer.valueOf(5));

            // Dialog
            add(createDialogUI2(w, h), Integer.valueOf(10));

            revalidate();
            repaint();
        }
    }

    // ========================== TOP LEFT ==========================
    private JPanel createTopLeftUI(int screenW, int screenH) {

        int panelW = (int) (screenW * 0.33);
        int panelH = (int) (screenH * 0.10);

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds((int) (screenW * 0.02), (int) (screenH * 0.02), panelW, panelH);

        JLabel heart = new JLabel(new ImageIcon("heart.png"));
        heart.setBounds(0, (int) (panelH * 0.15), (int) (panelH * 0.7), (int) (panelH * 0.7));
        panel.add(heart);

        int barX = (int) (panelH * 0.75);
        int barW = panelW - barX;

        RoundedPanel barBg = new RoundedPanel(30);
        barBg.setBackground(new Color(255, 230, 235));
        barBg.setBounds(barX, (int) (panelH * 0.15), barW, (int) (panelH * 0.6));
        barBg.setLayout(null);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(20);
        bar.setString("20%");
        bar.setStringPainted(true);
        bar.setBounds(5, 3, barW - 10, (int) (panelH * 0.5));

        barBg.add(bar);
        panel.add(barBg);

        return panel;
    }

    // ========================== TOP RIGHT ==========================
    private JPanel createTopRightUI(int screenW, int screenH) {

        int panelW = (int) (screenW * 0.14);
        int panelH = (int) (screenH * 0.10);

        JPanel panel = new RoundedPanel(30);
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 230, 220));

        panel.setBounds(screenW - panelW - (int) (screenW * 0.02), (int) (screenH * 0.02), panelW, panelH);

        int iconSize = (int) (panelH * 0.7);
        int iconY = (int) (panelH * 0.15);

        JLabel bag = new JLabel(new ImageIcon("bag.png"));
        bag.setBounds((int) (panelW * 0.15), iconY, iconSize, iconSize);
        bag.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    controller.showShop();
                }
            }
        });

        JLabel gear = new JLabel(new ImageIcon("gear.png"));
        gear.setBounds((int) (panelW * 0.55), iconY, iconSize, iconSize);
        gear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    controller.showSettings();
                }
            }
        });

        panel.add(bag);
        panel.add(gear);

        return panel;
    }

    // ========================== DIALOG SCENE 1 ==========================
    private JPanel createDialogUI1(int w, int h) {

        return createDialogBase(w, h, new String[] {
                "เช้าวันเปิดเทอม ลมเช้าเย็นกว่าที่คาด",
                "คุณยืนอยู่หน้าประตูโรงเรียนในชุดนักเรียนใหม่เอี่ยม",
                "เสียงนักเรียนรอบตัวเต็มไปด้วยบทสนทนาและเสียงหัวเราะ",
                "แต่ไม่มีเสียงไหนเรียกชื่อคุณ",

                "คุณสูดหายใจลึก ก้าวเท้าเข้าไปในรั้วโรงเรียน",
                "และทันทีที่คุณเดินผ่านมุมตึกเรียน — ปึก!",
                "คุณชนเข้ากับใครบางคนอย่างแรง",
                "หนังสือในมือเขา/เธอร่วงกระจายบนพื้น",

                "ดวงตาคู่หนึ่งเงยขึ้นมามองคุณ",
                "แววตานั้นนิ่ง เย็น…แต่มีบางอย่างซ่อนอยู่",

                "\u201cนักเรียนใหม่…?\u201d",
                "น้ำเสียงไม่ได้เย็นชา แต่ก็ไม่ได้เป็นมิตร",

                "วินาทีนั้นเอง คุณยังไม่รู้เลยว่า",
                "การชนกันเพียงครั้งเดียว",
                "จะเปลี่ยน 7 วันแรกของคุณไปตลอดกาล"
        }, true);
    }

    // ========================== DIALOG SCENE 2 ==========================
    private JPanel createDialogUI2(int w, int h) {

        return createDialogBase(w, h, new String[] {

                "\uD83C\uDFEB ห้องเรียน",
                "หลังแนะนำตัวหน้าห้องเสร็จ",
                "ครูให้คุณไปนั่งที่ว่างด้านหลัง",

                "และเมื่อคุณเดินไปถึงโต๊ะเรียน",
                "อีกฝ่ายก็นั่งอยู่โต๊ะข้าง ๆ",

                "คุณหยุดชะงักเล็กน้อย",
                "โลกมันกลมเกินไปหรือเปล่า…",

                "อีกฝ่ายเหลือบมองคุณนิดเดียว",
                "\u201cบังเอิญอีกแล้ว\u201d",

                "คุณหัวเราะ"

        }, false); // false = ไม่ต้องเปลี่ยนฉากต่อ
    }

    // ========================== DIALOG BASE ==========================
    private JPanel createDialogBase(int screenW, int screenH, String[] dialogs, boolean changeScene) {

        int dialogWidth = screenW - (int) (screenW * 0.08);
        int dialogHeight = (int) (screenH * 0.28);
        int containerX = (int) (screenW * 0.04);
        int containerY = screenH - dialogHeight - (int) (screenH * 0.04);

        JPanel container = new JPanel(null);
        container.setOpaque(false);
        container.setBounds(containerX, containerY, dialogWidth, dialogHeight);

        int nameBoxH = (int) (dialogHeight * 0.22);
        int dialogBoxY = (int) (nameBoxH * 0.6);
        int dialogBoxH = dialogHeight - dialogBoxY;

        RoundedPanel dialog = new RoundedPanel(40);
        dialog.setBackground(new Color(244, 169, 193, 220));
        dialog.setBounds(0, dialogBoxY, dialogWidth, dialogBoxH);
        dialog.setLayout(null);

        RoundedPanel nameBox = new RoundedPanel(25);
        nameBox.setBackground(Color.WHITE);
        nameBox.setBounds((int) (dialogWidth * 0.03), 0, (int) (dialogWidth * 0.18), nameBoxH);
        nameBox.setLayout(new FlowLayout(FlowLayout.CENTER, 10, (int) (nameBoxH * 0.2)));

        int fontSize = Math.max(12, screenW / 70);

        JLabel name = new JLabel("คิม แจฮยอน");
        name.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        nameBox.add(name);

        JLabel text = new JLabel(dialogs[0]);
        text.setFont(new Font("Tahoma", Font.PLAIN, (int) (fontSize * 1.2)));
        text.setBounds((int) (dialogWidth * 0.05), (int) (dialogBoxH * 0.35),
                dialogWidth - (int) (dialogWidth * 0.2), (int) (dialogBoxH * 0.3));

        JLabel day = new JLabel("Day 1");
        day.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        day.setBounds(dialogWidth - (int) (dialogWidth * 0.1), (int) (dialogBoxH * 0.1),
                (int) (dialogWidth * 0.08), (int) (dialogBoxH * 0.2));

        JLabel sparkle = new JLabel("✨");
        sparkle.setFont(new Font("Dialog", Font.PLAIN, Math.max(16, screenW / 50)));
        sparkle.setBounds(dialogWidth - (int) (dialogWidth * 0.05),
                (int) (dialogBoxH * 0.7), (int) (dialogWidth * 0.04), (int) (dialogBoxH * 0.25));

        dialog.add(text);
        dialog.add(day);
        dialog.add(sparkle);

        container.add(nameBox);
        container.add(dialog);

        final int[] index = { 0 };

        dialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                index[0]++;

                if (index[0] < dialogs.length) {
                    text.setText(dialogs[index[0]]);
                } else if (changeScene) {
                    cardLayout.show(mainPanel, "next");
                }
            }
        });

        return container;
    }

    // ========================== BACKGROUND ==========================
    private JLabel createBackground(String path, int w, int h) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);

        JLabel bg = new JLabel(new ImageIcon(img));
        bg.setBounds(0, 0, w, h);

        return bg;
    }

    // ========================== CHARACTER ==========================
    private JLabel createCharacter(int screenW, int screenH) {

        int charW = screenW / 4;
        int charH = (int) (screenH * 0.72);

        ImageIcon icon = new ImageIcon("src/main/resources/images/backgrounds/ผู้ชาย ตัวเอก.png");
        Image img = icon.getImage().getScaledInstance(charW, charH, Image.SCALE_SMOOTH);

        JLabel character = new JLabel(new ImageIcon(img));

        int x = (screenW - charW) / 2;
        int y = screenH - charH - (int) (screenH * 0.11);

        character.setBounds(x, y, charW, charH);

        return character;
    }

    // ========================== ROUNDED PANEL ==========================
    class RoundedPanel extends JPanel {

        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            super.paintComponent(g);
        }
    }

}
