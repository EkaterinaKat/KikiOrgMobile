package com.katyshevtseva.kikiorgmobile.view.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.view.DatelessTaskEditDialog;
import com.katyshevtseva.kikiorgmobile.view.QuestionDialog;

import java.util.List;

public class DatelessTaskRecycleView {

    static class TaskHolder extends RecyclerView.ViewHolder {
        private TaskListAdapter taskListAdapter;
        private AppCompatActivity context;
        private Service service;

        TaskHolder(View view, TaskListAdapter taskListAdapter, AppCompatActivity context, Service service) {
            super(view);
            this.taskListAdapter = taskListAdapter;
            this.context = context;
            this.service = service;
        }

        void bind(DatelessTask task) {
            ((TextView) itemView.findViewById(R.id.task_title_view)).setText(task.getIdAndTitleInfo());
            itemView.findViewById(R.id.edit_task_button).setOnClickListener(view ->
                    new DatelessTaskEditDialog(task, service, taskListAdapter::updateContent)
                            .show(context.getSupportFragmentManager(), "DatelessTaskEditDialog"));
            itemView.findViewById(R.id.delete_task_button).setOnClickListener(view ->
                    new QuestionDialog("Delete?", getDeletionDialogAnswerHandler(task))
                            .show(context.getSupportFragmentManager(), "dialog111"));
        }

        private QuestionDialog.AnswerHandler getDeletionDialogAnswerHandler(final DatelessTask task) {
            return answer -> {
                if (answer) {
                    service.deleteTask(task);
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_LONG).show();
                    taskListAdapter.updateContent();
                }
            };
        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private final int TASK_LAYOUT = R.layout.admin_task_list_item;

        private List<DatelessTask> tasks;
        private AppCompatActivity context;
        private Service service;

        public TaskListAdapter(AppCompatActivity context, Service service) {
            this.context = context;
            this.service = service;
            updateContent();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(TASK_LAYOUT, parent, false);
            return new TaskHolder(view, this, context, service);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            DatelessTask task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public void updateContent() {
            tasks = service.getAllDatelessTasks();
            notifyDataSetChanged();
        }
    }
}