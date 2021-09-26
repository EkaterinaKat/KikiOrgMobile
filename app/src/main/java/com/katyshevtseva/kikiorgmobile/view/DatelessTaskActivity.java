package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.view.utils.DatelessTaskRecycleView.TaskListAdapter;

public class DatelessTaskActivity extends AppCompatActivity {
    private Service service;
    private TaskListAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateless_task);
        findViewById(R.id.add_dateless_task_button).setOnClickListener(view -> {
            DatelessTaskEditDialog dialog = new DatelessTaskEditDialog(null, service, this::updateTaskList);
            dialog.show(getSupportFragmentManager(), "DatelessTaskEditDialog");
        });
        service = new Service(this);

        RecyclerView taskList = findViewById(R.id.dateless_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(this, service);
        taskList.setAdapter(taskListAdapter);

        updateTaskList();
    }

    private void updateTaskList() {
        taskListAdapter.updateContent();
    }
}
