package com.koensimonides.onbalansmonitor.view.chart;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.data.types.CompactRecord;

import java.util.ArrayList;
import java.util.List;

public class HistoryChart extends ChartWrapper {

    public static final int VALUE_COUNT = 100;

    private final OnbalansMonitorApp app;

    public HistoryChart(LineChart chart, OnbalansMonitorApp app) {
        super(chart);

        this.app = app;
    }

    @Override
    protected LineData getChartData() {
        CompactRecord[] records = app.getDataManager().getRecords().get();

        final List<Entry> entries = new ArrayList<>();

        entries.add(new Entry(1 - records.length, DEFAULT_HIGH_Y));
        entries.add(new Entry(1 - records.length, DEFAULT_LOW_Y));

        if(records.length > VALUE_COUNT) {
            final double step = records.length / (double) VALUE_COUNT;

            float lastPrice = 0f;

            for(int index = VALUE_COUNT - 1; index >= 0; index--)
                search: {
                    final int subCap = (int) Math.floor((index + 1) * step - 1);

                    for(int subIndex = (int) Math.floor(index * step); subIndex <= subCap; subIndex++)
                        if(records[subIndex] != null && records[subIndex].hasPrice()) {
                            entries.add(new Entry(-subIndex, records[subIndex].getPrice()));
                            lastPrice = records[subIndex].getPrice();
                            break search;
                        }

                    entries.add(new Entry(-subCap, lastPrice));
                }

        } else {
            float lastPrice = 0f;

            for (int index = records.length - 1; index >= 0; index--) {
                if(records[index] != null && records[index].hasPrice())
                    lastPrice = records[index].getPrice();
                entries.add(new Entry(index, lastPrice));
            }
        }

        final LineDataSet prices = new LineDataSet(entries, app.getString(R.string.dashboard_chart_prices));

        prices.setDrawCircleHole(false);
        prices.setDrawCircles(false);
        prices.setLineWidth(2f);
        prices.setMode(app.getConfiguration().isGraphLinear() ? LineDataSet.Mode.LINEAR : LineDataSet.Mode.CUBIC_BEZIER);
        prices.setColor(app.getResources().getColor(R.color.general_green_highlight));

        return  new LineData(new ArrayList<ILineDataSet>() {{
            add(prices);
        }});
    }
}
