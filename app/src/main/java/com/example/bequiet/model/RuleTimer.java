package com.example.bequiet.model;

import android.os.Handler;
import android.os.HandlerThread;

public class RuleTimer {
    private static RuleTimer instance = null;
    private Handler handler;
    private Runnable runnable;

    private HandlerThread handlerThread;

    private RuleTimer() {
        handlerThread = new HandlerThread("RuleTimerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public static RuleTimer getInstance() {
        if (instance == null) {
            instance = new RuleTimer();
        }
        return instance;
    }

    public void startTimer(long duration, Runnable action) {
        stopTimer(); //remove the last callback
        runnable = action;
        handler.postDelayed(runnable, duration);
    }

    public void stopTimer() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }
}
