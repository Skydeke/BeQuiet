package com.example.bequiet.model.receivers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

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
                    while (wifiInfo[0].getSupplicantState() != SupplicantState.COMPLETED){
                        wifiInfo[0] = wifiManager.getConnectionInfo();
                    }
                    Log.d(TAG, wifiInfo[0].toString());
                }).start();
            }
        }
    }

}