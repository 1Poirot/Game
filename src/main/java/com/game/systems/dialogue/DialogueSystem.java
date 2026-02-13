package com.game.systems.dialogue;

/**
 * Dialogue System - ระบบบทสนทนา
 * จัดการการแสดงบทสนทนาและโหลดบทสนทนาจากไฟล์
 */
public class DialogueSystem {

    public void loadDialogue(String dialogueId) {
        // TODO: Load dialogue from resources/dialogues
        System.out.println("Loading dialogue: " + dialogueId);
    }

    public void displayDialogue(String text) {
        // TODO: Display dialogue text with character portrait
        System.out.println("Dialogue: " + text);
    }

    public void processDialogue(String dialogueId) {
        // TODO: Process through dialogue tree
    }
}
