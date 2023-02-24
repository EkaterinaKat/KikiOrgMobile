package com.katyshevtseva.kikiorgmobile.view;

import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.selectSpinnerItemByValue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.RtSettingService;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.utils.OneInKnob;
import com.katyshevtseva.kikiorgmobile.view.utils.MyTimePicker;
import com.katyshevtseva.kikiorgmobile.view.utils.SwipeManager;

import java.util.Arrays;

public class SettingCreationActivity extends AppCompatActivity {
    private static final String EXTRA_SETTING_ID = "setting_id";
    private SwipeManager swipeManager;

    private RtSetting existing;
    private MyTimePicker beginTp;
    private MyTimePicker durationTp;

    private Spinner taskSpinner;
    private Spinner wobsSpinner;
    private Button saveButton;
    private LinearLayout beginTimeContainer;
    private Button durationClearButton;

    public static Intent newIntent(Context context, @Nullable RegularTask task) {
        Intent intent = new Intent(context, SettingCreationActivity.class);

        if (task != null) {
            try {
                RtSetting rtSetting = RtSettingService.INSTANCE.getRtSettingOrNull(task);
                if (rtSetting != null) {
                    intent.putExtra(EXTRA_SETTING_ID, rtSetting.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_creation);

        initializeControls();
        initTimePickers();
        setSaveButtonAccessibility();
        setControlListeners();
        setInitFieldValues();

        swipeManager = new SwipeManager(this);
        swipeManager.setLeftSwipeListener(this::askToFinish);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Boolean result = swipeManager.dispatchTouchEvent(ev);
        return result == null ? super.dispatchTouchEvent(ev) : result;
    }

    private void askToFinish() {
        new QuestionDialog("Exit?", answer -> {
            if (answer)
                finish();
        }).show(getSupportFragmentManager(), "exitDialog");
    }

    private void setInitFieldValues() {
        long settingId = getIntent().getLongExtra(EXTRA_SETTING_ID, -1);
        boolean intentIsEmpty = settingId == -1;

        if (!intentIsEmpty) {
            existing = RtSettingService.INSTANCE.getRgSettingById(settingId);
            selectSpinnerItemByValue(taskSpinner, Service.INSTANCE.findTask(TaskType.REGULAR, existing.getRtId()));
            taskSpinner.setEnabled(false);

            WayOfBeginSpecifying wobs = WayOfBeginSpecifying.NONE;
            if (existing.getBeginTime() != null) {
                wobs = existing.isAbsoluteWobs() ? WayOfBeginSpecifying.ABSOLUTE : WayOfBeginSpecifying.RELATIVE;
                beginTp.setTime(existing.getBeginTime());
            }
            selectSpinnerItemByValue(wobsSpinner, wobs);

            if (existing.getDuration() != null)
                durationTp.setTime(existing.getDuration());
        }
    }

    private void setControlListeners() {
        adjustSpinner(this, wobsSpinner,
                Arrays.asList(WayOfBeginSpecifying.values()), wobsSpinnerListener);
        selectSpinnerItemByValue(wobsSpinner, WayOfBeginSpecifying.NONE);
        adjustSpinner(this, taskSpinner, Service.INSTANCE.getNotArchivedRegularTasks(null),
                selectedItem -> setSaveButtonAccessibility());
        saveButton.setOnClickListener(view -> saveSetting());

        durationClearButton.setOnClickListener(view -> {
            durationTp.clear();
            setSaveButtonAccessibility();
        });
    }

    private final OneInKnob<WayOfBeginSpecifying> wobsSpinnerListener = wobs -> {
        switch (wobs) {
            case NONE:
                beginTp.clear();
                beginTimeContainer.setVisibility(View.GONE);
                break;
            case RELATIVE:
            case ABSOLUTE:
                beginTimeContainer.setVisibility(View.VISIBLE);
        }
        setSaveButtonAccessibility();
    };

    private void saveSetting() {
        if (existing != null) {
            RtSettingService.INSTANCE.editRtSetting(existing, durationTp.getTime(), beginTp.getTime(),
                    wobsSpinner.getSelectedItem() == WayOfBeginSpecifying.ABSOLUTE);
        } else {
            RtSettingService.INSTANCE.saveNewRgSetting((RegularTask) taskSpinner.getSelectedItem(),
                    durationTp.getTime(), beginTp.getTime(),
                    wobsSpinner.getSelectedItem() == WayOfBeginSpecifying.ABSOLUTE);
        }
        finish();
    }

    private void setSaveButtonAccessibility() {
        boolean taskSpinnerIsFilled = taskSpinner.getSelectedItem() != null;
        boolean beginTimeIsFilled = taskSpinner.getSelectedItem() != WayOfBeginSpecifying.NONE && beginTp.isFilled();
        saveButton.setEnabled(taskSpinnerIsFilled && (durationTp.isFilled() || beginTimeIsFilled));
    }

    private void initializeControls() {
        taskSpinner = findViewById(R.id.task_spinner);
        wobsSpinner = findViewById(R.id.way_of_begin_specifying_spinner);
        saveButton = findViewById(R.id.save_button);
        beginTimeContainer = findViewById(R.id.begin_time_container);
        durationClearButton = findViewById(R.id.duration_clear_button);
    }

    private void initTimePickers() {
        beginTp = new MyTimePicker(findViewById(R.id.begin_view), this,
                (hour, min) -> setSaveButtonAccessibility(), null, findViewById(R.id.begin_time_container));
        durationTp = new MyTimePicker(findViewById(R.id.duration_view), this,
                (hour, min) -> setSaveButtonAccessibility(), null, findViewById(R.id.duration_container));
    }

    private enum WayOfBeginSpecifying {
        NONE, RELATIVE, ABSOLUTE
    }

    @Override
    protected void onResume() {
        GeneralUtil.setImmersiveStickyMode(getWindow());
        super.onResume();
    }
}