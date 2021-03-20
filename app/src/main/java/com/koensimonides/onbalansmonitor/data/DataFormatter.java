package com.koensimonides.onbalansmonitor.data;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.data.types.CompactRecord;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataFormatter {

    private final DataManager data;

    private final OnbalansMonitorApp app;

    private final Format format;

    private String lastPrice, lastTime, lastUpdate, emergencyPower, error;

    DataFormatter(OnbalansMonitorApp app, DataManager data) {
        this.app = app;
        this.data = data;

        format = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    public void update() {
        final CompactRecord firstNonNull = data.getRecords().getFirstNonNull();
        final CompactRecord firstValued = data.getRecords().getFirstValued();
        final boolean hasPrice = firstValued != null && firstValued.hasPrice();

        lastPrice = hasPrice ? "\u20ac" + firstValued.getPrice() : app.getString(R.string.data_format_no_price);

        lastTime = hasPrice ? getTime(firstValued) : getTime(null);

        lastUpdate = "-" + (hasPrice ? (data.getRecords().offsetOf(firstValued, true)) + " " : "? ") + app.getString(R.string.data_format_minute);

        emergencyPower = (firstNonNull != null && firstNonNull.isEmergencyPower()) ? app.getString(R.string.data_format_emergency_power) : app.getString(R.string.data_format_no_emergency_power);

        error = data.getLastFail() > data.getRecords().getLastUpdate() ? app.getString(R.string.data_format_no_connection) : "";
    }

    public String getTime(CompactRecord record) {
        return record != null ? format.format(new Date(record.getFullUnix())) + "" : app.getString(R.string.data_format_no_time);
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getEmergencyPower() {
        return emergencyPower;
    }

    public String getError() {
        return error;
    }
}
