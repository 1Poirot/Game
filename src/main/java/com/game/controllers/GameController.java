package com.game.controllers;

import com.game.models.Player;
import com.game.multi.dating.MultiDatingSound; // ✅ สำหรับจัดการเสียงระบบใหม่
import com.game.network.GameClient;
import com.game.systems.audio.AudioSystem;
import com.game.systems.shop.ShopSystem;
import com.game.systems.choice.Day1;
import com.game.ui.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GameController {

    private List<Player> players;
    private int currentPlayerIndex = 0;
    private Player player;

    private ShopSystem shopSystem;
    private AudioSystem audioSystem;
    private String lastScene = "MAIN_MENU";

    private JFrame mainFrame;

    // ✅ ป้องกันการเปิดหน้าจอ Multiplayer ซ้อนกัน
    private boolean isConnecting = false;

    public GameController() {
        this.player = new Player("Hero", 100);
        this.players = new ArrayList<>();
        this.players.add(this.player);
        this.players.add(new Player("Player 2", 100));
        this.players.add(new Player("Player 3", 100));

        this.shopSystem = new ShopSystem();
        this.audioSystem = new AudioSystem();

        applyThaiFontGlobal();

        mainFrame = new JFrame("Love Game - Multiplayer Rivalry");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setResizable(true);
        mainFrame.setMinimumSize(new Dimension(960, 540));
        mainFrame.setLocationRelativeTo(null);
    }

    // ================== ระบบจัดการ Turn ==================
    public Player getCurrentPlayer() {
        if (players == null || players.isEmpty())
            return player;
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        System.out.println("ตอนนี้ตาของ: " + getCurrentPlayer().getName());
    }

    // ================== เริ่มเกม ==================
    public void start() {
        showMainMenu();
        mainFrame.setVisible(true);
<<<<<<< HEAD

        SwingUtilities.invokeLater(() -> {
    if (audioSystem != null) {
        audioSystem.playBGM("Dream.wav");
    }
});
=======
>>>>>>> c9083940bf6486e9ed4a371c14605321fe80f71b
    }

    private void applyThaiFontGlobal() {
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 12));
        Font thaiPlain = new Font("Segoe UI", Font.PLAIN, 16);
        Font thaiBold = new Font("Segoe UI", Font.BOLD, 18);
        UIManager.put("Label.font", thaiPlain);
        UIManager.put("Button.font", thaiBold);
        UIManager.put("TextField.font", thaiPlain);
        UIManager.put("TextArea.font", thaiPlain);
    }

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
        mainFrame.setVisible(true);
        changeScreen(new MenuGame(this));
    }

    public void showChangescene() {
        MultiDatingSound.getInstance().stopBGM();
        if (audioSystem != null)
            audioSystem.stopBGM();

        mainFrame.setVisible(false);
        SwingUtilities.invokeLater(() -> new Day1().CREATEANDSHOWGUI());
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
    }

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

    // ================== Multiplayer (แก้ไขเพื่อส่งไม้ต่อ) ==================
    public void showMultiplayer() {
        if (isConnecting)
            return; // ป้องกันการกดซ้ำ

        LobbyDialog.LobbyResult lobbyResult = LobbyDialog.show(mainFrame);
        if (lobbyResult == null)
            return;

        isConnecting = true;

        new Thread(() -> {
            GameClient client = new GameClient(lobbyResult.ip, 9090, lobbyResult.playerName);
            boolean ok = client.connect();

            SwingUtilities.invokeLater(() -> {
                isConnecting = false;
                if (!ok) {
                    JOptionPane.showMessageDialog(mainFrame, "เชื่อมต่อไม่สำเร็จ!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ✅ หยุดเสียงเมนูก่อนเข้า Lobby
                if (audioSystem != null)
                    audioSystem.stopBGM();
                MultiDatingSound.getInstance().stopBGM();

                // ✅ ซ่อนหน้าหลัก
                mainFrame.setVisible(false);

                // ✅ เปิดหน้า Lobby โดยส่ง 'this' (Controller) ไปด้วยเพื่อหายแดง
                MultiplayerScreen lobby = new MultiplayerScreen(client, true, this);
                lobby.setVisible(true);
                lobby.toFront();
            });
        }).start();
    }

    public void exitGame() {
        System.exit(0);
    }

    // ================== Getters ==================
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