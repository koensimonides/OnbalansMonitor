package com.koensimonides.onbalansmonitor.view.chart;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

public abstract class ChartWrapper {

    protected static final float DEFAULT_HIGH_Y = 200f, DEFAULT_LOW_Y = -50f;

    private final LineChart chart;

    protected  ChartWrapper(@NonNull final LineChart chart) {
        this.chart = chart;

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);

        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGridLineWidth(1f);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(true);
        yAxis.setZeroLineWidth(1f);
        yAxis.setZeroLineColor(Color.BLACK);

        chart.resetZoom();
    }

    public void update() {
        LineData data = getChartData();
        data.setDrawValues(false);
        data.setHighlightEnabled(false);

        chart.setData(data);
        chart.animateX(1000);
        chart.invalidate();
    }

    protected abstract LineData getChartData();

}
