package com.example.bequiet.model.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.example.bequiet.model.LocationListenerRegisterer;

public class BrodcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            LocationListenerRegisterer.INSTANCE(context); //Register LocListeners
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
            WifiListener wifiListener = new WifiListener();
            context.getApplicationContext().registerReceiver(wifiListener, filter);
        }
    }

}