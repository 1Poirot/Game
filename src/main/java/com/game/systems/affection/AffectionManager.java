package com.game.systems.affection;

import java.util.HashMap;
import java.util.Map;

public class AffectionManager {

    private static AffectionManager INSTANCE;

    private Map<String, Integer> affectionMap = new HashMap<>();

    private AffectionManager() {}

    public static AffectionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AffectionManager();
        }
        return INSTANCE;
    }

    // เพิ่มคะแนน
    public void addAffection(String characterId, int points) {
    int current = affectionMap.getOrDefault(characterId, 0);
    int updated = Math.max(0, current + points); // ไม่ให้ต่ำกว่า 0
    affectionMap.put(characterId, updated);

    System.out.println("Affection of " + characterId + " = " + updated);
}

    // ดึงคะแนน
    public int getAffection(String characterId) {
        return affectionMap.getOrDefault(characterId, 0);
    }

    // รีเซ็ต (ใช้ตอนเริ่มเกมใหม่)
    public void reset(String characterId) {
        affectionMap.put(characterId, 0);
    }
}