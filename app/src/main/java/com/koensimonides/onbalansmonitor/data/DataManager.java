package com.koensimonides.onbalansmonitor.data;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.configuration.HistoryLength;
import com.koensimonides.onbalansmonitor.data.types.OperationalMessage;
import com.koensimonides.onbalansmonitor.data.types.OperationalMessageArray;
import com.koensimonides.onbalansmonitor.data.types.RecordArray;
import com.koensimonides.onbalansmonitor.data.types.UnprocessedRecord;
import com.koensimonides.onbalansmonitor.xml.ImbalanceDeltaReader;
import com.koensimonides.onbalansmonitor.xml.OperationalMessagesReader;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final OnbalansMonitorApp app;

    private final ImbalanceDeltaReader imbalanceDeltaReader;
    private final OperationalMessagesReader operationalMessagesReader;

    private final RecordArray recordArray;
    private final OperationalMessageArray operationalMessageArray;

    private final DataFormatter formatter;

    private HistoryLength length;

    private List<UnprocessedRecord> lastLoadValues;

    private long lastFail;

    public DataManager(OnbalansMonitorApp app) {
        this.app = app;

        imbalanceDeltaReader = ImbalanceDeltaReader.of(app.getResources().getString(R.string.url_imbalance_delta));
        operationalMessagesReader = OperationalMessagesReader.of(app.getResources().getString(R.string.url_operational_messages));

        length = app.getConfiguration().getHistoryLength();

        recordArray = new RecordArray(length.getSize());
        operationalMessageArray = new OperationalMessageArray();

        formatter = new DataFormatter(app, this);

        lastLoadValues = null;
        lastFail = -2L;
    }

    public void load() {
        if(length != app.getConfiguration().getHistoryLength()) {
            length = app.getConfiguration().getHistoryLength();
            recordArray.resize(length.getSize());
        }

        boolean failed = false;

        if(imbalanceDeltaReader != null) {
            List<UnprocessedRecord> result = imbalanceDeltaReader.load();

            recordArray.add(result);

            if(result == null || result.size() == 0)
                failed = true;
            else
                lastLoadValues = result;
        }

        List<OperationalMessage> newMessages = new ArrayList<>();

        if(!failed && operationalMessagesReader != null) {
            List<OperationalMessage> result = operationalMessagesReader.load();

            newMessages = operationalMessageArray.add(result);
        }

        if(failed)
            lastFail = System.currentTimeMillis();

        formatter.update();

        app.onDataUpdate(newMessages);
    }

    public DataFormatter getFormatter() {
        return formatter;
    }

    public RecordArray getRecords() {
        return recordArray;
    }

    public OperationalMessageArray getOperationalMessages() {
        return operationalMessageArray;
    }

    public long getLastFail() {
        return lastFail;
    }

    public List<UnprocessedRecord> getLastLoad() {
        return lastLoadValues;
    }

}
