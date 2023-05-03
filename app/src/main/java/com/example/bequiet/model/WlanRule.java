package com.example.bequiet.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class WlanRule extends Rule{
    @ColumnInfo(name = "wlanName")
    private String wlanName;

    public WlanRule(String ruleName, int startHour, int startMinute, int endHour, int endMinute, String wlanName) {
        super(ruleName, startHour, startMinute, endHour, endMinute);
        this.wlanName = wlanName;
    }

    public String getWlanName() {
        return wlanName;
    }

    public void setWlanName(String wlanName) {
        this.wlanName = wlanName;
    }
}
