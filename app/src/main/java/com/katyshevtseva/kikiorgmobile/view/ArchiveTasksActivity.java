package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.ArrayList;
import java.util.List;

public class ArchiveTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_tasks);

        RecyclerView taskList = findViewById(R.id.archived_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(new TaskListAdapter(this, new Service(this)));
    }

    private static class TaskHolder extends RecyclerView.ViewHolder {
        private final TaskListAdapter taskListAdapter;
        private final Service service;

        TaskHolder(View view, TaskListAdapter taskListAdapter, Service service) {
            super(view);
            this.taskListAdapter = taskListAdapter;
            this.service = service;
        }

        void bind(RegularTask regularTask) {
            ((TextView) itemView.findViewById(R.id.archive_task_title_view)).setText(regularTask.getTitle());
            ((TextView) itemView.findViewById(R.id.archive_task_desc_view)).setText(regularTask.getAdminTaskListDesk());
            itemView.findViewById(R.id.resume_archive_task_button).setOnClickListener(view -> {
                service.resumeTask(regularTask);
                taskListAdapter.updateContent();
            });
        }
    }

    private static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<RegularTask> tasks;
        private final ArchiveTasksActivity context;
        private final Service service;

        TaskListAdapter(ArchiveTasksActivity context, Service service) {
            this.context = context;
            this.service = service;
            updateContent();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.archived_task_item, parent, false);
            return new TaskHolder(view, this, service);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            RegularTask task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        void updateContent() {
            tasks = new ArrayList<>(service.getArchivedRegularTasks());
            notifyDataSetChanged();
        }
    }
}
