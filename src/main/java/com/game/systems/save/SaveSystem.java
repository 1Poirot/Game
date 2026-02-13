package com.game.systems.save;

import com.game.models.Player;

/**
 * Save System - ระบบ Save/Load
 * จัดการการบันทึกและโหลดเกม
 */
public class SaveSystem {

    public void saveGame(Player player, String saveSlot) {
        // TODO: Serialize player data and save to file
        System.out.println("Saving game to slot: " + saveSlot);
        // Save to: src/main/resources/saves/
    }

    public Player loadGame(String saveSlot) {
        // TODO: Load player data from file
        System.out.println("Loading game from slot: " + saveSlot);
        // Load from: src/main/resources/saves/
        return null;
    }

    public boolean saveExists(String saveSlot) {
        // TODO: Check if save file exists
        return false;
    }

    public void deleteSave(String saveSlot) {
        // TODO: Delete save file
        System.out.println("Deleting save slot: " + saveSlot);
    }
}
