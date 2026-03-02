package com.game.multi.dating;

import java.awt.Font;
import java.util.*;
import javax.swing.*;

public class MultiDatingResultDialog {

    /**
     * ✅ แสดงผลการแข่งขันออนไลน์และประกาศผู้ชนะ
     * * @param playerScores Map เก็บชื่อและคะแนนของผู้เล่นทุกคน (เช่น {"Me": 80, "Player 2": 70, "Player 3": 80})
     * @param myName ชื่อของผู้เล่นเครื่องนี้
     */
    public static void showResult(Map<String, Integer> playerScores, String myName) {
        // --- 1. ตั้งค่าฟอนต์ไทยให้ UI ---
        Font thaiFont = new Font("Tahoma", Font.BOLD, 16);
        UIManager.put("OptionPane.messageFont", thaiFont);
        UIManager.put("OptionPane.buttonFont", thaiFont);

        if (playerScores == null || playerScores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ไม่พบข้อมูลคะแนน", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- 2. หาคะแนนสูงสุด (เพื่อตัดสินคนชนะ) ---
        int maxScore = Collections.max(playerScores.values());

        // --- 3. แยกกลุ่มผู้ชนะและผู้แพ้ ---
        List<String> winners = new ArrayList<>();
        List<String> losers = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
            if (entry.getValue() == maxScore) {
                winners.add(entry.getKey());
            } else {
                losers.add(entry.getKey());
            }
        }

        // --- 4. สร้างข้อความสรุปผลแบบ HTML ---
        StringBuilder msg = new StringBuilder("<html><body style='width: 300px; text-align: center; font-family: Tahoma;'>");
        msg.append("<h2 style='color: #FF1493;'>📊 สรุปผลการศึกชิงนาง</h2>");
        msg.append("<hr>");

        // ส่วนของผู้ชนะ
        if (winners.size() > 1) {
            msg.append("<b style='color: #0000FF;'>💍 เสมอ! พวกคุณคะแนนเท่ากันและได้เป็นแฟนกับเธอ:</b><br>");
            for (String w : winners) {
                msg.append("<span style='font-size: 14px;'>💖 ").append(w).append("</span><br>");
            }
        } else {
            msg.append("<b style='color: #008000;'>🏆 ผู้ชนะที่ได้หัวใจเธอไปครอง:</b><br>");
            msg.append("<span style='font-size: 20px; color: #FF0000;'><b>").append(winners.get(0)).append("</b></span><br>");
        }

        msg.append("<br><p style='color: #666666; font-size: 12px;'>--- ผลการตัดสินคนอื่น ---</p>");

        // ส่วนของผู้แพ้
        for (String l : losers) {
            msg.append("<span style='color: #8B0000;'>❌ ").append(l)
               .append(": เธอมองข้ามและจะไม่มองหน้าคุณอีกเลย...</span><br>");
        }

        msg.append("<br><hr>");
        msg.append("คะแนนสูงสุดในรอบนี้: <b>").append(maxScore).append("</b>");
        msg.append("</body></html>");

        // --- 5. แสดงหน้าต่างผลลัพธ์ ---
        String title = winners.contains(myName) ? "🎉 ยินดีด้วย! คุณสมหวังแล้ว" : "💔 แห้วแดก... เธอไม่เลือกคุณ";
        
        JOptionPane.showMessageDialog(null, msg.toString(), title, JOptionPane.PLAIN_MESSAGE);
    }
}