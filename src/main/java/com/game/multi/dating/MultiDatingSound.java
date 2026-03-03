package com.game.multi.dating;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class MultiDatingSound {
    private static MultiDatingSound instance;
    private static Clip bgmClip;
    private boolean isMuted = false;
    private float currentVolume = 0.8f; // ค่าเริ่มต้น 80%

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
            if (isMuted)
                setGain(-80.0f);
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
        setGain(isMuted ? -80.0f : 0.0f);
    }

    public boolean isMuted() {
        return isMuted;
    }

    private void setGain(float value) {
        if (bgmClip != null && bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(value);
        }
    }

    // ✅ เพิ่มเมธอดสำหรับดึงค่าระดับเสียง
    public float getVolume() {
        return currentVolume;
    }

    // ✅ เพิ่มเมธอดสำหรับตั้งค่าระดับเสียง
    public void setVolume(float volume) {
        this.currentVolume = volume;
        if (bgmClip != null && bgmClip.isOpen()) {
            try {
                javax.sound.sampled.FloatControl gainControl = (javax.sound.sampled.FloatControl) bgmClip
                        .getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);

                // แปลงค่าจาก 0.0 - 1.0 เป็นเดซิเบล (dB)
                float dB = (float) (Math.log(volume <= 0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } catch (Exception e) {
                System.err.println("ไม่สามารถปรับระดับเสียงได้: " + e.getMessage());
            }
        }
    }
}