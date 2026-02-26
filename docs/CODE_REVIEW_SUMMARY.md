# สรุปการรีวิวโค้ด (Code Review Summary)

**วันที่รีวิว**: 17 กุมภาพันธ์ 2026  
**จำนวนไฟล์ที่รีวิว**: 21 ไฟล์ Java  
**คะแนนรวม**: 6.9/10

---

## 📊 คะแนนแยกตามส่วน

| Component                        | คะแนน | สถานะ                 |
| -------------------------------- | ----- | --------------------- |
| Main.java                        | 10/10 | ✅ Perfect            |
| GameController                   | 8/10  | ✅ ดี                 |
| Models (Player, Character, Item) | 8/10  | ✅ ดี                 |
| ShopSystem                       | 6/10  | ⚠️ ต้องปรับปรุง       |
| SaveSystem                       | 5/10  | ⚠️ ต้องปรับปรุง       |
| DialogueSystemAndChoice          | 4/10  | ⚠️ ต้อง refactor ด่วน |
| UI Screens                       | 7/10  | ✅ ดี                 |

---

## ✅ จุดเด่นของโค้ด

1. **โครงสร้าง MVC ชัดเจน**
   - แยก Models, Controllers, Systems, UI ออกจากกันดี
   - ง่ายต่อการทำความเข้าใจ

2. **ใช้ Swing UI**
   - มีระบบ UI พื้นฐานครบถ้วน
   - UI design สวยงาม มีสีสันดี

3. **System แยกกันดี**
   - แต่ละ system อยู่ใน package ของตัวเอง
   - แยก concern ชัดเจน

4. **มี Documentation**
   - มี README.md และ STRUCTURE.md
   - มี Javadoc ในบางส่วน

5. **รองรับภาษาไทย**
   - ใช้ UTF-8 encoding
   - มี Thai font support

---

## ⚠️ ข้อควรปรับปรุง

### 1. DialogueSystemAndChoice.java (ความสำคัญ: 🔴 สูงมาก)

**ปัญหา**:

- ไฟล์ใหญ่เกินไป (407 บรรทัด)
- มี nested classes มากเกินไป (4 classes)
- ใช้ชื่อ method แบบ UPPERCASE ไม่ตาม Java convention
- Hard-coded story data

**แนะนำ**:

```
dialogue/
  ├─ DialogueSystem.java      (หลัก)
  ├─ Scene.java               (โมเดล scene)
  ├─ DialoguePanel.java       (UI panel)
  ├─ BackgroundView.java      (UI background)
  └─ ChoiceButton.java        (UI button)
```

### 2. SaveSystem.java (ความสำคัญ: 🔴 สูง)

**ปัญหา**:

- บันทึกไฟล์ใน project directory (ควรใช้ user home)
- ไม่ได้บันทึก inventory
- ใช้ plain text แทน JSON

**แนะนำ**:

```java
// ใช้ user home directory
String userHome = System.getProperty("user.home");
Path savePath = Paths.get(userHome, ".datinggame", "saves");

// ใช้ JSON format
{
  "name": "Hero",
  "money": 100,
  "inventory": {"item_chocolate": 2},
  "date": "2026-02-17"
}
```

### 3. ShopSystem.java (ความสำคัญ: 🟡 กลาง)

**ปัญหา**:

- Hard-coded item data

**แนะนำ**:

- สร้าง `items.json` ใน `src/main/resources/data/`
- สร้าง ItemLoader class

```json
{
  "items": [
    {
      "id": "item_chocolate",
      "name": "Chocolate Bar",
      "price": 50,
      "imagePath": "546546546.jpg",
      "affectionBoost": 3
    }
  ]
}
```

### 4. ระบบที่ยังไม่ได้ implement (ความสำคัญ: 🟡 กลาง)

**ไฟล์ที่ยังว่างเปล่า**:

- `AffectionSystem.java` - ระบบคะแนนความสัมพันธ์
- `ChoiceSystem.java` - ระบบเลือกคำตอบ
- `EndingSystem.java` - ระบบหลายตอนจบ

**แนะนำ**: Implement ตาม spec ใน STRUCTURE.md

### 5. Dead Code (ความสำคัญ: 🟢 ต่ำ)

**ใน Item.java**:

```java
private Item item;        // ❌ ไม่ได้ใช้
private Image itemImage;  // ❌ ไม่ได้ใช้
```

**แนะนำ**: ลบทิ้ง

---

## 🎯 แผนปรับปรุง (Improvement Roadmap)

### Phase 1: แก้ไขปัญหาด่วน (2-3 วัน)

- [ ] Refactor DialogueSystemAndChoice.java
- [ ] แก้ GamePanel.java compilation error
- [ ] ปรับปรุง SaveSystem ให้บันทึก inventory

### Phase 2: ปรับปรุงคุณภาพ (1 สัปดาห์)

- [ ] สร้าง items.json และ ItemLoader
- [ ] Implement AffectionSystem, ChoiceSystem, EndingSystem
- [ ] ลบ dead code

### Phase 3: เพิ่มฟีเจอร์ (2 สัปดาห์)

- [ ] เขียน Unit Tests
- [ ] สร้าง ResourceManager
- [ ] ทำให้ UI responsive

---

## 📈 แนวทางการพัฒนาต่อ

### Best Practices ที่ควรปฏิบัติ

1. **Separation of Concerns**
   - แยก logic, data, และ UI
   - ใช้ design patterns ที่เหมาะสม

2. **Configuration Management**
   - ใช้ JSON/XML สำหรับ config
   - ไม่ hard-code values

3. **Error Handling**
   - เพิ่ม try-catch ที่จำเป็น
   - แสดง error message ที่เป็นประโยชน์

4. **Testing**
   - เขียน unit tests
   - Test edge cases

5. **Documentation**
   - อัปเดต Javadoc
   - เขียน inline comments สำหรับโค้ดซับซ้อน

---

## 📚 Resources

- [Java Naming Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html)
- [Effective Java (Joshua Bloch)](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Clean Code (Robert C. Martin)](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)

---

**สรุป**: โปรเจกต์มีโครงสร้างพื้นฐานที่ดี แต่ต้องการ refactoring ในบางส่วน โดยเฉพาะ DialogueSystemAndChoice.java และ SaveSystem.java หากแก้ไขตามแนะนำ โปรเจกต์จะมีคุณภาพสูงขึ้นและง่ายต่อการพัฒนาต่อ
