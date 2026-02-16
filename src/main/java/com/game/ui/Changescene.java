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
        mainPanel.add(new SceneNext(), "next");

        add(mainPanel);
        setVisible(true);
    }

    // ========================== SCENE 1 ==========================
    class SceneRoom extends JLayeredPane {

        public SceneRoom() {

            setLayout(null);

            JLabel bg = createBackground("src/main/resources/images/backgrounds/มุมตึก.png");
            add(bg, Integer.valueOf(0));

            JLabel character = createCharacter();
            add(character, Integer.valueOf(1));

            add(createTopLeftUI(), Integer.valueOf(5));
            add(createTopRightUI(), Integer.valueOf(5));

            add(createDialogUI1(), Integer.valueOf(10));
        }
    }

    // ========================== SCENE 2 ==========================
    // ========================== SCENE 2 ==========================
class SceneNext extends JLayeredPane {

    public SceneNext() {

        setLayout(null);

        JLabel bg = createBackground("src/main/resources/images/backgrounds/ห้องเรียน.jpg");
        add(bg, Integer.valueOf(0));

        JLabel character = createCharacter();
        add(character, Integer.valueOf(1));

        add(createTopLeftUI(), Integer.valueOf(5));
        add(createTopRightUI(), Integer.valueOf(5));

        add(createDialogUI2(), Integer.valueOf(10)); // 👈 ใช้อันนี้
    }
}


    // ========================== TOP LEFT ==========================
    private JPanel createTopLeftUI() {

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(30, 20, 420, 70);

        JLabel heart = new JLabel(new ImageIcon("heart.png"));
        heart.setBounds(0, 10, 50, 50);
        panel.add(heart);

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

    // ========================== DIALOG SCENE 1 ==========================
    private JPanel createDialogUI1() {

        return createDialogBase(new String[]{
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

                "“นักเรียนใหม่…?”",
                "น้ำเสียงไม่ได้เย็นชา แต่ก็ไม่ได้เป็นมิตร",

                "วินาทีนั้นเอง คุณยังไม่รู้เลยว่า",
                "การชนกันเพียงครั้งเดียว",
                "จะเปลี่ยน 7 วันแรกของคุณไปตลอดกาล"
        }, true);
    }
// ========================== DIALOG SCENE 2 ==========================
private JPanel createDialogUI2() {

    return createDialogBase(new String[]{

            "🏫 ห้องเรียน",
            "หลังแนะนำตัวหน้าห้องเสร็จ",
            "ครูให้คุณไปนั่งที่ว่างด้านหลัง",

            "และเมื่อคุณเดินไปถึงโต๊ะเรียน",
            "อีกฝ่ายก็นั่งอยู่โต๊ะข้าง ๆ",

            "คุณหยุดชะงักเล็กน้อย",
            "โลกมันกลมเกินไปหรือเปล่า…",

            "อีกฝ่ายเหลือบมองคุณนิดเดียว",
            "“บังเอิญอีกแล้ว”",

            "คุณหัวเราะ"

    }, false); // false = ไม่ต้องเปลี่ยนฉากต่อ
}

    // ========================== DIALOG BASE ==========================
    private JPanel createDialogBase(String[] dialogs, boolean changeScene) {

        int dialogWidth = WIDTH - 100;

        JPanel container = new JPanel(null);
        container.setOpaque(false);
        container.setBounds(50, HEIGHT - 260, dialogWidth, 230);

        RoundedPanel dialog = new RoundedPanel(40);
        dialog.setBackground(new Color(244, 169, 193, 220));
        dialog.setBounds(0, 30, dialogWidth, 200);
        dialog.setLayout(null);

        RoundedPanel nameBox = new RoundedPanel(25);
        nameBox.setBackground(Color.WHITE);
        nameBox.setBounds(40, 0, 220, 50);
        nameBox.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel name = new JLabel("คิม แจฮยอน");
        name.setFont(new Font("Tahoma", Font.BOLD, 18));
        nameBox.add(name);

        JLabel text = new JLabel(dialogs[0]);
        text.setFont(new Font("Tahoma", Font.PLAIN, 22));
        text.setBounds(60, 70, dialogWidth - 200, 40);

        JLabel day = new JLabel("Day 1");
        day.setFont(new Font("Tahoma", Font.BOLD, 18));
        day.setBounds(dialogWidth - 120, 20, 100, 30);

        JLabel sparkle = new JLabel("✨");
        sparkle.setFont(new Font("Dialog", Font.PLAIN, 28));
        sparkle.setBounds(dialogWidth - 70, 140, 40, 40);

        dialog.add(text);
        dialog.add(day);
        dialog.add(sparkle);

        container.add(nameBox);
        container.add(dialog);

        final int[] index = {0};

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
