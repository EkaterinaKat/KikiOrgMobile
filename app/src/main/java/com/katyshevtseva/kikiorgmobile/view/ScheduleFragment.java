package com.katyshevtseva.kikiorgmobile.view;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Interval;
import com.katyshevtseva.kikiorgmobile.core.ScheduleService;
import com.katyshevtseva.kikiorgmobile.core.ScheduleService.Schedule;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.utils.NoArgKnob;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleFragment extends Fragment {
    private static final int scale = 2;
    private LinearLayout intervalsBox;
    private LinearLayout notScheduledBox;
    private TextView alarmTextView;
    private Date date;
    private Schedule schedule;
    private final NoArgKnob activityUpdateKnob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        intervalsBox = view.findViewById(R.id.intervals_box);
        notScheduledBox = view.findViewById(R.id.not_scheduled_box);
        alarmTextView = view.findViewById(R.id.schedule_alarm_text_view);

        view.findViewById(R.id.add_irt_button).setOnClickListener(view1 -> {
            getContext().startActivity(IrtEditActivity.newIntent(getContext(), null));
        });

        if (schedule != null) {
            showPartsOfSchedule();
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
            Schedule schedule = ScheduleService.INSTANCE.getSchedule(date);
            this.schedule = schedule;

            if (intervalsBox != null) {
                showPartsOfSchedule();
            }

        } catch (Exception e) {
            ViewUtils.showAlertDialog(getActivity(), e.getMessage());
        }
    }

    private void showPartsOfSchedule() {
        updateAlarmBanner(schedule.getWarning());
        showIntervals(schedule.getIntervals());
        showNotScheduledTasks(schedule.getNotScheduledTasks());
    }

    private void showIntervals(List<Interval> intervals) {
        intervalsBox.removeAllViews();
        for (Interval interval : intervals) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setMinimumHeight(interval.getLength() * scale);
            linearLayout.setMinimumWidth(600);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
            if (interval.isEmpty()) {
                linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            if (interval.getTask() != null) {
                linearLayout.setOnClickListener(view -> taskClickListener(interval.getTask()));
            }

            if (!GeneralUtil.isEmpty(interval.getTitle())) {
                TextView titleView = new TextView(getActivity());
                titleView.setText(interval.getTitle());
                titleView.setTextSize(20);
                linearLayout.addView(titleView);
            }

            TextView timeView = new TextView(getActivity());
            timeView.setText(interval.getTimeString());
            linearLayout.addView(timeView);

            intervalsBox.addView(linearLayout);
        }
    }

    private void showNotScheduledTasks(List<Task> tasks) {
        notScheduledBox.removeAllViews();
        for (Task task : tasks) {
            TextView textView = new TextView(getContext());
            textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.task_block));
            textView.setText(task.getTitle());
            textView.setSingleLine(false);
            textView.setPadding(15, 15, 15, 15);
            notScheduledBox.addView(textView);

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

    private void updateAlarmBanner(String warning) {
        if (warning != null) {
            alarmTextView.setText(warning);
            alarmTextView.setVisibility(View.VISIBLE);
        } else {
            alarmTextView.setVisibility(View.GONE);
        }
    }
}