package com.game.models;

/**
 * Character model - ตัวละครในเกม
 * เก็บข้อมูลตัวละคร, คะแนนความสัมพันธ์, และ state ต่างๆ
 */
public class Character {
    private String name;
    private int affectionPoints;
    private String currentState;
    
    public Character(String name) {
        this.name = name;
        this.affectionPoints = 0;
        this.currentState = "neutral";
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public int getAffectionPoints() {
        return affectionPoints;
    }
    
    public void addAffectionPoints(int points) {
        this.affectionPoints += points;
    }
    
    public String getCurrentState() {
        return currentState;
    }
    
    public void setCurrentState(String state) {
        this.currentState = state;
    }
}
