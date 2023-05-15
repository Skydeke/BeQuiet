package com.example.bequiet.model;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

public class LocationListenerRegisterer {

    private static final String TAG = "LocationListenerBeQuiet";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;

    private static LocationListenerRegisterer INSTANCE = null;

    public static LocationListenerRegisterer INSTANCE(Context c) {
        if (INSTANCE == null) {
            INSTANCE = new LocationListenerRegisterer(c);
        }
        return INSTANCE;
    }

    private final LocationListener[] mLocationListeners;

    private LocationListenerRegisterer(Context c) {
        mLocationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER, c),
                new LocationListener(LocationManager.NETWORK_PROVIDER, c)
        };
        initializeLocationManager(c);
        addListeners();
    }

    public void addListeners() {
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    public void removeListeners() {
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager(Context c) {
        Log.i(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        }
    }
}