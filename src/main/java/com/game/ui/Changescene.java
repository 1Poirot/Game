package com.game.ui;

import com.game.controllers.GameController;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Changescene extends JFrame {

    private GameController controller;
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

<<<<<<< HEAD
    public Changescene(GameController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
=======
<<<<<<< HEAD
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int WIDTH = screen.width;
    int HEIGHT = screen.height;

    public Changescene(GameController controller) {
        this.controller = controller;

        setTitle("Love Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
=======
    public Changescene(GameController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> origin/dev/neko

        mainPanel.add(new SceneRoom(), "room");
        mainPanel.add(new SceneNext(), "next");

        add(mainPanel);
        setVisible(true);

        if (controller != null && controller.getAudioSystem() != null) {
            controller.getAudioSystem().playBGM("audiotest2.wav");
        }
    }

    public Changescene() {
        this(null);
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
<<<<<<< HEAD
=======
<<<<<<< HEAD
    class SceneRoom extends JLayeredPane {
        public SceneRoom() {
            setLayout(null);
            add(createBackground("src/main/resources/images/backgrounds/มุมตึก.png"), Integer.valueOf(0));
            add(createCharacter(), Integer.valueOf(1));
            add(createTopLeftUI(), Integer.valueOf(5));
            add(createTopRightUI(), Integer.valueOf(5));
            add(createDialogUI1(), Integer.valueOf(10));
=======
>>>>>>> origin/dev/neko
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
<<<<<<< HEAD
=======
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> origin/dev/neko
        }
    }

    // ========================== SCENE 2 ==========================
<<<<<<< HEAD
=======
<<<<<<< HEAD
    class SceneNext extends JLayeredPane {
        public SceneNext() {
            setLayout(null);
            add(createBackground("src/main/resources/images/backgrounds/ห้องเรียน.jpg"), Integer.valueOf(0));
            add(createCharacter(), Integer.valueOf(1));
            add(createTopLeftUI(), Integer.valueOf(5));
            add(createTopRightUI(), Integer.valueOf(5));
            add(createDialogUI2(), Integer.valueOf(10));
=======
>>>>>>> origin/dev/neko
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
<<<<<<< HEAD
        }
    }

    // ========================== TOP LEFT ==========================
    private JPanel createTopLeftUI(int screenW, int screenH) {

        int panelW = (int) (screenW * 0.33);
        int panelH = (int) (screenH * 0.10);

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
=======
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
        }
    }

<<<<<<< HEAD
    // ========================== TOP LEFT (Affection UI) ==========================
    private JPanel createTopLeftUI() {

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(30, 20, 420, 90);

        RoundedPanel bg = new RoundedPanel(40);
        bg.setLayout(null);
        bg.setBackground(new Color(255, 240, 245, 230));
        bg.setBounds(0, 0, 420, 80);

        JLabel heart = new JLabel("❤");
        heart.setFont(new Font("Dialog", Font.BOLD, 28));
        heart.setForeground(new Color(255, 80, 120));
        heart.setBounds(20, 20, 40, 40);
        bg.add(heart);

        JLabel loveText = new JLabel("Affection");
        loveText.setFont(new Font("Tahoma", Font.BOLD, 16));
        loveText.setBounds(70, 10, 120, 20);
        bg.add(loveText);

        JLabel percent = new JLabel("20%");
        percent.setFont(new Font("Tahoma", Font.BOLD, 18));
        percent.setForeground(new Color(255, 80, 120));
        percent.setBounds(350, 10, 60, 20);
        bg.add(percent);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(20);
        bar.setBounds(70, 35, 300, 25);
        bar.setUI(new RoundedProgressBarUI());
        bar.setBorderPainted(false);
        bar.setOpaque(false);

        bg.add(bar);
        panel.add(bg);
=======
    // ========================== TOP LEFT ==========================
<<<<<<< HEAD
    private JPanel createTopLeftUI() {
=======
    private JPanel createTopLeftUI(int screenW, int screenH) {

        int panelW = (int) (screenW * 0.33);
        int panelH = (int) (screenH * 0.10);

>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
>>>>>>> origin/dev/neko
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
<<<<<<< HEAD
=======
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
>>>>>>> origin/dev/neko

        return panel;
    }

<<<<<<< HEAD
    // ========================== TOP RIGHT ==========================
    private JPanel createTopRightUI(int screenW, int screenH) {

        int panelW = (int) (screenW * 0.14);
        int panelH = (int) (screenH * 0.10);

        JPanel panel = new RoundedPanel(30);
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 230, 220));
=======
<<<<<<< HEAD
    // ========================== TOP RIGHT ==========================
private JPanel createTopRightUI() {
=======
<<<<<<< HEAD
    // ========================== TOP RIGHT (เพิ่มปุ่มกดได้)
    // ==========================
    private JPanel createTopRightUI() {
        JPanel panel = new RoundedPanel(30);
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 230, 220));
        panel.setBounds(WIDTH - 230, 20, 180, 70);
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a

    // ===== กล่องพื้นหลัง =====
    RoundedPanel panel = new RoundedPanel(30);
    panel.setLayout(null);
    panel.setBackground(new Color(255, 255, 255, 210));
    panel.setBounds(WIDTH - 240, 20, 190, 70);

    // ===== ปุ่มกระเป๋า =====
    JLabel bag = new JLabel(new ImageIcon("\"src/main/resources/images/icon/school-bag.png"));
    bag.setBounds(30, 15, 40, 40);
    bag.setCursor(new Cursor(Cursor.HAND_CURSOR));

<<<<<<< HEAD
    // ===== ปุ่มตั้งค่า =====
    JLabel gear = new JLabel(new ImageIcon("src/main/resources/images/icon/setting.png"));
    gear.setBounds(110, 15, 40, 40);
    gear.setCursor(new Cursor(Cursor.HAND_CURSOR));
=======
        // ✅ แทรก: เมื่อกดที่รูปเฟือง ให้ไปหน้าตั้งค่า
=======
    // ========================== TOP RIGHT ==========================
    private JPanel createTopRightUI(int screenW, int screenH) {

        int panelW = (int) (screenW * 0.14);
        int panelH = (int) (screenH * 0.10);

        JPanel panel = new RoundedPanel(30);
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 230, 220));
>>>>>>> origin/dev/neko

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
<<<<<<< HEAD
=======
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> origin/dev/neko
        gear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    // เล่นเสียงคลิกปุ่ม
                    if (controller.getAudioSystem() != null) {
                        controller.getAudioSystem().playSFX("click.wav");
                    }
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a

    // ===== Hover Effect =====
    gear.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            gear.setLocation(110, 12);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            gear.setLocation(110, 15);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (controller != null) {

                if (controller.getAudioSystem() != null) {
                    controller.getAudioSystem().playSFX("click.wav");
                }

                controller.getMainFrame().setVisible(true);
                controller.showAudioSettings();
                dispose();
            }
        }
    });

    // ===== Hover กระเป๋า =====
    bag.addMouseListener(new MouseAdapter() {

<<<<<<< HEAD
        @Override
        public void mouseEntered(MouseEvent e) {
            bag.setLocation(30, 12);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            bag.setLocation(30, 15);
        }
    });

    panel.add(bag);
    panel.add(gear);

    return panel;
}

    // ========================== DIALOG ==========================
    private JPanel createDialogUI1() {
        return createDialogBase(new String[]{
=======
    // ========================== DIALOG SCENE 1 ==========================
<<<<<<< HEAD
    private JPanel createDialogUI1(int w, int h) {

        return createDialogBase(w, h, new String[] {
=======
<<<<<<< HEAD
    private JPanel createDialogUI1() {
        return createDialogBase(new String[] {
=======
    private JPanel createDialogUI1(int w, int h) {

        return createDialogBase(w, h, new String[] {
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
>>>>>>> origin/dev/neko
                "เช้าวันเปิดเทอม ลมเช้าเย็นกว่าที่คาด",
                "คุณยืนอยู่หน้าประตูโรงเรียนในชุดนักเรียนใหม่เอี่ยม",
                "เสียงนักเรียนรอบตัวเต็มไปด้วยบทสนทนาและเสียงหัวเราะ",
                "แต่ไม่มีเสียงไหนเรียกชื่อคุณ",
<<<<<<< HEAD
                "คุณสูดหายใจลึก ก้าวเท้าเข้าไปในรั้วโรงเรียน"
        }, true);
    }

    private JPanel createDialogUI2() {
        return createDialogBase(new String[]{
                "ห้องเรียน",
=======
                "คุณสูดหายใจลึก ก้าวเท้าเข้าไปในรั้วโรงเรียน",
                "และทันทีที่คุณเดินผ่านมุมตึกเรียน — ปึก!",
                "คุณชนเข้ากับใครบางคนอย่างแรง",
                "หนังสือในมือเขา/เธอร่วงกระจายบนพื้น",
                "ดวงตาคู่หนึ่งเงยขึ้นมามองคุณ",
                "แววตานั้นนิ่ง เย็น…แต่มีบางอย่างซ่อนอยู่",
<<<<<<< HEAD

                "\u201cนักเรียนใหม่…?\u201d",
=======
<<<<<<< HEAD
                "“นักเรียนใหม่…?”",
=======

                "\u201cนักเรียนใหม่…?\u201d",
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> origin/dev/neko
                "น้ำเสียงไม่ได้เย็นชา แต่ก็ไม่ได้เป็นมิตร",
                "วินาทีนั้นเอง คุณยังไม่รู้เลยว่า",
                "การชนกันเพียงครั้งเดียว",
                "จะเปลี่ยน 7 วันแรกของคุณไปตลอดกาล"
        }, true);
    }

    // ========================== DIALOG SCENE 2 ==========================
<<<<<<< HEAD
=======
<<<<<<< HEAD
    private JPanel createDialogUI2() {
        return createDialogBase(new String[] {
                "🏫 ห้องเรียน",
=======
>>>>>>> origin/dev/neko
    private JPanel createDialogUI2(int w, int h) {

        return createDialogBase(w, h, new String[] {

                "\uD83C\uDFEB ห้องเรียน",
<<<<<<< HEAD
=======
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
>>>>>>> origin/dev/neko
                "หลังแนะนำตัวหน้าห้องเสร็จ",
                "ครูให้คุณไปนั่งที่ว่างด้านหลัง",
                "อีกฝ่ายก็นั่งอยู่โต๊ะข้าง ๆ",
<<<<<<< HEAD
                "“บังเอิญอีกแล้ว”",
                "“โลกมันกลมดีเนอะ”",

        }, false);
    }

    // ========================== Dialog Base + Name Tag ==========================
=======
                "คุณหยุดชะงักเล็กน้อย",
                "โลกมันกลมเกินไปหรือเปล่า…",
                "อีกฝ่ายเหลือบมองคุณนิดเดียว",
<<<<<<< HEAD
                "\u201cบังเอิญอีกแล้ว\u201d",

=======
<<<<<<< HEAD
                "“บังเอิญอีกแล้ว”",
=======
                "\u201cบังเอิญอีกแล้ว\u201d",

>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> origin/dev/neko
                "คุณหัวเราะ"
        }, false);
    }

    // ========================== DIALOG BASE ==========================
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
    private JPanel createDialogBase(String[] dialogs, boolean changeScene) {

        int dialogWidth = WIDTH - 100;
<<<<<<< HEAD

=======
=======
>>>>>>> origin/dev/neko
    private JPanel createDialogBase(int screenW, int screenH, String[] dialogs, boolean changeScene) {

        int dialogWidth = screenW - (int) (screenW * 0.08);
        int dialogHeight = (int) (screenH * 0.28);
        int containerX = (int) (screenW * 0.04);
        int containerY = screenH - dialogHeight - (int) (screenH * 0.04);

<<<<<<< HEAD
        JPanel container = new JPanel(null);
        container.setOpaque(false);
        container.setBounds(containerX, containerY, dialogWidth, dialogHeight);

        int nameBoxH = (int) (dialogHeight * 0.22);
        int dialogBoxY = (int) (nameBoxH * 0.6);
        int dialogBoxH = dialogHeight - dialogBoxY;
=======
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
        JPanel container = new JPanel(null);
        container.setOpaque(false);
        container.setBounds(containerX, containerY, dialogWidth, dialogHeight);
>>>>>>> origin/dev/neko

        int nameBoxH = (int) (dialogHeight * 0.22);
        int dialogBoxY = (int) (nameBoxH * 0.6);
        int dialogBoxH = dialogHeight - dialogBoxY;

        // ===== Dialog Box =====
        RoundedPanel dialog = new RoundedPanel(40);
        dialog.setBackground(new Color(244, 169, 193, 220));
        dialog.setBounds(0, dialogBoxY, dialogWidth, dialogBoxH);
        dialog.setLayout(null);

<<<<<<< HEAD
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
=======
        JLabel text = new JLabel(dialogs[0]);
        text.setFont(new Font("Tahoma", Font.PLAIN, 22));
        text.setForeground(Color.WHITE);
        text.setBounds(60, 70, dialogWidth - 200, 40);
>>>>>>> origin/dev/neko

        dialog.add(text);

        // ===== Name Tag =====
        RoundedPanel nameBox = new RoundedPanel(25);
        nameBox.setBackground(Color.WHITE);
<<<<<<< HEAD
        nameBox.setBounds(40, 0, 260, 55);
        nameBox.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel name = new JLabel("คิม แจฮยอน");
        name.setFont(new Font("Tahoma", Font.BOLD, 20));
        name.setForeground(new Color(255, 80, 120));

        nameBox.add(name);

        // ===== Day =====
        JLabel day = new JLabel("Day 1");
<<<<<<< HEAD
        day.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        day.setBounds(dialogWidth - (int) (dialogWidth * 0.1), (int) (dialogBoxH * 0.1),
                (int) (dialogWidth * 0.08), (int) (dialogBoxH * 0.2));
=======
        day.setFont(new Font("Tahoma", Font.BOLD, 18));
        day.setForeground(Color.WHITE);
        day.setBounds(dialogWidth - 120, 20, 100, 30);
>>>>>>> origin/dev/neko

=======
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
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
        dialog.add(day);

        container.add(nameBox);
        container.add(dialog);

        final int[] index = {0};

        dialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (controller != null && controller.getAudioSystem() != null) {
                    controller.getAudioSystem().playSFX("click.wav");
                }

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
<<<<<<< HEAD
    private JLabel createBackground(String path, int w, int h) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);

        JLabel bg = new JLabel(new ImageIcon(img));
        bg.setBounds(0, 0, w, h);

=======
<<<<<<< HEAD
    private JLabel createBackground(String path) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        JLabel bg = new JLabel(new ImageIcon(img));
        bg.setBounds(0, 0, WIDTH, HEIGHT);
<<<<<<< HEAD

=======
=======
    private JLabel createBackground(String path, int w, int h) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);

        JLabel bg = new JLabel(new ImageIcon(img));
        bg.setBounds(0, 0, w, h);

>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
>>>>>>> origin/dev/neko
        return bg;
    }

    // ========================== CHARACTER ==========================
<<<<<<< HEAD
=======
<<<<<<< HEAD
    private JLabel createCharacter() {
<<<<<<< HEAD

        int charW = WIDTH / 90 * 30;
        int charH = HEIGHT - 100;

        ImageIcon icon = new ImageIcon("src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png");
=======
        int charW = WIDTH / 4;
        int charH = HEIGHT - 300;
=======
>>>>>>> origin/dev/neko
    private JLabel createCharacter(int screenW, int screenH) {

        int charW = screenW / 4;
        int charH = (int) (screenH * 0.72);

<<<<<<< HEAD
=======
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> origin/dev/neko
        ImageIcon icon = new ImageIcon("src/main/resources/images/backgrounds/ผู้ชาย ตัวเอก.png");
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
        Image img = icon.getImage().getScaledInstance(charW, charH, Image.SCALE_SMOOTH);

        JLabel character = new JLabel(new ImageIcon(img));
<<<<<<< HEAD
=======
<<<<<<< HEAD

        int x = (WIDTH - charW) / 2;
        int y = HEIGHT - charH - 120;

=======
<<<<<<< HEAD
        int x = (WIDTH - charW) / 2;
        int y = HEIGHT - charH - 120;
=======
>>>>>>> origin/dev/neko

        int x = (screenW - charW) / 2;
        int y = screenH - charH - (int) (screenH * 0.11);

<<<<<<< HEAD
=======
>>>>>>> 0becec2a56481e4c0a93934ab74c926e8298f718
>>>>>>> 5d12d8de96408033f02661d172ff0ec86af1b14a
>>>>>>> origin/dev/neko
        character.setBounds(x, y, charW, charH);

        return character;
    }

    // ========================== Rounded Panel ==========================
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

    // ========================== ProgressBar UI ==========================
    class RoundedProgressBarUI extends javax.swing.plaf.basic.BasicProgressBarUI {

        @Override
        protected void paintDeterminate(Graphics g, JComponent c) {

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int width = progressBar.getWidth();
            int height = progressBar.getHeight();

            g2.setColor(new Color(255, 220, 230));
            g2.fillRoundRect(0, 0, width, height, height, height);

            int amountFull = (int) (width * progressBar.getPercentComplete());

            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(255, 120, 160),
                    width, 0, new Color(255, 80, 120)
            );

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, amountFull, height, height, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Changescene());
    }
}