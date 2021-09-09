package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.DateUtils;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.view.QuestionDialog.AnswerHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InactiveTasksActivity extends AppCompatActivity {
    private static final String EXTRA_TASK_TYPE = "task_type";

    public static Intent newIntent(Context context, TaskType taskType) {
        Intent intent = new Intent(context, InactiveTasksActivity.class);
        intent.putExtra(EXTRA_TASK_TYPE, taskType.getCode());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inactive_tasks);

        TaskType taskType = TaskType.findByCode(getIntent().getIntExtra(EXTRA_TASK_TYPE, 0));
        RecyclerView taskList = findViewById(R.id.inactive_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(new TaskListAdapter(this, new Service(this), taskType));
    }

    private static class TaskHolder extends RecyclerView.ViewHolder {
        private TaskListAdapter taskListAdapter;
        private Service service;
        private InactiveTasksActivity context;

        TaskHolder(View view, InactiveTasksActivity context, TaskListAdapter taskListAdapter, Service service) {
            super(view);
            this.taskListAdapter = taskListAdapter;
            this.service = service;
            this.context = context;
        }

        void bind(final Task task) {
            switch (task.getType()) {
                case REGULAR:
                    bindRegularTask((RegularTask) task);
                    break;
                case IRREGULAR:
                    bindIrregularTask((IrregularTask) task);
            }
        }

        private void bindRegularTask(RegularTask regularTask) {
            ((TextView) itemView.findViewById(R.id.archive_task_title_view)).setText(regularTask.getTitle());
            ((TextView) itemView.findViewById(R.id.archive_task_desc_view)).setText(regularTask.getAdminTaskListDesk());
            itemView.findViewById(R.id.resume_archive_task_button).setOnClickListener(view -> {
                service.resumeTask(regularTask);
                taskListAdapter.updateContent();
            });
        }

        private void bindIrregularTask(IrregularTask irregularTask) {
            ((TextView) itemView.findViewById(R.id.done_task_title_view)).setText(irregularTask.getTitle());
            ((TextView) itemView.findViewById(R.id.done_task_desc_view)).setText(irregularTask.getAdminTaskListDesk());
            itemView.findViewById(R.id.return_to_work_done_task_button).setOnClickListener(view -> {
                returnToWorkButtonListener(irregularTask);
            });
            itemView.findViewById(R.id.delete_done_task).setOnClickListener(view -> {
                DialogFragment dlg1 = new QuestionDialog("Delete?", getDeletionDialogAnswerHandler(irregularTask));
                dlg1.show(context.getSupportFragmentManager(), "dialog1");
            });
        }

        private void returnToWorkButtonListener(IrregularTask irregularTask) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context);
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                Date selectedDate = DateUtils.parse(year, month + 1, day);
                service.returnToWorkTask(irregularTask, selectedDate);
                taskListAdapter.updateContent();
            });
            datePickerDialog.show();
        }

        private AnswerHandler getDeletionDialogAnswerHandler(final IrregularTask task) {
            return answer -> {
                if (answer) {
                    service.deleteTask(task);
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_LONG).show();
                    taskListAdapter.updateContent();
                }
            };
        }
    }

    private static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;
        private TaskType taskType;
        private InactiveTasksActivity context;
        private Service service;

        TaskListAdapter(InactiveTasksActivity context, Service service, TaskType taskType) {
            this.context = context;
            this.service = service;
            this.taskType = taskType;
            updateContent();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int layout = 0;
            switch (taskType) {
                case REGULAR:
                    layout = R.layout.archived_task_item;
                    break;
                case IRREGULAR:
                    layout = R.layout.done_task_item;
            }
            View view = LayoutInflater.from(context).inflate(layout, parent, false);
            return new TaskHolder(view, context, this, service);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        void updateContent() {
            switch (taskType) {
                case REGULAR:
                    tasks = new ArrayList<>(service.getArchivedRegularTasks());
                    break;
                case IRREGULAR:
                    tasks = new ArrayList<>(service.getDoneIrregularTasks());
            }
            notifyDataSetChanged();
        }
    }
}
