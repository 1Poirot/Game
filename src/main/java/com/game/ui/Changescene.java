package com.game.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
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

    // ========================= SCENE ROOM =========================
    class SceneRoom extends JLayeredPane {

        private JLabel dialogPanel;
        private Map<String, SceneData> scenes = new HashMap<>();
        private String current = "S1";

        public SceneRoom() {

            setLayout(null);

            JLabel bg = createBackground("src/main/resources/images/backgrounds/มุมตึก.png");
            add(bg, Integer.valueOf(0));

            JLabel character = createCharacter();
            add(character, Integer.valueOf(1));

            dialogPanel = createDialogPanel();
            add(dialogPanel, Integer.valueOf(100));
            setComponentZOrder(dialogPanel, 0);

            dialogPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    nextScene();
                }
            });

            buildStory();
            showScene("S1");
        }

        private void buildStory() {

            scenes.put("S1", new SceneData(
                    "คิม แจฮยอน",
                    "Day 1",
                    "“ระวังหน่อย… ตรงนี้คนเดินผ่านเยอะ”",
                    "S2"
            ));

            scenes.put("S2", new SceneData(
                    "คุณ",
                    "Day 1",
                    "“ขอโทษนะ เราเดินไม่ทันระวังเอง”",
                    "END"
            ));
        }

        private void showScene(String id) {

            current = id;

            SceneData s = scenes.get(id);
            if (s == null) return;

            dialogPanel.setText(
                    "<html><b>" + s.name + "</b><br>" +
                            s.day + "<br><br>" +
                            s.text + "</html>"
            );
        }

        private void nextScene() {

            SceneData s = scenes.get(current);
            if (s == null) return;

            if (s.next.equals("END")) {
                cardLayout.show(mainPanel, "class"); // เปลี่ยนฉาก
                return;
            }

            showScene(s.next);
        }
    }

    // ========================= SCENE CLASS =========================
    class SceneClass extends JLayeredPane {

        public SceneClass() {

            setLayout(null);

            JLabel bg = createBackground("src/main/resources/images/backgrounds/ห้องเรียน.jpg");
            add(bg, Integer.valueOf(0));

            JLabel character = createCharacter();
            add(character, Integer.valueOf(1));

            JLabel label = new JLabel("ห้องเรียน");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Tahoma", Font.BOLD, 40));
            label.setBounds(100, 100, 400, 60);
            add(label, Integer.valueOf(2));
        }
    }

    // ========================= DATA =========================
    class SceneData {

        String name;
        String day;
        String text;
        String next;

        SceneData(String name, String day, String text, String next) {
            this.name = name;
            this.day = day;
            this.text = text;
            this.next = next;
        }
    }

    // ========================= DIALOG =========================
    private JLabel createDialogPanel() {

        JLabel dialog = new JLabel();
        dialog.setOpaque(true);
        dialog.setBackground(new Color(244, 169, 193, 230));
        dialog.setFont(new Font("Tahoma", Font.PLAIN, 22));

        int dialogW = WIDTH - 200;
        int dialogH = 200;

        dialog.setBounds(100, HEIGHT - dialogH - 40, dialogW, dialogH);

        dialog.setVerticalAlignment(SwingConstants.TOP);
        dialog.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return dialog;
    }

    // ========================= BACKGROUND =========================
    private JLabel createBackground(String path) {

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        JLabel bg = new JLabel(new ImageIcon(img));
        bg.setBounds(0, 0, WIDTH, HEIGHT);

        return bg;
    }

    // ========================= CHARACTER =========================
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Changescene());
    }
}
