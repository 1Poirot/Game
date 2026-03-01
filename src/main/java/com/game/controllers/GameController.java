package com.game.controllers;

import com.game.models.Player;
import com.game.network.GameClient;
import com.game.systems.audio.AudioSystem;
import com.game.systems.dialogue.DialogueSystemAndChoice;
import com.game.systems.shop.ShopSystem;
import com.game.ui.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GameController {

    // เปลี่ยนจาก Player คนเดียว เป็น List เพื่อรองรับ 3 คน
    private List<Player> players;
    private int currentPlayerIndex = 0; // เก็บว่าตอนนี้ตาใคร (0, 1, 2)

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
    // ดึงข้อมูลผู้เล่นที่กำลังเล่นอยู่ตอนนี้
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // สลับไปตาผู้เล่นคนถัดไป
    public void nextTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0; // วนกลับมาคนที่ 1
        }
        System.out.println("ตอนนี้ตาของ: " + getCurrentPlayer().getName());
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
        panel.setPreferredSize(null);
        panel.setMinimumSize(null);
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // ================== หน้าต่างต่าง ๆ ==================
    public void showMainMenu() {
        lastScene = "MAIN_MENU";
        changeScreen(new MenuGame(this));
    }

    public void showChangescene() {
        if (audioSystem != null)
            audioSystem.stopBGM();
        mainFrame.setVisible(false);
        new Changescene(this);
    }

    public void showGameScene() {
        lastScene = "GAME_SCENE";
        showChangescene();
    }

    public void showShop() {
        changeScreen(new ShopScreen(this));
    }

    public void showSettings() {
        if (!mainFrame.isVisible()) {
            lastScene = "CHANGESCENE";
            mainFrame.setVisible(true);
        } else {
            lastScene = "MAIN_MENU";
        }
        changeScreen(new SettingsScreen(this));
    }

    public void showAudioSettings() {
        changeScreen(new AudioSettingsScreen(this));
        if (audioSystem != null && audioSystem.getCurrentBgmName() != null) {
            audioSystem.playBGM(audioSystem.getCurrentBgmName());
        }
    }

    // เมธอดสำหรับปุ่มย้อนกลับ
    public void backToPreviousScreen() {
        if (lastScene.equals("CHANGESCENE") || lastScene.equals("GAME_SCENE")) {
            showChangescene();
        } else {
            showMainMenu();
        }
    }

    public void showSaveScreen() {
        showSaveScreen(() -> showSettings());
    }

    public void showSaveScreen(Runnable onBack) {
        changeScreen(new SaveScreen(this, onBack));
    }

    // ================== Multiplayer ==================
    /**
     * เปิดหน้า Lobby Dialog ให้กรอก IP + ชื่อ แล้วเชื่อมต่อ GameServer
     * เรียกจาก MenuGame เมื่อกดปุ่ม "เล่นออนไลน์"
     */
    public void showMultiplayer() {
        LobbyDialog.LobbyResult lobbyResult = LobbyDialog.show(mainFrame);
        if (lobbyResult == null)
            return; // ผู้ใช้กด Cancel

        GameClient client = new GameClient(lobbyResult.ip, 9090, lobbyResult.playerName);
        boolean ok = client.connect();
        if (!ok) {
            JOptionPane.showMessageDialog(mainFrame,
                    "เชื่อมต่อ IP: " + lobbyResult.ip + " ไม่สำเร็จ!\nโปรดเช็คว่า Host เปิด GameServer แล้ว",
                    "เชื่อมต่อไม่สำเร็จ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // เปิดหน้าต่าง Multiplayer (เป็น JFrame แยกต่างหาก)
        new MultiplayerScreen(client);
    }

    public void exitGame() {
        System.exit(0);
    }

    // ================== Getters ==================
    // ให้ getPlayer() คืนค่าผู้เล่นคนปัจจุบันเสมอ เพื่อให้หน้า Save/Shop ทำงานถูกคน
    public Player getPlayer() {
        return getCurrentPlayer();
    }

    public List<Player> getAllPlayers() {
        return players;
    }

    public ShopSystem getShopSystem() {
        return shopSystem;
    }

    public AudioSystem getAudioSystem() {
        return audioSystem;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }
}