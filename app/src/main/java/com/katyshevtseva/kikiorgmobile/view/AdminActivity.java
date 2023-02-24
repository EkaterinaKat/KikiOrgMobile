package com.katyshevtseva.kikiorgmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.view.utils.AdminTaskRecycleView.TaskListAdapter;
import com.katyshevtseva.kikiorgmobile.view.utils.SwipeManager;

public class AdminActivity extends AppCompatActivity {
    private TaskListAdapter taskListAdapter;
    private SwipeManager swipeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        GeneralUtil.setImmersiveStickyMode(getWindow());

        findViewById(R.id.new_task_button).setOnClickListener(view ->
                startActivity(TaskCreationActivity.newIntent(AdminActivity.this, null)));
        findViewById(R.id.archive_button).setOnClickListener(view -> startActivity(new Intent(this, ArchiveTasksActivity.class)));
        findViewById(R.id.logs_button).setOnClickListener(view -> startActivity(new Intent(this, LogsActivity.class)));
        ((EditText) findViewById(R.id.search_edit_text)).addTextChangedListener(searchTextWatcher);
        RecyclerView taskList = findViewById(R.id.admin_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(this);
        taskList.setAdapter(taskListAdapter);

        swipeManager = new SwipeManager(this);
        swipeManager.setLeftSwipeListener(this::finish);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Boolean result = swipeManager.dispatchTouchEvent(ev);
        return result == null ? super.dispatchTouchEvent(ev) : result;
    }

    private final TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            taskListAdapter.updateContent(editable.toString());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        taskListAdapter.updateContent();
    }

    @Override
    protected void onResume() {
        GeneralUtil.setImmersiveStickyMode(getWindow());
        super.onResume();
    }
}
