package com.game.ui;

import java.awt.*;
import javax.swing.*;

public class RoundedPanel extends JPanel {

    private int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8,
                cornerRadius, cornerRadius);

        // Main panel
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8,
                cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}