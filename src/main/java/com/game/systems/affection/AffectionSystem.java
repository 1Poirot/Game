package com.game.systems.affection;

import com.game.models.Character;

/**
 * Affection System - ระบบคะแนนความสัมพันธ์
 * จัดการคะแนนความสัมพันธ์ระหว่างผู้เล่นกับตัวละคร
 */
public class AffectionSystem {

    public void updateAffection(Character character, int points) {
        character.addAffectionPoints(points);
        checkAffectionLevel(character);
    }

    private void checkAffectionLevel(Character character) {
        int points = character.getAffectionPoints();

        // TODO: Define affection levels and trigger events
        if (points >= 100) {
            System.out.println(character.getName() + " - Maximum Affection!");
        } else if (points >= 75) {
            System.out.println(character.getName() + " - High Affection");
        } else if (points >= 50) {
            System.out.println(character.getName() + " - Medium Affection");
        } else if (points >= 25) {
            System.out.println(character.getName() + " - Low Affection");
        }
    }

    public String getAffectionRank(int points) {
        if (points >= 100)
            return "S";
        if (points >= 75)
            return "A";
        if (points >= 50)
            return "B";
        if (points >= 25)
            return "C";
        return "D";
    }
}
