package com.game.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;

public class ModernSliderUI extends BasicSliderUI {

    public ModernSliderUI(JSlider slider) {
        super(slider);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(255, 182, 193));
        g2.fillRoundRect(trackRect.x,
                trackRect.y + trackRect.height / 2 - 4,
                trackRect.width,
                8,
                8,
                8);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(255, 105, 180));
        g2.fillOval(thumbRect.x,
                thumbRect.y,
                thumbRect.width,
                thumbRect.height);
    }
}