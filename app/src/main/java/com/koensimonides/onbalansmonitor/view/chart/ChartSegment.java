package com.koensimonides.onbalansmonitor.view.chart;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.koensimonides.onbalansmonitor.R;

import java.util.ArrayList;

public class ChartSegment {

    private final int x, colorId;

    private final String timeStamp;

    public ChartSegment(final int index, @NonNull final String timeStamp) {
        this.timeStamp = timeStamp;

        x = -index;

        int colorId = R.color.general_gray;

        if(timeStamp.endsWith(":00"))
            colorId = R.color.chart_00;
        if(timeStamp.endsWith(":15"))
            colorId = R.color.chart_15;
        if(timeStamp.endsWith(":30"))
            colorId = R.color.chart_30;
        if(timeStamp.endsWith(":45"))
            colorId = R.color.chart_45;

        this.colorId = colorId;
    }

    public LineDataSet generateLine(Context context) {
        final ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(x, ChartWrapper.DEFAULT_HIGH_Y));
        entries.add(new Entry(x, ChartWrapper.DEFAULT_LOW_Y));

        final LineDataSet bar = new LineDataSet(entries, timeStamp);

        bar.setDrawCircleHole(false);
        bar.setDrawCircles(false);
        bar.setLineWidth(2f);
        bar.enableDashedLine(10, 10, 0);
        bar.setMode(LineDataSet.Mode.LINEAR);
        bar.setColor(context.getResources().getColor(colorId));

        return bar;
    }
}
