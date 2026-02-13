package com.game.systems.choice;

import java.util.List;
import java.util.ArrayList;

/**
 * Choice System - ระบบเลือกคำตอบ
 * จัดการตัวเลือกและผลลัพธ์จากการเลือก
 */
public class ChoiceSystem {

    public static class Choice {
        private String text;
        private int affectionChange;
        private String nextDialogueId;

        public Choice(String text, int affectionChange, String nextDialogueId) {
            this.text = text;
            this.affectionChange = affectionChange;
            this.nextDialogueId = nextDialogueId;
        }

        public String getText() {
            return text;
        }

        public int getAffectionChange() {
            return affectionChange;
        }

        public String getNextDialogueId() {
            return nextDialogueId;
        }
    }

    public void displayChoices(List<Choice> choices) {
        // TODO: Display available choices to player
        System.out.println("Available choices:");
        for (int i = 0; i < choices.size(); i++) {
            System.out.println((i + 1) + ". " + choices.get(i).getText());
        }
    }

    public Choice processChoice(int choiceIndex, List<Choice> choices) {
        // TODO: Process selected choice and return result
        if (choiceIndex >= 0 && choiceIndex < choices.size()) {
            return choices.get(choiceIndex);
        }
        return null;
    }
}
