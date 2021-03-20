package com.koensimonides.onbalansmonitor.work;

import android.os.Handler;
import android.os.Looper;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;

import java.util.concurrent.TimeUnit;

public class BackgroundWorkManager {

    private static BackgroundWorkManager instance;

    private OnbalansMonitorApp app;

    private BackgroundWorkManager(OnbalansMonitorApp app) {
        this.app = app;
        work();
    }

    public static void of(OnbalansMonitorApp app) {
        if(BackgroundWorkManager.instance == null)
            instance = new BackgroundWorkManager(app);
        else
            instance.app = app;
    }

    private void observeWork() {
        WorkManager.getInstance(app).getWorkInfosForUniqueWorkLiveData(BackgroundWorkManager.class.getSimpleName())
                .observeForever(workInfo -> {
                    if(workInfo.get(0).getState() == WorkInfo.State.SUCCEEDED)
                        work();
                });
    }

    private void work() {
       new Thread(() -> {
           syncWork(app.getConfiguration().getReloadFrequency().getSeconds());

           new Handler(Looper.getMainLooper()).post(this::observeWork);
       }).start();
    }

    private synchronized void syncWork(final long delay) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(app).enqueueUniqueWork(BackgroundWorkManager.class.getSimpleName(), ExistingWorkPolicy.KEEP, request);
    }


}
