package com.game.controllers;

import com.game.models.Player;
import com.game.systems.shop.ShopSystem;
import com.game.systems.dialogue.DialogueSystemAndChoice; // เพิ่ม Import ระบบไดอะล็อก
import com.game.ui.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.game.models.Character;

public class GameController {
    private Player player;
    private ShopSystem shopSystem;
    private JFrame mainFrame;
    private DialogueSystemAndChoice dialogueSystem; // ถืออ็อบเจกต์ระบบพูดคุยไว้

    public GameController() {
        // สร้างข้อมูลผู้เล่นเริ่มต้น
        this.player = new Player("Hero", 100);
        this.shopSystem = new ShopSystem();

        // สร้างระบบไดอะล็อกเตรียมไว้
        this.dialogueSystem = new DialogueSystemAndChoice();

        mainFrame = new JFrame("Game Shop - Fantasy RPG");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- ตั้งขนาดหน้าจอ 1920 x 1080 ---
        mainFrame.setSize(1920, 1080);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
    }

    public void start() {
        // เริ่มต้นด้วยหน้าเมนูหลัก
        showMainMenu();
        mainFrame.setVisible(true);
    }

    /**
     * เมธอดกลางสำหรับเปลี่ยนหน้าจอ
     */
    private void changeScreen(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // --- หน้าแรกของเกม ---
    public void showMainMenu() {
        changeScreen(new MenuGame(this));
    }

    public void showGameScene() {
        com.game.models.Character npc = new com.game.models.Character("Kim Jae-hyun", "assets/npc.png");
        changeScreen(new GamePanel(npc, dialogueSystem)); // หายแดงแน่นอน
    }

    // --- หน้าร้านค้า ---
    public void showShop() {
        changeScreen(new ShopScreen(this));
    }

    // --- หน้าตั้งค่า ---
    public void showSettings() {
        changeScreen(new SettingsScreen(this));
    }

    // --- หน้าตั้งค่าเสียง ---
    public void showAudioSettings() {
        changeScreen(new AudioSettingsScreen(this));
    }

    // --- หน้า Save & Load (5 Slots บันทึกลงเครื่อง) ---
    public void showSaveScreen() {
        // หน้านี้จะดึงข้อมูลจากไฟล์ resources/data/save/ มาโชว์อัตโนมัติ
        changeScreen(new SaveScreen(this));
    }

    public void exitGame() {
        System.exit(0);
    }

    // --- Getters ---
    public Player getPlayer() {
        return player;
    }

    public ShopSystem getShopSystem() {
        return shopSystem;
    }

    public DialogueSystemAndChoice getDialogueSystem() {
        return dialogueSystem;
    }
}