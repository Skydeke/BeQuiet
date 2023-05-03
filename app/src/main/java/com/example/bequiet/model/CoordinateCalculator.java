package com.example.bequiet.model;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class CoordinateCalculator {
    private static final double EARTH_RADIUS = 6371.0; // Durchmesser der Erde in Kilometern

    public static boolean isCoordinateInRange(GeoPoint center, double radiusInMeters, GeoPoint coordinate) {
        // Umrechnung des Radius von Metern in Kilometer
        double radiusInKilometers = radiusInMeters / 1000.0;

        // Umwandlung der center- und coordinate-Koordinaten in Radiant
        double latCenterRad = Math.toRadians(center.getLatitude());
        double lonCenterRad = Math.toRadians(center.getLongitude());
        double latCoordinateRad = Math.toRadians(coordinate.getLatitude());
        double lonCoordinateRad = Math.toRadians(coordinate.getLongitude());

        // Berechnung der Differenz der Breiten- und Längengrade zwischen center und coordinate
        double latDiff = latCoordinateRad - latCenterRad;
        double lonDiff = lonCoordinateRad - lonCenterRad;

        // Berechnung der Entfernung zwischen center und coordinate mit dem Haversine-Formel
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(latCenterRad) * Math.cos(latCoordinateRad)
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        // Überprüfung, ob die Entfernung kleiner oder gleich dem Radius ist
        return distance <= radiusInKilometers;
    }
}

