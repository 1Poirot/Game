package com.game.multi.dating;

import java.awt.Font;
import javax.swing.*;

public class MultiDatingResultDialog {
    
    /**
     * ✅ Method สำหรับแสดงหน้าต่างสรุปคะแนน
     * @param score คะแนนความสัมพันธ์ที่ทำได้
     */
    public static void showResult(int score) {
        // ตั้งค่าฟอนต์ภาษาไทยให้ OptionPane
        Font thaiFont = new Font("Tahoma", Font.BOLD, 16);
        UIManager.put("OptionPane.messageFont", thaiFont);
        UIManager.put("OptionPane.buttonFont", thaiFont);

        String result;
        String icon;

        // Logic ตัดสินผลลัพธ์ตามคะแนนที่คุณตั้งไว้
        if (score >= 70) {
            result = "ตัวละครชอบคุณมาก (ชนะใจ) 💖";
            icon = "SUCCESS";
        } else if (score >= 40) {
            result = "ตัวละครสนใจคุณ 😊";
            icon = "INFO";
        } else {
            result = "เป็นได้แค่เพื่อนกัน 👥";
            icon = "WARNING";
        }

        // แสดงผลผ่าน JOptionPane
        JOptionPane.showMessageDialog(null, 
            "<html><center>สรุปคะแนนความสัมพันธ์: <font color='red'><b>" + score + "</b></font><br>" +
            "ผลลัพธ์: <b>" + result + "</b></center></html>",
            "📊 สรุปผลการแข่งขัน", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}