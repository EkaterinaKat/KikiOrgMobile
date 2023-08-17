package com.katyshevtseva.kikiorgmobile.view;

import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.selectSpinnerItemByValue;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.setEditTextListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.RegularTaskService;
import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.view.utils.FragmentUpdateListener;
import com.katyshevtseva.kikiorgmobile.view.utils.KomActivity;

import java.util.Arrays;

public class RtEditActivity extends KomActivity implements FragmentUpdateListener {
    private static final String EXTRA_TASK_ID = "task_id";
    private RegularTask existing;
    private final DatesSelectFragment datesFragment = new DatesSelectFragment();

    private EditText titleEdit;
    private EditText descEdit;
    private Spinner periodTypeSpinner;
    private Spinner timeOfDaySpinner;
    private EditText periodEditText;
    private Button doneButton;

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
        adjustControls();
        setInitFieldValues();
    }

    private void initializeControls() {
        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        doneButton = findViewById(R.id.save_task_button);
        periodTypeSpinner = findViewById(R.id.period_type_spinner);
        periodEditText = findViewById(R.id.period_edit_text);
        getSupportFragmentManager().beginTransaction().add(R.id.rt_dates_container, datesFragment).commit();
        timeOfDaySpinner = findViewById(R.id.time_of_day_spinner);
    }

    private void setDoneButtonAccessibility() {
        doneButton.setEnabled(!isEmpty(titleEdit)
                && !datesFragment.isEmpty()
                && !isEmpty(periodEditText)
                && !periodEditText.getText().toString().equals("0"));

    }

    private void adjustControls() {
        adjustSpinner(this, periodTypeSpinner, Arrays.asList(PeriodType.values()),
                selectedItem -> setDoneButtonAccessibility());
        adjustSpinner(this, timeOfDaySpinner, Arrays.asList(TimeOfDay.values()),
                timeOfDay -> setDoneButtonAccessibility());
        doneButton.setOnClickListener(view -> saveTask());
        setEditTextListener(periodEditText, text -> setDoneButtonAccessibility());
        setEditTextListener(titleEdit, text -> setDoneButtonAccessibility());
    }

    private void saveTask() {
        RegularTaskService.INSTANCE.save(
                existing,
                titleEdit.getText().toString(),
                descEdit.getText().toString(),
                (TimeOfDay) timeOfDaySpinner.getSelectedItem(),
                (PeriodType) periodTypeSpinner.getSelectedItem(),
                datesFragment.getDates(),
                Integer.parseInt(periodEditText.getText().toString()));
        finish();
    }

    private void setInitFieldValues() {
        boolean intentIsEmpty = getIntent().getLongExtra(EXTRA_TASK_ID, -1) == -1;

        if (intentIsEmpty)
            selectSpinnerItemByValue(timeOfDaySpinner, TimeOfDay.AFTERNOON);
        else
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
        selectSpinnerItemByValue(timeOfDaySpinner, existing.getTimeOfDay());
    }

    @Override
    public void onUpdate() {
        setDoneButtonAccessibility();
    }
}