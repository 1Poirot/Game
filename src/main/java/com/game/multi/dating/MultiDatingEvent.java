package com.game.multi.dating;

public class MultiDatingEvent {
    private final String background, dialog;
    private final String choiceA, choiceB, choiceC;
    private final int scoreA, scoreB, scoreC;

    public MultiDatingEvent(String background, String dialog,
            String choiceA, int scoreA,
            String choiceB, int scoreB,
            String choiceC, int scoreC) {
        this.background = background;
        this.dialog = dialog;
        this.choiceA = choiceA;
        this.scoreA = scoreA;
        this.choiceB = choiceB;
        this.scoreB = scoreB;
        this.choiceC = choiceC;
        this.scoreC = scoreC;
    }

    public String getBackground() {
        return background;
    }

    public String getDialog() {
        return dialog;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public int getScoreA() {
        return scoreA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public int getScoreB() {
        return scoreB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public int getScoreC() {
        return scoreC;
    }
}