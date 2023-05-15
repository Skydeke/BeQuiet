package com.example.bequiet.model.receivers;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.bequiet.model.RuleTimer;
import com.example.bequiet.model.VolumeManager;
import com.example.bequiet.model.database.Database;
import com.example.bequiet.model.dataclasses.WlanRule;

public class WifiListener extends BrodcastReceiver {
    private static final String TAG = "WifiBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
            if (connected) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                //Array needs to be final to access it inside runnable.
                final WifiInfo[] wifiInfo = {wifiManager.getConnectionInfo()};
                // Connecting takes a while so we wait until the INfo is there.
                new Thread(() -> {
                    while (wifiInfo[0].getSupplicantState() != SupplicantState.COMPLETED) {
                        wifiInfo[0] = wifiManager.getConnectionInfo();
                    }
                    checkRules(context, wifiInfo[0].getSSID().replace("\"", ""));
                    Log.d(TAG, wifiInfo[0].toString());
                }).start();
            }
        }
    }

    private void checkRules(Context context, String ssid) {
        VolumeManager volumeManager = new VolumeManager(context);
        Database database = new Database(context);
        database.getWlanRulesInCallback(wlanRules -> {
            for (WlanRule wlanRule : wlanRules) {
                if (wlanRule.getWlanName().equals(ssid)) {
                    if (wlanRule.isActive()) {
                        volumeManager.actOnNoiseAction(wlanRule.getReactionType());
                        RuleTimer.getInstance().startTimer(wlanRule.getDurationEnd(), () -> {
                            volumeManager.turnNoiseOn();
                            Log.d(TAG, "Reset Volume in Handler.");
                            RuleTimer.getInstance().startTimer(wlanRule.getDurationStart(), () -> checkRules(context, ssid));
                        });
                    } else {
                        volumeManager.turnNoiseOn();
                        Log.d(TAG, "Reset Volume in Handler.");
                        RuleTimer.getInstance().startTimer(wlanRule.getDurationStart(), () -> {
                            volumeManager.actOnNoiseAction(wlanRule.getReactionType());
                            RuleTimer.getInstance().startTimer(wlanRule.getDurationEnd(), () -> checkRules(context, ssid));
                        });
                    }
                }
            }
        });
    }

}