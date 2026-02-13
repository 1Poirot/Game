# คู่มือการพัฒนา (Development Guide)

## การเริ่มต้น

### 1. ติดตั้ง Requirements

- Java Development Kit (JDK) 8 หรือสูงกว่า
- IDE แนะนำ: IntelliJ IDEA, Eclipse, หรือ VS Code
- Git สำหรับ version control

### 2. Clone Project

```bash
git clone <repository-url>
cd Game
```

### 3. Import Project

- เปิดโปรเจกต์ใน IDE
- ตั้งค่า Project SDK เป็น Java 8+
- Mark `src/main/java` เป็น Sources Root
- Mark `src/main/resources` เป็น Resources Root
- Mark `src/test/java` เป็น Test Sources Root

## โครงสร้างโค้ด

### Package Organization

```
com.game
├── Main.java               # Entry point
├── models/                 # Data models
├── systems/                # Game systems
│   ├── dialogue/
│   ├── choice/
│   ├── affection/
│   ├── ending/
│   └── save/
├── controllers/            # Game controllers
├── ui/                     # User interface
└── utils/                  # Utilities
```

### Coding Standards

#### การตั้งชื่อ

- **Classes**: PascalCase (เช่น `GameController`)
- **Methods**: camelCase (เช่น `updateAffection`)
- **Constants**: UPPER_SNAKE_CASE (เช่น `MAX_TURNS`)
- **Variables**: camelCase (เช่น `playerName`)

#### Comments

- ใช้ JavaDoc สำหรับ public classes และ methods
- Comment เป็นภาษาไทยหรืออังกฤษก็ได้
- อธิบาย "ทำไม" มากกว่า "ทำอะไร"

```java
/**
 * อัพเดทคะแนนความสัมพันธ์และตรวจสอบ level
 * @param character ตัวละครที่จะอัพเดท
 * @param points คะแนนที่จะเพิ่ม/ลด
 */
public void updateAffection(Character character, int points) {
    // Implementation
}
```

## Workflow การพัฒนา

### 1. สร้าง Feature ใหม่

```bash
git checkout -b feature/feature-name
```

### 2. พัฒนา Feature

- เขียนโค้ดตามมาตรฐาน
- Test ให้แน่ใจว่าทำงานถูกต้อง
- เพิ่ม JavaDoc และ comments

### 3. Commit

```bash
git add .
git commit -m "Add: feature description"
```

### 4. Push และ Merge

```bash
git push origin feature/feature-name
# Create pull request for review
```

## การเพิ่มระบบใหม่

### ขั้นตอน:

1. **สร้าง Package** ใน `src/main/java/com/game/systems/`
2. **สร้าง Class** หลักของระบบ
3. **เชื่อมต่อกับ GameController**
4. **สร้าง Test Cases**
5. **อัพเดทเอกสาร**

### ตัวอย่าง: เพิ่มระบบ Inventory

```java
// 1. สร้างไฟล์ systems/inventory/InventorySystem.java
package com.game.systems.inventory;

public class InventorySystem {
    // Implementation
}

// 2. เพิ่มใน GameController.java
private InventorySystem inventorySystem;

public GameController() {
    this.inventorySystem = new InventorySystem();
    // ...
}
```

## การเพิ่มตัวละครใหม่

1. เพิ่มข้อมูลใน `resources/data/characters.json`
2. เพิ่มรูปภาพใน `resources/images/characters/`
3. สร้างไฟล์บทสนทนาใน `resources/dialogues/`

```json
{
  "id": "char_003",
  "name": "ชื่อตัวละคร",
  "description": "คำอธิบาย",
  "sprite_path": "images/characters/char_003.png",
  "personality": "cheerful"
}
```

## Testing

### Unit Tests

```bash
# Run all tests
java -cp junit.jar:bin org.junit.runner.JUnitCore com.game.AllTests
```

### Manual Testing Checklist

- [ ] เริ่มเกมใหม่ได้
- [ ] บทสนทนาแสดงถูกต้อง
- [ ] เลือกตัวเลือกได้
- [ ] คะแนนความสัมพันธ์เปลี่ยนตามการเลือก
- [ ] Save/Load ทำงานถูกต้อง
- [ ] ไปถึงตอนจบได้

## Debugging Tips

### Common Issues

**บทสนทนาไม่แสดง**

- ตรวจสอบ path ของไฟล์ dialogue
- ตรวจสอบ JSON syntax
- ดูว่า dialogue_id ถูกต้องหรือไม่

**รูปภาพไม่โหลด**

- ตรวจสอบ path (ต้องเริ่มด้วย `/`)
- ตรวจสอบว่าไฟล์อยู่ใน resources/
- ตรวจสอบชื่อไฟล์และ extension

**เซฟไม่ได้**

- ตรวจสอบ permissions ของโฟลเดอร์ saves/
- ตรวจสอบว่า serialization ถูกต้อง

## Build และ Distribution

### Compile

```bash
javac -d bin -sourcepath src/main/java src/main/java/com/game/**/*.java
```

### Create JAR

```bash
jar cvfe Game.jar com.game.Main -C bin . -C src/main/resources .
```

### Run JAR

```bash
java -jar Game.jar
```

## Resources

### Libraries แนะนำ

- **Gson** - JSON parsing
- **JavaFX** - Modern UI (Java 11+)
- **Swing** - Classic UI
- **JLayer** - Audio playback

### External References

- [Java Documentation](https://docs.oracle.com/javase/8/docs/)
- [JavaFX Documentation](https://openjfx.io/)

## Getting Help

หากพบปัญหาหรือมีคำถาม:

1. ตรวจสอบเอกสารนี้ก่อน
2. ดู source code ของระบบที่คล้ายกัน
3. Google error message
4. สอบถามทีมพัฒนา
