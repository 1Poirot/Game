package com.game.multi.dating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiDatingEvent {
    private String background;
    private String dialog;
    private String defaultExpression;
    // ✅ เก็บเป็น List ของ Choice แทนตัวแปรแยก
    private List<Choice> choices = new ArrayList<>();

    // ✅ คลาสภายในเพื่อเก็บข้อความคู่กับคะแนน
    public static class Choice {
        String text;
        int score;

        public Choice(String text, int score) {
            this.text = text;
            this.score = score;
        }
    }

    public MultiDatingEvent(String bg, String dlg, String cA, int sA, String cB, int sB, String cC, int sC,
            String exp) {
        this.background = bg;
        this.dialog = dlg;
        this.defaultExpression = exp;
        // เพิ่มคำตอบลง List
        this.choices.add(new Choice(cA, sA));
        this.choices.add(new Choice(cB, sB));
        this.choices.add(new Choice(cC, sC));

        // ✅ สลับตำแหน่งทันทีที่สร้าง Event
        Collections.shuffle(this.choices);
    }

    public String getBackground() {
        return background;
    }

    public String getDialog() {
        return dialog;
    }

    public String getDefaultExpression() {
        return defaultExpression;
    }

    // ✅ เพิ่มเมธอดสำหรับดึงข้อมูลตามลำดับที่สลับแล้ว
    public String getChoiceText(int i) {
        return choices.get(i).text;
    }

    public int getChoiceScore(int i) {
        return choices.get(i).score;
    }
}