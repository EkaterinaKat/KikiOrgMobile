package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.view.utils.AdminTaskRecycleView.TaskListAdapter;

public class AdminActivity extends AppCompatActivity {
    private TaskListAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findViewById(R.id.new_task_button).setOnClickListener(view ->
                startActivity(TaskCreationActivity.newIntent(AdminActivity.this, null)));
        findViewById(R.id.archive_button).setOnClickListener(view ->
                startActivity(InactiveTasksActivity.newIntent(this, TaskType.REGULAR)));
        findViewById(R.id.done_tasks_button).setOnClickListener(view ->
                startActivity(InactiveTasksActivity.newIntent(this, TaskType.IRREGULAR)));
        ((EditText) findViewById(R.id.search_edit_text)).addTextChangedListener(searchTextWatcher);
        RecyclerView taskList = findViewById(R.id.admin_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(this, new Service(this));
        taskList.setAdapter(taskListAdapter);
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
}
