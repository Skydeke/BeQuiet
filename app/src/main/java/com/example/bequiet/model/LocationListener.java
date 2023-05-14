package com.example.bequiet.model;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.room.Room;

import com.example.bequiet.model.dataclasses.AreaRule;

import java.util.List;

public class LocationListener implements android.location.LocationListener {

    private static final String TAG = "LocationListener";

    Location mLastLocation;
    Context context;

    public LocationListener(String provider, Context c) {
        Log.i(TAG, "LocationListener " + provider);
        this.context = c;
        mLastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
        checkRules();
    }

    private void checkRules() {
        Thread thread = new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(context,
                    AppDatabase.class, "rules").build();

            List<AreaRule> areaRules = db.ruleDAO().loadAllAreaRules();
            Log.d(TAG, "Rules: " + areaRules.size());
            for (AreaRule areaRule : areaRules) {
                if (Haversine.pointIsWithinCircle(
                        areaRule.getCenterLatitude(),
                        areaRule.getCenterLongitude(),
                        mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(),
                        (int) areaRule.getRadius())) {
                    Log.d(TAG, "Inside Area of rule.");
                    if (areaRule.isActive()) {
                        switch (areaRule.getReactionType()) {
                            case SILENT:
                                VolumeManager.getInstance().muteDevice(context);
                                Log.d(TAG, "Muted device.");
                                break;
                            case VIBRATE:
                                VolumeManager.getInstance().turnVibrationOn(context);
                                Log.d(TAG, "Device vibrating.");
                                break;
                            case NOISE:
                                VolumeManager.getInstance().turnNoiseOn(context);
                                Log.d(TAG, "Device making noise.");
                        }
                        RuleTimer.getInstance().startTimer(areaRule.getDurationEnd(), () -> {
                            VolumeManager.getInstance().turnNoiseOn(context);
                            Log.d(TAG, "Reset Volume in Handler.");
                            RuleTimer.getInstance().startTimer(areaRule.getDurationStart(), () -> {
                                checkRules();
                            });
                        });
                    } else {
                        VolumeManager.getInstance().turnNoiseOn(context);
                        Log.d(TAG, "Reset Volume in Handler.");
                        RuleTimer.getInstance().startTimer(areaRule.getDurationStart(), () -> {
                            switch (areaRule.getReactionType()) {
                                case SILENT:
                                    VolumeManager.getInstance().muteDevice(context);
                                    Log.d(TAG, "Muted device. In Handler");
                                    break;
                                case VIBRATE:
                                    VolumeManager.getInstance().turnVibrationOn(context);
                                    Log.d(TAG, "Device vibrating. In Handler");
                                    break;
                                case NOISE:
                                    VolumeManager.getInstance().turnNoiseOn(context);
                                    Log.d(TAG, "Device making noise. In Handler");
                            }
                            RuleTimer.getInstance().startTimer(areaRule.getDurationEnd(), () -> {
                                checkRules();
                            });
                        });
                    }
                }
            }
            db.close();
        });
        thread.start();
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