package com.game.ui;

import com.game.controllers.GameController;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AudioSettingsScreen extends JPanel {

    private boolean isMuted = false;
    private float lastVolume = 0.3f;

    private final Color PINK_ACCENT = new Color(255, 105, 180);

    public AudioSettingsScreen(GameController controller) {

        setLayout(new BorderLayout());
        setOpaque(false);

        // ================= HEADER =================
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 40, 10, 0));

        JLabel title = new JLabel("🎵 Settings Audio");
        title.setFont(new Font("\"Segoe UI Emoji", Font.BOLD, 32));
        title.setForeground(new Color(150, 70, 110));

        JButton backBtn = createModernButton("← ย้อนกลับ", 170, 45, Color.WHITE, Color.BLACK, 16);
        backBtn.addActionListener(e -> controller.showSettings());

        header.add(backBtn);
        header.add(Box.createHorizontalStrut(30));
        header.add(title);

        add(header, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel whiteBox = new RoundedPanel(35);
        whiteBox.setPreferredSize(new Dimension(750, 380));
        whiteBox.setLayout(new GridBagLayout());
        whiteBox.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== ICON =====
        JLabel speakerIcon = new JLabel("🔊", SwingConstants.CENTER);
        speakerIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        speakerIcon.setPreferredSize(new Dimension(80, 80));
        speakerIcon.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        whiteBox.add(speakerIcon, gbc);

        // ดึงระดับเสียงปัจจุบัน
        float currentVol = (controller.getAudioSystem() != null) ? controller.getAudioSystem().getVolume() : 0.2f;
        JSlider volumeSlider = new JSlider(0, 100, (int) (currentVol * 100));
        volumeSlider.setOpaque(false);
        volumeSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volumeSlider.setUI(new ModernSliderUI(volumeSlider));

        gbc.gridx = 1;
        gbc.weightx = 1;
        whiteBox.add(volumeSlider, gbc);

        // ===== TOGGLE BUTTON =====
        JToggleButton toggleBtn = new JToggleButton(currentVol > 0 ? "เปิด" : "ปิด") {

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Color top;
                Color bottom;

                if (isSelected()) {
                    top = new Color(255, 170, 210);
                    bottom = new Color(255, 105, 180);
                } else {
                    top = new Color(230, 230, 230);
                    bottom = new Color(200, 200, 200);
                }

                GradientPaint gp = new GradientPaint(
                        0, 0, top,
                        0, getHeight(), bottom);

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                g2.setColor(isSelected() ? Color.WHITE : new Color(80, 80, 80));
                g2.setFont(new Font("Tahoma", Font.BOLD, 14));

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;

                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        toggleBtn.setPreferredSize(new Dimension(110, 45));
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setBorderPainted(false);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.setSelected(currentVol > 0);

        gbc.gridx = 2;
        gbc.weightx = 0;
        whiteBox.add(toggleBtn, gbc);

        // ===== REALTIME VOLUME =====
        volumeSlider.addChangeListener(e -> {

            float vol = volumeSlider.getValue() / 100f;

            if (controller.getAudioSystem() != null) {
                controller.getAudioSystem().setVolume(vol);
            }

            if (vol > 0) {
                toggleBtn.setText("เปิด");
                toggleBtn.setSelected(true);
                isMuted = false;
            } else {
                toggleBtn.setText("ปิด");
                toggleBtn.setSelected(false);
                isMuted = true;
            }
        });

        toggleBtn.addActionListener(e -> {

            if (controller.getAudioSystem() == null)
                return;

            if (toggleBtn.isSelected()) {
                volumeSlider.setValue((int) (lastVolume * 100));
            } else {
                lastVolume = controller.getAudioSystem().getVolume();
                volumeSlider.setValue(0);
            }
        });

        // ===== FOOTER =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        footer.setOpaque(false);

        JButton btnCancel = createRedGradientButton("ยกเลิก", 160, 55);
        JButton btnConfirm = createGreenGradientButton("บันทึก", 160, 55);

        btnCancel.addActionListener(e -> controller.showSettings());

        btnConfirm.addActionListener(e -> {
            if (controller.getAudioSystem() != null)
                controller.getAudioSystem().playSFX("click.wav");
            controller.showSettings();
        });

        footer.add(btnCancel);
        footer.add(btnConfirm);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(60, 0, 0, 0);

        whiteBox.add(footer, gbc);

        centerWrapper.add(whiteBox);

        add(centerWrapper, BorderLayout.CENTER);
    }

    // ================= พื้นหลัง =================
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(
                0, 0, new Color(255, 200, 220),
                0, h, new Color(255, 240, 245));

        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }

    // ================= ปุ่มปกติ =================
    private JButton createModernButton(String text, int w, int h, Color bg, Color fg, int fontSize) {

        JButton btn = new JButton(text);

        btn.setPreferredSize(new Dimension(w, h));
        btn.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    // ================= ปุ่มเขียว =================
    private JButton createGreenGradientButton(String text, int w, int h) {

        JButton btn = new JButton(text) {

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(170, 240, 190),
                        0, getHeight(), new Color(60, 170, 110));

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Tahoma", Font.BOLD, 18));

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;

                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        btn.setPreferredSize(new Dimension(w, h));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        return btn;
    }

    // ================= ปุ่มแดง =================
    private JButton createRedGradientButton(String text, int w, int h) {

        JButton btn = new JButton(text) {

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 120, 120),
                        0, getHeight(), new Color(200, 40, 40));

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Tahoma", Font.BOLD, 18));

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;

                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        btn.setPreferredSize(new Dimension(w, h));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        return btn;
    }
}