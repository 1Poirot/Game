package com.game.controllers;

import com.game.models.Player;
import com.game.systems.dialogue.DialogueSystemAndChoice;
import com.game.systems.shop.ShopSystem;
import com.game.ui.*;

import javax.swing.*;
import java.awt.*;

public class GameController {

    private Player player;
    private ShopSystem shopSystem;
    private DialogueSystemAndChoice dialogueSystem;

    private JFrame mainFrame;

    public GameController() {

        // ===== สร้างข้อมูลเกม =====
        this.player = new Player("Hero", 100);
        this.shopSystem = new ShopSystem();
        this.dialogueSystem = new DialogueSystemAndChoice();

        // ===== สร้างหน้าต่างหลัก =====
        mainFrame = new JFrame("Game Shop - Fantasy RPG");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // กำหนดขนาดเริ่มต้น (ไม่ล็อกตายตัว)
        mainFrame.setSize(1280, 720);

        // อนุญาตให้ย่อ-ขยายได้
        mainFrame.setResizable(true);

        // ป้องกันย่อเล็กเกินไป
        mainFrame.setMinimumSize(new Dimension(960, 540));

        // เปิดกลางจอ
        mainFrame.setLocationRelativeTo(null);
    }

    // ================== เริ่มเกม ==================
    public void start() {
        showMainMenu();
        mainFrame.setVisible(true);
    }

    // ================== เปลี่ยนหน้าจอ ==================
    private void changeScreen(JPanel panel) {

        // สำคัญ: ทำให้ panel ขยายเต็มพื้นที่อัตโนมัติ
        panel.setPreferredSize(null);
        panel.setMinimumSize(null);

        mainFrame.setContentPane(panel);

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // ================== หน้าต่างต่าง ๆ ==================

    public void showMainMenu() {
        changeScreen(new MenuGame(this));
    }

    public void showGameScene() {
        com.game.models.Character npc =
                new com.game.models.Character(
                        "Kim Jae-hyun",
                        "src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png"
                );

        changeScreen(new GamePanel(npc, dialogueSystem));
    }

    public void showShop() {
        changeScreen(new ShopScreen(this));
    }

    public void showSettings() {
        changeScreen(new SettingsScreen(this));
    }

    public void showAudioSettings() {
        changeScreen(new AudioSettingsScreen(this));
    }

    public void showSaveScreen() {
        changeScreen(new SaveScreen(this));
    }

    public void exitGame() {
        System.exit(0);
    }

    // ================== Getters ==================

    public Player getPlayer() {
        return player;
    }

    public ShopSystem getShopSystem() {
        return shopSystem;
    }

    public DialogueSystemAndChoice getDialogueSystem() {
        return dialogueSystem;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }
}