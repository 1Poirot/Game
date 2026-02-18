package com.game.ui;

import com.game.models.Character;
import com.game.systems.dialogue.DialogueSystemAndChoice;
import com.game.utils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main game panel for character interaction
 */
public class GamePanel extends JPanel {

    private Image characterImage;
    private Font thaiFont;

    private Character character;
    private DialogueSystemAndChoice dialogueSystem;

    private JButton btn1, btn2, btn3;

    public GamePanel(Character character, DialogueSystemAndChoice dialogueSystem) { {
        this.character = character;
        this.dialogueSystem = dialogueSystem;

        setPreferredSize(new Dimension(600, 500));
        setLayout(null);
        setBackground(Color.WHITE);

        // Load character image
        characterImage = new ImageIcon(character.getImagePath()).getImage();

        // Load Thai font
        thaiFont = FontUtils.getThaiFont(24);

        // Create buttons
        btn1 = ButtonFactory.createStyledButton("โสดไหมครับ");
        btn2 = ButtonFactory.createStyledButton("ถามชื่อ");
        btn3 = ButtonFactory.createStyledButton("เดินจากไป");

        // Set button positions
        btn1.setBounds(80, 410, 140, 40);
        btn2.setBounds(230, 410, 140, 40);
        btn3.setBounds(380, 410, 140, 40);

        // Add button listeners
        btn1.addActionListener(e -> {
            dialogueSystem.setCurrentDialogue("โสดค่ะ!");
            repaint();
        });

        btn2.addActionListener(e -> {
            dialogueSystem.setCurrentDialogue("ฉันชื่อ อิอิ ค่ะ");
            repaint();
        });

        btn3.addActionListener(e -> {
            dialogueSystem.hideDialogue();
            btn1.setVisible(false);
            btn2.setVisible(false);
            btn3.setVisible(false);
            repaint();
        });

        // Add buttons to panel
        add(btn1);
        add(btn2);
        add(btn3);

        // Initially hide buttons
        btn1.setVisible(false);
        btn2.setVisible(false);
        btn3.setVisible(false);

        // Add mouse listener for character interaction
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (character.getBounds().contains(e.getPoint())) {
                    dialogueSystem.setCurrentDialogue("สวัสดี! มีอะไรให้ช่วยไหม?");
                    btn1.setVisible(true);
                    btn2.setVisible(true);
                    btn3.setVisible(true);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw character
        g.drawImage(characterImage, character.getX(), character.getY(),
                character.getWidth(), character.getHeight(), this);

        // Draw instruction text
        g.setFont(thaiFont);
        g.setColor(Color.BLACK);
        g.drawString("คลิกที่ตัวละคร", 220, 40);

        // Draw dialogue box if visible
        if (dialogueSystem.isDialogueVisible()) {
            // Draw dialogue box background
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRoundRect(50, 340, 500, 70, 20, 20);

            // Draw dialogue text
            g.setColor(Color.WHITE);
            g.setFont(thaiFont);
            g.drawString(dialogueSystem.getCurrentDialogue(), 70, 380);
        }
    }
}
