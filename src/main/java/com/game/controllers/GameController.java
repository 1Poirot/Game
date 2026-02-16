package com.game.controllers;

import com.game.models.Player;
import com.game.systems.shop.ShopSystem;
import com.game.ui.SettingsScreen;
import com.game.ui.ShopScreen;
import javax.swing.JFrame;

public class GameController {
    private Player player;
    private ShopSystem shopSystem;
    private JFrame mainFrame;

    public GameController() {
        this.player = new Player("Hero", 100);
        this.shopSystem = new ShopSystem();

        mainFrame = new JFrame("Game Shop - Swing Version");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 700);
        mainFrame.setLocationRelativeTo(null);
    }

    public void start() {
        showShop();
    }

    public void showShop() {
        ShopScreen shopScreen = new ShopScreen(this);
        mainFrame.setContentPane(shopScreen);
        mainFrame.revalidate();
        mainFrame.setVisible(true);
    }

    public void showSettings() {
        // ล้างหน้าจอเก่าออกแล้วใส่ SettingsScreen เข้าไปแทน
        mainFrame.getContentPane().removeAll();
        mainFrame.add(new SettingsScreen(this));
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public Player getPlayer() {
        return player;
    }

    public ShopSystem getShopSystem() {
        return shopSystem;
    }
}