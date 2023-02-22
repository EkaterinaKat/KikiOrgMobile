package com.katyshevtseva.kikiorgmobile.view;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.OneDaySettingService;
import com.katyshevtseva.kikiorgmobile.core.ScheduleService;
import com.katyshevtseva.kikiorgmobile.core.model.Setting;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;
import com.katyshevtseva.kikiorgmobile.utils.NoArgKnob;
import com.katyshevtseva.kikiorgmobile.utils.Time;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OneDaySettingCreationDialog extends DialogFragment {
    private final Task task;
    private final NoArgKnob activityUpdateKnob;
    private final Date date;

    private Map<TextView, Time> textViewTimeMap;

    private TextView durationView;
    private TextView beginView;
    private Button saveButton;
    private LinearLayout beginTimeContainer;
    private LinearLayout durationContainer;

    public OneDaySettingCreationDialog(Task task, NoArgKnob activityUpdateKnob, Date date) {
        this.task = task;
        this.activityUpdateKnob = activityUpdateKnob;
        this.date = date;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_one_day_setting_creation, null);
        durationView = v.findViewById(R.id.duration_view);
        beginView = v.findViewById(R.id.begin_view);
        saveButton = v.findViewById(R.id.save_button);
        beginTimeContainer = v.findViewById(R.id.begin_time_container);
        durationContainer = v.findViewById(R.id.duration_container);

        initializeTextViewTimeMap();

        ((TextView) v.findViewById(R.id.one_day_setting_dialog_title_view))
                .setText(task.getTitle() + "\n" + DateUtils.getDateString(date));

        setControlListeners();

        try {
            fillViewsWithExistingSetting(ScheduleService.INSTANCE.getAnySettingByTaskOrNull(task, date));
        } catch (Exception e) {
            ViewUtils.showAlertDialog(getContext(), e.getMessage());
        }

        return v;
    }

    private void setControlListeners() {
        saveButton.setOnClickListener(view -> saveSetting());
        durationContainer.setOnClickListener(view -> openTimePicker(durationView));
        beginTimeContainer.setOnClickListener(view -> openTimePicker(beginView));
    }

    private void saveSetting() {
        try {
            OneDaySettingService.INSTANCE.saveNew(task, textViewTimeMap.get(durationView), textViewTimeMap.get(beginView), date);
        } catch (Exception e) {
            ViewUtils.showAlertDialog(getContext(), e.getMessage());
        }
        dismiss();
    }

    private void openTimePicker(TextView textView) {
        new TimePickerDialog(getContext(),
                (timePicker, hour, min) -> {
                    setTime(textView, new Time(hour, min));
                    setSaveButtonAccessibility();
                }, 0, 0, true).show();
    }

    private void setSaveButtonAccessibility() {
        saveButton.setEnabled(timeViewIsFilled(durationView) && timeViewIsFilled(beginView));
    }

    private boolean timeViewIsFilled(TextView textView) {
        return textViewTimeMap.get(textView) != null;
    }

    private void fillViewsWithExistingSetting(Setting setting) {
        if (setting != null) {
            if (setting.getDuration() != null) {
                setTime(durationView, setting.getDuration());
            }
            if (setting.getBeginTime() != null) {
                setTime(beginView, ScheduleService.INSTANCE.getAbsoluteBeginTime(setting));
            }
        }
        setSaveButtonAccessibility();
    }

    private void setTime(TextView textView, Time time) {
        textViewTimeMap.put(textView, time);
        textView.setText(time == null ? getResources().getString(R.string.empty_field) : time.getS());
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        activityUpdateKnob.execute();
    }

    private void initializeTextViewTimeMap() {
        textViewTimeMap = new HashMap<>();
        textViewTimeMap.put(durationView, null);
        textViewTimeMap.put(beginView, null);
    }
}
