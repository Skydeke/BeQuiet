package com.example.bequiet.view.loading;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.bequiet.view.home.HomePageActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bequiet.R;

import java.util.ArrayList;
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
        checkAndRequestPermissions();

    }

    private void checkAndRequestPermissions() {
        String[] permissionsList = new String[]{
                android.Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.NEARBY_WIFI_DEVICES,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        };

        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissionsList) { //check which permissions are missing
            if (ContextCompat.checkSelfPermission(LoadingScreen.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
        } else {
            goToHomeActivity();
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