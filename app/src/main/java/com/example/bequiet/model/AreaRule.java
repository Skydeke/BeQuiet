package com.example.bequiet.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.osmdroid.util.GeoPoint;
@Entity
public class AreaRule extends Rule {

    @ColumnInfo(name = "radius")
    private float radius;
    @ColumnInfo(name = "centerLongitude")
    private double centerLongitude;
    @ColumnInfo(name = "centerLatitude")
    private double centerLatitude;

    public AreaRule(String ruleName, int startHour, int startMinute, int endHour, int endMinute, float radius, double centerLatitude, double centerLongitude) {
        super(ruleName, startHour, startMinute, endHour, endMinute);
        this.radius = radius;
        this.centerLongitude = centerLongitude;
        this.centerLatitude = centerLatitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }
}
