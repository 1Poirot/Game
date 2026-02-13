package com.game.utils;

import java.awt.*;

/**
 * Utility class for font management, especially Thai fonts
 */
public class FontUtils {

    private static final String[] PREFERRED_THAI_FONTS = {
            "Leelawadee UI",
            "TH Sarabun New",
            "Angsana New",
            "Tahoma",
            "Cordia New"
    };

    /**
     * Get a Thai-compatible font with the specified size
     * 
     * @param size Font size
     * @return Font object with Thai support
     */
    public static Font getThaiFont(int size) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (String fontName : PREFERRED_THAI_FONTS) {
            for (String available : ge.getAvailableFontFamilyNames()) {
                if (available.equalsIgnoreCase(fontName)) {
                    return new Font(fontName, Font.PLAIN, size);
                }
            }
        }

        // Fallback to SansSerif if no Thai font is found
        return new Font("SansSerif", Font.PLAIN, size);
    }
}
