package com.game.multi.dating;

import javax.swing.*;

/**
 * MultiDatingTimer — ตัวจับเวลาถอยหลังสำหรับโหมด Multi-Dating
 *
 * วิธีใช้งาน:
 * JLabel timerLabel = new JLabel("⏰ 03:00");
 * MultiDatingTimer timer = new MultiDatingTimer(3, timerLabel, () -> {
 * // โค้ดที่จะทำงานเมื่อเวลาหมด เช่น ปิดหน้าจอ
 * });
 * timer.start();
 */
public class MultiDatingTimer {

    /** ตัวนับวินาทีที่เหลืออยู่ (ลดลงทุก 1 วิ) */
    private int seconds;

    /** Swing Timer ที่ทำงานทุก 1,000 ms */
    private final Timer timer;

    /**
     * สร้าง Timer ถอยหลัง
     *
     * @param minutes  จำนวนนาทีที่ต้องการนับ
     * @param label    JLabel ที่จะแสดงเวลาที่เหลือ
     * @param onFinish Callback ที่เรียกเมื่อเวลาหมด
     */
    public MultiDatingTimer(int minutes, JLabel label, Runnable onFinish) {
        this.seconds = minutes * 60;

        // ใช้ parameter โดยตรงใน lambda (effectively final)
        // ไม่ต้องเก็บเป็น field เพราะไม่มีเมธอดอื่นใช้
        this.timer = new Timer(1000, e -> {
            if (--seconds <= 0) {
                ((Timer) e.getSource()).stop(); // หยุด Timer ผ่าน source เพื่อความปลอดภัย
                onFinish.run(); // แจ้ง callback ว่าเวลาหมด
            }
            label.setText(String.format("⏰ %02d:%02d", seconds / 60, seconds % 60));
        });
    }

    /** เริ่มนับเวลา */
    public void start() {
        timer.start();
    }

    /** หยุดนับเวลา (ใช้เมื่อออกจากหน้าจอก่อนเวลาหมด) */
    public void stop() {
        timer.stop();
    }
}