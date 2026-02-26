# Resources Directory

โฟลเดอร์นี้เก็บไฟล์ resources ทั้งหมดของเกม

## โครงสร้าง

### images/

เก็บไฟล์รูปภาพทั้งหมด

- **characters/** - รูปตัวละคร (character sprites, expressions)
- **backgrounds/** - รูปฉากหลัง (locations, rooms)
- **ui/** - รูป UI elements (buttons, frames, icons)

รองรับไฟล์: `.png`, `.jpg`, `.jpeg`

### sounds/

เก็บไฟล์เสียงทั้งหมด

- **bgm/** - เพลงประกอบ (background music)
- **sfx/** - เสียงเอฟเฟค (sound effects, voice clips)

รองรับไฟล์: `.mp3`, `.wav`, `.ogg`

### data/

เก็บไฟล์ข้อมูลเกม

- `characters.json` - ข้อมูลตัวละครทั้งหมด
- `game_config.json` - การตั้งค่าเกม
- อื่นๆ เช่น events, items, locations

### dialogues/

เก็บไฟล์บทสนทนา (JSON format)

- แต่ละ dialogue จะมี ID เฉพาะ
- รองรับ branching conversations
- เชื่อมโยงกับระบบ choices

### saves/

เก็บไฟล์เซฟเกมของผู้เล่น

- Auto-generated จากระบบ SaveSystem
- ไม่ควน commit ลง Git (ดู .gitignore)

## การใช้งาน

### โหลดรูปภาพ

```java
ImageIcon charSprite = new ImageIcon(getClass().getResource("/images/characters/char_001.png"));
```

### โหลดไฟล์ JSON

```java
InputStream is = getClass().getResourceAsStream("/data/characters.json");
// Parse JSON using library (Gson, Jackson, etc.)
```

### โหลดเสียง

```java
AudioInputStream audio = AudioSystem.getAudioInputStream(
    getClass().getResource("/sounds/bgm/main_theme.mp3")
);
```

## คำแนะนำ

1. **ขนาดรูปภาพ**
   - Character sprites: 800x1200px (แนะนำ)
   - Backgrounds: 1920x1080px
   - UI elements: ตามความเหมาะสม

2. **ไฟล์เสียง**
   - BGM: ควรเป็น loop ได้
   - SFX: ควรสั้นกระชับ
   - แนะนำใช้ `.ogg` สำหรับประสิทธิภาพ

3. **ไฟล์ JSON**
   - ใช้ UTF-8 encoding
   - Format ให้อ่านง่าย (pretty print)
   - ตรวจสอบ syntax ก่อน commit
