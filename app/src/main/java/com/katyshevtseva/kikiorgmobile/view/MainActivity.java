package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.IrregularTaskService;
import com.katyshevtseva.kikiorgmobile.core.KomDao;
import com.katyshevtseva.kikiorgmobile.core.LogService;
import com.katyshevtseva.kikiorgmobile.core.OneDaySettingService;
import com.katyshevtseva.kikiorgmobile.core.PrefService;
import com.katyshevtseva.kikiorgmobile.core.RegularTaskService;
import com.katyshevtseva.kikiorgmobile.core.ScheduleService;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils.TimeUnit;
import com.katyshevtseva.kikiorgmobile.view.utils.KomActivity;

import java.util.Date;

public class MainActivity extends KomActivity {
    private Date date;
    private TextView dateView;
    private final ScheduleFragment scheduleFragment = new ScheduleFragment(this::fragmentUpdateListener);
    private TextView alarmTextView;
    private boolean prevDateIsAvailable;

    public MainActivity() {
        setOnStart(this::updateTaskPane);
        setImmersiveStickyMode(true);
        setOnLeftSwipe(this::previousDate);
        setOnRightSwipe(this::nextDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KomDao komDao = new KomDaoImpl(this);
        Service.init(komDao);
        PrefService.init(komDao);
        ScheduleService.init(komDao);
        OneDaySettingService.init(komDao);
        RegularTaskService.init(komDao);
        IrregularTaskService.init(komDao);
        LogService.init(komDao);
        alarmTextView = findViewById(R.id.alarm_text_view);

        date = new Date();
        dateView = findViewById(R.id.main_date_text_view);
        findViewById(R.id.admin_button).setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), AdminActivity.class)));
        dateView.setOnClickListener(view -> openDatePicker());

        getSupportFragmentManager().beginTransaction().add(R.id.task_list_container, scheduleFragment).commit();

        updateTaskPane();
    }

    private void updateTaskPane() {
        dateView.setText(DateUtils.getDateStringWithWeekDay(date));
        scheduleFragment.setDate(date);
        setDateViewStyle();
        updateAlarmBanner();
        prevDateIsAvailable = DateUtils.beforeIgnoreTime(Service.INSTANCE.getEarliestTaskDate(), date);
    }

    private void fragmentUpdateListener() {
        updateAlarmBanner();
        prevDateIsAvailable = DateUtils.beforeIgnoreTime(Service.INSTANCE.getEarliestTaskDate(), date);
    }

    private void setDateViewStyle() {
        if (DateUtils.equalsIgnoreTime(date, new Date())) {
            dateView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            dateView.setTypeface(null, Typeface.BOLD);
        } else {
            dateView.setTextColor(ContextCompat.getColor(this, R.color.black));
            dateView.setTypeface(null, Typeface.NORMAL);
        }
    }

    public void updateAlarmBanner() {
        if (DateUtils.equalsIgnoreTime(date, new Date()) && Service.INSTANCE.overdueTasksExist()) {
            alarmTextView.setVisibility(View.VISIBLE);
        } else {
            alarmTextView.setVisibility(View.GONE);
        }
    }

    public void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
            date = DateUtils.parse(year, month + 1, day);
            updateTaskPane();
        });
        datePickerDialog.show();
    }

    private void previousDate() {
        if (prevDateIsAvailable) {
            date = DateUtils.shiftDate(date, TimeUnit.DAY, -1);
            updateTaskPane();
        }
    }

    private void nextDate() {
        date = DateUtils.shiftDate(date, TimeUnit.DAY, 1);
        updateTaskPane();
    }
}
