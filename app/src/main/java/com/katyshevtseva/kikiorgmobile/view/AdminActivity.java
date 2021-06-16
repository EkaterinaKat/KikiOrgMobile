package com.katyshevtseva.kikiorgmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Core;
import com.katyshevtseva.kikiorgmobile.view.utils.TaskRecycleViewHelper;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findViewById(R.id.new_task_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewTaskActivity();
            }
        });
        RecyclerView taskList = findViewById(R.id.task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(new TaskRecycleViewHelper.TaskListAdapter(this));
    }

    private void openNewTaskActivity() {
        Intent intent = new Intent(this, TaskCreationActivity.class);
        startActivity(intent);
    }
}
