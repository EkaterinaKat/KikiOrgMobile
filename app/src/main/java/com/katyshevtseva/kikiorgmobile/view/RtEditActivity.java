package com.katyshevtseva.kikiorgmobile.view;

import static com.katyshevtseva.kikiorgmobile.core.enums.WayOfBeginSpecifying.ABSOLUTE;
import static com.katyshevtseva.kikiorgmobile.core.enums.WayOfBeginSpecifying.RELATIVE;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.selectSpinnerItemByValue;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.setEditTextListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.RegularTaskService;
import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.enums.WayOfBeginSpecifying;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.utils.OneInKnob;
import com.katyshevtseva.kikiorgmobile.view.utils.FragmentUpdateListener;
import com.katyshevtseva.kikiorgmobile.view.utils.MyTimePicker;

import java.util.Arrays;

public class RtEditActivity extends AppCompatActivity implements FragmentUpdateListener {
    private static final String EXTRA_TASK_ID = "task_id";
    private RegularTask existing;
    private final DatesSelectFragment datesFragment = new DatesSelectFragment();

    private MyTimePicker beginTp;
    private MyTimePicker durationTp;

    private EditText titleEdit;
    private EditText descEdit;
    private Spinner periodTypeSpinner;
    private EditText periodEditText;
    private Button doneButton;
    private Spinner wobsSpinner;
    private LinearLayout beginTimeContainer;
    private Button durationClearButton;

    public static Intent newIntent(Context context, @Nullable RegularTask task) {
        Intent intent = new Intent(context, RtEditActivity.class);
        if (task != null) {
            intent.putExtra(EXTRA_TASK_ID, task.getId());
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rt_edit);

        initializeControls();
        setDoneButtonAccessibility();
        setControlListeners();
        initTimePickers();
        setInitFieldValues();
    }

    private void initializeControls() {
        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        doneButton = findViewById(R.id.save_task_button);
        periodTypeSpinner = findViewById(R.id.period_type_spinner);
        periodEditText = findViewById(R.id.period_edit_text);
        wobsSpinner = findViewById(R.id.way_of_begin_specifying_spinner);
        beginTimeContainer = findViewById(R.id.begin_time_container);
        durationClearButton = findViewById(R.id.duration_clear_button);
        getSupportFragmentManager().beginTransaction().add(R.id.rt_dates_container, datesFragment).commit();
    }

    private void initTimePickers() {
        beginTp = new MyTimePicker(findViewById(R.id.begin_view), this,
                null, null, findViewById(R.id.begin_time_container));
        durationTp = new MyTimePicker(findViewById(R.id.duration_view), this,
                null, null, findViewById(R.id.duration_container));
    }

    private void setDoneButtonAccessibility() {
        doneButton.setEnabled(!isEmpty(titleEdit)
                && !datesFragment.isEmpty()
                && !isEmpty(periodEditText)
                && !periodEditText.getText().toString().equals("0"));

    }

    private void setControlListeners() {
        adjustSpinner(this, periodTypeSpinner, Arrays.asList(PeriodType.values()),
                selectedItem -> setDoneButtonAccessibility());
        doneButton.setOnClickListener(view -> saveTask());
        setEditTextListener(periodEditText, text -> setDoneButtonAccessibility());
        setEditTextListener(titleEdit, text -> setDoneButtonAccessibility());

        adjustSpinner(this, wobsSpinner,
                Arrays.asList(WayOfBeginSpecifying.values()), wobsSpinnerListener);
        selectSpinnerItemByValue(wobsSpinner, WayOfBeginSpecifying.NONE);

        durationClearButton.setOnClickListener(view -> durationTp.clear());
    }

    private void saveTask() {
        RegularTaskService.INSTANCE.save(
                existing,
                titleEdit.getText().toString(),
                descEdit.getText().toString(),
                (PeriodType) periodTypeSpinner.getSelectedItem(),
                datesFragment.getDates(),
                Integer.parseInt(periodEditText.getText().toString()),
                durationTp.getTime(),
                beginTp.getTime(),
                wobsSpinner.getSelectedItem() == WayOfBeginSpecifying.ABSOLUTE);
        finish();
    }

    private void setInitFieldValues() {
        boolean intentIsEmpty = getIntent().getLongExtra(EXTRA_TASK_ID, -1) == -1;

        if (!intentIsEmpty)
            handleIntent();
    }

    private void handleIntent() {
        long id = getIntent().getLongExtra(EXTRA_TASK_ID, -1);

        if (id == -1)
            return;
        existing = RegularTaskService.INSTANCE.findById(id);

        titleEdit.setText(existing.getTitle());
        descEdit.setText(existing.getDesc());
        selectSpinnerItemByValue(periodTypeSpinner, existing.getPeriodType());
        periodEditText.setText("" + existing.getPeriod());
        datesFragment.setDates(existing.getDates());

        WayOfBeginSpecifying wobs = WayOfBeginSpecifying.NONE;
        if (existing.getBeginTime() != null) {
            wobs = existing.isAbsoluteWobs() ? ABSOLUTE : RELATIVE;
            beginTp.setTime(existing.getBeginTime());
        }
        selectSpinnerItemByValue(wobsSpinner, wobs);

        if (existing.getDuration() != null)
            durationTp.setTime(existing.getDuration());
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
    };

    @Override
    public void onUpdate() {
        setDoneButtonAccessibility();
    }
}