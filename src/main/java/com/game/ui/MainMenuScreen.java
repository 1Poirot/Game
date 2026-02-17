package com.game.ui;

import com.game.controllers.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * Main Menu Screen - หน้าแรกของเกม
 * ให้ผู้เล่นเลือก New Game, Settings, หรือ Exit
 */
public class MainMenuScreen extends JPanel {

    private GameController controller;

    public MainMenuScreen(GameController controller) {
        this.controller = controller;

        setLayout(null);
        setPreferredSize(new Dimension(1920, 1080));
        setBackground(new Color(240, 200, 220));

        // Title
        JLabel title = new JLabel("เกมจีบสาว", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 72));
        title.setForeground(new Color(200, 50, 100));
        title.setBounds(560, 200, 800, 100);
        add(title);

        // Subtitle
        JLabel subtitle = new JLabel("Dating Simulation Game", SwingConstants.CENTER);
        subtitle.setFont(new Font("Tahoma", Font.ITALIC, 28));
        subtitle.setForeground(new Color(150, 50, 100));
        subtitle.setBounds(560, 300, 800, 40);
        add(subtitle);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(710, 450, 500, 300);

        // New Game Button
        JButton newGameBtn = createMenuButton("เริ่มเกมใหม่");
        newGameBtn.addActionListener(e -> controller.showGameScene());

        // Settings Button
        JButton settingsBtn = createMenuButton("ตั้งค่า");
        settingsBtn.addActionListener(e -> controller.showSettings());

        // Exit Button
        JButton exitBtn = createMenuButton("ออกจากเกม");
        exitBtn.addActionListener(e -> controller.exitGame());

        buttonPanel.add(newGameBtn);
        buttonPanel.add(settingsBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel);

        // Footer
        JLabel footer = new JLabel("© 2026 Dating Sim Project", SwingConstants.CENTER);
        footer.setFont(new Font("Tahoma", Font.PLAIN, 16));
        footer.setForeground(new Color(100, 100, 100));
        footer.setBounds(710, 900, 500, 30);
        add(footer);
    }

    /**
     * สร้างปุ่มเมนูแบบ styled
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 32));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(220, 100, 150));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 130, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 100, 150));
            }
        });

        return button;
    }
}
