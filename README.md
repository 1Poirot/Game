# เกมจีบสาว — Love Game (Dating Simulation)

เกม Dating Simulation แบบเทิร์นเบส สร้างด้วยภาษา Java Swing  
รองรับโหมดเดี่ยว, 3 ผู้เล่น (Local), และโหมดออนไลน์ผ่าน TCP Socket

---

## 🚀 วิธีรันโปรเจกต์

### โหมดเดี่ยว

```bash
javac -d out src/main/java/com/game/**/*.java
java -cp out com.game.Main
```

### โหมดออนไลน์ (เปิด Server ก่อน)

```bash
# Host รันก่อน
javac -d out src/main/java/com/game/network/GameServer.java
java -cp out com.game.network.GameServer

# แล้วทุกคนรันเกมแล้วกด "เล่นออนไลน์"
```

> 💡 ใน VS Code กด ▶ Run บนไฟล์ได้เลย ไม่ต้อง compile เอง

---

## 📋 ระบบหลัก

| ระบบ                                  | สถานะ                     |
| ------------------------------------- | ------------------------- |
| บทสนทนา (DialogueSystem)              | ✅ สมบูรณ์                |
| ร้านค้า (ShopSystem)                  | ✅ สมบูรณ์                |
| บันทึกเกม (SaveSystem)                | ✅ สมบูรณ์                |
| ระบบเสียง (AudioSystem)               | ✅ สมบูรณ์                |
| Multiplayer (GameServer + GameClient) | ✅ สมบูรณ์                |
| คะแนนความสัมพันธ์ (AffectionSystem)   | 🔴 TODO                   |
| ระบบตอนจบ (EndingSystem)              | 🔴 TODO                   |
| ตัวเลือก (ChoiceSystem)               | 🟡 UI มีแล้ว ยังไม่เชื่อม |

---

## 📚 เอกสารรายละเอียด

- [DEV_DOCS.md](DEV_DOCS.md) — คู่มือนักพัฒนาฉบับเต็ม (ภาษาไทย)
- [STRUCTURE.md](STRUCTURE.md) — โครงสร้างโปรเจกต์
- [DEVELOPMENT.md](DEVELOPMENT.md) — แนวทางการพัฒนา

---

## Requirements

- Java 11 หรือสูงกว่า
- ไม่มี dependency ภายนอก (ใช้ Java Standard Library ล้วน)

## License

สำหรับการพัฒนาส่วนตัว
