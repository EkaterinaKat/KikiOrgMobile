package com.katyshevtseva.kikiorgmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Core;
import com.katyshevtseva.kikiorgmobile.core.TaskService;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskService taskService;

    private LinearLayout taskPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskService = Core.getTaskService(this);

        taskPane = findViewById(R.id.task_pane);
        findViewById(R.id.admin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        updateTaskPane();
        super.onResume();
    }

    private void openAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    private void updateTaskPane() {
        taskPane.removeAllViews();
        List<IrregularTask> tasks = taskService.getAllIrregularTasks();
        for (IrregularTask task : tasks) {
            TextView textView = new TextView(this);
            textView.setText(task.toString());
            taskPane.addView(textView);
        }
    }
}
