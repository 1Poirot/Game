package com.game.systems.ending;

import com.game.models.Player;
import java.util.Map;

/**
 * Ending System - ระบบหลายตอนจบ
 * กำหนดตอนจบตามคะแนนความสัมพันธ์และการเลือกของผู้เล่น
 */
public class EndingSystem {

    public enum EndingType {
        TRUE_ENDING,
        GOOD_ENDING,
        NORMAL_ENDING,
        BAD_ENDING,
        ALONE_ENDING
    }

    public EndingType determineEnding(Player player) {
        // TODO: Implement ending logic based on affection points

        // Example logic:
        int maxAffection = 0;
        String favoriteCharacter = "";

        // Find character with highest affection
        // (This is placeholder logic)

        if (maxAffection >= 100) {
            return EndingType.TRUE_ENDING;
        } else if (maxAffection >= 75) {
            return EndingType.GOOD_ENDING;
        } else if (maxAffection >= 50) {
            return EndingType.NORMAL_ENDING;
        } else if (maxAffection >= 25) {
            return EndingType.BAD_ENDING;
        }

        return EndingType.ALONE_ENDING;
    }

    public void playEnding(EndingType ending) {
        System.out.println("Playing ending: " + ending);
        // TODO: Display ending cutscene/text
    }
}
