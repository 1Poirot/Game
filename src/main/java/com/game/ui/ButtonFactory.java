package com.game.ui;

import com.game.utils.FontUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Factory class for creating styled game buttons
 */
public class ButtonFactory {

    private static final Font BUTTON_FONT = FontUtils.getThaiFont(24);
    private static final Color BUTTON_BG = new Color(100, 149, 237);
    private static final Color BUTTON_FG = Color.WHITE;

    /**
     * Create a styled button with Thai font support
     * 
     * @param text Button text
     * @return Styled JButton
     */
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBackground(BUTTON_BG);
        button.setForeground(BUTTON_FG);
        return button;
    }
}
