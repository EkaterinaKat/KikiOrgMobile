package com.katyshevtseva.kikiorgmobile.view;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.OptionalTaskService;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.core.model.OptionalTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.knobs.NoArgKnob;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainTaskListFragment extends Fragment {
    private LinearLayout taskBox;
    private Date date;
    private List<Task> tasks;
    private final NoArgKnob activityUpdateKnob;
    private boolean otShown = false;
    private ImageView otImageView;
    private ScrollView otScrollView;
    private LinearLayout otBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_task_list, container, false);
        taskBox = view.findViewById(R.id.task_box);
        otImageView = view.findViewById(R.id.ot_image_view);
        otImageView.setOnClickListener(view1 -> otButtonListener());
        otScrollView = view.findViewById(R.id.ot_scroll_view);
        otBox = view.findViewById(R.id.ot_box);
        fillOtBox();

        if (tasks != null) {
            showTasks();
        }
        return view;
    }

    private void fillOtBox() {
        otBox.removeAllViews();
        for (OptionalTask task : OptionalTaskService.INSTANCE.getOptionalTasks()) {
            otBox.addView(getTaskView(task.getTitle(), () -> optTaskClickListener(task),
                    ContextCompat.getDrawable(getContext(), R.drawable.ot)));
        }
        otBox.addView(getTaskView("+", () -> optTaskClickListener(null),
                ContextCompat.getDrawable(getContext(), R.drawable.ot)));
    }

    private void optTaskClickListener(OptionalTask task) {
        new OtDialog(task, this::fillOtBox).show(getActivity().getSupportFragmentManager(), "TaskMenuDialog");
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

    private void otButtonListener() {
        if (otShown) {
            otImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.ot_show));
            otScrollView.setVisibility(View.GONE);
        } else {
            otImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.ot_hide));
            otScrollView.setVisibility(View.VISIBLE);
        }
        otShown = !otShown;
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

        for (TimeOfDay timeOfDay : TimeOfDay.values()) {

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackground(ViewUtils.getBackground2(timeOfDay, getContext()));
            linearLayout.setMinimumWidth(650);
            linearLayout.setGravity(Gravity.CENTER);

            LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);
            linearLayout.setLayoutParams(params);

            for (Task task : getTasksByTimeOfDay(timeOfDay)) {
                linearLayout.addView(getTaskView(task.getTitle(), () -> taskClickListener(task),
                        ViewUtils.getBackground(task.getUrgency(), getContext())));
            }
            taskBox.addView(linearLayout);
        }
    }

    private List<Task> getTasksByTimeOfDay(TimeOfDay timeOfDay) {
        return tasks.stream().filter(task -> task.getTimeOfDay() == timeOfDay).collect(Collectors.toList());
    }

    private View getTaskView(String text, NoArgKnob onClickListener, Drawable background) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        TextView textView = new TextView(getContext());

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackground(background);
        linearLayout.setMinimumWidth(500);
        linearLayout.setOnClickListener(view -> onClickListener.execute());

        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        linearLayout.setLayoutParams(params);

        textView.setText(text);
        textView.setSingleLine(false);
        textView.setPadding(15, 15, 15, 15);
        textView.setTextSize(16);

        linearLayout.addView(textView);
        return linearLayout;
    }

    private void taskClickListener(Task task) {
        TaskMenuDialog taskMenuDialog = new TaskMenuDialog(task, () -> {
            updateSchedule();
            activityUpdateKnob.execute();
        }, date, (AppCompatActivity) getActivity());
        taskMenuDialog.show(getActivity().getSupportFragmentManager(), "TaskMenuDialog");
    }
}