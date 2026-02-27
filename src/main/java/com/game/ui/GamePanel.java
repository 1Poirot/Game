package com.game.ui;

import com.game.controllers.GameController; // controller reference for navigation
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private GameController controller;
    private com.game.models.Character npc;
    private com.game.systems.dialogue.DialogueSystemAndChoice dialogueSystem;

    // ปรับ Constructor ให้รับ GameController ก่อน พร้อม NPC และ dialogue system
    public GamePanel(GameController controller,
            com.game.models.Character npc,
            com.game.systems.dialogue.DialogueSystemAndChoice dialogueSystem) {
        this.controller = controller;
        this.npc = npc;
        this.dialogueSystem = dialogueSystem;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 240, 245)); // สีชมพูอ่อนๆ ให้เข้ากับธีม

        // ====== ส่วนหัว: แสดงว่าตอนนี้ตาใคร (สำคัญมากสำหรับเล่น 3 คน) ======
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);

        JLabel turnLabel = new JLabel("รอบของ: " + controller.getCurrentPlayer().getName(), SwingConstants.CENTER);
        turnLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        turnLabel.setForeground(new Color(255, 20, 147)); // สีชมพูเข้ม

        JLabel npcLabel = new JLabel("กำลังคุยกับ: " + npc.getName(), SwingConstants.CENTER);
        npcLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

        headerPanel.add(turnLabel);
        headerPanel.add(npcLabel);
        add(headerPanel, BorderLayout.NORTH);

        // ====== ส่วนกลาง: ปุ่มคุย และ ปุ่มจบตา ======
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // 1. ปุ่มคุยกับ NPC
        JButton talkButton = createCustomButton("คุยกับ " + npc.getName(), new Color(100, 149, 237));
        talkButton.addActionListener(e -> {
            if (dialogueSystem != null) {
                dialogueSystem.CREATEANDSHOWGUI();
            }
        });

        // 2. ปุ่มจบตา (เพื่อสลับคนเล่น)
        JButton endTurnButton = createCustomButton("จบตา (สลับผู้เล่น)", new Color(50, 205, 50));
        endTurnButton.addActionListener(e -> {
            controller.nextTurn(); // สั่ง Controller ให้สลับคน
            // แจ้งเตือนคนถัดไป
            JOptionPane.showMessageDialog(this, "จบตาแล้ว! ต่อไปตาของ " + controller.getCurrentPlayer().getName());

            // อัปเดตหน้าจอใหม่เพื่อโชว์ชื่อคนปัจจุบัน
            controller.showGameScene();
        });

        gbc.gridy = 0;
        center.add(talkButton, gbc);
        gbc.gridy = 1;
        center.add(endTurnButton, gbc);

        add(center, BorderLayout.CENTER);

        // ====== ปุ่มตั้งค่า (เฟือง) มุมขวาล่าง ======
        JButton settingsBtn = new JButton("⚙ ตั้งค่า");
        settingsBtn.addActionListener(e -> controller.showSettings());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(settingsBtn);
        add(footer, BorderLayout.SOUTH);
    }

    // Helper สร้างปุ่มสวยๆ
    private JButton createCustomButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 22));
        btn.setPreferredSize(new Dimension(300, 80));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}