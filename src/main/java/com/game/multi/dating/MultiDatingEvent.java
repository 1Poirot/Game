package com.game.multi.dating;

public class MultiDatingEvent {
    private String background, dialog;
    private String choiceA, choiceB, choiceC;
    private int scoreA, scoreB, scoreC;
    private String defaultExpression;

    public MultiDatingEvent(String bg, String dialog, String cA, int sA, String cB, int sB, String cC, int sC,
            String exp) {
        this.background = bg;
        this.dialog = dialog;
        this.choiceA = cA;
        this.scoreA = sA;
        this.choiceB = cB;
        this.scoreB = sB;
        this.choiceC = cC;
        this.scoreC = sC;
        this.defaultExpression = exp;
    }

    // Getters
    public String getBackground() {
        return background;
    }

    public String getDialog() {
        return dialog;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public int getScoreA() {
        return scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public int getScoreC() {
        return scoreC;
    }

    public String getDefaultExpression() {
        return defaultExpression;
    }
}