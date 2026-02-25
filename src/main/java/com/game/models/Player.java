package com.game.models;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private int money;
    private Map<String, Integer> inventory; // Item ID -> Quantity

    public Player(String name, int initialMoney) {
        this.name = name;
        this.money = initialMoney;
        this.inventory = new HashMap<>();
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getMoney() { return money; }
    public Map<String, Integer> getInventory() { return inventory; }

    // --- Setters (เพิ่มมาใหม่เพื่อแก้ตัวแดงในหน้า Load) ---
    
    /**
     * ใช้สำหรับอัปเดตชื่อผู้เล่น (เช่น ตอนโหลดเซฟ)
     */
    public void setName(String name) { 
        this.name = name; 
    }

    /**
     * ใช้สำหรับอัปเดตจำนวนเงิน
     */
    public void setMoney(int money) { 
        this.money = money; 
    }

    // --- Methods for Inventory ---
    public void addItemToInventory(Item item, int quantity) {
        inventory.put(item.getId(), inventory.getOrDefault(item.getId(), 0) + quantity);
    }

    public void removeItemFromInventory(Item item, int quantity) {
        int currentQuantity = inventory.getOrDefault(item.getId(), 0);
        if (currentQuantity >= quantity) {
            inventory.put(item.getId(), currentQuantity - quantity);
            if (inventory.get(item.getId()) <= 0) {
                inventory.remove(item.getId());
            }
        }
    }

    public int getItemQuantity(Item item) {
        return inventory.getOrDefault(item.getId(), 0);
    }
    
    /**
     * สำหรับล้างไอเทมในตัว (ใช้ตอนเริ่มเกมใหม่หรือโหลดเซฟใหม่)
     */
    public void clearInventory() {
        this.inventory.clear();
    }
}