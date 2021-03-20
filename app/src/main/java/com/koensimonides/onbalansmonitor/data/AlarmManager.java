package com.koensimonides.onbalansmonitor.data;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.data.types.CompactRecord;
import com.koensimonides.onbalansmonitor.data.types.OperationalMessage;

import java.util.List;

public class AlarmManager {

    private final OnbalansMonitorApp app;

    private final DataManager data;

    private boolean calledEmergencyPower, calledHighPrice, calledLowPrice, calledNewOperationalMessage;

    public AlarmManager(OnbalansMonitorApp app) {
        this.app = app;
        data = app.getDataManager();

        calledEmergencyPower = calledHighPrice = calledLowPrice = calledNewOperationalMessage = true;
    }

    public void update(List<OperationalMessage> messages) {
        final CompactRecord nonNull = data.getRecords().getFirstNonNull();
        final CompactRecord valued = data.getRecords().getFirstValued();

        records: {
            if(nonNull == null)
                break records;

            if(nonNull.isEmergencyPower()) {
                if(!calledEmergencyPower) {
                    app.getNotificationController().alarmEmergencyPower();
                    calledEmergencyPower = true;
                }
            } else if(calledEmergencyPower)
                calledEmergencyPower = false;

            if(valued == null) {
                if(calledHighPrice)
                    calledHighPrice = false;
                if(calledLowPrice)
                    calledLowPrice = false;
                break records;
            }

            if(valued.getPrice() >= app.getConfiguration().getAlarmHighMargin()) {
                if(!calledHighPrice) {
                    app.getNotificationController().alarmHighPrice();
                    calledHighPrice = true;
                }
            } else if(calledHighPrice)
                calledHighPrice = false;

            if(valued.getPrice() <= app.getConfiguration().getAlarmLowMargin()) {
                if(!calledLowPrice) {
                    app.getNotificationController().alarmLowPrice();
                    calledLowPrice = true;
                }
            } else if(calledLowPrice)
                calledLowPrice = false;
        }

        if(!calledNewOperationalMessage && messages.size() > 0) {
            app.getNotificationController().alarmOperationalMessage(messages.get(0).getTitle());
        } else if(calledNewOperationalMessage)
            calledNewOperationalMessage = false;
    }
}
