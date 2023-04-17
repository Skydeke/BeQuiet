package com.example.bequiet;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.bequiet.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private MapView map = null;
    private ActivityMainBinding binding;

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 123; // Replace with your desired value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Configuration.getInstance().load(MainActivity.this, PreferenceManager.getDefaultSharedPreferences(MainActivity.this));


        map = (MapView) findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);


        CircleOverlay mCircleOverlay;
        mCircleOverlay = new CircleOverlay(map.getContext(), 40, map.getProjection().metersToPixels(40, 47.8127457112777, 20), map.getProjection().metersToPixels(1, 47.8127457112777, 20), 47.8127457112777, 9.656508679012063);
        map.getOverlayManager().add(mCircleOverlay);

        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                mCircleOverlay.setReferenceScale(map.getProjection().metersToPixels(1, 47.8127457112777, map.getZoomLevel()));
                Log.i("Meter", "" + mCircleOverlay.getRadiusInMeter()); //radius scales
                return false;
            }
        });

        Log.i("GeoCoordinate in rage", ""+CoordinateCalculator.isCoordinateInRange(new GeoPoint(map.getMapCenter().getLatitude(),map.getMapCenter().getLongitude()),mCircleOverlay.getRadiusInMeter(),new GeoPoint(47.8127457112777,9.656508679012063)));

        IMapController mapController = map.getController();
        mapController.setZoom(20);
        mapController.setCenter(new GeoPoint(47.8127457112777, 9.656508679012063)); //geographic point of Hochschule Ravensbrug-Weingarten


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {

        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                int desiredVolume = 0; // Replace with your desired volume level
                audioManager.setStreamVolume(AudioManager.STREAM_RING, desiredVolume, 0);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch (SecurityException e) {

        }
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            int desiredVolume = 0; // Replace with your desired volume level
            audioManager.setStreamVolume(AudioManager.STREAM_RING, desiredVolume, 0);
        } else {
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            mNotificationPolicyLauncher.launch(intent);

        }
    }

    private ActivityResultLauncher<Intent> mNotificationPolicyLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle success, user granted permission
                    // You can perform the desired action here
                    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    int desiredVolume = 0; // Replace with your desired volume level
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, desiredVolume, 0);
                } else {
                    // Handle failure, user denied permission or cancelled
                    // You can handle the failure scenario here
                }
            });

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

}