package com.game.controllers;

import com.game.models.Player;
import com.game.network.GameClient;
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
        this.player = new Player("Hero", 100);
        this.shopSystem = new ShopSystem();
        this.dialogueSystem = new DialogueSystemAndChoice();
        this.audioSystem = new AudioSystem();

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

    // ================== หน้าต่างต่าง ๆ ==================

    public void showMainMenu() {
        changeScreen(new MenuGame(this));
    }

    public void showChangescene() {
        if (audioSystem != null)
            audioSystem.stopBGM();
        mainFrame.setVisible(false);
        new Changescene(this);
    }

    public void showGameScene() {
        showChangescene();
    }

    public void showShop() {
        changeScreen(new ShopScreen(this));
    }

    public void showSettings() {
        changeScreen(new SettingsScreen(this));
    }

    public void showAudioSettings() {
        changeScreen(new AudioSettingsScreen(this));
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
