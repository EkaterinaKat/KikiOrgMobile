package com.katyshevtseva.kikiorgmobile.view;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateByString;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.isDate;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.selectSpinnerItemByValue;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.setEditTextListener;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.OneInKnob;
import com.katyshevtseva.kikiorgmobile.view.utils.FragmentUpdateListener;

import java.util.Arrays;
import java.util.Date;

public class TaskCreationActivity extends AppCompatActivity implements FragmentUpdateListener {
    private static final String EXTRA_TASK_ID = "task_id";
    private static final String EXTRA_TASK_TYPE = "task_type";
    private static final String EXTRA_REG_TO_IRREG = "reg_to_irreg";
    private Task existing;
    private final DatesSelectFragment datesFragment = new DatesSelectFragment();

    private EditText titleEdit;
    private EditText descEdit;
    private Spinner taskTypeSpinner;
    private LinearLayout irregularLayout;
    private TextView itDateTextView;
    private LinearLayout regularLayout;
    private Spinner periodTypeSpinner;
    private EditText periodEditText;
    private Button doneButton;

    public static Intent newIntent(Context context, @Nullable Task task) {
        Intent intent = new Intent(context, TaskCreationActivity.class);
        if (task != null) {
            intent.putExtra(EXTRA_TASK_TYPE, task.getType().getCode());
            intent.putExtra(EXTRA_TASK_ID, task.getId());
            intent.putExtra(EXTRA_REG_TO_IRREG, false);
        }
        return intent;
    }

    public static Intent getRegToIrregIntent(Context context, @NonNull RegularTask regularTask) {
        Intent intent = new Intent(context, TaskCreationActivity.class);
        intent.putExtra(EXTRA_TASK_ID, regularTask.getId());
        intent.putExtra(EXTRA_REG_TO_IRREG, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        initializeControls();
        setDoneButtonAccessibility();
        setControlListeners();
        setInitFieldValues();
    }

    private void setInitFieldValues() {
        boolean intentIsEmpty = getIntent().getLongExtra(EXTRA_TASK_ID, -1) == -1;

        if (intentIsEmpty)
            setFieldDefaultValues();
        else
            handleIntent();
    }

    private void setFieldDefaultValues() {
        selectSpinnerItemByValue(taskTypeSpinner, TaskType.IRREGULAR);
        itDateTextView.setText(getDateString(new Date()));
    }

    private void handleIntent() {
        boolean regToIrreg = getIntent().getBooleanExtra(EXTRA_REG_TO_IRREG, false);
        long id = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
        if (regToIrreg) {
            existing = Service.INSTANCE.makeIrregularTaskFromRegular(id);
        } else {
            if (id == -1)
                return;
            TaskType taskType = TaskType.findByCode(getIntent().getIntExtra(EXTRA_TASK_TYPE, 1));
            existing = Service.INSTANCE.findTask(taskType, id);
        }

        titleEdit.setText(existing.getTitle());
        descEdit.setText(existing.getDesc());
        selectSpinnerItemByValue(taskTypeSpinner, existing.getType());
        taskTypeSpinner.setEnabled(false);
        switch (existing.getType()) {
            case REGULAR:
                RegularTask regularTask = (RegularTask) existing;
                selectSpinnerItemByValue(periodTypeSpinner, regularTask.getPeriodType());
                periodEditText.setText("" + regularTask.getPeriod());
                datesFragment.setDates(regularTask.getDates());
                break;
            case IRREGULAR:
                IrregularTask irregularTask = (IrregularTask) existing;
                itDateTextView.setText(getDateString(irregularTask.getDate()));
        }
    }

    private void initializeControls() {
        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        taskTypeSpinner = findViewById(R.id.task_type_spinner);
        doneButton = findViewById(R.id.save_task_button);
        irregularLayout = findViewById(R.id.irregular_layout);
        itDateTextView = findViewById(R.id.it_date_text_view);
        regularLayout = findViewById(R.id.regular_task_layout);
        periodTypeSpinner = findViewById(R.id.period_type_spinner);
        periodEditText = findViewById(R.id.period_edit_text);
        getSupportFragmentManager().beginTransaction().add(R.id.rt_dates_container, datesFragment).commit();
    }

    private void setControlListeners() {
        adjustSpinner(this, taskTypeSpinner, Arrays.asList(TaskType.values()), taskTypeSpinnerListener);
        adjustSpinner(this, periodTypeSpinner, Arrays.asList(PeriodType.values()),
                selectedItem -> setDoneButtonAccessibility());
        doneButton.setOnClickListener(view -> saveTask());
        itDateTextView.setOnClickListener(view -> openDatePicker(itDateTextView));
        setEditTextListener(periodEditText, text -> setDoneButtonAccessibility());
        setEditTextListener(titleEdit, text -> setDoneButtonAccessibility());
        setEditTextListener(descEdit, text -> setDoneButtonAccessibility());
    }

    public void openDatePicker(final TextView textViewToChange) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
            textViewToChange.setText(getDateString(year, month + 1, day));
            setDoneButtonAccessibility();
        });
        datePickerDialog.show();
    }

    private void saveTask() {
        switch ((TaskType) taskTypeSpinner.getSelectedItem()) {
            case IRREGULAR:
                Service.INSTANCE.saveIrregularTask(
                        (IrregularTask) existing,
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        getDateByString(itDateTextView.getText().toString())
                );
                break;
            case REGULAR:
                Service.INSTANCE.saveRegularTask(
                        (RegularTask) existing,
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        (PeriodType) periodTypeSpinner.getSelectedItem(),
                        datesFragment.getDates(),
                        Integer.parseInt(periodEditText.getText().toString()));
        }
        finish();
    }

    private final OneInKnob<TaskType> taskTypeSpinnerListener = taskType -> {
        irregularLayout.setVisibility(View.GONE);
        regularLayout.setVisibility(View.GONE);
        switch ((TaskType) taskTypeSpinner.getSelectedItem()) {
            case IRREGULAR:
                irregularLayout.setVisibility(View.VISIBLE);
                break;
            case REGULAR:
                regularLayout.setVisibility(View.VISIBLE);
        }
        setDoneButtonAccessibility();
    };

    private void setDoneButtonAccessibility() {
        doneButton.setEnabled(true);

        if (isEmpty(titleEdit)) {
            doneButton.setEnabled(false);
            return;
        }

        switch ((TaskType) taskTypeSpinner.getSelectedItem()) {
            case IRREGULAR:
                doneButton.setEnabled(isDate(itDateTextView.getText().toString()));
                break;
            case REGULAR:
                doneButton.setEnabled(
                        !datesFragment.isEmpty()
                                && !isEmpty(periodEditText)
                                && !periodEditText.getText().toString().equals("0"));
        }
    }

    @Override
    public void onUpdate() {
        setDoneButtonAccessibility();
    }
}
