package com.game.systems.dialogue;

/**
 * Dialogue System - ระบบบทสนทนา
 * จัดการการแสดงบทสนทนาและโหลดบทสนทนาจากไฟล์
 */
public class DialogueSystem {

    private boolean showDialogue = false;
    private String currentDialogue = "สวัสดี! มีอะไรให้ช่วยไหม?";

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

    // State management for current dialogue
    public boolean isDialogueVisible() {
        return showDialogue;
    }

    public void setDialogueVisible(boolean visible) {
        this.showDialogue = visible;
    }

    public String getCurrentDialogue() {
        return currentDialogue;
    }

    public void setCurrentDialogue(String dialogue) {
        this.currentDialogue = dialogue;
        this.showDialogue = true;
    }

    public void hideDialogue() {
        this.showDialogue = false;
    }
}
