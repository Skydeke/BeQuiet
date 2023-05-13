package com.example.bequiet.model;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;

public class VolumeManager {

    private static VolumeManager INSTANCE;

    private VolumeManager() {

    }


    public static VolumeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VolumeManager();
        }
        return INSTANCE;
    }

    public void muteDevice(Context context) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public void turnVibrationOn(Context context) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

    public void turnNoiseOn(Context context) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
}
