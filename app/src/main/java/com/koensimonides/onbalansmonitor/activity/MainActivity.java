package com.koensimonides.onbalansmonitor.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.fragment.ActivityFragment;
import com.koensimonides.onbalansmonitor.fragment.DashboardFragment;
import com.koensimonides.onbalansmonitor.fragment.HistoryFragment;
import com.koensimonides.onbalansmonitor.fragment.MessagesFragment;
import com.koensimonides.onbalansmonitor.fragment.SettingsFragment;
import com.koensimonides.onbalansmonitor.fragment.TableFragment;
import com.koensimonides.onbalansmonitor.view.MaskManager;

public class MainActivity extends AppCompatActivity {

    private static final int DEFAULT_UPDATE_PERIOD = 30 * 1000;

    private DashboardFragment dashboardFragment;
    private SettingsFragment settingsFragment;
    private MessagesFragment messagesFragment;
    private TableFragment tableFragment;
    private HistoryFragment historyFragment;

    private MaskManager maskManager;

    private ActivityThread thread;

    private ActivityFragment requestedFragment;

    private TextView fragmentTitle;

    private boolean locked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setup();

        openFragment(R.id.nav_home);
    }

    @SuppressLint("BatteryLife")
    private void setup() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dashboardFragment = new DashboardFragment();
        settingsFragment = new SettingsFragment();
        messagesFragment = new MessagesFragment();
        tableFragment = new TableFragment();
        historyFragment = new HistoryFragment();

        locked = false;

        maskManager = new MaskManager(this, R.id.main_mask, true);

        final BottomNavigationView navigationView = findViewById(R.id.main_bottom_navigation);
        navigationView.setSelectedItemId(R.id.nav_home);

        fragmentTitle = findViewById(R.id.main_fragment_title);

        // has to be post selection or it will trigger
        navigationView.setOnNavigationItemSelectedListener(item -> openFragment(item.getItemId()));

        if (!((OnbalansMonitorApp) getApplication()).getDataManager().getRecords().hasValues())
            ((OnbalansMonitorApp) getApplication()).getDataManager().load();

        if (thread != null)
            thread.stop();

        thread = new ActivityThread(this, DEFAULT_UPDATE_PERIOD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !((PowerManager) getSystemService(POWER_SERVICE)).isIgnoringBatteryOptimizations(getPackageName())) {
            final Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.hide();

        final Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.general_white_highlight));
    }

    public void onFragmentResume() {
        maskManager.fadeOut();
        locked = false;
        update();
    }

    public void onMaskFadeComplete(boolean fadeIn) {
        if (!fadeIn)
            return;

        if (requestedFragment == null) {
            maskManager.fadeOut();
            return;
        }

        fragmentTitle.setText(requestedFragment.getTitleResource());
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, requestedFragment).commit();
    }

    public void update() {
        if (locked)
            return;

        final ActivityFragment fragment = getFragment();
        if (fragment != null)
            fragment.update();
    }

    @Override
    protected void onDestroy() {
        thread.stop();
        maskManager.cancel();
        super.onDestroy();
    }

    @SuppressLint("NonConstantResourceId")
    private boolean openFragment(int id) {
        if (locked)
            return false;

        ActivityFragment selected;

        switch (id) {
            case R.id.nav_home:
                selected = dashboardFragment;
                break;
            case R.id.nav_settings:
                selected = settingsFragment;
                break;
            case R.id.nav_messages:
                selected = messagesFragment;
                break;
            case R.id.nav_history:
                selected = historyFragment;
                break;
            case R.id.nav_table:
                selected = tableFragment;
                break;
            default:
                return false;
        }

        if (selected.equals(getFragment()))
            return true;

        //TODO
        if(selected == historyFragment) {
            Toast.makeText(this, R.string.main_history_unavailable, Toast.LENGTH_LONG).show();
            return false;
        }

        requestedFragment = selected;
        locked = true;
        maskManager.fadeIn();

        return true;
    }

    private ActivityFragment getFragment() {
        return (ActivityFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
    }

}