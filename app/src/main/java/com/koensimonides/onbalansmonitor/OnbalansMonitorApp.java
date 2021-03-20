package com.koensimonides.onbalansmonitor;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.koensimonides.onbalansmonitor.configuration.OnbalansMonitorConfiguration;
import com.koensimonides.onbalansmonitor.data.AlarmManager;
import com.koensimonides.onbalansmonitor.data.DataManager;
import com.koensimonides.onbalansmonitor.data.types.OperationalMessage;
import com.koensimonides.onbalansmonitor.notification.NotificationController;
import com.koensimonides.onbalansmonitor.work.BackgroundWorkManager;

import java.util.List;

public class OnbalansMonitorApp extends Application {

    private OnbalansMonitorConfiguration configuration;

    private AlarmManager alarmManager;

    private DataManager dataManager;

    private NotificationController notificationController;

    @Override
    public void onCreate() {
        super.onCreate();

        configuration = new OnbalansMonitorConfiguration(this);

        dataManager = new DataManager(this);

        alarmManager = new AlarmManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationController = new NotificationController(this);

        BackgroundWorkManager.of(this);

        setup();
    }

    public void onDataUpdate(List<OperationalMessage> newMessages) {
        alarmManager.update(newMessages);
        notificationController.updatePersistent();
    }

    private void setup() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dataManager.getFormatter().update();
    }

    public OnbalansMonitorConfiguration getConfiguration() {
        return configuration;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public NotificationController getNotificationController() {
        return notificationController;
    }

}
