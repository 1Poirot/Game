import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CharacterInteractionGame extends JPanel {

    private Image characterImage;
    private Font thaiFont;

    private int charX = 200;
    private int charY = 60;
    private int charWidth = 200;
    private int charHeight = 300;

    private boolean showDialogue = false;
    private String dialogueText = "สวัสดี! มีอะไรให้ช่วยไหม?";

    private JButton btn1, btn2, btn3;

    public CharacterInteractionGame() {

        setPreferredSize(new Dimension(600, 500));
        setLayout(null);
        setBackground(Color.WHITE);

        characterImage = new ImageIcon("1.png").getImage();

        thaiFont = getThaiFont(24);

        btn1 = createButton("โสดไหมครับ");
        btn2 = createButton("ถามชื่อ");
        btn3 = createButton("เดินจากไป");

        btn1.setBounds(80, 410, 140, 40);
        btn2.setBounds(230, 410, 140, 40);
        btn3.setBounds(380, 410, 140, 40);

        btn1.addActionListener(e -> {
            dialogueText = "โสดค่ะ!";
            repaint();
        });

        btn2.addActionListener(e -> {
            dialogueText = "ฉันชื่อ อิอิ ค่ะ";
            repaint();
        });

        btn3.addActionListener(e -> {
            showDialogue = false;
            btn1.setVisible(false);
            btn2.setVisible(false);
            btn3.setVisible(false);
            repaint();
        });

        add(btn1);
        add(btn2);
        add(btn3);

        btn1.setVisible(false);
        btn2.setVisible(false);
        btn3.setVisible(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Rectangle characterArea =
                        new Rectangle(charX, charY, charWidth, charHeight);

                if (characterArea.contains(e.getPoint())) {
                    showDialogue = true;
                    dialogueText = "สวัสดี! มีอะไรให้ช่วยไหม?";
                    btn1.setVisible(true);
                    btn2.setVisible(true);
                    btn3.setVisible(true);
                    repaint();
                }
            }
        });
    }

    private Font getThaiFont(int size) {

        String[] preferredFonts = {
                "Leelawadee UI",
                "TH Sarabun New",
                "Angsana New",
                "Tahoma",
                "Cordia New"
        };

        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (String fontName : preferredFonts) {
            for (String available : ge.getAvailableFontFamilyNames()) {
                if (available.equalsIgnoreCase(fontName)) {
                    return new Font(fontName, Font.PLAIN, size);
                }
            }
        }

        return new Font("SansSerif", Font.PLAIN, size);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(thaiFont);
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(characterImage, charX, charY,
                charWidth, charHeight, this);

        g.setFont(thaiFont);
        g.setColor(Color.BLACK);
        g.drawString("คลิกที่ตัวละคร", 220, 40);

        if (showDialogue) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRoundRect(50, 340, 500, 70, 20, 20);

            g.setColor(Color.WHITE);
            g.setFont(thaiFont);
            g.drawString(dialogueText, 70, 380);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("2D Character Interaction Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CharacterInteractionGame game =
                new CharacterInteractionGame();

        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
