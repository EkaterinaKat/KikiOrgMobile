package com.katyshevtseva.kikiorgmobile.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Interval;
import com.katyshevtseva.kikiorgmobile.core.ScheduleService;
import com.katyshevtseva.kikiorgmobile.core.ScheduleService.Schedule;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

import java.util.Date;
import java.util.List;

public class ScheduleFragment extends Fragment {
    private LinearLayout intervalsBox;
    private LinearLayout notScheduledBox;
    private TextView alarmTextView;
    private Date date;
    private Schedule schedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        intervalsBox = view.findViewById(R.id.intervals_box);
        notScheduledBox = view.findViewById(R.id.not_scheduled_box);
        alarmTextView = view.findViewById(R.id.schedule_alarm_text_view);

        if (schedule != null) {
            showSchedule();
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
        if (date == null)
            return;

        try {
            Schedule schedule = ScheduleService.INSTANCE.getSchedule(date);
            this.schedule = schedule;

            if (intervalsBox != null) {
                showSchedule();
            }

        } catch (Exception e) {
            ViewUtils.showAlertDialog(getActivity(), e.getMessage());
        }
    }

    private void showSchedule() {
        updateAlarmBanner(schedule.getWarning());
        showIntervals(schedule.getIntervals());
        showNotScheduledTasks(schedule.getNotScheduledTasks());
    }

    private void showIntervals(List<Interval> intervals) {
        intervalsBox.removeAllViews();
        for (Interval interval : intervals) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setMinimumHeight(interval.getLength() * 2);
            linearLayout.setMinimumWidth(600);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundColor(Color.parseColor(interval.getColor()));

            TextView titleView = new TextView(getActivity());
            titleView.setText(interval.getTitle());
            titleView.setTextSize(20);
            linearLayout.addView(titleView);

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
            textView.setText(task.getTitle());
            textView.setSingleLine(false);
            textView.setWidth(230);
            textView.setPadding(15, 20, 15, 20);
            notScheduledBox.addView(textView);
        }
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