package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date = DateUtils.getProperDate();
        dateView = findViewById(R.id.main_date_text_view);
        findViewById(R.id.admin_button).setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), AdminActivity.class)));
        findViewById(R.id.prev_date_button).setOnClickListener(view -> previousDate());
        findViewById(R.id.next_date_button).setOnClickListener(view -> nextDate());
        dateView.setOnClickListener(view -> openDatePicker());

        RecyclerView taskList = findViewById(R.id.main_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(this, new Service(this), date);
        taskList.setAdapter(taskListAdapter);

        GestureDetectorCompat lSwipeDetector = new GestureDetectorCompat(this, new MyGestureListener());
        taskList.setOnTouchListener((v, event) -> lSwipeDetector.onTouchEvent(event));

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

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 130;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE)
                previousDate();
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE)
                nextDate();
            return false;
        }
    }
}
