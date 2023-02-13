package com.katyshevtseva.kikiorgmobile.view.utils;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.view.MainActivity;
import com.katyshevtseva.kikiorgmobile.view.TaskMenuDialog;

import java.util.Date;
import java.util.List;

public class MainTaskRecycleView {

    static class TaskHolder extends RecyclerView.ViewHolder {
        private final AppCompatActivity context;
        private final TaskListAdapter adapter;

        TaskHolder(View view, AppCompatActivity context, TaskListAdapter adapter) {
            super(view);
            this.context = context;
            this.adapter = adapter;
        }

        void bind(Task task, Date date) {
            ((TextView) itemView.findViewById(R.id.task_title_view)).setText(task.getTitle());
            ((TextView) itemView.findViewById(R.id.task_desc_view)).setText(task.getDesc());
            setBackground(itemView, task);

            itemView.setOnClickListener(view -> {
                TaskMenuDialog taskMenuDialog = new TaskMenuDialog(task, adapter::updateContent, date, context);
                taskMenuDialog.show(context.getSupportFragmentManager(), "TaskMenuDialog");
            });
        }

        private void setBackground(View itemView, Task task) {
            Drawable background = null;
            switch (task.getTimeOfDay()) {
                case MORNING:
                    background = ContextCompat.getDrawable(context, R.drawable.morning);
                    break;
                case AFTERNOON:
                    background = ContextCompat.getDrawable(context, R.drawable.afternoon);
                    break;
                case EVENING:
                    background = ContextCompat.getDrawable(context, R.drawable.evening);
            }
            itemView.findViewById(R.id.root_layout).setBackground(background);
        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;
        private final MainActivity context;
        private Date date;

        public TaskListAdapter(MainActivity context, Date date) {
            this.context = context;
            this.date = date;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.main_task_list_item, parent, false);
            return new TaskHolder(view, context, this);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task, date);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public void setDate(Date date) {
            this.date = date;
            updateContent();
        }

        void updateContent() {
            tasks = Service.INSTANCE.getTasksForMainList(date);
            context.updateAlarmBanner();
            notifyDataSetChanged();
        }
    }
}
