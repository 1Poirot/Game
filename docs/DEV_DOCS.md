# 📚 เอกสารนักพัฒนา — Love Game (ศึกชิงนาง)

> **สำหรับคนที่จะมาทำต่อ** — อ่านให้จบก่อนแตะโค้ดนะครับ 🙏

---

## 📋 สารบัญ

1. [ภาพรวมโปรเจกต์](#ภาพรวมโปรเจกต์)
2. [โครงสร้างโฟลเดอร์](#โครงสร้างโฟลเดอร์)
3. [สถาปัตยกรรมระบบ (Architecture)](#สถาปัตยกรรมระบบ)
4. [ระบบหลักต่าง ๆ](#ระบบหลักต่างๆ)
5. [โปรโตคอล Multiplayer](#โปรโตคอล-multiplayer)
6. [วิธีรันโปรเจกต์](#วิธีรันโปรเจกต์)
7. [สิ่งที่ยังต้องทำ (TODO)](#สิ่งที่ยังต้องทำ)
8. [ข้อควรระวัง](#ข้อควรระวัง)

---

## ภาพรวมโปรเจกต์

**Love Game** เป็นเกม Dating Simulation สไตล์ Visual Novel ที่สร้างด้วย Java Swing  
ผู้เล่น 1-3 คนแข่งกันสะสม "คะแนนความสัมพันธ์" กับตัวละคร NPC ภายในเวลาที่กำหนด

**ฟีเจอร์หลัก:**

- โหมดเดี่ยว (Single Player) — เล่น Visual Novel บทสนทนา
- โหมดกลุ่ม (Local 3-Player) — ผลัดกันเล่นบนเครื่องเดียวกัน
- โหมดออนไลน์ (Multiplayer) — สูงสุด 3 คน ผ่าน TCP Socket

---

## โครงสร้างโฟลเดอร์

```
Game/
├── src/main/java/com/game/
│   ├── Main.java                    ← จุดเริ่มต้นโปรแกรม
│   ├── controllers/
│   │   └── GameController.java      ← ควบคุมการเปลี่ยนหน้าจอและ state ทั้งหมด ⭐
│   ├── models/
│   │   ├── Player.java              ← ข้อมูลผู้เล่น (ชื่อ, เงิน)
│   │   ├── Character.java           ← ข้อมูล NPC (ชื่อ, คะแนนความสัมพันธ์)
│   │   └── Item.java                ← ไอเทมในร้านค้า
│   ├── ui/                          ← หน้าจอต่าง ๆ (JPanel ทั้งหมด)
│   │   ├── MenuGame.java            ← หน้าเมนูหลัก
│   │   ├── Changescene.java         ← หน้าตัดฉาก / เปลี่ยนฉาก
│   │   ├── GamePanel.java           ← หน้าเกมหลัก (เลือกคุย/ให้ของ)
│   │   ├── ShopScreen.java          ← ร้านค้า
│   │   ├── SaveScreen.java          ← บันทึก/โหลดเกม
│   │   ├── SettingsScreen.java      ← หน้าตั้งค่า
│   │   ├── AudioSettingsScreen.java ← ตั้งค่าเสียง
│   │   ├── LobbyDialog.java         ← Dialog กรอก IP ก่อนเล่นออนไลน์
│   │   └── MultiplayerScreen.java   ← หน้าเกม Multiplayer
│   ├── network/
│   │   ├── GameClient.java          ← Client TCP สำหรับ Multiplayer ⭐
│   │   └── GameServer.java          ← เซิร์ฟเวอร์ Multiplayer (รันแยกต่างหาก) ⭐
│   ├── systems/
│   │   ├── audio/AudioSystem.java          ← เล่น BGM / SFX
│   │   ├── save/SaveSystem.java            ← บันทึก/โหลด Save file
│   │   ├── shop/ShopSystem.java            ← ระบบร้านค้า
│   │   ├── dialogue/
│   │   │   ├── DialogueSystem.java              ← Visual Novel engine
│   │   │   └── DialogueSystemAndChoice.java     ← Wrapper สำหรับ DialogueSystem
│   │   ├── affection/AffectionSystem.java   ← คะแนนความสัมพันธ์ (ยังไม่สมบูรณ์)
│   │   ├── choice/ChoiceSystem.java         ← ระบบตัวเลือก (ยังไม่เชื่อมกับเกม)
│   │   └── ending/EndingSystem.java         ← ระบบตอนจบ (ยังไม่สมบูรณ์)
│   └── multi/dating/                ← โหมด Multi-Dating แบบ local
│       ├── MultiDatingScreen.java
│       ├── MultiDatingTimer.java
│       ├── MultiDatingSound.java
│       └── ...
└── src/main/resources/
    ├── images/
    │   ├── backgrounds/             ← รูปฉากหลัง (.png/.jpg)
    │   ├── Characters/              ← รูปตัวละคร (.png)
    │   └── icon/                   ← ไอคอน UI
    └── audio/
        ├── bgm/                    ← เพลงประกอบ (.wav)  ⚠️ ไฟล์ใหญ่มาก >50MB
        └── sfx/                    ← เสียงเอฟเฟกต์ (.wav)
```

---

## สถาปัตยกรรมระบบ

โปรเจกต์ใช้รูปแบบ **MVC (Model-View-Controller)** อย่างหลวม ๆ:

```
┌─────────────────────────────────────────────────────┐
│                   GameController                     │  ← สมองหลัก
│  - จัดการ JFrame หน้าต่างเดียว (Single Frame)       │
│  - เปลี่ยนหน้าจอด้วย setContentPane()               │
│  - เก็บ state: players[], currentPlayerIndex         │
└────────────┬────────────────────────────────────────┘
             │ สร้าง/สั่งงาน
    ┌────────┼────────────────────┐
    │        │                   │
 Models    Views              Systems
 Player    MenuGame           AudioSystem
 Character Changescene        SaveSystem
 Item      GamePanel          ShopSystem
           MultiplayerScreen  AffectionSystem
```

**กฎสำคัญ:** ทุก JPanel (View) **ต้อง** รับ `GameController` ผ่าน constructor  
แล้วเรียก `controller.showXxx()` เพื่อเปลี่ยนหน้า — ห้าม `new JFrame()` เพิ่มเองโดยเด็ดขาด  
(ยกเว้น `MultiplayerScreen` และ `Changescene` ที่มี JFrame ของตัวเอง)

---

## ระบบหลักต่างๆ

### 🎮 GameController — ควบคุมทุกอย่าง

```java
// เปลี่ยนหน้าจอ
controller.showMainMenu();      // → MenuGame
controller.showGameScene();     // → Changescene (เปิด JFrame ใหม่)
controller.showShop();          // → ShopScreen
controller.showSettings();      // → SettingsScreen
controller.showAudioSettings(); // → AudioSettingsScreen
controller.showMultiplayer();   // → LobbyDialog → MultiplayerScreen

// ดึงข้อมูลผู้เล่น
controller.getPlayer();         // ผู้เล่นที่กำลังเล่นอยู่ตอนนี้
controller.getAllPlayers();      // List ผู้เล่นทั้ง 3 คน
controller.nextTurn();          // สลับไปยังผู้เล่นคนถัดไป
```

### 🔊 AudioSystem — ระบบเสียง

```java
AudioSystem audio = controller.getAudioSystem();
audio.playBGM("audiotest.wav");   // เล่น BGM (วนซ้ำ)
audio.stopBGM();                   // หยุด BGM
audio.playSFX("click.wav");        // เสียงเอฟเฟกต์ (ไม่วน)
audio.setVolume(0.8f);             // ปรับเสียง (0.0 - 1.0)
```

> ⚠️ ไฟล์เสียงอยู่ที่ `src/main/resources/audio/bgm/` และ `.../sfx/`  
> BGM ขนาดใหญ่มาก (>50MB) — GitHub แนะนำให้ใช้ Git LFS

### 💾 SaveSystem — บันทึกเกม

```java
// บันทึก slot 1
SaveSystem.saveToFile(1, "ชื่อผู้เล่น", 1000, "01/03/2026 22:00");

// โหลด slot 1 → คืน Map<String,String> หรือ null ถ้าไม่มีข้อมูล
Map<String, String> data = SaveSystem.loadFromLocal(1);
// data.get("Name") | data.get("Money") | data.get("Date")
```

### 💬 DialogueSystem — Visual Novel Engine

```java
// เปิดหน้าต่างบทสนทนาแบบ standalone
new DialogueSystemAndChoice().CREATEANDSHOWGUI();
```

บทสนทนาทั้งหมดอยู่ใน `DialogueSystem.BUILD_STORY()` — เพิ่ม Scene ได้ด้วย:

```java
ADD_SCENE("S99", "ชื่อตัวละคร", "Day 1", "ข้อความบทสนทนา", "S100"); // S100 = scene ถัดไป
// ถ้า NEXT = null -> จบบทสนทนา
```

---

## โปรโตคอล Multiplayer

### การเชื่อมต่อ

```
Host: รัน GameServer.java ก่อน (port 9090)
Client: เปิดเกม → "เล่นออนไลน์" → ใส่ IP ของ Host → เชื่อมต่อ
```

### ลำดับการสื่อสาร

```
Client                          Server
  |── "ชื่อผู้เล่น" ──────────────→|  (บรรทัดแรกหลัง connect)
  |←── PLAYER_LIST:คน1,คน2 ───────|
  |←── SYSTEM:ข้อความต้อนรับ ──────|

  | (เมื่อ Host กด Start)
  |── "START_GAME" ───────────────→|
  |←── START ──────────────────────|  (broadcast ทุกคน)

  | (เมื่อเล่นจบ)
  |── "FINISH:95" ────────────────→|  (ส่งคะแนน)
  |←── FINAL_SCORE ────────────────|
  |←── SCORE:คน1:95 ───────────────|
  |←── SCORE:คน2:80 ───────────────|
```

### MessageListener — รับ Event จาก Server

ทุกคลาสที่ต้องรับข้อมูลจาก Server ให้ implement `GameClient.MessageListener`:

```java
public interface MessageListener {
    void onPlayerListUpdate(List<String> players); // รายชื่อผู้เล่นเปลี่ยน
    void onGameStart();                             // เกมเริ่ม
    void onRejected(String reason);                 // ห้องเต็ม
    void onSystemMessage(String message);           // ข้อความระบบ
    void onConnectionFailed(String ip);             // เชื่อมต่อไม่ได้
    void onScoreUpdate(String message);             // คะแนนอัปเดต
    void onWinner(String winnerName);               // ประกาศผู้ชนะ
    void onFinalScore();                            // ประกาศเริ่มสรุปคะแนน
    void onFinalScoreItem(String playerName, int score); // คะแนนรายบุคคล
}
```

---

## วิธีรันโปรเจกต์

### โหมดเดี่ยว

```bash
# รัน Main.java
javac -cp src/main/java src/main/java/com/game/Main.java
java -cp src/main/java com.game.Main
```

### โหมดออนไลน์ (ต้องรัน 2 ขั้นตอน)

```bash
# ขั้นตอนที่ 1: Host รัน Server ก่อน (GameServer อยู่ใน package com.game.network)
javac -d out src/main/java/com/game/network/GameServer.java
java -cp out com.game.network.GameServer

# หรือใน VS Code: เปิดไฟล์ GameServer.java แล้วคลิก ▶ Run ด้านบน

# ขั้นตอนที่ 2: ทุกคนรันเกมปกติ แล้วกด "เล่นออนไลน์"
# ใส่ IP ของ Host (Host ใส่ localhost)
```

> 💡 ใช้ VS Code + Extension Pack for Java จะง่ายกว่ามาก — กด ▶ Run ได้เลย

---

## สิ่งที่ยังต้องทำ

| ระบบ                 | ไฟล์                                  | สถานะ       | รายละเอียด                                  |
| -------------------- | ------------------------------------- | ----------- | ------------------------------------------- |
| AffectionSystem      | `affection/AffectionSystem.java`      | 🔴 TODO     | ยังไม่ได้กำหนด Level และ Event Trigger      |
| EndingSystem         | `ending/EndingSystem.java`            | 🔴 TODO     | ยังไม่ได้ implement ตรรกะตอนจบจริง          |
| ChoiceSystem         | `choice/ChoiceSystem.java`            | 🟡 บางส่วน  | มี UI แล้วแต่ยังไม่เชื่อมกับ GameController |
| MultiDatingScreen    | `multi/dating/MultiDatingScreen.java` | 🟡 บางส่วน  | โหมด Local 3-Player ยังไม่สมบูรณ์           |
| Score ใน Multiplayer | `MultiplayerScreen.java`              | 🟡 บางส่วน  | รับคะแนนจาก Server ได้แล้วแต่ยังไม่ส่งขึ้น  |
| ระบบ Day/Time        | —                                     | 🔴 ยังไม่มี | ควรมีระบบการผ่านวันและการจำกัดเวลา          |
| Database/Cloud Save  | —                                     | 🔴 ยังไม่มี | ปัจจุบัน Save เป็น local file เท่านั้น      |

---

## ข้อควรระวัง

### 🚨 อย่าลืม

1. **ไฟล์เสียง** — `audiotest.wav` และ `audiotest2.wav` ใหญ่เกิน 50MB  
   GitHub จะ warn แต่ยัง push ได้ — พิจารณาใช้ **Git LFS** ถ้า repo โต

2. **Thread Safety** — `GameClient` ส่ง event จาก background thread  
   ทุกการอัปเดต UI **ต้องอยู่ใน** `SwingUtilities.invokeLater()` เสมอ!

   ```java
   // ✅ ถูกต้อง
   SwingUtilities.invokeLater(() -> label.setText("hello"));

   // ❌ อันตราย — อาจ crash หรือ UI ค้าง
   label.setText("hello"); // เรียกจาก background thread
   ```

3. **Single Frame Pattern** — `GameController` ใช้ `JFrame` เดียวสลับ `JPanel`  
   ห้ามสร้าง `JFrame` เพิ่มเองใน JPanel ปกติ (ยกเว้น `Changescene` และ `MultiplayerScreen`)

4. **Lambda Capture vs Field** — หลายคลาสเช่น `SaveScreen`, `AudioSettingsScreen`  
   ใช้ constructor parameter โดยตรงใน lambda — ไม่จำเป็นต้องเก็บเป็น field

5. **Port 9090** — ถ้ารันบน Windows และ port ถูกใช้งาน:
   ```powershell
   netstat -ano | findstr :9090
   taskkill /PID <PID> /F
   ```

### 📌 Convention โค้ด

- ชื่อไฟล์/Class: `PascalCase`
- ชื่อเมธอด/ตัวแปร: `camelCase`
- Comment ภาษาไทยได้ — ไฟล์ต้องเป็น **UTF-8**
- UI Panel ทุกอันต้อง implements ผ่าน `GameController` (Dependency Injection)

---

_อัปเดตล่าสุด: มีนาคม 2026 — หากมีคำถามดูที่ commit history หรือ Issues บน GitHub_
