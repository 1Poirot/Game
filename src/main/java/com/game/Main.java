package com.game;

import com.game.controllers.GameController;

import javax.swing.*;

/**
 * Main entry point for Dating Simulation Game
 * เกมจีบสาวแบบเทิร์นเบส
 */
public class Main {
    public static void main(String[] args) {
        // Create game controller
        GameController controller = new GameController();

        // Create and set up the window
        JFrame frame = new JFrame("2D Character Interaction Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add game panel to frame
        frame.add(controller.getGamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
