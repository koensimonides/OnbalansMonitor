package com.koensimonides.onbalansmonitor.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;

public class BackgroundWorker extends Worker {

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        ((OnbalansMonitorApp) getApplicationContext()).getDataManager().load();
        return Result.success();
    }
}
