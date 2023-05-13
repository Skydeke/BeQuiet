package com.example.bequiet.view.loading;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.example.bequiet.model.receivers.WifiListener;
import com.example.bequiet.view.home.HomePageActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bequiet.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoadingScreen extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        WifiListener wifiListener = new WifiListener();
        getApplicationContext().registerReceiver(wifiListener, filter);
        Log.i("Perms", "Hello App.: ");

        checkDoNotDisturbPermission();
        checkAndRequestPermissions();
    }


    private void checkAndRequestPermissions() {
        String[] permissionsList = new String[]{
//                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.NEARBY_WIFI_DEVICES,
                android.Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED
        };

        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissionsList) { //check which permissions are missing
            if (ContextCompat.checkSelfPermission(LoadingScreen.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            } else {
                Log.i("Perms", "Got: " + permission);
            }
        }

        if (!permissionsNeeded.isEmpty()) {
            Log.i("Perms", "Requesting: " + Arrays.toString(permissionsNeeded.toArray()));
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
        } else {
            goToHomeActivity();
        }
    }

    private void checkDoNotDisturbPermission() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        ActivityResultLauncher<Intent> notificationPolicyLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle success, user denied permission or cancelled

                    } else {
                        // Handle failure, user denied permission or cancelled
                        // You can handle the failure scenario here
                    }
                });

        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            notificationPolicyLauncher.launch(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        goToHomeActivity();
    }

    private void goToHomeActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(LoadingScreen.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}