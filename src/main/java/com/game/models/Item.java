// File: Game/src/main/java/com/game/models/Item.java
package com.game.models;

import java.awt.*;

public class Item {
    private String id; // เพื่อใช้ในการอ้างอิงและโหลดข้อมูลจากไฟล์
    private String name;
    private int price;
    private String imagePath; // Path ไปยังรูปภาพไอเทม เช่น "ui/items/chocolate.png"
    private String description; // คำอธิบายไอเทม (อาจจะแสดงเมื่อกดดูรายละเอียด)
    private int affectionBoost; // ค่าเพิ่มความสัมพันธ์ถ้าเป็นไอเทมประเภทของขวัญ
    private Item item;
    private Image itemImage;

    public Item(String id, String name, int price, String imagePath, int affectionBoost, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.affectionBoost = affectionBoost;
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getAffectionBoost() {
        return affectionBoost;
    }

    public String getDescription() {
        return description;
    }

    // Setters (ถ้าจำเป็น, แต่โดยปกติ Item model จะเป็น Immutable)
}