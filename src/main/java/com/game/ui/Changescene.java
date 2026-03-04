package com.game.ui;

import com.game.controllers.GameController;
import com.game.systems.choice.Day1;
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

    public Changescene(GameController controller) {
        this.controller = controller;

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int WIDTH = screen.width;
        int HEIGHT = screen.height;

        setTitle("Love Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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
    abstract class ResponsiveScene extends JLayeredPane {

        public ResponsiveScene() {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    layoutComponents();
                }
            });
        }

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

            JLabel bg = createBackground("src/main/resources/images/backgrounds/มุมตึก.png", w, h);
            bg.setBounds(0, 0, w, h);
            add(bg, Integer.valueOf(0));

            add(createCharacter(w, h), Integer.valueOf(1));
            add(createTopLeftUI(w, h), Integer.valueOf(5));
            add(createTopRightUI(w, h), Integer.valueOf(5));
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

            JLabel bg = createBackground("src/main/resources/images/backgrounds/ห้องเรียน.jpg", w, h);
            bg.setBounds(0, 0, w, h);
            add(bg, Integer.valueOf(0));

            add(createCharacter(w, h), Integer.valueOf(1));
            add(createTopLeftUI(w, h), Integer.valueOf(5));
            add(createTopRightUI(w, h), Integer.valueOf(5));
            add(createDialogUI2(w, h), Integer.valueOf(10));

            revalidate();
            repaint();
        }
    }

    // ========================== TOP LEFT (Affection UI) ==========================
    private JPanel createTopLeftUI(int screenW, int screenH) {

        int panelW = (int) (screenW * 0.33);
        int panelH = (int) (screenH * 0.10);

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds((int) (screenW * 0.02), (int) (screenH * 0.02), panelW, panelH);

        RoundedPanel bg = new RoundedPanel(40);
        bg.setLayout(null);
        bg.setBackground(new Color(255, 240, 245, 230));
        bg.setBounds(0, 0, panelW, panelH);

        JLabel heart = new JLabel("❤");
        heart.setFont(new Font("Dialog", Font.BOLD, Math.max(16, screenW / 60)));
        heart.setForeground(new Color(255, 80, 120));
        heart.setBounds(10, (int) (panelH * 0.2), (int) (panelH * 0.6), (int) (panelH * 0.6));
        bg.add(heart);

        int barX = (int) (panelH * 0.75);
        int barW = panelW - barX - 10;
        int fontSize = Math.max(12, screenW / 70);

        JLabel loveText = new JLabel("Affection");
        loveText.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        loveText.setBounds(barX, (int) (panelH * 0.08), barW / 2, (int) (panelH * 0.35));
        bg.add(loveText);

        JLabel percent = new JLabel("20%");
        percent.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        percent.setForeground(new Color(255, 80, 120));
        percent.setBounds(panelW - (int) (panelW * 0.2), (int) (panelH * 0.08), (int) (panelW * 0.18),
                (int) (panelH * 0.35));
        bg.add(percent);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(20);
        bar.setBounds(barX, (int) (panelH * 0.5), barW, (int) (panelH * 0.38));
        bar.setUI(new RoundedProgressBarUI());
        bar.setBorderPainted(false);
        bar.setOpaque(false);
        bg.add(bar);

        panel.add(bg);
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

        JLabel bag = new JLabel(new ImageIcon("src/main/resources/images/icon/school-bag.png"));
        bag.setBounds((int) (panelW * 0.15), iconY, iconSize, iconSize);
        bag.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null)
                    controller.showShop();
            }
        });

        JLabel gear = new JLabel(new ImageIcon("src/main/resources/images/icon/setting.png"));
        gear.setBounds((int) (panelW * 0.55), iconY, iconSize, iconSize);
        gear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    if (controller.getAudioSystem() != null)
                        controller.getAudioSystem().playSFX("click.wav");
                    controller.getMainFrame().setVisible(true);
                    controller.showAudioSettings();
                    dispose();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                gear.setLocation((int) (panelW * 0.55), iconY - 3);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                gear.setLocation((int) (panelW * 0.55), iconY);
            }
        });

        panel.add(bag);
        panel.add(gear);
        return panel;
    }

    // ========================== DIALOG SCENE 1 ==========================
    private JPanel createDialogUI1(int w, int h) {
        return createDialogBase(w, h, new String[] {
                "เช้าวันเปิดเทอรม ลมเช้าเย็นกว่าที่คาด",
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
        }, true, null);
    }

    // ========================== DIALOG SCENE 2 ==========================
    private JPanel createDialogUI2(int w, int h) {
        return createDialogBase(w, h, new String[] {
                "\uD83C\uDFEB ห้องเรียน",
                "หลังแนะนำตัวหน้าห้องเสร็จ",
                "ครูให้คุณไปนั่งที่ว่างด้านหลัง",
                "อีกฝ่ายก็นั่งอยู่โต๊ะข้าง ๆ",
                "คุณหยุดชะงักเล็กน้อย",
                "โลกมันกลมเกินไปหรือเปล่า…",
                "อีกฝ่ายเหลือบมองคุณนิดเดียว",
                "\u201cบังเอิญอีกแล้ว\u201d",
                "คุณหัวเราะ"
        }, false, () -> {
            dispose();
            SwingUtilities.invokeLater(() -> new Day1().CREATEANDSHOWGUI());
        });
    }

    // ========================== DIALOG BASE ==========================
    private JPanel createDialogBase(int screenW, int screenH, String[] dialogs, boolean changeScene, Runnable onEnd) {

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

        // ===== Dialog Box =====
        RoundedPanel dialog = new RoundedPanel(40);
        dialog.setBackground(new Color(244, 169, 193, 220));
        dialog.setBounds(0, dialogBoxY, dialogWidth, dialogBoxH);
        dialog.setLayout(null);

        int fontSize = Math.max(12, screenW / 70);

        JLabel text = new JLabel(dialogs[0]);
        text.setFont(new Font("Tahoma", Font.PLAIN, (int) (fontSize * 1.2)));
        text.setForeground(Color.WHITE);
        text.setBounds((int) (dialogWidth * 0.05), (int) (dialogBoxH * 0.35),
                dialogWidth - (int) (dialogWidth * 0.2), (int) (dialogBoxH * 0.3));
        dialog.add(text);

        JLabel day = new JLabel("Day 1");
        day.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        day.setForeground(Color.WHITE);
        day.setBounds(dialogWidth - (int) (dialogWidth * 0.1), (int) (dialogBoxH * 0.1),
                (int) (dialogWidth * 0.08), (int) (dialogBoxH * 0.2));
        dialog.add(day);

        JLabel sparkle = new JLabel("✨");
        sparkle.setFont(new Font("Dialog", Font.PLAIN, Math.max(16, screenW / 50)));
        sparkle.setBounds(dialogWidth - (int) (dialogWidth * 0.05),
                (int) (dialogBoxH * 0.7), (int) (dialogWidth * 0.04), (int) (dialogBoxH * 0.25));
        dialog.add(sparkle);

        // ===== Name Tag =====
        RoundedPanel nameBox = new RoundedPanel(25);
        nameBox.setBackground(Color.WHITE);
        nameBox.setBounds((int) (dialogWidth * 0.03), 0, (int) (dialogWidth * 0.18), nameBoxH);
        nameBox.setLayout(new FlowLayout(FlowLayout.CENTER, 10, (int) (nameBoxH * 0.2)));

        JLabel name = new JLabel("คิม แจฮยอน");
        name.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        name.setForeground(new Color(255, 80, 120));
        nameBox.add(name);

        container.add(nameBox);
        container.add(dialog);

        final int[] index = { 0 };

        dialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null && controller.getAudioSystem() != null)
                    controller.getAudioSystem().playSFX("click.wav");

                index[0]++;
                if (index[0] < dialogs.length) {
                    text.setText(dialogs[index[0]]);
                } else if (onEnd != null) {
                    onEnd.run();
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

        ImageIcon icon = new ImageIcon("src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png");
        Image img = icon.getImage().getScaledInstance(charW, charH, Image.SCALE_SMOOTH);

        JLabel character = new JLabel(new ImageIcon(img));
        character.setBounds((screenW - charW) / 2, screenH - charH - (int) (screenH * 0.11), charW, charH);
        return character;
    }

    // ========================== Rounded Panel ==========================
    class RoundedPanel extends JPanel {

        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = progressBar.getWidth();
            int height = progressBar.getHeight();

            g2.setColor(new Color(255, 220, 230));
            g2.fillRoundRect(0, 0, width, height, height, height);

            int amountFull = (int) (width * progressBar.getPercentComplete());
            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(255, 120, 160),
                    width, 0, new Color(255, 80, 120));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, amountFull, height, height, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Changescene());
    }
}