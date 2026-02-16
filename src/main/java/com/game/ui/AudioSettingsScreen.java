package com.game.ui;

import com.game.controllers.GameController;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AudioSettingsScreen extends JPanel {
    private GameController controller;

    public AudioSettingsScreen(GameController controller) {
        this.controller = controller;
        // ใช้ BorderLayout เป็นโครงสร้างหลัก
        setLayout(new BorderLayout());
        setBackground(new Color(255, 209, 220));

        // 1. Header (ปุ่ม < ตั้งค่า)
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 30, 0, 0));

        JButton backTitleBtn = createRoundedButton("< ตั้งค่า", 160, 60, Color.WHITE, Color.BLACK, 20);
        backTitleBtn.addActionListener(e -> controller.showSettings());
        header.add(backTitleBtn);
        add(header, BorderLayout.NORTH);

        // 2. Center Panel (ใช้ GridBagLayout เพื่อให้กล่องขาวหดตัวตามพื้นที่จริง)
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // กล่องสีขาวหลัก (วาดขอบมน)
        JPanel whiteBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 30, 30);
                g2.dispose();
            }
        };
        // ลดขนาดลงเหลือ 750x400 เพื่อให้มั่นใจว่าไม่ล้นแน่นอน
        whiteBox.setPreferredSize(new Dimension(800, 420));
        whiteBox.setOpaque(false);
        whiteBox.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- จัดของในกล่องขาวด้วย GBC ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // แถวบน: ลำโพง + Slider + On/Off
        // ไอคอนลำโพง
        JLabel speakerIcon = new JLabel("🔊", SwingConstants.CENTER);
        speakerIcon.setFont(new Font("Tahoma", Font.BOLD, 30));
        speakerIcon.setPreferredSize(new Dimension(70, 70));
        speakerIcon.setOpaque(true);
        speakerIcon.setBackground(Color.BLACK);
        speakerIcon.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        whiteBox.add(speakerIcon, gbc);

        // Slider
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBackground(Color.WHITE);
        volumeSlider.setForeground(new Color(255, 105, 180));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        whiteBox.add(volumeSlider, gbc);

        // ปุ่ม On/Off
        JButton onOffBtn = createRoundedButton("On / Off", 120, 50, new Color(255, 105, 180), Color.WHITE, 16);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        whiteBox.add(onOffBtn, gbc);

        // แถวล่าง: ปุ่มกลับ + ยืนยัน (เว้นระยะห่างตรงกลาง)
        JPanel footer = new JPanel(new GridLayout(1, 2, 100, 0));
        footer.setOpaque(false);
        
        JButton btnBack = createRoundedButton("กลับ", 180, 65, Color.WHITE, Color.BLACK, 22);
        btnBack.addActionListener(e -> controller.showSettings());
        
        JButton btnConfirm = createRoundedButton("ยืนยัน", 180, 65, Color.WHITE, Color.BLACK, 22);
        btnConfirm.addActionListener(e -> controller.showSettings());

        footer.add(btnBack);
        footer.add(btnConfirm);

        gbc.gridx = 0; gbc.gridy = 1; 
        gbc.gridwidth = 3; 
        gbc.insets = new Insets(80, 0, 0, 0); // เว้นระยะลงมาจากแถวบนเยอะหน่อย
        whiteBox.add(footer, gbc);

        centerWrapper.add(whiteBox);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private JButton createRoundedButton(String text, int w, int h, Color bg, Color fg, int fontSize) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 40, 40);
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(getForeground());
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent())/2 - 5);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }
}