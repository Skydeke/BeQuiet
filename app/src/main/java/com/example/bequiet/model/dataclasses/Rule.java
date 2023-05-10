package com.example.bequiet.model.dataclasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bequiet.model.NoiseType;

@Entity(tableName = "Rule")
public abstract class Rule {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "ruleName")
    private String ruleName;
    @ColumnInfo(name = "startHour")
    private int startHour;
    @ColumnInfo(name = "startMinute")
    private int startMinute;

    @ColumnInfo(name = "endHour")
    private int endHour;
    @ColumnInfo(name = "endMinute")
    private int endMinute;
    @ColumnInfo(name = "reactionType")
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
