package com.katyshevtseva.kikiorgmobile.view.utils;

import android.app.DatePickerDialog;
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
import com.katyshevtseva.kikiorgmobile.core.DateUtils;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;

import java.util.Date;
import java.util.List;

public class MainTaskRecycleView {

    static class TaskHolder extends RecyclerView.ViewHolder {
        private AppCompatActivity context;
        private Service service;
        private TaskListAdapter adapter;

        TaskHolder(View view, AppCompatActivity context, Service service, TaskListAdapter adapter) {
            super(view);
            this.context = context;
            this.service = service;
            this.adapter = adapter;
        }

        void bind(Task task, Date date) {
            ((TextView) itemView.findViewById(R.id.task_title_view)).setText(task.getTitle());
            ((TextView) itemView.findViewById(R.id.task_desc_view)).setText(task.getDesc());
            itemView.findViewById(R.id.done_button).setOnClickListener(view -> {
                switch (task.getType()) {
                    case IRREGULAR:
                        service.done((IrregularTask) task);
                        break;
                    case REGULAR:
                        service.done((RegularTask) task, date);
                }
                adapter.updateContent();
            });
            itemView.findViewById(R.id.one_day_reschedule_button).setOnClickListener(view -> {
                switch (task.getType()) {
                    case IRREGULAR:
                        service.rescheduleForOneDay((IrregularTask) task);
                        break;
                    case REGULAR:
                        //todo
                }
                adapter.updateContent();
            });
            itemView.findViewById(R.id.more_days_reschedule_button).setOnClickListener(
                    view -> rescheduleWithDatePicker(task));
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

        void rescheduleWithDatePicker(Task task) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context);
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                Date date = DateUtils.parse(year, month + 1, day);
                switch (task.getType()) {
                    case IRREGULAR:
                        service.rescheduleToCertainDate((IrregularTask) task, date);
                        break;
                    case REGULAR:
                        //todo
                }
                adapter.updateContent();
            });
            datePickerDialog.show();
        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;
        private AppCompatActivity context;
        private Service service;
        private Date date;

        public TaskListAdapter(AppCompatActivity context, Service service, Date date) {
            this.context = context;
            this.service = service;
            this.date = date;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.main_task_list_item, parent, false);
            return new TaskHolder(view, context, service, this);
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
            tasks = service.getTasksForMainList(date);
            notifyDataSetChanged();
        }
    }
}
