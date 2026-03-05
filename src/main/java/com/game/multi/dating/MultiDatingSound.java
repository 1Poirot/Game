package com.game.multi.dating;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class MultiDatingSound {
    private static MultiDatingSound instance;
    private static Clip bgmClip;
    private boolean isMuted = false;

    // ✅ ปรับค่าเริ่มต้นเป็น 0.5f (ประมาณ 50% ของความดังที่หูรับรู้)
    private float currentVolume = 0.2f;

    private MultiDatingSound() {
    }

    public static MultiDatingSound getInstance() {
        if (instance == null) {
            instance = new MultiDatingSound();
        }
        return instance;
    }

    public void playBGM(String fileName) {
        stopBGM();
        try {
            String path = "/audio/bgm/" + fileName;
            URL soundURL = getClass().getResource(path);
            AudioInputStream audioStream;

            if (soundURL != null) {
                audioStream = AudioSystem.getAudioInputStream(soundURL);
            } else {
                File fallback = new File("src/main/resources/audio/bgm/" + fileName);
                if (!fallback.exists())
                    return;
                audioStream = AudioSystem.getAudioInputStream(fallback);
            }

            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioStream);

            // ✅ บังคับใช้ระดับเสียงปัจจุบันทันทีที่เปิดไฟล์ (ก่อนเริ่มเล่น)
            applyCurrentVolume();

            if (isMuted) {
                setGain(-80.0f);
            }

            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBGM() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.flush();
            bgmClip.close();
            bgmClip = null;
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            setGain(-80.0f);
        } else {
            applyCurrentVolume(); // กลับไปใช้ระดับเสียงล่าสุด
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    // Helper ภายในสำหรับปรับ Gain ตรงๆ
    private void setGain(float value) {
        if (bgmClip != null && bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            // ป้องกันค่าเกินขอบเขตของระบบ
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            gainControl.setValue(Math.max(min, Math.min(max, value)));
        }
    }

    // ✅ เมธอดสำหรับดึงค่าระดับเสียง
    public float getVolume() {
        return currentVolume;
    }

    // ✅ เมธอดสำหรับตั้งค่าระดับเสียง
    public void setVolume(float volume) {
        this.currentVolume = volume;
        applyCurrentVolume();
    }

    // ✅ ฟังก์ชันหัวใจหลักในการคำนวณเสียงให้เป็น Background นุ่มๆ
    private void applyCurrentVolume() {
        if (bgmClip != null && bgmClip.isOpen()) {
            if (bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);

                // ใช้สูตร Logarithmic เพื่อให้การปรับเสียงดูเป็นธรรมชาติเหมือนหูมนุษย์ได้ยิน
                // 0.0 -> -80dB (เงียบสนิท), 1.0 -> 0dB (ดังปกติของไฟล์)
                float dB = (float) (Math.log10(Math.max(0.0001, currentVolume)) * 20.0);

                float min = gainControl.getMinimum();
                float max = gainControl.getMaximum();
                gainControl.setValue(Math.max(min, Math.min(max, dB)));
            }
        }
    }
}