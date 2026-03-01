package com.game.multi.dating;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MultiDatingTimer {
    private int seconds;
    private final Timer timer;
    private final Runnable onFinish;
    private final JLabel label;

    public MultiDatingTimer(int minutes, JLabel label, Runnable onFinish) {
        this.seconds = minutes * 60;
        this.label = label;
        this.onFinish = onFinish;
        this.timer = new Timer(1000, e -> {
            if (--seconds <= 0) {
                // ✅ แก้จุดแดง: ใช้ getSource เพื่อหยุด Timer
                ((Timer) e.getSource()).stop();
                onFinish.run();
            }
            label.setText(String.format("⏰ %02d:%02d", seconds / 60, seconds % 60));
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}