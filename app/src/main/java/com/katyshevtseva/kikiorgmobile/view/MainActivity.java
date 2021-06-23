package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.DateUtils;
import com.katyshevtseva.kikiorgmobile.core.DateUtils.TimeUnit;
import com.katyshevtseva.kikiorgmobile.core.Service;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Service service;
    private Date date;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new Service(this);
        date = DateUtils.getProperDate();
        dateView = findViewById(R.id.main_date_text_view);
        findViewById(R.id.admin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
            }
        });
        findViewById(R.id.prev_date_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = DateUtils.shiftDate(date, TimeUnit.DAY, -1);
                updateTaskPane();
            }
        });
        findViewById(R.id.next_date_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = DateUtils.shiftDate(date, TimeUnit.DAY, 1);
                updateTaskPane();
            }
        });
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
        updateTaskPane();
    }

    @Override
    protected void onResume() {
        updateTaskPane();
        super.onResume();
    }

    private void updateTaskPane() {
        dateView.setText(DateUtils.getDateStringWithWeekDay(date));
    }

    public void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = DateUtils.parse(year, month + 1, day);
                updateTaskPane();
            }
        });
        datePickerDialog.show();
    }
}
