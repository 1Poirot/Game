// File: Game/src/main/java/com/game/systems/shop/ShopSystem.java
package com.game.systems.shop;

import com.game.models.Item;
import com.game.models.Player;
import java.util.ArrayList;
import java.util.List;

public class ShopSystem {
    private List<Item> availableItems; // รายการไอเทมที่มีในร้าน

    public ShopSystem() {
        // โหลดข้อมูลไอเทมจากไฟล์ (ในโปรเจกต์จริงจะอ่านจาก JSON/XML)
        // นี่คือตัวอย่าง Hard-coded สำหรับตอนนี้
        availableItems = new ArrayList<>();
        availableItems.add(new Item("item_bouquet", "Bouquet", 1, "546546546.jpg", 5, "A beautiful bouquet."));
        availableItems.add(new Item("item_chocolate", "Chocolate Bar", 50, "546546546.jpg", 3, "Sweet and delicious."));
        availableItems.add(new Item("item_coffee", "Coffee", 10, "546546546.jpg", 2, "A warm cup of coffee."));
        availableItems.add(new Item("item_tulip", "Tulip Bouquet", 30, "546546546.jpg", 4, "A bouquet of tulips."));
        availableItems.add(new Item("item_cake", "Strawberry Cake", 80, "546546546.jpg", 6, "A slice of strawberry cake."));
        availableItems.add(new Item("item_sandwich", "Sandwich", 90, "546546546.jpg", 1, "A quick meal."));
    }

    public List<Item> getAvailableItems() {
        return availableItems;
    }

    /**
     * ดำเนินการซื้อไอเทม
     * @param player ผู้เล่นที่ต้องการซื้อ
     * @param item ไอเทมที่ต้องการซื้อ
     * @return true ถ้าซื้อสำเร็จ, false ถ้าเงินไม่พอ
     */
    public boolean buyItem(Player player, Item item) {
        if (player.getMoney() >= item.getPrice()) {
            player.setMoney(player.getMoney() - item.getPrice());
            player.addItemToInventory(item, 1); // เพิ่มไอเทม 1 ชิ้น
            System.out.println(player.getName() + " bought " + item.getName() + " for " + item.getPrice() + " money. Remaining: " + player.getMoney());
            return true;
        } else {
            System.out.println(player.getName() + " tried to buy " + item.getName() + " but doesn't have enough money. Needs " + item.getPrice() + ", has " + player.getMoney());
            return false;
        }
    }

    // อาจจะมี method สำหรับขายไอเทมคืนในอนาคต: sellItem(Player player, Item item)
}