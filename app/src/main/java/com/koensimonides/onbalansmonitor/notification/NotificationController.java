package com.koensimonides.onbalansmonitor.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.R;

public class NotificationController {

    public static final String ALARM_CHANNEL = "om_alarm_notification_channel", PERSIST_CHANNEL = "om_persistent_notification_channel";

    private static final int PERSIST_ID = 1,
            ALARM_EMERGENCY_POWER_ID = 10,
            ALARM_HIGH_PRICE_ID = 11,
            ALARM_LOW_PRICE_ID = 12,
            ALARM_OPERATIONAL_MESSAGE_ID = 13;

    private final OnbalansMonitorApp app;

    private final NotificationManager manager;

    private final NotificationIntent intent;

    private boolean isShowingPersistent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationController(OnbalansMonitorApp app) {
        this.app = app;

        manager = app.getSystemService(NotificationManager.class);

        intent = new NotificationIntent(app);

        isShowingPersistent = true;

        createNotificationChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels() {
        NotificationChannel alarm = new NotificationChannel(ALARM_CHANNEL,
                app.getString(R.string.notification_alarm_channel_name),
                NotificationManager.IMPORTANCE_HIGH);
        alarm.setDescription(app.getString(R.string.notification_alarm_channel_description));

        NotificationChannel persist = new NotificationChannel(PERSIST_CHANNEL,
                app.getString(R.string.notification_persist_channel_name),
                NotificationManager.IMPORTANCE_LOW);
        persist.setDescription(app.getString(R.string.notification_persist_channel_description));

        manager.createNotificationChannel(alarm);
        manager.createNotificationChannel(persist);
    }

    public void updatePersistent() {
        if(!app.getConfiguration().isShowPersistentNotification()) {
            if(isShowingPersistent)
                manager.cancel(PERSIST_ID);
            return;
        }

        final Notification notification = new NotificationCompat.Builder(app, PERSIST_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_monitor_24)
                .setContentTitle(app.getString(R.string.notification_prefix_imbalance_price_normal) + " "
                        + app.getDataManager().getFormatter().getLastPrice())
                .setContentText(app.getString(R.string.notification_prefix_record_time) + " "
                        + app.getDataManager().getFormatter().getLastTime())
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setOngoing(true)
                .setContentIntent(intent.generate())
                .build();

        manager.notify(PERSIST_ID, notification);

        if(!isShowingPersistent)
            isShowingPersistent = true;
    }

    public void alarmHighPrice() {
        if(app.getConfiguration().isAlarmMuted() || !app.getConfiguration().isAlarmHighEnabled())
            return;

        final Notification notification = new NotificationCompat.Builder(app, ALARM_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle(app.getString(R.string.notification_prefix_imbalance_price_high) + " "
                        + app.getDataManager().getFormatter().getLastPrice())
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(intent.generate())
                .build();

        manager.notify(ALARM_HIGH_PRICE_ID, notification);
    }

    public void alarmLowPrice() {
        if(app.getConfiguration().isAlarmMuted() || !app.getConfiguration().isAlarmLowEnabled())
            return;

        final Notification notification = new NotificationCompat.Builder(app, ALARM_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle(app.getString(R.string.notification_prefix_imbalance_price_low) + " "
                        + app.getDataManager().getFormatter().getLastPrice())
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(intent.generate())
                .build();

        manager.notify(ALARM_LOW_PRICE_ID, notification);
    }

    public void alarmEmergencyPower() {
        if(app.getConfiguration().isAlarmMuted() || !app.getConfiguration().isAlarmEmergencyPowerEnabled())
            return;

        final Notification notification = new NotificationCompat.Builder(app, ALARM_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle(app.getString(R.string.notification_emergency_power))
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(intent.generate())
                .build();

        manager.notify(ALARM_EMERGENCY_POWER_ID, notification);
    }

    public void alarmOperationalMessage(String title) {
        if(app.getConfiguration().isAlarmMuted() || !app.getConfiguration().isAlarmMessageEnabled())
            return;

        final Notification notification = new NotificationCompat.Builder(app, ALARM_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle(app.getString(R.string.notification_operational_message))
                .setContentText(title)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(intent.generate())
                .build();

        manager.notify(ALARM_OPERATIONAL_MESSAGE_ID, notification);
    }
 }
