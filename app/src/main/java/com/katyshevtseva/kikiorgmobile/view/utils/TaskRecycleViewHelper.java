package com.katyshevtseva.kikiorgmobile.view.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
        private TextView headerView;
        private TextView descView;

        TaskHolder(View view) {
            super(view);
            titleView = itemView.findViewById(R.id.task_title_view);
            headerView = itemView.findViewById(R.id.header_text_view);
            descView = itemView.findViewById(R.id.task_desc_view);
        }

        void bind(TaskListItem item) {
            if (item.getType() == TaskListItemType.HEADER) {
                headerView.setText(item.getText());
            } else {
                titleView.setText(item.getText());
                descView.setText(item.getDesc());
            }

        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private final int TASK_LAYOUT = R.layout.task_list_item;
        private final int HEADER_LAYOUT = R.layout.task_list_header;

        private List<TaskListItem> items;
        private Context context;

        public TaskListAdapter(Context context) {
            items = getTaskListItems(context);
            this.context = context;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskHolder(LayoutInflater.from(context).inflate(viewType, parent, false));
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

        @Override
        public int getItemViewType(int position) {
            TaskListItem item = items.get(position);
            if (item.getType() == TaskListItemType.HEADER)
                return HEADER_LAYOUT;
            return TASK_LAYOUT;
        }

    }

    private static List<TaskListItem> getTaskListItems(Context context) {
        List<TaskListItem> items = new ArrayList<>();
        items.add(getHeader("Irregular tasks"));
        for (IrregularTask irregularTask : Core.getTaskService(context).getNotDoneIrregularTasks()) {
            items.add(toListItem(irregularTask));
        }
        items.add(getHeader("Regular tasks"));
        for (RegularTask regularTask : Core.getTaskService(context).getNotArchivedRegularTasks()) {
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

        String getDesc();
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

            @Override
            public String getDesc() {
                return null;
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

            @Override
            public String getDesc() {
                return regularTask.getFullDesc();
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

            @Override
            public String getDesc() {
                return irregularTask.getFullDesc();
            }
        };
    }
}
