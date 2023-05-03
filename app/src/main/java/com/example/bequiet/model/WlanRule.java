package com.example.bequiet.model;

public class WlanRule extends Rule{

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
