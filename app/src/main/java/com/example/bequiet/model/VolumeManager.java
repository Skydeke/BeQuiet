package com.example.bequiet.model;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.example.bequiet.model.dataclasses.NoiseType;

public class VolumeManager {

    private final Context context;
    private final static String TAG = "VolumeManager";

    public VolumeManager(Context context){
        this.context = context;
    }

    public void actOnNoiseAction(NoiseType noiseType){
        switch (noiseType) {
            case SILENT:
                muteDevice();
                Log.d(TAG, "Muted device.");
                break;
            case VIBRATE:
                turnVibrationOn();
                Log.d(TAG, "Device vibrating.");
                break;
            case NOISE:
                turnNoiseOn();
                Log.d(TAG, "Device making noise.");
        }
    }

    private void muteDevice() {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    private void turnVibrationOn() {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

    public void turnNoiseOn() {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
}
