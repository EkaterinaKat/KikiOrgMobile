package com.katyshevtseva.kikiorgmobile.view.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.model.Task;

import java.util.List;

public class TaskRecycleViewHelper {

    static class TaskHolder extends RecyclerView.ViewHolder {
        private TextView titleView;

        TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_list_item, parent, false));
            titleView = itemView.findViewById(R.id.task_title_view);
        }

        void bind(Task task) {
            titleView.setText(task.getTitle());
        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;
        private Context context;

        public TaskListAdapter(List<Task> tasks, Context context) {
            this.tasks = tasks;
            this.context = context;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            return new TaskHolder(layoutInflater, parent);
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
    }
}
