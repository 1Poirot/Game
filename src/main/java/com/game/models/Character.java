package com.game.models;

import java.awt.Rectangle;

/**
 * Character model - ตัวละครในเกม
 * เก็บข้อมูลตัวละคร, คะแนนความสัมพันธ์, state ต่างๆ และข้อมูลการแสดงผล
 */
public class Character {
    private String name;
    private int affectionPoints;
    private String currentState;

    // Visual properties
    private int x;
    private int y;
    private int width;
    private int height;
    private String imagePath;

    public Character(String name) {
        this.name = name;
        this.affectionPoints = 0;
        this.currentState = "neutral";

        // Default visual properties
        this.x = 200;
        this.y = 60;
        this.width = 200;
        this.height = 300;
        this.imagePath = "src/main/resources/images/characters/1.png";
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

    // Visual property getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Get bounding rectangle for collision detection
     * 
     * @return Rectangle representing character bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
