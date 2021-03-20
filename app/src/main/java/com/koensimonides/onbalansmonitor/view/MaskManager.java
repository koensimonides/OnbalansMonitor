package com.koensimonides.onbalansmonitor.view;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.koensimonides.onbalansmonitor.activity.MainActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MaskManager {

    public static final int FADE_SPEED = 20, FADE_STAGES = 12;

    private final MainActivity activity;

    private final FrameLayout layout;

    private final int fadeStep, fadeComplete;

    private boolean show;

    private int fade;

    public ScheduledFuture<?> task;

    public MaskManager(@NonNull MainActivity activity, int layoutId, boolean show) {
        this.activity = activity;

        layout = activity.findViewById(layoutId);
        fadeStep = (int) Math.floor(255d / FADE_STAGES);
        fadeComplete = fadeStep * FADE_STAGES;

        task = null;

        force(show);
    }

    public void force(boolean show) {
        this.show = show;

        layout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        fade = show ? fadeComplete : 0;

        cancel();

        setForce();
    }

    public void fadeIn() {
        if(show) {
            activity.onMaskFadeComplete(show);
            return;
        }

        show = true;
        layout.setVisibility(View.VISIBLE);
        startTask();
    }

    public void fadeOut() {
        if(!show) {
            activity.onMaskFadeComplete(show);
            return;
        }

        show = false;
        startTask();
    }

    public void cancel() {
        if(task != null) {
            task.cancel(false);
            task = null;
        }
    }

    private void fade() {
        activity.runOnUiThread(() -> {
            if(!isComplete()) {

                if(show)
                    fade += fadeStep;
                else
                    fade -= fadeStep;

                if(fade == fadeComplete)
                    layout.setBackgroundColor(Color.rgb(255,255,255));
                else
                    layout.setBackgroundColor(Color.argb(fade, 255, 255, 255));

            } else {
                cancel();

                if(!show)
                    layout.setVisibility(View.INVISIBLE);

                activity.onMaskFadeComplete(show);
            }
        });
    }

    private void startTask() {
        if(task == null)
            task = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::fade, FADE_SPEED, FADE_SPEED, TimeUnit.MILLISECONDS);
    }

    private void setForce() {
        if(show)
            layout.setBackgroundColor(Color.argb(255, 255, 255, 255));
        else
            layout.setBackgroundColor(Color.argb(0, 255, 255, 255));
    }

    private boolean isComplete() {
        if(show) {
            return fade == fadeComplete;
        } else {
            return fade == 0;
        }
    }
}
