package com.game.controllers;

import com.game.models.Player;
import com.game.systems.shop.ShopSystem;
import com.game.ui.AudioSettingsScreen;
import com.game.ui.Changescene;
import com.game.ui.MainMenuScreen;
import com.game.ui.SaveScreen;
import com.game.ui.SettingsScreen;
import com.game.ui.ShopScreen;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameController {
    private Player player;
    private ShopSystem shopSystem;
    private JFrame mainFrame;

    public GameController() {
        this.player = new Player("Hero", 100);
        this.shopSystem = new ShopSystem();

        mainFrame = new JFrame("Game Shop - Swing Version");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- ตั้งขนาดหน้าจอ 1920 x 1080 ---
        mainFrame.setSize(1920, 1080);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
    }

    public void start() {
        showMainMenu();
        mainFrame.setVisible(true);
    }

    /**
     * เมธอดกลางสำหรับเปลี่ยนหน้าจอ
     * ใช้ setContentPane เพื่อความเสถียรในการวาดภาพใหม่บนจอใหญ่
     */
    private void changeScreen(JPanel panel) {
        mainFrame.setContentPane(panel);
        // บังคับให้ระบบคำนวณ Layout และวาด Component ใหม่ทันที
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showMainMenu() {
        changeScreen(new MainMenuScreen(this));
    }

    public void showGameScene() {
        changeScreen(new Changescene(this));
    }

    public void showShop() {
        changeScreen(new ShopScreen(this));
    }

    public void showSettings() {
        changeScreen(new SettingsScreen(this));
    }

    public void showAudioSettings() {
        System.out.println("Switching to Audio Settings...");
        // สร้างหน้าใหม่และสลับทันที
        AudioSettingsScreen audioScreen = new AudioSettingsScreen(this);
        changeScreen(audioScreen);
    }

    public void showSaveScreen() {
        changeScreen(new SaveScreen(this));
    }

    public void exitGame() {
        System.exit(0);
    }

    public Player getPlayer() {
        return player;
    }

    public ShopSystem getShopSystem() {
        return shopSystem;
    }
}