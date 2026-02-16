package com.game.ui;

import com.game.controllers.GameController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AudioSettingsScreen extends JPanel {
    private GameController controller;

    public AudioSettingsScreen(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(255, 209, 220)); // สีชมพูอ่อนพื้นหลัง

        // --- Header (ปุ่มตั้งค่า ย้อนกลับ) ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(40, 40, 0, 0));

        JButton backBtn = createRoundedButton(" <  ตั้งค่า ", 180, 70);
        backBtn.addActionListener(e -> controller.showSettings()); // กลับไปหน้าเมนูรวม
        header.add(backBtn);
        add(header, BorderLayout.NORTH);

        // --- Center Panel (กรอบตั้งค่าเสียง) ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel settingsBox = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 30, 30));
                g2.dispose();
            }
        };
        settingsBox.setPreferredSize(new Dimension(1200, 600));
        settingsBox.setOpaque(false);

        // ไอคอนลำโพง
        JLabel speakerIcon = new JLabel("🔊"); // หรือใช้รูปภาพของคุณ
        speakerIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        speakerIcon.setBounds(100, 100, 120, 120);
        settingsBox.add(speakerIcon);

        // แถบ Volume Slider (Custom)
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBounds(250, 130, 600, 50);
        volumeSlider.setBackground(Color.WHITE);
        volumeSlider.setForeground(new Color(255, 105, 180)); // สีชมพู
        settingsBox.add(volumeSlider);

        // ปุ่ม On / Off
        JButton onOffBtn = createPinkRoundedButton("On / Off", 180, 70);
        onOffBtn.setBounds(900, 120, 180, 70);
        settingsBox.add(onOffBtn);

        // ปุ่ม กลับ และ ยืนยัน ด้านล่าง
        JButton bottomBackBtn = createRoundedButton("กลับ", 250, 80);
        bottomBackBtn.setBounds(150, 450, 250, 80);
        bottomBackBtn.addActionListener(e -> controller.showSettings());

        JButton confirmBtn = createRoundedButton("ยืนยัน", 250, 80);
        confirmBtn.setBounds(800, 450, 250, 80);
        confirmBtn.addActionListener(e -> controller.showSettings());

        settingsBox.add(bottomBackBtn);
        settingsBox.add(confirmBtn);

        centerWrapper.add(settingsBox);
        add(centerWrapper, BorderLayout.CENTER);
    }

    // สร้างปุ่มขอบมนสีขาวขอบดำ
    private JButton createRoundedButton(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 40, 40);
                super.paintChildren(g);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Tahoma", Font.BOLD, 24));
        btn.setPreferredSize(new Dimension(w, h));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }

    // สร้างปุ่มสีชมพู On/Off
    private JButton createPinkRoundedButton(String text, int w, int h) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(new Color(255, 105, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return btn;
    }
}