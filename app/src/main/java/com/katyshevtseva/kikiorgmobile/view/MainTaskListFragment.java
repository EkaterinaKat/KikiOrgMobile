package com.katyshevtseva.kikiorgmobile.view;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.knobs.NoArgKnob;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainTaskListFragment extends Fragment {
    private LinearLayout taskBox;
    private Date date;
    private List<Task> tasks;
    private final NoArgKnob activityUpdateKnob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        taskBox = view.findViewById(R.id.task_box);

        view.findViewById(R.id.add_irt_button).setOnClickListener(view1 -> {
            getContext().startActivity(IrtEditActivity.newIntent(getContext(), null));
        });

        if (tasks != null) {
            showTasks();
        }
        return view;
    }

    @Override
    public void onResume() {
        updateSchedule();
        super.onResume();
    }

    public void setDate(Date date) {
        this.date = date;
        updateSchedule();
    }

    private void updateSchedule() {
        if (date == null || getActivity() == null)
            return;

        try {
            List<Task> tasks = Service.INSTANCE.getTasksForMainList(date);
            this.tasks = tasks;

            if (taskBox != null) {
                showTasks();
            }

        } catch (Exception e) {
            ViewUtils.showAlertDialog(getActivity(), e.getMessage());
        }
    }

    private void showTasks() {
        taskBox.removeAllViews();
        for (Task task : tasks) {
            TextView textView = new TextView(getContext());
            textView.setBackground(ViewUtils.getBackground(task.getUrgency(), getContext()));
            textView.setText(task.getTitle());
            textView.setSingleLine(false);
            textView.setPadding(15, 15, 15, 15);
            taskBox.addView(textView);

            LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);
            textView.setLayoutParams(params);

            textView.setOnClickListener(view -> taskClickListener(task));
        }
    }

    private void taskClickListener(Task task) {
        TaskMenuDialog taskMenuDialog = new TaskMenuDialog(task, () -> {
            updateSchedule();
            activityUpdateKnob.execute();
        }, date, (AppCompatActivity) getActivity());
        taskMenuDialog.show(getActivity().getSupportFragmentManager(), "TaskMenuDialog");
    }
}