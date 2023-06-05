package com.example.bequiet.model;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.bequiet.model.database.Database;
import com.example.bequiet.model.dataclasses.AreaRule;

public class LocationListener implements android.location.LocationListener {

    private static final String TAG = "LocationListener";

    private final Location lastLocation;
    final Context context;

    public LocationListener(String provider, Context c) {
        Log.i(TAG, "LocationListener " + provider);
        this.context = c;
        lastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: " + location);
        lastLocation.set(location);
        checkRules(context);
    }

    private void checkRules(Context context){
        VolumeManager volumeManager = new VolumeManager(context);
        Database database = new Database(context);
        database.getAreaRulesInCallback(areaRules -> {
            Log.d(TAG, "Rules: " + areaRules.size());
            for (AreaRule areaRule : areaRules) {
                if (Haversine.pointIsWithinCircle(
                        areaRule.getCenterLatitude(),
                        areaRule.getCenterLongitude(),
                        lastLocation.getLatitude(),
                        lastLocation.getLongitude(),
                        (int) areaRule.getRadius())) {
                    Log.d(TAG, "Inside Area of rule.");
                    if (areaRule.isActive()) {
                        volumeManager.actOnNoiseAction(areaRule.getReactionType());
                        RuleTimer.getInstance().startTimer(areaRule.getDurationEnd(), () -> {
                            volumeManager.turnNoiseOn();
                            Log.d(TAG, "Reset Volume in Handler.");
                            RuleTimer.getInstance().startTimer(areaRule.getDurationStart(), () -> checkRules(context));
                        });
                    } else {
                        volumeManager.turnNoiseOn();
                        Log.d(TAG, "Reset Volume in Handler.");
                        RuleTimer.getInstance().startTimer(areaRule.getDurationStart(), () -> {
                            volumeManager.actOnNoiseAction(areaRule.getReactionType());
                            RuleTimer.getInstance().startTimer(areaRule.getDurationEnd(), () -> checkRules(context));
                        });
                    }
                }
            }
        });
    }
}