package com.game.controllers;

import com.game.models.Player;
import com.game.systems.audio.AudioSystem;
import com.game.systems.dialogue.DialogueSystemAndChoice;
import com.game.systems.shop.ShopSystem;
import com.game.ui.*;
import java.awt.*;
import javax.swing.*;

public class GameController {

    private Player player;
    private ShopSystem shopSystem;
    private DialogueSystemAndChoice dialogueSystem;
    private AudioSystem audioSystem;

    private JFrame mainFrame;

    public GameController() {
        // ===== สร้างข้อมูลเกม =====
        this.player = new Player("Hero", 100);
        this.shopSystem = new ShopSystem();
        this.dialogueSystem = new DialogueSystemAndChoice();

        // ===== สร้างระบบเสียง =====
        this.audioSystem = new AudioSystem();

        // ===== สร้างหน้าต่างหลัก =====
        mainFrame = new JFrame("Game Shop - Fantasy RPG");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setResizable(true);
        mainFrame.setMinimumSize(new Dimension(960, 540));
        mainFrame.setLocationRelativeTo(null);
    }

    // ================== เริ่มเกม ==================
    public void start() {
        showMainMenu();
        mainFrame.setVisible(true);

        // ✅ วิธีแก้แบบถวายหัว: ใช้ InvokeLater เพื่อสั่งให้เพลงเล่น "หลังจาก"
        // หน้าต่างโผล่ขึ้นมาสมบูรณ์แล้ว
        SwingUtilities.invokeLater(() -> {
            if (audioSystem != null) {
                audioSystem.playBGM("audiotest.wav");
            }
        });
    }

    // ================== เปลี่ยนหน้าจอ ==================
    private void changeScreen(JPanel panel) {
        panel.setPreferredSize(null);
        panel.setMinimumSize(null);
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // ================== หน้าต่างต่าง ๆ (โค้ดเดิมอยู่ครบ + แทรกของใหม่)
    // ==================

    public void showMainMenu() {
        changeScreen(new MenuGame(this));
    }

    // ✅ แทรกเมธอดนี้: เพื่อเรียกหน้า Changescene (JFrame) ที่คุณส่งมา
    public void showChangescene() {
        // หยุดเพลงเมนูก่อนเปลี่ยนฉาก
        if (audioSystem != null) {
            audioSystem.stopBGM();
        }

        // ซ่อนหน้าต่างเมนูหลัก
        mainFrame.setVisible(false);

        // เปิดหน้าต่างเนื้อเรื่อง (ส่ง controller
        // ไปด้วยเพื่อให้หน้าใหม่ใช้ระบบเสียงเดิมได้)
        new Changescene(this);
    }

    public void showGameScene() {
<<<<<<< HEAD
        // *** โค้ดเดิมที่คุณต้องการ (กู้กลับมาให้แล้ว) ***
        com.game.models.Character npc = new com.game.models.Character(
                "Kim Jae-hyun",
                "src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png");

        changeScreen(new GamePanel(npc, dialogueSystem));
=======
        // Changescene เป็น JFrame แยกต่างหาก จึงต้องเปิดเป็นหน้าต่าง ไม่ใช่ใส่เป็น JPanel
        showChangescene();
>>>>>>> f303d15 (Add new UI item image and initial save slot data)
    }

    public void showShop() {
        changeScreen(new ShopScreen(this));
    }

    public void showSettings() {
        changeScreen(new SettingsScreen(this));
    }

    public void showAudioSettings() {
        changeScreen(new AudioSettingsScreen(this));

        // ✅ ระบบจะเช็คเอง: ถ้าเพลงปัจจุบันคือ audiotest2.wav (หน้าเนื้อเรื่อง)
        // แล้วเรากดตั้งค่า มันก็จะเล่น audiotest2.wav ต่อไปยาวๆ ไม่กระตุกครับ
        // แต่ถ้ามาจากหน้าเมนู (audiotest.wav) มันก็จะเล่นเพลงเมนูต่อไป
        if (audioSystem != null && audioSystem.getCurrentBgmName() != null) {
            audioSystem.playBGM(audioSystem.getCurrentBgmName());
        }
    }

    public void showSaveScreen() {
        showSaveScreen(() -> showSettings());
    }

    public void showSaveScreen(Runnable onBack) {
        changeScreen(new SaveScreen(this, onBack));
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

    public AudioSystem getAudioSystem() {
        return audioSystem;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }
}
