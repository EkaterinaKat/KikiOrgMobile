package com.katyshevtseva.kikiorgmobile.view.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Core;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.view.TaskCreationActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskRecycleView {

    static class TaskHolder extends RecyclerView.ViewHolder {
        private TaskListAdapter taskListAdapter;
        private Context context;

        TaskHolder(View view, TaskListAdapter taskListAdapter, Context context) {
            super(view);
            this.taskListAdapter = taskListAdapter;
            this.context = context;
        }

        void bind(TaskListItem item) {
            switch (item.getType()) {
                case HEADER:
                    ((TextView) itemView.findViewById(R.id.header_text_view)).setText(item.getText());
                    break;
                case REGULAR_TASK:
                    bindRegularTask(item.getRegularTask());
                    break;
                case IRREGULAR_TASK:
                    bindIrregularTask(item.getIrregularTask());
            }
        }

        private void bindRegularTask(final RegularTask task) {
            ((TextView) itemView.findViewById(R.id.task_title_view)).setText(task.getTitle());
            ((TextView) itemView.findViewById(R.id.task_desc_view)).setText(task.getFullDesc());
            itemView.findViewById(R.id.edit_task_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(TaskCreationActivity.newIntent(context, task));
                }
            });
            Button archiveButton = itemView.findViewById(R.id.delete_task_button);
            archiveButton.setText("Archive");
            archiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Core.getTaskService(context).archiveTask(task);
                    Toast.makeText(context, "Archived!", Toast.LENGTH_LONG).show();
                    taskListAdapter.updateContent();
                }
            });
        }

        private void bindIrregularTask(final IrregularTask task) {
            ((TextView) itemView.findViewById(R.id.task_title_view)).setText(task.getTitle());
            ((TextView) itemView.findViewById(R.id.task_desc_view)).setText(task.getFullDesc());
            itemView.findViewById(R.id.edit_task_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(TaskCreationActivity.newIntent(context, task));
                }
            });
            Button deleteButton = itemView.findViewById(R.id.delete_task_button);
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Core.getTaskService(context).deleteTask(task);
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_LONG).show();
                    taskListAdapter.updateContent();
                }
            });
        }
    }

    public static class TaskListAdapter extends RecyclerView.Adapter<TaskHolder> {
        private final int TASK_LAYOUT = R.layout.task_list_item;
        private final int HEADER_LAYOUT = R.layout.task_list_header;

        private List<TaskListItem> items;
        private Context context;

        public TaskListAdapter(Context context) {
            this.context = context;
            updateContent();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskHolder(LayoutInflater.from(context).inflate(viewType, parent, false), this, context);
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

        public void updateContent() {
            items = getTaskListItems(context);
            notifyDataSetChanged();
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

        RegularTask getRegularTask();

        IrregularTask getIrregularTask();
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
            public RegularTask getRegularTask() {
                return null;
            }

            @Override
            public IrregularTask getIrregularTask() {
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
            public RegularTask getRegularTask() {
                return regularTask;
            }

            @Override
            public IrregularTask getIrregularTask() {
                return null;
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
            public RegularTask getRegularTask() {
                return null;
            }

            @Override
            public IrregularTask getIrregularTask() {
                return irregularTask;
            }
        };
    }
}
