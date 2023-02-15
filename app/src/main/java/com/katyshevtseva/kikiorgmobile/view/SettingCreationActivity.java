package com.katyshevtseva.kikiorgmobile.view;

import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.selectSpinnerItemByValue;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.SettingService;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.utils.OneInKnob;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SettingCreationActivity extends AppCompatActivity {
    private static final String EXTRA_SETTING_ID = "setting_id";

    private Map<TextView, Time> textViewTimeMap;

    private Spinner taskSpinner;
    private Spinner wobsSpinner;
    private TextView durationView;
    private TextView beginView;
    private Button saveButton;
    private LinearLayout beginTimeContainer;
    private LinearLayout durationContainer;

    public static Intent newIntent(Context context, @Nullable RtSetting rtSetting) {
        Intent intent = new Intent(context, SettingCreationActivity.class);
        if (rtSetting != null) {
            intent.putExtra(EXTRA_SETTING_ID, rtSetting.getId());
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_creation);

        initializeTextViewTimeMap();
        initializeControls();
        setSaveButtonAccessibility();
        setControlListeners();
        setInitFieldValues();
    }

    private void setInitFieldValues() {
        long settingId = getIntent().getLongExtra(EXTRA_SETTING_ID, -1);
        boolean intentIsEmpty = settingId == -1;

        if (!intentIsEmpty) {
            RtSetting setting = SettingService.INSTANCE.getRgSettingById(settingId);
            selectSpinnerItemByValue(taskSpinner, Service.INSTANCE.findTask(TaskType.REGULAR, setting.getRtId()));

            WayOfBeginSpecifying wobs = WayOfBeginSpecifying.NONE;
            if (setting.getBeginTime() != null) {
                wobs = setting.isAbsoluteWobs() ? WayOfBeginSpecifying.ABSOLUTE : WayOfBeginSpecifying.RELATIVE;
                setTime(beginView, setting.getBeginTime());
            }
            selectSpinnerItemByValue(wobsSpinner, wobs);

            if (setting.getDuration() != null)
                setTime(durationView, setting.getDuration());
        }
    }

    private void setControlListeners() {
        adjustSpinner(this, wobsSpinner,
                Arrays.asList(WayOfBeginSpecifying.values()), wobsSpinnerListener);
        selectSpinnerItemByValue(wobsSpinner, WayOfBeginSpecifying.NONE);
        adjustSpinner(this, taskSpinner, Service.INSTANCE.getNotArchivedRegularTasks(null),
                selectedItem -> setSaveButtonAccessibility());
        saveButton.setOnClickListener(view -> saveSetting());

        durationContainer.setOnClickListener(view -> openTimePicker(durationView));
        beginTimeContainer.setOnClickListener(view -> openTimePicker(beginView));
    }

    private void openTimePicker(TextView textView) {
        new TimePickerDialog(this,
                (timePicker, hour, min) -> {
                    Time time = new Time(hour, min);
                    setTime(textView, time);
                    setSaveButtonAccessibility();
                }, 0, 0, true).show();
    }

    private void setTime(TextView textView, Time time) {
        textViewTimeMap.put(textView, time);
        textView.setText(time == null ? getResources().getString(R.string.empty_field) : time.getS());
    }

    private final OneInKnob<WayOfBeginSpecifying> wobsSpinnerListener = wobs -> {
        switch (wobs) {
            case NONE:
                setTime(beginView, null);
                beginTimeContainer.setVisibility(View.GONE);
                break;
            case RELATIVE:
            case ABSOLUTE:
                beginTimeContainer.setVisibility(View.VISIBLE);
        }
    };

    private void saveSetting() {
        SettingService.INSTANCE.saveNewRgSetting((RegularTask) taskSpinner.getSelectedItem(),
                textViewTimeMap.get(durationView), textViewTimeMap.get(beginView),
                wobsSpinner.getSelectedItem() == WayOfBeginSpecifying.ABSOLUTE);
        finish();
    }

    private void setSaveButtonAccessibility() {
        boolean taskSpinnerIsFilled = taskSpinner.getSelectedItem() != null;
        boolean durationIsFilled = filled(durationView);
        boolean beginTimeIsFilled = taskSpinner.getSelectedItem() != WayOfBeginSpecifying.NONE && filled(beginView);
        saveButton.setEnabled(taskSpinnerIsFilled && (durationIsFilled || beginTimeIsFilled));
    }

    private void initializeControls() {
        taskSpinner = findViewById(R.id.task_spinner);
        wobsSpinner = findViewById(R.id.way_of_begin_specifying_spinner);
        durationView = findViewById(R.id.duration_view);
        beginView = findViewById(R.id.begin_view);
        saveButton = findViewById(R.id.save_button);
        beginTimeContainer = findViewById(R.id.begin_time_container);
        durationContainer = findViewById(R.id.duration_container);
    }

    private void initializeTextViewTimeMap() {
        textViewTimeMap = new HashMap<>();
        textViewTimeMap.put(durationView, null);
        textViewTimeMap.put(beginView, null);
    }

    private boolean filled(TextView textView) {
        return textViewTimeMap.get(textView) != null;
    }

    private enum WayOfBeginSpecifying {
        NONE, RELATIVE, ABSOLUTE
    }
}