package com.example.bequiet.model;

public class Haversine {

    //https://de.acervolima.com/haversine-formel-zum-ermitteln-des-abstands-zwischen-zwei-punkten-auf-einer-kugel/
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        // distance between latitudes and longitudes in radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert degree to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply haversine formula to calculate distance of the points
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);


        //earth radius in kilometers
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));

        //return result in meters
        return rad * c * 1000;
    }

    public static boolean pointIsWithinCircle(double latCenter, double lonCenter, double latPoint, double lonPoint, int radius) {
        return haversine(latCenter, lonCenter, latPoint, lonPoint) <= radius;
    }
}
