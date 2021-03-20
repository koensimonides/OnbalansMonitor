package com.koensimonides.onbalansmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.activity.MainActivity;
import com.koensimonides.onbalansmonitor.data.DataManager;

public abstract class ActivityFragment extends Fragment {

    private final int titleId;

    protected OnbalansMonitorApp app;

    protected DataManager dataManager;

    protected ActivityFragment(int titleId) {
        super();

        this.titleId = titleId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = ((OnbalansMonitorApp) view.getContext().getApplicationContext());
        dataManager = app.getDataManager();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) requireActivity()).onFragmentResume();
    }

    public void update() {
        if(app != null)
            onUpdate();
    }

    public int getTitleResource() {
        return titleId;
    }

    public void onUpdate() {}

}
