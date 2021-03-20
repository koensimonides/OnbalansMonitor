package com.koensimonides.onbalansmonitor.notification;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.koensimonides.onbalansmonitor.activity.MainActivity;

public class NotificationIntent {

    private final Application app;

    NotificationIntent(@NonNull Application app) {
        this.app = app;
    }

    public PendingIntent generate() {
        final Intent notificationIntent = new Intent(app, MainActivity.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity (app, 0, notificationIntent, 0);
    }
}
