package com.katyshevtseva.kikiorgmobile.view;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.PrefService;
import com.katyshevtseva.kikiorgmobile.utils.Time;
import com.katyshevtseva.kikiorgmobile.view.utils.SettingRecycleView.SettingListAdapter;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

public class ScheduleSettingsActivity extends AppCompatActivity {
    private SettingListAdapter settingListAdapter;
    private TextView apStartView;
    private TextView apEndView;
    private Time apStart;
    private Time apEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_settings);

        findViewById(R.id.add_setting_button).setOnClickListener(view ->
                startActivity(SettingCreationActivity.newIntent(this, null)));

        apStartView = findViewById(R.id.activity_period_start_view);
        apEndView = findViewById(R.id.activity_period_end_view);

        setTimeViewsValues();

        apStartView.setOnClickListener(view -> new TimePickerDialog(this,
                (timePicker, hour, min) -> {
                    try {
                        PrefService.INSTANCE.updateStartActivityPeriodValue(hour, min);
                    } catch (Exception e) {
                        ViewUtils.showAlertDialog(this, e.getMessage());
                    }
                    setTimeViewsValues();
                }, apStart.getHour(), apStart.getMinute(), true).show());

        apEndView.setOnClickListener(view -> new TimePickerDialog(this,
                (timePicker, hour, min) -> {
                    try {
                        PrefService.INSTANCE.updateEndActivityPeriodValue(hour, min);
                    } catch (Exception e) {
                        ViewUtils.showAlertDialog(this, e.getMessage());
                    }
                    setTimeViewsValues();
                }, apEnd.getHour(), apEnd.getMinute(), true).show());

        RecyclerView settingList = findViewById(R.id.setting_list);
        settingList.setLayoutManager(new LinearLayoutManager(this));
        settingListAdapter = new SettingListAdapter(this);
        settingList.setAdapter(settingListAdapter);
    }

    private void setTimeViewsValues() {
        apStart = PrefService.INSTANCE.getStartActivityPeriod();
        apEnd = PrefService.INSTANCE.getEndActivityPeriod();

        apStartView.setText(apStart.getS());
        apEndView.setText(apEnd.getS());
    }

    @Override
    protected void onStart() {
        super.onStart();
        settingListAdapter.updateContent();
    }
}