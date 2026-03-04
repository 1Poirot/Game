package com.game.systems.audio;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class AudioSystem {
    private Clip bgmClip;
<<<<<<< HEAD
    private String currentBGMPath ="";
    private String lastFileName = "Dream.wav"; // ✅ แทรก: เก็บชื่อไฟล์ล่าสุดที่ส่งเข้ามา (เช่น "audiotest.wav")
    private float currentVolume = 0.8f; 
=======
    private String currentBGMPath = "";
    private String lastFileName = ""; // ✅ แทรก: เก็บชื่อไฟล์ล่าสุดที่ส่งเข้ามา (เช่น "audiotest.wav")
    private float currentVolume = 0.8f;
>>>>>>> c9083940bf6486e9ed4a371c14605321fe80f71b

    // ================== ระบบปรับระดับเสียง ==================

    public void setVolume(float volume) {
        if (volume < 0f)
            volume = 0f;
        if (volume > 1f)
            volume = 1f;
        this.currentVolume = volume;

        if (bgmClip != null) {
            try {
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume != 0 ? volume : 0.0001) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } catch (Exception e) {
                System.err.println("AudioSystem: ไม่สามารถปรับระดับเสียงได้ -> " + e.getMessage());
            }
        }
    }

    public float getVolume() {
        return currentVolume;
    }

    // ✅ แทรก: เมธอดสำหรับดึงชื่อไฟล์เพลงปัจจุบัน (แก้ตัวแดงใน GameController)
    public String getCurrentBgmName() {
        return lastFileName;
    }

    // ================== ระบบเล่นเพลง ==================

    public void playBGM(String fileName) {
        this.lastFileName = fileName;

        // ✅ ตรวจสอบ Path ให้ฉลาดขึ้น
        String fullPath = fileName.startsWith("audio/") ? fileName : "audio/bgm/" + fileName;

        // ✅ ป้องกันการเล่นซ้ำถ้าเพลงเดิมกำลังเล่นอยู่
        if (fullPath.equals(currentBGMPath) && bgmClip != null && bgmClip.isRunning()) {
            return;
        }

        stopBGM();

        try {
            URL url = getClass().getClassLoader().getResource(fullPath);
            if (url == null)
                url = getClass().getResource("/" + fullPath);

            AudioInputStream audioInput;

            if (url != null) {
                audioInput = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
            } else {
                File file = new File("src/main/resources/" + fullPath);
                if (file.exists()) {
                    audioInput = javax.sound.sampled.AudioSystem.getAudioInputStream(file);
                } else {
                    System.err.println("AudioSystem Error: หาไฟล์ไม่เจอที่ -> " + fullPath);
                    return;
                }
            }

            // ✅ ตรวจสอบนามสกุลไฟล์ ถ้าเป็น mp3 ให้แจ้งเตือนทันที
            if (fullPath.toLowerCase().endsWith(".mp3")) {
                System.err.println("❌ AudioSystem Error: Java มาตรฐานไม่รองรับ .mp3 โปรดแปลงเป็น .wav");
                return;
            }

            bgmClip = javax.sound.sampled.AudioSystem.getClip();
            bgmClip.open(audioInput);
            setVolume(currentVolume);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
            currentBGMPath = fullPath;

        } catch (UnsupportedAudioFileException e) {
            System.err.println("❌ Error: นามสกุลไฟล์ไม่รองรับ (โปรดใช้ .wav) -> " + fullPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSFX(String fileName) {
        try {
            String fullPath = fileName.startsWith("audio/") ? fileName : "audio/sfx/" + fileName;
            URL url = getClass().getClassLoader().getResource(fullPath);
            if (url == null)
                url = getClass().getResource("/" + fullPath);

            if (url != null) {
                AudioInputStream audioInput = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
                Clip sfxClip = javax.sound.sampled.AudioSystem.getClip();
                sfxClip.open(audioInput);
                sfxClip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning())
                bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
            currentBGMPath = "";
            // lastFileName = ""; // ไม่ต้องล้างค่า
            // เพื่อให้หน้าตั้งค่าดึงไปใช้ต่อได้แม้เพลงหยุด
        }
    }
}