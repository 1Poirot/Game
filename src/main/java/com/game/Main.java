package com.game;

import com.game.controllers.GameController;
import java.awt.*;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // 🔥 บังคับฟอนต์ที่รองรับภาษาไทยทั้งแอป
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);

        UIManager.put("Label.font", thaiFont);
        UIManager.put("Button.font", thaiFont);
        UIManager.put("TextArea.font", thaiFont);   
        UIManager.put("TextField.font", thaiFont);
        UIManager.put("TitledBorder.font", thaiFont);
        UIManager.put("ComboBox.font", thaiFont);
        UIManager.put("Table.font", thaiFont);
        UIManager.put("TableHeader.font", thaiFont);

        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            controller.start();
        });
    }
}