package com.koensimonides.onbalansmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.view.chart.HistoryChart;

public class HistoryFragment extends ActivityFragment {

    private HistoryChart chart;

    public HistoryFragment() {
        super(R.string.history_fragment_title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = new HistoryChart(requireView().findViewById(R.id.history_chart), app);
    }

    @Override
    public void onUpdate() {
        chart.update();
    }
}
