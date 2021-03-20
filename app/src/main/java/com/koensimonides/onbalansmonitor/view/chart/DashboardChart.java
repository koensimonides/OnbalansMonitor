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

public class DashboardChart extends ChartWrapper {

    private static final int LENGTH = 33;

    private final OnbalansMonitorApp app;

    public DashboardChart(LineChart chart, OnbalansMonitorApp app) {
        super(chart);

        this.app = app;
    }

    @Override
    protected LineData getChartData() {
        final CompactRecord[] records = app.getDataManager().getRecords().getValueFilled(LENGTH);

        final List<ChartSegment> segments = new ArrayList<>();

        final List<Entry> entries = new ArrayList<>();

        for(int i = LENGTH - 1; i >= 0; i--) {
            entries.add(new Entry(-i, records[i].getPrice()));

            if(records[i].getUnix() % 15 == 0 && i != 0 && i != LENGTH)
                segments.add(new ChartSegment(i, app.getDataManager().getFormatter().getTime(records[i])));

        }

        entries.add(new Entry(1 - LENGTH, DEFAULT_HIGH_Y));
        entries.add(new Entry(1 - LENGTH, DEFAULT_LOW_Y));

        final LineDataSet prices = new LineDataSet(entries, app.getString(R.string.dashboard_chart_prices));

        prices.setDrawCircleHole(false);
        prices.setDrawCircles(false);
        prices.setLineWidth(2f);
        prices.setMode(app.getConfiguration().isGraphLinear() ? LineDataSet.Mode.LINEAR : LineDataSet.Mode.CUBIC_BEZIER);
        prices.setColor(app.getResources().getColor(R.color.general_green_highlight));

        return  new LineData(new ArrayList<ILineDataSet>() {{
            add(prices);

            for(ChartSegment segment : segments)
                add(segment.generateLine(app));

        }});
    }

}
