package com.koensimonides.onbalansmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.view.chart.DashboardChart;

public class DashboardFragment extends ActivityFragment {

    private DashboardChart chart;

    public DashboardFragment() {
        super(R.string.dashboard_fragment_title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = new DashboardChart(requireView().findViewById(R.id.dashboard_chart), app);
    }

    @Override
    public void onUpdate() {
        chart.update();

        ((TextView) requireView().findViewById(R.id.dashboard_recent_price)).setText(dataManager.getFormatter().getLastPrice());
        ((TextView) requireView().findViewById(R.id.dashboard_price_backlog)).setText(dataManager.getFormatter().getLastUpdate());
        ((TextView) requireView().findViewById(R.id.dashboard_price_error)).setText(dataManager.getFormatter().getError());
        ((TextView) requireView().findViewById(R.id.dashboard_emergency_power)).setText(dataManager.getFormatter().getEmergencyPower());

        final SwitchCompat switchMuted = requireView().findViewById(R.id.dashboard_switch_mute);
        switchMuted.setOnCheckedChangeListener(null);
        switchMuted.setChecked(app.getConfiguration().isAlarmMuted());
        switchMuted.setOnCheckedChangeListener((buttonView, isChecked) -> app.getConfiguration().setAlarmMuted(isChecked));
    }
}
