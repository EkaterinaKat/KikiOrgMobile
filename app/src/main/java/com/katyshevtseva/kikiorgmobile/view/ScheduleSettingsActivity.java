package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.PrefService;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.view.utils.MyTimePicker;
import com.katyshevtseva.kikiorgmobile.view.utils.SettingRecycleView.SettingListAdapter;
import com.katyshevtseva.kikiorgmobile.view.utils.SwipeManager;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

public class ScheduleSettingsActivity extends AppCompatActivity {
    private SettingListAdapter settingListAdapter;
    private MyTimePicker startTp;
    private MyTimePicker endTp;
    private SwipeManager swipeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_settings);
        GeneralUtil.setImmersiveStickyMode(getWindow());

        findViewById(R.id.add_setting_button).setOnClickListener(view ->
                startActivity(SettingCreationActivity.newIntent(this, null)));

        RecyclerView settingList = findViewById(R.id.setting_list);
        settingList.setLayoutManager(new LinearLayoutManager(this));
        settingListAdapter = new SettingListAdapter(this);
        settingList.setAdapter(settingListAdapter);

        initTimePickers();

        swipeManager = new SwipeManager(this);
        swipeManager.setLeftSwipeListener(this::finish);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Boolean result = swipeManager.dispatchTouchEvent(ev);
        return result == null ? super.dispatchTouchEvent(ev) : result;
    }

    private void initTimePickers() {
        startTp = new MyTimePicker(findViewById(R.id.activity_period_start_view), this,
                this::startTimeUpdateListener, PrefService.INSTANCE.getActivityStart(), null);
        endTp = new MyTimePicker(findViewById(R.id.activity_period_end_view), this,
                this::endTimeUpdateListener, PrefService.INSTANCE.getActivityEnd(), null);
    }

    private void startTimeUpdateListener(int hour, int min) {
        try {
            PrefService.INSTANCE.updateStartActivityPeriodValue(hour, min);
        } catch (Exception e) {
            ViewUtils.showAlertDialog(this, e.getMessage());
        }
        updateTimePickersValues();
    }

    private void endTimeUpdateListener(int hour, int min) {
        try {
            PrefService.INSTANCE.updateEndActivityPeriodValue(hour, min);
        } catch (Exception e) {
            ViewUtils.showAlertDialog(this, e.getMessage());
        }
        updateTimePickersValues();
    }

    private void updateTimePickersValues() {
        startTp.setTime(PrefService.INSTANCE.getActivityStart());
        endTp.setTime(PrefService.INSTANCE.getActivityEnd());
    }

    @Override
    protected void onStart() {
        super.onStart();
        settingListAdapter.updateContent();
    }

    @Override
    protected void onResume() {
        GeneralUtil.setImmersiveStickyMode(getWindow());
        super.onResume();
    }
}