package com.game.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        mainPanel.add(new SceneClass(), "class");

        add(mainPanel);
        setVisible(true);
    }

    // ================= SCENE 1 =================
    class SceneRoom extends JLayeredPane {

        int relationship = 1;

        public SceneRoom() {

            setLayout(null);

            JLabel bg = createBackground("src/main/resources/images/backgrounds/มุมตึก.png");
            add(bg, Integer.valueOf(0));

            addTopUI();

            JLabel character = createCharacter();
            add(character, Integer.valueOf(1));

            JPanel dialog = createDialog();
            add(dialog, Integer.valueOf(2));

            dialog.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(mainPanel, "class");
                }
            });
        }

        private void addTopUI() {

            JLabel heart = new JLabel(new ImageIcon("heart.png"));
            heart.setBounds(40, 30, 50, 50);
            add(heart, Integer.valueOf(2));

            JProgressBar bar = new JProgressBar(0, 5);
            bar.setValue(relationship);
            bar.setBounds(100, 40, 300, 30);
            bar.setStringPainted(true);

            int percent = (relationship * 100) / 5;
            bar.setString(percent + "%");

            add(bar, Integer.valueOf(2));

            JLabel bag = new JLabel(new ImageIcon("bag.png"));
            bag.setBounds(WIDTH - 170, 30, 50, 50);
            add(bag, Integer.valueOf(2));

            JLabel gear = new JLabel(new ImageIcon("gear.png"));
            gear.setBounds(WIDTH - 100, 30, 50, 50);
            add(gear, Integer.valueOf(2));
        }
    }

    // ================= SCENE 2 =================
    class SceneClass extends JLayeredPane {

        int relationship = 1;

        public SceneClass() {

            setLayout(null);

            JLabel bg = createBackground("src/main/resources/images/backgrounds/ห้องเรียน.jpg");
            add(bg, Integer.valueOf(0));

            addTopUI();

            JLabel character = createCharacter();
            add(character, Integer.valueOf(1));

            JPanel dialog = createDialog();
            add(dialog, Integer.valueOf(2));
        }

        private void addTopUI() {

            JLabel heart = new JLabel(new ImageIcon("heart.png"));
            heart.setBounds(40, 30, 50, 50);
            add(heart, Integer.valueOf(2));

            JProgressBar bar = new JProgressBar(0, 5);
            bar.setValue(relationship);
            bar.setBounds(100, 40, 300, 30);
            bar.setStringPainted(true);

            int percent = (relationship * 100) / 5;
            bar.setString(percent + "%");

            add(bar, Integer.valueOf(2));

            JLabel bag = new JLabel(new ImageIcon("bag.png"));
            bag.setBounds(WIDTH - 170, 30, 50, 50);
            add(bag, Integer.valueOf(2));

            JLabel gear = new JLabel(new ImageIcon("gear.png"));
            gear.setBounds(WIDTH - 100, 30, 50, 50);
            add(gear, Integer.valueOf(2));
        }
    }

    // ================= Background =================
    private JLabel createBackground(String path) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        JLabel bg = new JLabel(new ImageIcon(img));
        bg.setBounds(0, 0, WIDTH, HEIGHT);

        return bg;
    }

    // ================= ตัวละคร =================
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

    // ================= Dialog =================
    private JPanel createDialog() {

        JPanel dialog = new JPanel(null);
        dialog.setBackground(new Color(244, 169, 193, 230));

        int dialogW = WIDTH - 200;
        int dialogH = 200;

        dialog.setBounds(100, HEIGHT - dialogH - 40, dialogW, dialogH);

        JLabel name = new JLabel("คิม แจฮยอน");
        name.setBounds(30, 20, 300, 40);
        name.setOpaque(true);
        name.setBackground(Color.WHITE);
        name.setFont(new Font("Tahoma", Font.BOLD, 20));

        JLabel text = new JLabel("“ระวังหน่อย… ตรงนี้คนเดินผ่านเยอะ”");
        text.setBounds(30, 100, 900, 40);
        text.setFont(new Font("Tahoma", Font.PLAIN, 22));

        JLabel day = new JLabel("Day 1");
        day.setBounds(dialogW - 120, 20, 100, 40);
        day.setFont(new Font("Tahoma", Font.BOLD, 20));

        dialog.add(name);
        dialog.add(text);
        dialog.add(day);

        return dialog;
    }

    public static void main(String[] args) {
        new Changescene();
    }
}
