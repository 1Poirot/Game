package com.game.controllers;

import com.game.models.Player;
import com.game.systems.shop.ShopSystem;
import com.game.ui.SettingsScreen;
import com.game.ui.ShopScreen;
import com.game.ui.AudioSettingsScreen; // อย่าลืม Import คลาสหน้าตั้งค่าเสียงที่สร้างใหม่
import javax.swing.JFrame;

public class GameController {
    private Player player;
    private ShopSystem shopSystem;
    private JFrame mainFrame;

    public GameController() {
        // เริ่มต้นผู้เล่นด้วยชื่อ "Hero" และเงิน 100
        this.player = new Player("Hero", 100);
        this.shopSystem = new ShopSystem();

        mainFrame = new JFrame("Game Shop - Swing Version");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // --- ปรับขนาดหน้าจอเป็น 1920 x 1080 ---
        mainFrame.setSize(1920, 1080);
        mainFrame.setResizable(false); // ล็อกขนาดหน้าจอไม่ให้ลากขยายจนภาพเพี้ยน
        mainFrame.setLocationRelativeTo(null); // ให้หน้าต่างเด้งขึ้นตรงกลางจอ
    }

    public void start() {
        showShop();
        mainFrame.setVisible(true);
    }

    // ฟังก์ชันแสดงหน้า ร้านค้า
    public void showShop() {
        mainFrame.getContentPane().removeAll();
        mainFrame.setContentPane(new ShopScreen(this));
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // ฟังก์ชันแสดงหน้า เมนูตั้งค่ารวม (ที่มีปุ่ม เซฟ, โปรไฟล์, ออกจากเกม)
    public void showSettings() {
        mainFrame.getContentPane().removeAll();
        mainFrame.add(new SettingsScreen(this));
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // --- เพิ่มฟังก์ชันแสดงหน้า ตั้งค่าเสียง (Audio Settings) ---
    public void showAudioSettings() {
        mainFrame.getContentPane().removeAll();
        mainFrame.add(new AudioSettingsScreen(this)); // เรียกใช้คลาสหน้าตั้งค่าเสียง
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