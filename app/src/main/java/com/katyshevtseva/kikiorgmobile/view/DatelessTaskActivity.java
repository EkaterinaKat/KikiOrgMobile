package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;

public class DatelessTaskActivity extends AppCompatActivity {
    private Service service;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateless_task);
        findViewById(R.id.add_dateless_task_button).setOnClickListener(view -> {
            DatelessTaskEditDialog dialog = new DatelessTaskEditDialog(null, service, this::updateTaskList);
            dialog.show(getSupportFragmentManager(), "DatelessTaskEditDialog");
        });
        textView = findViewById(R.id.text_view);
        service = new Service(this);
        updateTaskList();
    }

    private void updateTaskList() {
        textView.setText(service.getAllDatelessTasks().toString());
    }
}
