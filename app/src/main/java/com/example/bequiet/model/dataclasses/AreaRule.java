package com.example.bequiet.model.dataclasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class AreaRule extends Rule {

    @ColumnInfo(name = "radius")
    private final float radius;
    @ColumnInfo(name = "centerLongitude")
    private final double centerLongitude;
    @ColumnInfo(name = "centerLatitude")
    private final double centerLatitude;

    @ColumnInfo(name = "zoom")
    private final int zoom;

    public AreaRule(String ruleName, int startHour, int startMinute, int endHour, int endMinute, float radius, double centerLatitude, double centerLongitude, int zoom) {
        super(ruleName, startHour, startMinute, endHour, endMinute);
        this.radius = radius;
        this.centerLongitude = centerLongitude;
        this.centerLatitude = centerLatitude;
        this.zoom = zoom;
    }

    public float getRadius() {
        return radius;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public int getZoom() {
        return zoom;
    }

}
