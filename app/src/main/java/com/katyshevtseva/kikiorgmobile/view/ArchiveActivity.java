package com.katyshevtseva.kikiorgmobile.view;

import android.content.Context;
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

import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        RecyclerView taskList = findViewById(R.id.archive_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(new TaskListAdapter(this, new Service(this)));
    }

    private static class TaskHolder extends RecyclerView.ViewHolder {
        private TaskListAdapter taskListAdapter;
        private Service service;

        TaskHolder(View view, TaskListAdapter taskListAdapter, Service service) {
            super(view);
            this.taskListAdapter = taskListAdapter;
            this.service = service;
        }

        void bind(final RegularTask task) {
            ((TextView) itemView.findViewById(R.id.archive_task_title_view)).setText(task.getTitle());
            ((TextView) itemView.findViewById(R.id.archive_task_desc_view)).setText(task.getAdminTaskListDesk());
            itemView.findViewById(R.id.resume_button).setOnClickListener(view -> {
                service.resumeTask(task);
                taskListAdapter.updateContent();
            });
        }
    }

    private static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<RegularTask> items;
        private Context context;
        private Service service;

        TaskListAdapter(Context context, Service service) {
            this.context = context;
            this.service = service;
            updateContent();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.archivated_task_item, parent, false);
            return new TaskHolder(view, this, service);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            RegularTask item = items.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        void updateContent() {
            items = service.getArchivedRegularTasks();
            notifyDataSetChanged();
        }
    }
}
