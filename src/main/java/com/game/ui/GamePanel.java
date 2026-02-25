package com.game.ui;

import com.game.models.Character;
import com.game.systems.dialogue.DialogueSystemAndChoice; 
import com.game.utils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    private Image characterImage;
    private Font thaiFont;

    private Character character;
    private DialogueSystemAndChoice dialogueSystem;

    private JButton btn1, btn2, btn3;

    public GamePanel(Character character, DialogueSystemAndChoice dialogueSystem) {
        this.character = character;
        this.dialogueSystem = dialogueSystem;

        setPreferredSize(new Dimension(600, 500));
        setLayout(null);
        setBackground(Color.WHITE);

        // Load character image
        characterImage = new ImageIcon(character.getImagePath()).getImage();
        thaiFont = FontUtils.getThaiFont(24);

        // Create buttons
        btn1 = new JButton("โสดไหมครับ");
        btn2 = new JButton("ถามชื่อ");
        btn3 = new JButton("เดินจากไป");

        btn1.setFont(thaiFont);
        btn2.setFont(thaiFont);
        btn3.setFont(thaiFont);

        btn1.setBounds(80, 410, 140, 40);
        btn2.setBounds(230, 410, 140, 40);
        btn3.setBounds(380, 410, 140, 40);

        // --- แก้ไขจุดนี้: เรียกใช้ SHOW_SCENE ของเพื่อนแทน ---
        btn1.addActionListener(e -> {
            // สมมติว่า S1 คือประโยคเริ่มแรกของเพื่อน
            dialogueSystem.SHOW_SCENE("S1"); 
            repaint();
        });

        btn2.addActionListener(e -> {
            // ถ้าเพื่อนไม่มีเมธอดรับ String ตรงๆ เราต้องให้เพื่อนเพิ่ม 
            // หรือสร้าง Scene ID ใหม่ใน BUILD_STORY ของเพื่อนครับ
            dialogueSystem.SHOW_SCENE("S2");
            repaint();
        });

        btn3.addActionListener(e -> {
            // ซ่อนปุ่มเมื่อเดินจากไป
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
                if (character.getBounds().contains(e.getPoint())) {
                    // เมื่อคลิกตัวละคร ให้เริ่ม Scene แรกของเพื่อน
                    dialogueSystem.SHOW_SCENE("S1");
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
        // วาดตัวละคร
        g.drawImage(characterImage, character.getX(), character.getY(),
                character.getWidth(), character.getHeight(), this);

        g.setFont(thaiFont);
        g.setColor(Color.BLACK);
        g.drawString("คลิกที่ตัวละคร", 220, 40);
        
        // หมายเหตุ: การวาด Dialogue Box จะไปอยู่ที่คลาส DIALOGPANEL 
        // ภายในไฟล์ DialogueSystemAndChoice ของเพื่อนแล้วครับ
    }
}