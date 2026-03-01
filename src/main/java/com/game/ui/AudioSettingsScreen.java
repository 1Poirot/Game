package com.game.ui;

import com.game.controllers.GameController;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AudioSettingsScreen extends JPanel {
    private boolean isMuted = false;
    private float lastVolume = 0.8f;

    public AudioSettingsScreen(GameController controller) {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 209, 220));

        // 1. Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 30, 0, 0));

        JButton backTitleBtn = createRoundedButton("< ตั้งค่า", 160, 60, Color.WHITE, Color.BLACK, 20);
        backTitleBtn.addActionListener(e -> controller.showSettings());
        header.add(backTitleBtn);
        add(header, BorderLayout.NORTH);

        // 2. Center Panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // กล่องสีขาวหลัก
        JPanel whiteBox = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
                g2.dispose();
                super.paintComponent(g); // วาดคอมโพเนนต์ลูกทับลงบนพื้นหลังที่วาดเสร็จแล้ว
            }
        };
        whiteBox.setPreferredSize(new Dimension(800, 420));
        whiteBox.setOpaque(false);
        whiteBox.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- แถวบน: ไอคอนลำโพง + Slider + On/Off ---

        JLabel speakerIcon = new JLabel("🔊", SwingConstants.CENTER);
        speakerIcon.setFont(new Font("Tahoma", Font.BOLD, 30));
        speakerIcon.setPreferredSize(new Dimension(70, 70));
        speakerIcon.setOpaque(true);
        speakerIcon.setBackground(Color.BLACK);
        speakerIcon.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        whiteBox.add(speakerIcon, gbc);

        // ดึงระดับเสียงปัจจุบัน
        float currentVol = (controller.getAudioSystem() != null) ? controller.getAudioSystem().getVolume() : 0.8f;
        JSlider volumeSlider = new JSlider(0, 100, (int) (currentVol * 100));
        volumeSlider.setBackground(Color.WHITE);
        volumeSlider.setOpaque(true); // มั่นใจว่าเห็นตัว Slider ชัดเจน
        volumeSlider.setForeground(new Color(255, 105, 180));

        // ปุ่ม On/Off (สร้างก่อนเพื่อให้ Slider เรียกใช้ได้)
        JButton onOffBtn = createRoundedButton(currentVol > 0 ? "On" : "Off", 120, 50,
                currentVol > 0 ? new Color(255, 105, 180) : Color.LIGHT_GRAY, Color.WHITE, 16);

        // ====== ปรับเสียงแบบ Real-time (ดังตามมือ) ======
        volumeSlider.addChangeListener(e -> {
            float vol = volumeSlider.getValue() / 100f;
            if (controller.getAudioSystem() != null) {
                controller.getAudioSystem().setVolume(vol);

                // ปรับสถานะปุ่ม On/Off ตามการลาก
                if (vol > 0) {
                    onOffBtn.setText("On");
                    onOffBtn.setBackground(new Color(255, 105, 180));
                    isMuted = false;
                } else {
                    onOffBtn.setText("Off");
                    onOffBtn.setBackground(Color.LIGHT_GRAY);
                    isMuted = true;
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        whiteBox.add(volumeSlider, gbc);

        // Action สำหรับปุ่ม On/Off
        onOffBtn.addActionListener(e -> {
            if (controller.getAudioSystem() == null)
                return;
            if (!isMuted) {
                lastVolume = controller.getAudioSystem().getVolume();
                if (lastVolume == 0)
                    lastVolume = 0.5f; // กันบั๊กถ้า mute ตอนเป็น 0
                volumeSlider.setValue(0); // ChangeListener จะทำงานอัตโนมัติ
            } else {
                volumeSlider.setValue((int) (lastVolume * 100));
            }
        });

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        whiteBox.add(onOffBtn, gbc);

        // --- แถวล่าง: ปุ่มกลับ + ยืนยัน ---
        JPanel footer = new JPanel(new GridLayout(1, 2, 100, 0));
        footer.setOpaque(false);

        JButton btnBack = createRoundedButton("กลับ", 180, 65, Color.WHITE, Color.BLACK, 22);
        btnBack.addActionListener(e -> controller.showSettings());

        JButton btnConfirm = createRoundedButton("ยืนยัน", 180, 65, Color.WHITE, Color.BLACK, 22);
        btnConfirm.addActionListener(e -> {
            if (controller.getAudioSystem() != null)
                controller.getAudioSystem().playSFX("click.wav");
            controller.showSettings();
        });

        footer.add(btnBack);
        footer.add(btnConfirm);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(80, 0, 0, 0);
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
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 5);
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}