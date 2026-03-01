package com.game.multi.dating;

import java.util.ArrayList;
import java.util.List;

public class MultiDatingEventManager {
        private final List<MultiDatingEvent> events = new ArrayList<>();
        private int index = 0;

        public MultiDatingEventManager() {
                // ✅ ปรับชื่อไฟล์ BG ให้ตรงกับโครงสร้าง resources/images/backgrounds ที่คุณส่งมา
                events.add(new MultiDatingEvent("คาเฟ่.jpg", "ขอโทษนะ เราไม่ทันมองทาง...",
                                "ไม่เป็นไร เราเองก็รีบเหมือนกัน", 5, "คราวหน้าระวังหน่อยนะ", 2, "เอ่อ... ไม่เป็นไร",
                                1));

                events.add(new MultiDatingEvent("หน้าโรงเรียน.png", "เรา ชื่อ 'คิม แจฮยอน' นะ แล้วเธอล่ะ?",
                                "เราชื่อ... ยินดีที่ได้รู้จักนะ", 5, "เรียกเราว่า... ก็พอ", 2, "อืม...", 0));

                events.add(new MultiDatingEvent("ห้องเรียน.jpg", "วันนี้เรียนหนักมากเลย เหนื่อยสุด ๆ",
                                "สู้ ๆ นะ เธอเก่งอยู่แล้ว", 5, "ทุกคนก็เหนื่อยแหละ", 1, "ก็พักบ้างสิ", 3));

                events.add(new MultiDatingEvent("มุมตึก.png", "หิวจัง ยังไม่ได้กินอะไรเลย",
                                "ไปกินด้วยกันไหม เราเลี้ยง", 5, "งั้นรีบไปกินสิ", 2, "เราก็หิวเหมือนกัน", 3));

                events.add(new MultiDatingEvent("ห้องนอน.jpg", "อ๊ะ! หนังสือตกหมดเลย",
                                "ช่วยเก็บทันที", 5, "มองแล้วค่อยช่วย", 2, "ยืนเฉย", 0));

                // สำหรับเหตุการณ์ที่เหลือ
                // ผมจะใช้ภาพที่มีในระบบวนสลับกันเพื่อให้ภาพเปลี่ยนทุกเหตุการณ์ครับ
                events.add(new MultiDatingEvent("คาเฟ่.jpg", "เธอใจดีจังเลยนะ", "เฉพาะกับเธอแหละ", 5, "ก็ปกตินะ", 2,
                                "ไม่หรอก", 3));
                events.add(new MultiDatingEvent("หน้าโรงเรียน.png", "ฝนตกแรงจัง เราไม่ได้เอาร่มมา...",
                                "มากับเรา เรามีร่ม", 5, "วิ่งดีไหม", 2, "โชคดีนะ", 0));
                events.add(new MultiDatingEvent("ห้องเรียน.jpg", "วันนี้สนุกดีนะ", "อยู่กับเธอก็สนุกตลอดแหละ", 5,
                                "ก็ดีนะ", 2, "อืม", 0));
                events.add(new MultiDatingEvent("มุมตึก.png", "เราง่วงมากเลยเมื่อคืนไม่ได้นอน", "งั้นพักก่อนก็ได้นะ", 5,
                                "ก็ไปนอนสิ", 1, "เราก็ง่วงเหมือนกัน", 2));
                events.add(new MultiDatingEvent("บ้าน.png", "เธอคิดว่าเราเป็นคนยังไงเหรอ?", "เป็นคนน่ารักมากเลยนะ", 5,
                                "ก็โอเคนะ", 2, "ไม่รู้สิ", 0));

                events.add(new MultiDatingEvent("ห้องเรียน.jpg", "กลัวสอบไม่ผ่านเลย...", "เราช่วยติวให้ได้นะ", 5,
                                "อย่าคิดมาก", 2, "ก็ต้องอ่านสิ", 1));
                events.add(new MultiDatingEvent("คาเฟ่.jpg", "เธอตลกดีนะ", "อยากเห็นเธอยิ้มบ่อย ๆ", 5, "แน่นอนอยู่แล้ว",
                                2, "เหรอ", 1));
                events.add(new MultiDatingEvent("หน้าโรงเรียน.png", "เธอมีคนที่ชอบหรือยัง?", "มีแล้วนะ... (มองตัวละคร)",
                                5, "ยังเลย", 3, "ไม่บอกหรอก", 1));
                events.add(new MultiDatingEvent("บ้าน.png", "ทำไมต้องมองเราขนาดนั้น...", "ก็เธน่ารักนี่", 5, "เปล่านะ",
                                2, "หัวเราะ", 1));
                events.add(new MultiDatingEvent("มุมตึก.png", "โอ๊ะ!", "จับมือไว้", 5, "ถามว่าโอเคไหม", 3, "มองเฉย",
                                0));

                events.add(new MultiDatingEvent("คาเฟ่.jpg", "วันนี้เรารู้สึกไม่ค่อยดีเลย...", "เรานั่งเป็นเพื่อนได้นะ",
                                5, "เดี๋ยวก็หาย", 2, "อืม", 0));
                events.add(new MultiDatingEvent("บ้าน.png", "ดีใจนะที่ได้เจอเธอ", "เราก็ดีใจเหมือนกัน", 5, "อืม", 2,
                                "เหรอ", 0));
                events.add(new MultiDatingEvent("หน้าโรงเรียน.png", "วันนี้เราสนุกมากเลย", "เราก็เหมือนกัน อยากเจออีก",
                                5, "ก็ดี", 2, "อืม", 0));
                events.add(new MultiDatingEvent("มุมตึก.png", "งั้นเรากลับก่อนนะ", "ให้เราไปส่งไหม", 5, "กลับดี ๆ นะ",
                                3, "โอเค", 1));
                events.add(new MultiDatingEvent("หน้าโรงเรียน.png", "วันนี้...เรารู้สึกดีกับเธอนะ",
                                "เราก็รู้สึกเหมือนกัน", 10, "ดีใจนะ", 5, "อืม...", 1));
        }

        public MultiDatingEvent getCurrent() {
                if (index >= events.size())
                        return events.get(events.size() - 1);
                return events.get(index);
        }

        public void next() {
                index++;
        }

        public boolean isFinished() {
                return index >= events.size();
        }
}