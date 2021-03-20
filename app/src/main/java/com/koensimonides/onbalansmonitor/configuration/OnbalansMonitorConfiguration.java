package com.koensimonides.onbalansmonitor.configuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.R;

public class OnbalansMonitorConfiguration {

    private final OnbalansMonitorApp app;

    private final SharedPreferences preferences;

    private final SharedPreferences.Editor editor;

    private boolean graphLinear, alarmMuted, alarmHighEnabled, alarmLowEnabled, alarmEmergencyPower, alarmMessageEnabled, showPersistentNotification;

    private float alarmHighMargin, alarmLowMargin;

    private ReloadFrequency reloadFrequency;

    private HistoryLength historyLength;

    @SuppressLint("CommitPrefEdits")
    public OnbalansMonitorConfiguration(OnbalansMonitorApp app) {
        this.app = app;

        preferences = app.getSharedPreferences(app.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        load();
    }

    private void load() {
        graphLinear = preferences.getBoolean("graphLinear", false);

        showPersistentNotification = preferences.getBoolean("showPersistentNotification", true);

        alarmMuted = preferences.getBoolean("alarmMuted", false);
        alarmHighEnabled = preferences.getBoolean("alarmHighEnabled", true);
        alarmHighMargin = preferences.getFloat("alarmHighMargin", 100f);
        alarmLowEnabled = preferences.getBoolean("alarmLowEnabled", false);
        alarmLowMargin = preferences.getFloat("alarmLowMargin", -50f);
        alarmMessageEnabled = preferences.getBoolean("alarmMessageEnabled", false);
        alarmEmergencyPower = preferences.getBoolean("alarmEmergencyPower", true);

        reloadFrequency = ReloadFrequency.valueOf(preferences.getString("reloadFrequency", ReloadFrequency.MIN_01.name()));
        historyLength = HistoryLength.valueOf(preferences.getString("historyLength", HistoryLength.DAY_1.name()));
    }

    public void setGraphLinear(boolean graphLinear) {
        if(graphLinear == this.graphLinear)
            return;

        this.graphLinear = graphLinear;
        editor.putBoolean("graphLinear", graphLinear);
        editor.commit();
    }

    public void setAlarmMuted(boolean alarmMuted) {
        if(alarmMuted == this.alarmMuted)
            return;

        this.alarmMuted = alarmMuted;
        editor.putBoolean("alarmMuted", alarmMuted);
        editor.commit();
    }

    public void setAlarmHigh(boolean alarmHighEnabled) {
        if(alarmHighEnabled == this.alarmHighEnabled)
            return;

        this.alarmHighEnabled = alarmHighEnabled;
        editor.putBoolean("alarmHighEnabled", alarmHighEnabled);
        editor.commit();
    }

    public void setAlarmLow(boolean alarmLowEnabled) {
        if(alarmLowEnabled == this.alarmLowEnabled)
            return;

        this.alarmLowEnabled = alarmLowEnabled;
        editor.putBoolean("alarmLowEnabled", alarmLowEnabled);
        editor.commit();
    }

    public void setAlarmMessage(boolean alarmMessageEnabled) {
        if(alarmMessageEnabled == this.alarmMessageEnabled)
            return;

        this.alarmMessageEnabled = alarmMessageEnabled;
        editor.putBoolean("alarmMessageEnabled", alarmMessageEnabled);
        editor.commit();
    }

    public void setAlarmEmergencyPower(boolean alarmEmergencyPower) {
        if(alarmEmergencyPower == this.alarmEmergencyPower)
            return;

        this.alarmEmergencyPower = alarmEmergencyPower;
        editor.putBoolean("alarmEmergencyPower", alarmEmergencyPower);
        editor.commit();
    }

    public void setShowPersistentNotification(boolean showPersistentNotification) {
        if(showPersistentNotification == this.showPersistentNotification)
            return;

        this.showPersistentNotification = showPersistentNotification;
        editor.putBoolean("showPersistentNotification", showPersistentNotification);
        editor.commit();

        app.getNotificationController().updatePersistent();
    }

    public void setAlarmHighMargin(float alarmHighMargin) {
        if(alarmHighMargin == this.alarmHighMargin)
            return;

        this.alarmHighMargin = alarmHighMargin;
        editor.putFloat("alarmHighMargin", alarmHighMargin);
        editor.commit();
    }

    public void setAlarmLowMargin(float alarmLowMargin) {
        if(alarmLowMargin == this.alarmLowMargin)
            return;

        this.alarmLowMargin = alarmLowMargin;
        editor.putFloat("alarmLowMargin", alarmLowMargin);
        editor.commit();
    }

    public void setReloadFrequency(ReloadFrequency reloadFrequency) {
        if(reloadFrequency == this.reloadFrequency)
            return;

        this.reloadFrequency = reloadFrequency;
        editor.putString("reloadFrequency", reloadFrequency.name());
        editor.commit();
    }

    public void setHistoryLength(HistoryLength historyLength) {
        if(historyLength == this.historyLength)
            return;

        this.historyLength = historyLength;
        editor.putString("historyLength", historyLength.name());
        editor.commit();
    }

    public boolean isGraphLinear() {
        return graphLinear;
    }

    public boolean isAlarmMuted() {
        return alarmMuted;
    }

    public boolean isAlarmHighEnabled() {
        return alarmHighEnabled;
    }

    public boolean isAlarmLowEnabled() {
        return alarmLowEnabled;
    }

    public boolean isAlarmMessageEnabled() {
        return alarmMessageEnabled;
    }

    public boolean isAlarmEmergencyPowerEnabled() {
        return alarmEmergencyPower;
    }

    public boolean isShowPersistentNotification() {
        return showPersistentNotification;
    }

    public float getAlarmHighMargin() {
        return alarmHighMargin;
    }

    public float getAlarmLowMargin() {
        return alarmLowMargin;
    }

    public ReloadFrequency getReloadFrequency() {
        return reloadFrequency;
    }

    public HistoryLength getHistoryLength() {
        return historyLength;
    }
}
