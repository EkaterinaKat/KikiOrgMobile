package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.PrefService;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils.TimeUnit;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Date date;
    private TextView dateView;
    private final TaskListFragment taskListFragment = new TaskListFragment();
    private final ScheduleFragment scheduleFragment = new ScheduleFragment();
    private TextView alarmTextView;
    private Button datelessTaskButton;
    private boolean schedulerMode = false;
    private Button scheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Service.init(this);
        PrefService.init(this);
        alarmTextView = findViewById(R.id.alarm_text_view);

        date = new Date();
        dateView = findViewById(R.id.main_date_text_view);
        findViewById(R.id.admin_button).setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), AdminActivity.class)));
        findViewById(R.id.prev_date_button).setOnClickListener(view -> previousDate());
        findViewById(R.id.next_date_button).setOnClickListener(view -> nextDate());
        dateView.setOnClickListener(view -> openDatePicker());

        getSupportFragmentManager().beginTransaction().add(R.id.task_list_container, taskListFragment).commit();
        taskListFragment.initAdapter(date, this);

        datelessTaskButton = findViewById(R.id.dateless_task_button);
        datelessTaskButton.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), DatelessTaskActivity.class)));

        scheduleButton = findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(view -> schedulerButtonListener());

        findViewById(R.id.schedule_settings_button).setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), ScheduleSettingsActivity.class)));

        updateTaskPane();
    }

    private void schedulerButtonListener() {
        schedulerMode = !schedulerMode;

        if (schedulerMode) {
            scheduleButton.setBackground(ContextCompat.getDrawable(this, R.mipmap.hamburger));
            getSupportFragmentManager().beginTransaction().replace(R.id.task_list_container, scheduleFragment).commit();
        } else {
            scheduleButton.setBackground(ContextCompat.getDrawable(this, R.mipmap.schedule));
            getSupportFragmentManager().beginTransaction().replace(R.id.task_list_container, taskListFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        updateTaskPane();
        super.onResume();
    }

    private void updateTaskPane() {
        dateView.setText(DateUtils.getDateStringWithWeekDay(date));
        taskListFragment.setDate(date);
        setDateViewStyle();
        updateAlarmBanner();
        datelessTaskButton.setText("" + Service.INSTANCE.countDatelessTasks());
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
        date = DateUtils.shiftDate(date, TimeUnit.DAY, -1);
        updateTaskPane();
    }

    private void nextDate() {
        date = DateUtils.shiftDate(date, TimeUnit.DAY, 1);
        updateTaskPane();
    }
}
