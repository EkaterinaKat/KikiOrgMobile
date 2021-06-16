package com.katyshevtseva.kikiorgmobile.view.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Core;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.ArrayList;
import java.util.List;

public class TaskRecycleViewHelper {

    static class TaskHolder extends RecyclerView.ViewHolder {
        private TextView titleView;

        TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_list_item, parent, false));
            titleView = itemView.findViewById(R.id.task_title_view);
        }

        void bind(TaskListItem item) {
            titleView.setText(item.getText());
        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<TaskListItem> items;
        private Context context;

        public TaskListAdapter(Context context) {
            items = getTaskListItems(context);
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
            TaskListItem item = items.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static List<TaskListItem> getTaskListItems(Context context) {
        List<TaskListItem> items = new ArrayList<>();
        items.add(getHeader("Irregular tasks"));
        for(IrregularTask irregularTask: Core.getTaskService(context).getNotDoneIrregularTasks()){
            items.add(toListItem(irregularTask));
        }
        items.add(getHeader("Regular tasks"));
        for(RegularTask regularTask: Core.getTaskService(context).getNotArchivedRegularTasks()){
            items.add(toListItem(regularTask));
        }
        return items;
    }

    private enum TaskListItemType {
        REGULAR_TASK, IRREGULAR_TASK, HEADER
    }

    private interface TaskListItem {
        TaskListItemType getType();
        String getText();
        long getId();
    }

    private static TaskListItem getHeader(final String text) {
        return new TaskListItem() {
            @Override
            public TaskListItemType getType() {
                return TaskListItemType.HEADER;
            }

            @Override
            public String getText() {
                return text;
            }

            @Override
            public long getId() {
                return 0;
            }
        };
    }

    private static TaskListItem toListItem(final RegularTask regularTask) {
        return new TaskListItem() {
            @Override
            public TaskListItemType getType() {
                return TaskListItemType.REGULAR_TASK;
            }

            @Override
            public String getText() {
                return regularTask.getTitle();
            }

            @Override
            public long getId() {
                return regularTask.getId();
            }
        };
    }

    private static TaskListItem toListItem(final IrregularTask irregularTask) {
        return new TaskListItem() {
            @Override
            public TaskListItemType getType() {
                return TaskListItemType.IRREGULAR_TASK;
            }

            @Override
            public String getText() {
                return irregularTask.getTitle();
            }

            @Override
            public long getId() {
                return irregularTask.getId();
            }
        };
    }
}
