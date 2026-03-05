package com.game.systems.dialogue;

/**
 * DialogueSystemAndChoice — ระบบบทสนทนาพร้อมตัวเลือก
 * Wraps DialogueSystem and provides a unified entry point.
 */
public class DialogueSystemAndChoice {

    private final DialogueSystem dialogueSystem;

    public DialogueSystemAndChoice() {
        this.dialogueSystem = new DialogueSystem();
    }

    /**
     * เปิดหน้าต่างบทสนทนา
     */
    public void CREATEANDSHOWGUI() {
        dialogueSystem.CREATEANDSHOWGUI();
    }
}
