package com.game.controllers;

import com.game.models.Player;
import com.game.systems.audio.AudioSystem;
import com.game.systems.dialogue.DialogueSystemAndChoice;
import com.game.systems.shop.ShopSystem;
import com.game.ui.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GameController {

    private List<Player> players;
    private int currentPlayerIndex = 0;

    private ShopSystem shopSystem;
    private DialogueSystemAndChoice dialogueSystem;
    private AudioSystem audioSystem;
    private String lastScene = "MAIN_MENU";

    private JFrame mainFrame;

    public GameController() {
        // ===== สร้างข้อมูลผู้เล่น 3 คน แข่งจีบคนเดียวกัน =====
        this.players = new ArrayList<>();
        this.players.add(new Player("Player 1", 100));
        this.players.add(new Player("Player 2", 100));
        this.players.add(new Player("Player 3", 100));

        this.shopSystem = new ShopSystem();
        this.dialogueSystem = new DialogueSystemAndChoice();
        this.audioSystem = new AudioSystem();

        // ===== สร้างหน้าต่างหลักหน้าต่างเดียว (Single Frame) =====
        mainFrame = new JFrame("Love Game - 3 Players Rivalry");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setResizable(true);
        mainFrame.setMinimumSize(new Dimension(960, 540));
        mainFrame.setLocationRelativeTo(null);
    }

    // ================== ระบบจัดการ Turn ==================

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0; // วนกลับมาคนที่ 1
        }
        System.out.println("Turn Switched to: " + getCurrentPlayer().getName());
    }

    // ================== เริ่มเกม ==================

    public void start() {
        showMainMenu();
        mainFrame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            if (audioSystem != null) {
                audioSystem.playBGM("audiotest.wav");
            }
        });
    }

    // ================== เปลี่ยนหน้าจอ (รับเฉพาะ JPanel) ==================

    private void changeScreen(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // ================== หน้าต่างต่าง ๆ ==================

    public void showMainMenu() {
        lastScene = "MAIN_MENU";
        changeScreen(new MenuGame(this));
    }

    public void showGameScene() {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        // *** โค้ดเดิมที่คุณต้องการ (กู้กลับมาให้แล้ว) ***
=======
        lastScene = "GAME_SCENE";
        // เรียกหน้า GamePanel (JPanel) ที่มีตัวละคร คิม แจฮยอน
>>>>>>> 279179a (Refactor GameController and UI components for multi-player support; remove Changescene class; enhance MenuGame layout and button functionality; implement GamePanel for player interactions.)
        com.game.models.Character npc = new com.game.models.Character(
                "Kim Jae-hyun",
                "src/main/resources/images/Characters/ผู้ชาย ตัวเอก.png");

<<<<<<< HEAD
        changeScreen(new GamePanel(npc, dialogueSystem));
=======
        // Changescene เป็น JFrame แยกต่างหาก จึงต้องเปิดเป็นหน้าต่าง ไม่ใช่ใส่เป็น JPanel
=======
>>>>>>> 48e7dd8 (11)
        showChangescene();
>>>>>>> f303d15 (Add new UI item image and initial save slot data)
=======
        changeScreen(new com.game.ui.GamePanel(this, npc, dialogueSystem));
>>>>>>> 279179a (Refactor GameController and UI components for multi-player support; remove Changescene class; enhance MenuGame layout and button functionality; implement GamePanel for player interactions.)
    }

    public void showShop() {
        changeScreen(new ShopScreen(this));
    }

    public void showSettings() {
        changeScreen(new SettingsScreen(this));
    }

    public void showAudioSettings() {
        changeScreen(new AudioSettingsScreen(this));
        // ✅ ป้องกันตัวแดง: เรียกชื่อเพลงล่าสุดมาเล่นต่อในหน้าตั้งค่า
        if (audioSystem != null && audioSystem.getCurrentBgmName() != null) {
            audioSystem.playBGM(audioSystem.getCurrentBgmName());
        }
    }

    // ✅ เมธอดสำหรับปุ่มย้อนกลับ (แก้ Error: undefined backToPreviousScreen)
    public void backToPreviousScreen() {
        if (lastScene.equals("GAME_SCENE")) {
            showGameScene();
        } else {
            showMainMenu();
        }
    }

    public void showSaveScreen() {
<<<<<<< HEAD
        changeScreen(new SaveScreen(this));
=======
        // ✅ แก้ Error: Constructor mismatch โดยการส่ง Runnable (Callback) เข้าไป
        showSaveScreen(() -> showSettings());
    }

    public void showSaveScreen(Runnable onBack) {
        changeScreen(new SaveScreen(this, onBack));
>>>>>>> 279179a (Refactor GameController and UI components for multi-player support; remove Changescene class; enhance MenuGame layout and button functionality; implement GamePanel for player interactions.)
    }

    public void exitGame() {
        System.exit(0);
    }

    // ================== Getters ==================

    // คืนค่าผู้เล่นคนปัจจุบันที่กำลังอยู่ใน Turn
    public Player getPlayer() {
        return getCurrentPlayer();
    }

    public List<Player> getAllPlayers() {
        return players;
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