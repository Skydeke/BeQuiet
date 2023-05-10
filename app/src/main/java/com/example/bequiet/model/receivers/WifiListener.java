package com.example.bequiet.model.receivers;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiListener extends BrodcastReceiver{
    private static final String TAG = "WifiBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.d(TAG, "Wi-Fi is enabled");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.d(TAG, "Wi-Fi is disabled");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    Log.d(TAG, "Wi-Fi state is unknown");
                    break;
            }
        }
    }
}
