package com.game.systems.save;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SaveSystem {

    private static final String SAVE_PATH = "src/main/resources/saves/";

    // บันทึกข้อมูล (รวมเลขหน้าบทสนทนา)
    public static void saveToFile(int slot, String name, int money, String date, int dialogueIndex) {
        File directory = new File(SAVE_PATH);
        if (!directory.exists()) directory.mkdirs(); 

        String fileName = SAVE_PATH + "save_slot_" + slot + ".txt";

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            writer.write("Name:" + name);
            writer.newLine();
            writer.write("Money:" + money);
            writer.newLine();
            writer.write("Date:" + date);
            writer.newLine();
            writer.write("DialogueIndex:" + dialogueIndex); // เซฟตำแหน่งเนื้อเรื่อง

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // อ่านข้อมูล
    public static Map<String, String> loadFromLocal(int slot) {
        File file = new File(SAVE_PATH + "save_slot_" + slot + ".txt");
        if (!file.exists()) return null;

        Map<String, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    data.put(parts[0].trim(), parts[1].trim());
                }
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ลบไฟล์เซฟ (ระบบใหม่ที่เพิ่มเข้ามา)
    public static boolean deleteSave(int slot) {
        File file = new File(SAVE_PATH + "save_slot_" + slot + ".txt");
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}