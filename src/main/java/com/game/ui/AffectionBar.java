package com.game.ui;

import com.game.systems.affection.AffectionManager;
import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;

public class AffectionBar extends JPanel {

    private String characterId;
    private int maxAffection = 100;

    public AffectionBar(String characterId) {
        this.characterId = characterId;
        setOpaque(false);
    }

    public void setMaxAffection(int max) {
        this.maxAffection = max;
    }

    public void refresh() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int arc = 18; // ความโค้งกรอบ

        int score = AffectionManager.getInstance()
                .getAffection(characterId);

        float percent = Math.max(0f,
                Math.min(1f, (float) score / maxAffection));

        // =============================
        // Shadow
        // =============================
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(3, 3, width - 3, height - 3, arc, arc);

        // =============================
        // Background Card
        // =============================
        g2.setColor(new Color(255, 240, 245, 230));
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        // =============================
        // White Border
        // =============================
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, width - 2, height - 2, arc, arc);

        int padding = 15;
        int heartSize = height / 3;

        int hx = padding;
        int hy = (height - heartSize) / 2;

        // =============================
        // Heart Shadow
        // =============================
        Shape heartShadow = createHeart(hx + 2, hy + 2, heartSize);
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fill(heartShadow);

        // =============================
        // Heart
        // =============================
        Shape heart = createHeart(hx, hy, heartSize);
        g2.setColor(new Color(255, 90, 140));
        g2.fill(heart);

        // =============================
        // Title
        // =============================
        int fontSize = height / 4;
        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        g2.setColor(new Color(50, 50, 50));
        g2.drawString("Affection",
                hx + heartSize + 10,
                height / 2 - 2);

        // =============================
        // Percentage
        // =============================
        String percentText = score + "%";
        FontMetrics fm = g2.getFontMetrics();
        int percentWidth = fm.stringWidth(percentText);

        g2.setColor(new Color(255, 80, 120));
        g2.drawString(percentText,
                width - percentWidth - padding,
                height / 2 - 2);

        // =============================
        // Progress Bar Background
        // =============================
        int barX = hx + heartSize + 10;
        int barY = height / 2 + 10;
        int barWidth = width - barX - padding;
        int barHeight = height / 5;

        g2.setColor(new Color(240, 200, 210));
        g2.fillRoundRect(barX, barY,
                barWidth, barHeight,
                10, 10);

        // =============================
        // Progress Fill (Gradient)
        // =============================
        int fillWidth = (int) (barWidth * percent);

        GradientPaint gradient = new GradientPaint(
                barX, barY,
                new Color(255, 150, 180),
                barX + fillWidth, barY,
                new Color(255, 80, 130)
        );

        g2.setPaint(gradient);
        g2.fillRoundRect(barX, barY,
                fillWidth, barHeight,
                10, 10);

        g2.dispose();
    }

    // ===================================
    // Heart Shape Generator
    // ===================================
    private Shape createHeart(int x, int y, int size) {
        double s = size;

        Path2D.Double path = new Path2D.Double();
        path.moveTo(x + s * 0.5, y + s);

        path.curveTo(
                x + s * 1.1, y + s * 0.6,
                x + s * 0.9, y,
                x + s * 0.5, y + s * 0.3
        );

        path.curveTo(
                x + s * 0.1, y,
                x - s * 0.1, y + s * 0.6,
                x + s * 0.5, y + s
        );

        path.closePath();
        return path;
    }
}