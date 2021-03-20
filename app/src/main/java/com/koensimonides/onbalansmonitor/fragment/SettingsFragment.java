package com.koensimonides.onbalansmonitor.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.koensimonides.onbalansmonitor.OnbalansMonitorApp;
import com.koensimonides.onbalansmonitor.R;
import com.koensimonides.onbalansmonitor.configuration.HistoryLength;
import com.koensimonides.onbalansmonitor.configuration.OnbalansMonitorConfiguration;
import com.koensimonides.onbalansmonitor.configuration.ReloadFrequency;

import java.util.Objects;

public class SettingsFragment extends ActivityFragment {

    private OnbalansMonitorConfiguration configuration;

    public SettingsFragment() {
        super(R.string.settings_fragment_title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configuration = ((OnbalansMonitorApp) requireContext().getApplicationContext()).getConfiguration();

        final SwitchCompat switchPersistentNotification = requireView().findViewById(R.id.settings_switch_persistent_notification);
        switchPersistentNotification.setChecked(configuration.isShowPersistentNotification());
        switchPersistentNotification.setOnCheckedChangeListener((buttonView, isChecked) -> configuration.setShowPersistentNotification(isChecked));

        final CheckBox checkboxEmergencyPower = requireView().findViewById(R.id.settings_checkbox_emergency);
        checkboxEmergencyPower.setChecked(configuration.isAlarmEmergencyPowerEnabled());
        checkboxEmergencyPower.setOnCheckedChangeListener((buttonView, isChecked) -> configuration.setAlarmEmergencyPower(isChecked));

        final CheckBox checkboxOperationalMessage = requireView().findViewById(R.id.settings_checkbox_operational_message);
        checkboxOperationalMessage.setChecked(configuration.isAlarmMessageEnabled());
        checkboxOperationalMessage.setOnCheckedChangeListener((buttonView, isChecked) -> configuration.setAlarmMessage(isChecked));

        final CheckBox checkboxHighPrice = requireView().findViewById(R.id.settings_checkbox_high_price);
        checkboxHighPrice.setChecked(configuration.isAlarmHighEnabled());
        checkboxHighPrice.setOnCheckedChangeListener((buttonView, isChecked) -> configuration.setAlarmHigh(isChecked));

        final CheckBox checkboxLowPrice = requireView().findViewById(R.id.settings_checkbox_low_price);
        checkboxLowPrice.setChecked(configuration.isAlarmLowEnabled());
        checkboxLowPrice.setOnCheckedChangeListener((buttonView, isChecked) -> configuration.setAlarmLow(isChecked));

        final SwitchCompat switchGraphLinear = requireView().findViewById(R.id.settings_switch_linear_graph);
        switchGraphLinear.setChecked(configuration.isGraphLinear());
        switchGraphLinear.setOnCheckedChangeListener((buttonView, isChecked) -> configuration.setGraphLinear(isChecked));

        final EditText editHighMargin = requireView().findViewById(R.id.settings_high_price_decimal_field);
        editHighMargin.setText(String.valueOf(configuration.getAlarmHighMargin()));
        editHighMargin.setOnEditorActionListener(editorListener);

        final EditText editLowMargin = requireView().findViewById(R.id.settings_low_price_decimal_field);
        editLowMargin.setText(String.valueOf(configuration.getAlarmLowMargin()));
        editLowMargin.setOnEditorActionListener(editorListener);

        final Spinner updateFrequencySpinner = requireView().findViewById(R.id.settings_update_frequency_spinner);
        final ArrayAdapter<String> updateFrequencyAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, ReloadFrequency.getNames());
        updateFrequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateFrequencySpinner.setAdapter(updateFrequencyAdapter);
        resetUpdateFrequencySpinner(updateFrequencySpinner);
        updateFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(updateFrequencySpinner.getSelectedItem() == null)
                    resetUpdateFrequencySpinner(updateFrequencySpinner);
                else
                    configuration.setReloadFrequency(Objects.requireNonNull(ReloadFrequency.getReloadFrequency((String) updateFrequencySpinner.getSelectedItem())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resetUpdateFrequencySpinner(updateFrequencySpinner);
            }
        });

        final Spinner historyLengthSpinner = requireView().findViewById(R.id.settings_history_length_spinner);
        final ArrayAdapter<String> historyLengthAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, HistoryLength.getNames());
        historyLengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historyLengthSpinner.setAdapter(historyLengthAdapter);
        resetHistoryLengthSpinner(historyLengthSpinner);
        historyLengthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(historyLengthSpinner.getSelectedItem() == null)
                    resetHistoryLengthSpinner(historyLengthSpinner);
                else
                    configuration.setHistoryLength(Objects.requireNonNull(HistoryLength.getHistoryLength((String) historyLengthSpinner.getSelectedItem())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resetHistoryLengthSpinner(historyLengthSpinner);
            }
        });
    }

    @Override
    public void onUpdate() {
        final SwitchCompat switchMuted = requireView().findViewById(R.id.settings_switch_mute);
        switchMuted.setOnCheckedChangeListener(null);
        switchMuted.setChecked(configuration.isAlarmMuted());
        switchMuted.setOnCheckedChangeListener((buttonView, isChecked) -> configuration.setAlarmMuted(isChecked));
    }

    private void resetUpdateFrequencySpinner(final Spinner spinner) {
        final String[] updateFrequencyNames = ReloadFrequency.getNames();
        int selected = 0;
        for(int i = 0; i < updateFrequencyNames.length; i++)
            if(updateFrequencyNames[i].equalsIgnoreCase(configuration.getReloadFrequency().getName()))
                selected = i;


        spinner.setSelection(selected);
    }

    private void resetHistoryLengthSpinner(final Spinner spinner) {
        final String[] historyLengthNames = HistoryLength.getNames();
        int selected = 0;
        for(int i = 0; i < historyLengthNames.length; i++)
            if(historyLengthNames[i].equalsIgnoreCase(configuration.getHistoryLength().getName()))
                selected = i;


        spinner.setSelection(selected);
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId != EditorInfo.IME_ACTION_DONE)
                return false;

            float value = Float.MIN_VALUE;
            final String text = String.valueOf(v.getText());

            if(!text.equals("null") && !text.isEmpty())
                try {
                    value = Float.parseFloat(text);
                } catch (NumberFormatException ignored) {}

            switch(v.getId()) {
                case R.id.settings_high_price_decimal_field:
                    if(value != Float.MIN_VALUE)
                        configuration.setAlarmHighMargin(value);
                    else
                        v.setText(String.valueOf(configuration.getAlarmHighMargin()));
                    break;
                    
                case R.id.settings_low_price_decimal_field:
                    if(value != Float.MIN_VALUE)
                        configuration.setAlarmLowMargin(value);
                    else
                        v.setText(String.valueOf(configuration.getAlarmLowMargin()));
                    break;
            }

            return false;
        }
    };
}
