package com.game.controllers;

import com.game.models.Player;
import com.game.systems.dialogue.DialogueSystem;
import com.game.systems.choice.ChoiceSystem;
import com.game.systems.affection.AffectionSystem;
import com.game.systems.ending.EndingSystem;
import com.game.systems.save.SaveSystem;

/**
 * Game Controller - ควบคุมการเล่นเกมหลัก
 * จัดการ Game Loop และเชื่อมต่อระบบต่างๆ
 */
public class GameController {
    private Player player;
    private DialogueSystem dialogueSystem;
    private ChoiceSystem choiceSystem;
    private AffectionSystem affectionSystem;
    private EndingSystem endingSystem;
    private SaveSystem saveSystem;

    private boolean gameRunning;

    public GameController() {
        this.dialogueSystem = new DialogueSystem();
        this.choiceSystem = new ChoiceSystem();
        this.affectionSystem = new AffectionSystem();
        this.endingSystem = new EndingSystem();
        this.saveSystem = new SaveSystem();
        this.gameRunning = false;
    }

    public void startNewGame(String playerName) {
        this.player = new Player(playerName);
        this.gameRunning = true;
        gameLoop();
    }

    public void loadGame(String saveSlot) {
        this.player = saveSystem.loadGame(saveSlot);
        if (player != null) {
            this.gameRunning = true;
            gameLoop();
        }
    }

    private void gameLoop() {
        // TODO: Implement main game loop
        // เริ่มจาก -> สนทนา -> เลือกคำตอบ -> คะแนนความสัมพันธ์เปลี่ยน ->
        // ตำแหน่อต่อเนื่อง -> จบจาก

        while (gameRunning) {
            // 1. Display current situation/dialogue
            // 2. Present choices
            // 3. Process player choice
            // 4. Update affection
            // 5. Check for ending condition
            // 6. Next turn

            player.nextTurn();

            // Placeholder: End game after certain turns
            if (player.getCurrentTurn() >= 10) {
                endGame();
            }
        }
    }

    private void endGame() {
        EndingSystem.EndingType ending = endingSystem.determineEnding(player);
        endingSystem.playEnding(ending);
        this.gameRunning = false;
    }

    public void saveCurrentGame(String saveSlot) {
        saveSystem.saveGame(player, saveSlot);
    }
}
