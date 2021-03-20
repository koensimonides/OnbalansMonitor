package com.koensimonides.onbalansmonitor.activity;

import androidx.annotation.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ActivityThread implements Runnable {

    private final MainActivity activity;

    public final ScheduledFuture<?> scheduledFuture;

    public ActivityThread(@NonNull MainActivity activity, long period) {
        this.activity = activity;
        scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this, period, period, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduledFuture.cancel(false);
    }

    @Override
    public void run() {
        activity.runOnUiThread(activity::update);
    }
}
