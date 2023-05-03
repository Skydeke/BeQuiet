package com.example.bequiet.model;

public abstract class Rule {

    private String ruleName;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    private NoiseType reactionType = NoiseType.SILENT;

    public Rule(String ruleName, int startHour, int startMinute, int endHour, int endMinute) {
        this.ruleName = ruleName;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public NoiseType getReactionType() {
        return reactionType;
    }

    public void setReactionType(NoiseType reactionType) {
        this.reactionType = reactionType;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
}
