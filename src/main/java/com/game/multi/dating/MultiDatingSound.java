package com.game.multi.dating;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class MultiDatingSound {
    private static MultiDatingSound instance;
    private static Clip bgmClip; 
    private boolean isMuted = false;

    private MultiDatingSound() {}

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
                if (!fallback.exists()) return;
                audioStream = AudioSystem.getAudioInputStream(fallback);
            }

            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioStream);
            if (isMuted) setGain(-80.0f);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) { e.printStackTrace(); }
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

    public boolean isMuted() { return isMuted; }

    private void setGain(float value) {
        if (bgmClip != null && bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(value);
        }
    }
}