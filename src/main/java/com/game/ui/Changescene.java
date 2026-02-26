package com.game.ui;

import com.game.controllers.GameController;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Changescene extends JFrame {

    private GameController controller;
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

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

    // ========================== SCENE 1 ==========================
    class SceneRoom extends JLayeredPane {
        public SceneRoom() {
            setLayout(null);
            add(createBackground("src/main/resources/images/backgrounds/มุมตึก.png"), Integer.valueOf(0));
            add(createCharacter(), Integer.valueOf(1));
            add(createTopLeftUI(), Integer.valueOf(5));
            add(createTopRightUI(), Integer.valueOf(5));
            add(createDialogUI1(), Integer.valueOf(10));
        }
    }

    // ========================== SCENE 2 ==========================
    class SceneNext extends JLayeredPane {
        public SceneNext() {
            setLayout(null);
            add(createBackground("src/main/resources/images/backgrounds/ห้องเรียน.jpg"), Integer.valueOf(0));
            add(createCharacter(), Integer.valueOf(1));
            add(createTopLeftUI(), Integer.valueOf(5));
            add(createTopRightUI(), Integer.valueOf(5));
            add(createDialogUI2(), Integer.valueOf(10));
        }
    }

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

        return panel;
    }

    // ========================== TOP RIGHT ==========================
private JPanel createTopRightUI() {

    // ===== กล่องพื้นหลัง =====
    RoundedPanel panel = new RoundedPanel(30);
    panel.setLayout(null);
    panel.setBackground(new Color(255, 255, 255, 210));
    panel.setBounds(WIDTH - 240, 20, 190, 70);

    // ===== ปุ่มกระเป๋า =====
    JLabel bag = new JLabel(new ImageIcon("\"src/main/resources/images/icon/school-bag.png"));
    bag.setBounds(30, 15, 40, 40);
    bag.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // ===== ปุ่มตั้งค่า =====
    JLabel gear = new JLabel(new ImageIcon("src/main/resources/images/icon/setting.png"));
    gear.setBounds(110, 15, 40, 40);
    gear.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
                "เช้าวันเปิดเทอม ลมเช้าเย็นกว่าที่คาด",
                "คุณยืนอยู่หน้าประตูโรงเรียนในชุดนักเรียนใหม่เอี่ยม",
                "เสียงนักเรียนรอบตัวเต็มไปด้วยบทสนทนาและเสียงหัวเราะ",
                "แต่ไม่มีเสียงไหนเรียกชื่อคุณ",
                "คุณสูดหายใจลึก ก้าวเท้าเข้าไปในรั้วโรงเรียน"
        }, true);
    }

    private JPanel createDialogUI2() {
        return createDialogBase(new String[]{
                "ห้องเรียน",
                "หลังแนะนำตัวหน้าห้องเสร็จ",
                "ครูให้คุณไปนั่งที่ว่างด้านหลัง",
                "อีกฝ่ายก็นั่งอยู่โต๊ะข้าง ๆ",
                "“บังเอิญอีกแล้ว”",
                "“โลกมันกลมดีเนอะ”",

        }, false);
    }

    // ========================== Dialog Base + Name Tag ==========================
    private JPanel createDialogBase(String[] dialogs, boolean changeScene) {

        int dialogWidth = WIDTH - 100;

        JPanel container = new JPanel(null);
        container.setOpaque(false);
        container.setBounds(50, HEIGHT - 260, dialogWidth, 230);

        // ===== Dialog Box =====
        RoundedPanel dialog = new RoundedPanel(40);
        dialog.setBackground(new Color(244, 169, 193, 220));
        dialog.setBounds(0, 30, dialogWidth, 200);
        dialog.setLayout(null);

        JLabel text = new JLabel(dialogs[0]);
        text.setFont(new Font("Tahoma", Font.PLAIN, 22));
        text.setForeground(Color.WHITE);
        text.setBounds(60, 70, dialogWidth - 200, 40);

        dialog.add(text);

        // ===== Name Tag =====
        RoundedPanel nameBox = new RoundedPanel(25);
        nameBox.setBackground(Color.WHITE);
        nameBox.setBounds(40, 0, 260, 55);
        nameBox.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel name = new JLabel("คิม แจฮยอน");
        name.setFont(new Font("Tahoma", Font.BOLD, 20));
        name.setForeground(new Color(255, 80, 120));

        nameBox.add(name);

        // ===== Day =====
        JLabel day = new JLabel("Day 1");
        day.setFont(new Font("Tahoma", Font.BOLD, 18));
        day.setForeground(Color.WHITE);
        day.setBounds(dialogWidth - 120, 20, 100, 30);

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
    private JLabel createBackground(String path) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        JLabel bg = new JLabel(new ImageIcon(img));
        bg.setBounds(0, 0, WIDTH, HEIGHT);

        return bg;
    }

    // ========================== CHARACTER ==========================
    private JLabel createCharacter() {

        int charW = WIDTH / 90 * 30;
        int charH = HEIGHT - 100;

        ImageIcon icon = new ImageIcon("src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png");
        Image img = icon.getImage().getScaledInstance(charW, charH, Image.SCALE_SMOOTH);

        JLabel character = new JLabel(new ImageIcon(img));

        int x = (WIDTH - charW) / 2;
        int y = HEIGHT - charH - 120;

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