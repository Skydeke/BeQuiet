package com.example.bequiet.model.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.bequiet.model.LocationBackgroudService;

public class BrodcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            LocationBackgroudService.INSTANCE(context); //Register LocListeners
        }
//        else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//
//            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//
//            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
//                    networkInfo.isConnected()) {
//                // Wifi is connected
//                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                String ssid = wifiInfo.getSSID();
//
//                Log.e("Wifi", " -- Wifi connected --- " + " SSID " + ssid);
//
//            }
//        } else if (intent.getAction().equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
//            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
//            if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
//                Log.e("Wifi", " ----- Wifi  Disconnected ----- ");
//            } else {
//                Log.e("Wifi", " ----- Wifi  Connected ----- ");
//            }
//
//        }

    }
}