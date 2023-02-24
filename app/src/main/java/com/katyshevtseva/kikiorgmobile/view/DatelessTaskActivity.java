package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.view.utils.DatelessTaskRecycleView.TaskListAdapter;
import com.katyshevtseva.kikiorgmobile.view.utils.SwipeManager;

public class DatelessTaskActivity extends AppCompatActivity {
    private TaskListAdapter taskListAdapter;
    private SwipeManager swipeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateless_task);
        GeneralUtil.setImmersiveStickyMode(getWindow());

        findViewById(R.id.add_dateless_task_button).setOnClickListener(view -> {
            DatelessTaskEditDialog dialog = new DatelessTaskEditDialog(null, this::updateTaskList);
            dialog.show(getSupportFragmentManager(), "DatelessTaskEditDialog");
        });

        RecyclerView taskList = findViewById(R.id.dateless_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(this);
        taskList.setAdapter(taskListAdapter);

        updateTaskList();

        swipeManager = new SwipeManager(this);
        swipeManager.setLeftSwipeListener(this::finish);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Boolean result = swipeManager.dispatchTouchEvent(ev);
        return result == null ? super.dispatchTouchEvent(ev) : result;
    }

    private void updateTaskList() {
        taskListAdapter.updateContent();
    }
}
