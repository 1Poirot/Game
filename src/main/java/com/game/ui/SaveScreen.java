package com.game.ui;

import com.game.controllers.GameController;
import com.game.systems.save.SaveSystem;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SaveScreen extends JPanel {
    private GameController controller;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public SaveScreen(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(255, 209, 220));

        // --- Header ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 50, 0, 0));

        JButton backBtn = createRoundedButton("< ย้อนกลับ", 200, 70, Color.WHITE, Color.BLACK);
        backBtn.addActionListener(e -> controller.showSettings());
        header.add(backBtn);
        add(header, BorderLayout.NORTH);

        // --- Center Panel (ขนาดใหญ่ 1500x850) ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel saveBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(4));
                g2.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 50, 50);
                g2.dispose();
            }
        };
        saveBox.setPreferredSize(new Dimension(1500, 850));
        saveBox.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 15, 20);
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("ระบบจัดการข้อมูลเกม (Save & Load)");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 45));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        saveBox.add(titleLabel, gbc);

        // --- สร้าง 5 Slots ---
        for (int i = 1; i <= 5; i++) {
            final int slot = i;
            Map<String, String> data = SaveSystem.loadFromLocal(slot); // ดึงข้อมูลจาก SaveSystem
            boolean hasData = (data != null);
            
            String info = !hasData ? "--- ช่องว่าง (Empty Slot) ---" 
                         : "ผู้เล่น: " + data.get("Name") + " | เงิน: " + data.get("Money") + " (" + data.get("Date") + ")";
            
            // แผงปุ่มสำหรับแต่ละ Slot (รวมทั้งปุ่ม Save และ Load)
            JPanel slotRow = new JPanel(new BorderLayout(15, 0));
            slotRow.setOpaque(false);

            // 1. ปุ่มแสดงข้อมูลและกดเพื่อ LOAD
            JButton loadBtn = createRoundedButton("LOAD " + slot + " | " + info, 1050, 90, 
                                                hasData ? new Color(100, 149, 237) : Color.LIGHT_GRAY, Color.WHITE);
            loadBtn.addActionListener(e -> {
                if (hasData) {
                    int confirm = JOptionPane.showConfirmDialog(this, "ต้องการโหลดข้อมูลจาก Slot " + slot + " ใช่หรือไม่?", "ยืนยันการโหลด", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        controller.getPlayer().setName(data.get("Name"));
                        controller.getPlayer().setMoney(Integer.parseInt(data.get("Money")));
                        JOptionPane.showMessageDialog(this, "โหลดข้อมูลสำเร็จ!");
                        controller.showShop(); // กลับเข้าเกม
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "ไม่มีข้อมูลในช่องนี้");
                }
            });

            // 2. ปุ่มสำหรับ SAVE ทับช่องนี้
            JButton saveBtn = createRoundedButton("บันทึกทับ", 200, 90, new Color(255, 105, 180), Color.WHITE);
            saveBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "ต้องการบันทึกข้อมูลปัจจุบันลงใน Slot " + slot + "?", "ยืนยันการเซฟ", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    SaveSystem.saveToFile(slot, controller.getPlayer().getName(), controller.getPlayer().getMoney(), dateFormat.format(new Date()));
                    JOptionPane.showMessageDialog(this, "บันทึกลงเครื่องเรียบร้อย!");
                    controller.showSaveScreen(); // รีเฟรชหน้าจอทันทีเพื่อโชว์ข้อมูลใหม่
                }
            });

            slotRow.add(loadBtn, BorderLayout.CENTER);
            slotRow.add(saveBtn, BorderLayout.EAST);

            gbc.gridy = i;
            saveBox.add(slotRow, gbc);
        }
        
        centerWrapper.add(saveBox);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private JButton createRoundedButton(String text, int w, int h, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 40, 40);
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(getForeground());
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent())/2 - 5);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Tahoma", Font.BOLD, 22));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }
}