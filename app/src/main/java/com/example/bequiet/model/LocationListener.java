package com.example.bequiet.model;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class LocationListener implements android.location.LocationListener {

    private static final String TAG = "LocationListener";

    Location mLastLocation;

    public LocationListener(String provider) {
        Log.i(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "onStatusChanged: " + provider);
    }
}