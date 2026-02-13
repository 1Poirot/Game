package com.game.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Player model - ผู้เล่น
 * เก็บข้อมูลความสัมพันธ์กับตัวละครต่างๆ
 */
public class Player {
    private String name;
    private Map<String, Integer> characterRelationships;
    private int currentTurn;

    public Player(String name) {
        this.name = name;
        this.characterRelationships = new HashMap<>();
        this.currentTurn = 0;
    }

    public void addRelationship(String characterName, int points) {
        characterRelationships.put(characterName,
                characterRelationships.getOrDefault(characterName, 0) + points);
    }

    public int getRelationship(String characterName) {
        return characterRelationships.getOrDefault(characterName, 0);
    }

    public void nextTurn() {
        this.currentTurn++;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public String getName() {
        return name;
    }
}
