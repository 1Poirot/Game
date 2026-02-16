package com.game;

import com.game.controllers.GameController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            controller.start();
        });
    }
}