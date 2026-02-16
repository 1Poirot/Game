package com.game.systems.save;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SaveSystem {

    // บันทึกข้อมูลลงเครื่อง
    public static void saveToFile(int slot, String name, int money, String date) {
        String fileName = "save_slot_" + slot + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write("Name:" + name);
            writer.newLine();
            writer.write("Money:" + money);
            writer.newLine();
            writer.write("Date:" + date);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // อ่านข้อมูลจากเครื่อง (ใช้ทั้งในหน้า Save และ Load)
    public static Map<String, String> loadFromLocal(int slot) {
        File file = new File("save_slot_" + slot + ".txt");
        if (!file.exists()) return null;

        Map<String, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    data.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}