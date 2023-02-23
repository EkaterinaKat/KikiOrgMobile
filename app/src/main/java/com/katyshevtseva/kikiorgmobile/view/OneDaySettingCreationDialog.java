package com.katyshevtseva.kikiorgmobile.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.katyshevtseva.kikiorgmobile.view.utils.MyTimePicker;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

import java.util.Date;

public class OneDaySettingCreationDialog extends DialogFragment {
    private final Task task;
    private final NoArgKnob activityUpdateKnob;
    private final Date date;

    private MyTimePicker beginTp;
    private MyTimePicker durationTp;

    private Button saveButton;


    public OneDaySettingCreationDialog(Task task, NoArgKnob activityUpdateKnob, Date date) {
        this.task = task;
        this.activityUpdateKnob = activityUpdateKnob;
        this.date = date;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_one_day_setting_creation, null);

        saveButton = v.findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> saveSetting());

        ((TextView) v.findViewById(R.id.one_day_setting_dialog_title_view))
                .setText(task.getTitle() + "\n" + DateUtils.getDateString(date));

        try {
            initTimePickers(v, ScheduleService.INSTANCE.getAnySettingByTaskOrNull(task, date));
        } catch (Exception e) {
            ViewUtils.showAlertDialog(getContext(), e.getMessage());
        }

        setSaveButtonAccessibility();
        return v;
    }

    private void saveSetting() {
        try {
            OneDaySettingService.INSTANCE.saveNew(task, durationTp.getTime(), beginTp.getTime(), date);
        } catch (Exception e) {
            ViewUtils.showAlertDialog(getContext(), e.getMessage());
        }
        dismiss();
    }

    private void setSaveButtonAccessibility() {
        saveButton.setEnabled(durationTp.isFilled() && beginTp.isFilled());
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        activityUpdateKnob.execute();
    }

    private void initTimePickers(View v, Setting existing) {
        beginTp = new MyTimePicker(
                v.findViewById(R.id.begin_view),
                getContext(),
                (hour, min) -> setSaveButtonAccessibility(),
                existing != null ? ScheduleService.INSTANCE.getAbsoluteBeginTime(existing) : null,
                v.findViewById(R.id.begin_time_container));

        durationTp = new MyTimePicker(
                v.findViewById(R.id.duration_view),
                getContext(),
                (hour, min) -> setSaveButtonAccessibility(),
                existing != null ? existing.getDuration() : null,
                v.findViewById(R.id.duration_container));
    }
}
