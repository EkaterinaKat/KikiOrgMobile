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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.DateUtils;
import com.katyshevtseva.kikiorgmobile.core.DateUtils.TimeUnit;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.view.utils.MainTaskRecycleView.TaskListAdapter;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Date date;
    private TextView dateView;
    private TaskListAdapter taskListAdapter;
    private TextView alarmTextView;
    private Button datelessTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Service.init(this);
        alarmTextView = findViewById(R.id.alarm_text_view);

        date = new Date();
        dateView = findViewById(R.id.main_date_text_view);
        findViewById(R.id.admin_button).setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), AdminActivity.class)));
        findViewById(R.id.prev_date_button).setOnClickListener(view -> previousDate());
        findViewById(R.id.next_date_button).setOnClickListener(view -> nextDate());
        dateView.setOnClickListener(view -> openDatePicker());

        RecyclerView taskList = findViewById(R.id.main_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(this, date);
        taskList.setAdapter(taskListAdapter);

        datelessTaskButton = findViewById(R.id.dateless_task_button);
        datelessTaskButton.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), DatelessTaskActivity.class)));

        updateTaskPane();
    }

    @Override
    protected void onResume() {
        updateTaskPane();
        super.onResume();
    }

    private void updateTaskPane() {
        dateView.setText(DateUtils.getDateStringWithWeekDay(date));
        taskListAdapter.setDate(date);
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
