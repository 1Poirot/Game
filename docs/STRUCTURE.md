# โครงสร้างโปรเจกต์ Dating Simulation Game

## ภาพรวมโครงสร้าง

```
Game/
├── src/
│   ├── main/
│   │   ├── java/com/game/
│   │   │   ├── Main.java                    # Entry point หลัก
│   │   │   ├── models/                       # โมเดลข้อมูล
│   │   │   │   ├── Character.java           # ตัวละคร
│   │   │   │   └── Player.java              # ผู้เล่น
│   │   │   ├── systems/                      # ระบบต่างๆ
│   │   │   │   ├── dialogue/
│   │   │   │   │   └── DialogueSystem.java  # ระบบบทสนทนา
│   │   │   │   ├── choice/
│   │   │   │   │   └── ChoiceSystem.java    # ระบบเลือกคำตอบ
│   │   │   │   ├── affection/
│   │   │   │   │   └── AffectionSystem.java # ระบบคะแนนความสัมพันธ์
│   │   │   │   ├── ending/
│   │   │   │   │   └── EndingSystem.java    # ระบบหลายตอนจบ
│   │   │   │   └── save/
│   │   │   │       └── SaveSystem.java      # ระบบ Save/Load
│   │   │   ├── controllers/
│   │   │   │   └── GameController.java      # ควบคุม Game Loop
│   │   │   ├── ui/                          # User Interface
│   │   │   └── utils/                       # Utilities
│   │   └── resources/
│   │       ├── images/                      # รูปภาพ
│   │       │   ├── characters/              # รูปตัวละคร
│   │       │   ├── backgrounds/             # ฉากหลัง
│   │       │   └── ui/                      # UI elements
│   │       ├── sounds/                      # เสียง
│   │       │   ├── bgm/                     # เพลงพื้นหลัง
│   │       │   └── sfx/                     # เสียงเอฟเฟค
│   │       ├── data/                        # ข้อมูลเกม (JSON, XML)
│   │       ├── dialogues/                   # ไฟล์บทสนทนา
│   │       └── saves/                       # ไฟล์เซฟเกม
│   └── test/                                # Unit tests
│       └── java/com/game/
├── docs/                                    # เอกสารประกอบ
├── libs/                                    # External libraries
├── .gitignore
└── README.md
```

## ระบบหลัก

### 1. ระบบบทสนทนา (Dialogue System)

- จัดการการแสดงบทสนทนา
- โหลดบทสนทนาจากไฟล์
- แสดงรูปตัวละครประกอบ

### 2. ระบบเลือกคำตอบ (Choice System)

- แสดงตัวเลือกให้ผู้เล่น
- ประมวลผลการเลือกและส่งผลต่อเนื้อเรื่อง
- เปลี่ยนคะแนนความสัมพันธ์ตามการเลือก

### 3. ระบบคะแนนความสัมพันธ์ (Affection Points)

- ติดตามคะแนนความสัมพันธ์กับตัวละครแต่ละคน
- จัดอันดับความสัมพันธ์ (S, A, B, C, D)
- ปลดล็อกเหตุการณ์พิเศษเมื่อคะแนนถึงเกณฑ์

### 4. ระบบหลายตอนจบ (Multiple Ending)

- คำนวณตอนจบตามคะแนนความสัมพันธ์
- ตอนจบหลายแบบ: True, Good, Normal, Bad, Alone
- แสดง cutscene/ข้อความตอนจบ

### 5. ระบบ Save/Load

- บันทึกความคืบหน้าเกม
- โหลดเกมที่บันทึกไว้
- จัดการ save slots หลายช่อง

## Game Loop

```
เริ่มเกม
  ↓
สนทนา
  ↓
เลือกคำตอบ
  ↓
คะแนนความสัมพันธ์เปลี่ยน
  ↓
ตำแหน่อต่อเนื่อง
  ↓
จบเกม
```

## การพัฒนาต่อ

### TODO List

- [ ] สร้าง UI components (Swing/JavaFX)
- [ ] ออกแบบไฟล์ dialogue format (JSON/XML)
- [ ] สร้างตัวละครและเนื้อเรื่อง
- [ ] เพิ่มระบบ achievements
- [ ] เพิ่ม BGM และ sound effects
- [ ] สร้าง character sprites และ backgrounds
- [ ] เขียน unit tests
- [ ] เพิ่มระบบ auto-save
- [ ] สร้าง gallery mode
- [ ] เพิ่มระบบ skip และ auto-play

## การรันโปรเจกต์

```bash
# Compile
javac -d bin src/main/java/com/game/**/*.java

# Run
java -cp bin com.game.Main
```

## Dependencies

- Java 8+
- (เพิ่มเติมในอนาคต: JavaFX/Swing สำหรับ UI)
