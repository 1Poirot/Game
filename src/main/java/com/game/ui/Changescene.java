package com.game.ui;

import java.awt.*;
import javax.swing.*;

public class Changescene extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int WIDTH = screen.width;
    int HEIGHT = screen.height;

    public Changescene() {

        setTitle("Love Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.add(new SceneRoom(), "room");

        add(mainPanel);
        setVisible(true);
    }

    // ========================== SCENE ==========================
    class SceneRoom extends JLayeredPane {

        public SceneRoom() {

            setLayout(null);

            // Background
            JLabel bg = createBackground("src/main/resources/images/backgrounds/มุมตึก.png");
            add(bg, Integer.valueOf(0));

            // Character
            JLabel character = createCharacter();
            add(character, Integer.valueOf(1));

            // Top UI
            add(createTopLeftUI(), Integer.valueOf(5));
            add(createTopRightUI(), Integer.valueOf(5));

            // Dialog
            add(createDialogUI(), Integer.valueOf(10));
        }
    }

    // ========================== TOP LEFT ==========================
    private JPanel createTopLeftUI() {

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(30, 20, 420, 70);

        // Heart icon
        JLabel heart = new JLabel(new ImageIcon("heart.png"));
        heart.setBounds(0, 10, 50, 50);
        panel.add(heart);

        // Progress background
        RoundedPanel barBg = new RoundedPanel(30);
        barBg.setBackground(new Color(255, 230, 235));
        barBg.setBounds(50, 15, 350, 40);
        barBg.setLayout(null);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(20);
        bar.setString("20%");
        bar.setStringPainted(true);
        bar.setBounds(10, 5, 330, 30);

        barBg.add(bar);
        panel.add(barBg);

        return panel;
    }

    // ========================== TOP RIGHT ==========================
    private JPanel createTopRightUI() {

        JPanel panel = new RoundedPanel(30);
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 230, 220));

        panel.setBounds(WIDTH - 230, 20, 180, 70);

        JLabel bag = new JLabel(new ImageIcon("bag.png"));
        bag.setBounds(25, 10, 50, 50);

        JLabel gear = new JLabel(new ImageIcon("gear.png"));
        gear.setBounds(100, 10, 50, 50);

        panel.add(bag);
        panel.add(gear);

        return panel;
    }

    // ========================== DIALOG ==========================
    private JPanel createDialogUI() {

        JPanel container = new JPanel(null);
        container.setOpaque(false);
        container.setBounds(50, HEIGHT - 260, WIDTH - 100, 230);

        // Name box
        RoundedPanel nameBox = new RoundedPanel(25);
        nameBox.setBackground(Color.WHITE);
        nameBox.setBounds(20, 0, 200, 50);

        JLabel name = new JLabel("คิม แจฮยอน");
        name.setFont(new Font("Tahoma", Font.BOLD, 18));
        nameBox.add(name);

        // Dialog box
        RoundedPanel dialog = new RoundedPanel(40);
        dialog.setBackground(new Color(244, 169, 193, 220));
        dialog.setBounds(0, 30, WIDTH - 100, 200);
        dialog.setLayout(null);

        JLabel text = new JLabel("“ระวังหน่อย… ตรงนี้คนเดินผ่านเยอะ”");
        text.setFont(new Font("Tahoma", Font.PLAIN, 22));
        text.setBounds(40, 40, 900, 40);

        JLabel day = new JLabel("Day 1");
        day.setFont(new Font("Tahoma", Font.BOLD, 18));
        day.setBounds(dialog.getWidth() - 100, 20, 80, 30);

        JLabel sparkle = new JLabel("✨");
        sparkle.setFont(new Font("Dialog", Font.PLAIN, 28));
        sparkle.setBounds(dialog.getWidth() - 60, 150, 40, 40);

        dialog.add(text);
        dialog.add(day);
        dialog.add(sparkle);

        container.add(nameBox);
        container.add(dialog);

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

        int charW = WIDTH / 4;
        int charH = HEIGHT - 300;

        ImageIcon icon = new ImageIcon("src/main/resources/images/backgrounds/ผู้ชาย ตัวเอก.png");
        Image img = icon.getImage().getScaledInstance(charW, charH, Image.SCALE_SMOOTH);

        JLabel character = new JLabel(new ImageIcon(img));

        int x = (WIDTH - charW) / 2;
        int y = HEIGHT - charH - 120;

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Changescene());
    }
}
