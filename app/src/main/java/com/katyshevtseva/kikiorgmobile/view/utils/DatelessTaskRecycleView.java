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
import com.katyshevtseva.kikiorgmobile.core.DatelessTaskService;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.view.DatelessTaskEditDialog;
import com.katyshevtseva.kikiorgmobile.view.QuestionDialog;

import java.util.List;

public class DatelessTaskRecycleView {

    static class TaskHolder extends RecyclerView.ViewHolder {
        private final TaskListAdapter taskListAdapter;
        private final AppCompatActivity context;

        TaskHolder(View view, TaskListAdapter taskListAdapter, AppCompatActivity context) {
            super(view);
            this.taskListAdapter = taskListAdapter;
            this.context = context;
        }

        void bind(DatelessTask task) {
            ((TextView) itemView.findViewById(R.id.task_title_view)).setText(task.getIdAndTitleInfo());
            itemView.findViewById(R.id.edit_task_button).setOnClickListener(view ->
                    new DatelessTaskEditDialog(task, taskListAdapter::updateContent)
                            .show(context.getSupportFragmentManager(), "DatelessTaskEditDialog"));
            itemView.findViewById(R.id.delete_task_button).setOnClickListener(view ->
                    new QuestionDialog("Delete?", getDeletionDialogAnswerHandler(task))
                            .show(context.getSupportFragmentManager(), "dialog111"));
            itemView.findViewById(R.id.move_to_end_task_button).setOnClickListener(view ->
                    new QuestionDialog("Move to end?", getMoveToEndDialogAnswerHandler(task))
                            .show(context.getSupportFragmentManager(), "dialog112"));
        }

        private QuestionDialog.AnswerHandler getDeletionDialogAnswerHandler(final DatelessTask task) {
            return answer -> {
                if (answer) {
                    DatelessTaskService.INSTANCE.deleteTask(task);
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_LONG).show();
                    taskListAdapter.updateContent();
                }
            };
        }

        private QuestionDialog.AnswerHandler getMoveToEndDialogAnswerHandler(final DatelessTask task) {
            return answer -> {
                if (answer) {
                    DatelessTaskService.INSTANCE.moveDatelessTaskToEnd(task);
                    taskListAdapter.updateContent();
                }
            };
        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<DatelessTask> tasks;
        private final AppCompatActivity context;

        public TaskListAdapter(AppCompatActivity context) {
            this.context = context;
            updateContent();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.dateless_task_list_item, parent, false);
            return new TaskHolder(view, this, context);
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
            tasks = DatelessTaskService.INSTANCE.getAllDatelessTasks();
            notifyDataSetChanged();
        }
    }
}
