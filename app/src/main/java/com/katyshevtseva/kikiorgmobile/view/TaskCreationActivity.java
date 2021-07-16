package com.katyshevtseva.kikiorgmobile.view;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.view.utils.FragmentUpdateListener;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.SpinnerListener;

import java.util.Arrays;

import static com.katyshevtseva.kikiorgmobile.core.DateUtils.getDateByString;
import static com.katyshevtseva.kikiorgmobile.core.DateUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.core.DateUtils.isDate;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.selectSpinnerItemByValue;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.setEditTextListener;

public class TaskCreationActivity extends AppCompatActivity implements FragmentUpdateListener {
    private static final String EXTRA_TASK_ID = "task_id";
    private static final String EXTRA_TASK_TYPE = "task_type";
    private Service service;
    private Task existing;
    private DatesSelectFragment datesFragment = new DatesSelectFragment();

    private EditText titleEdit;
    private EditText descEdit;
    private Spinner timeOfDaySpinner;
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
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);
        service = new Service(this);

        initializeControls();
        setDoneButtonAccessibility();
        setControlListeners();
        handleIntent();
    }

    private void handleIntent() {
        long id = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
        TaskType taskType = TaskType.findByCode(getIntent().getIntExtra(EXTRA_TASK_TYPE, 1));

        if (id == -1)
            return;

        existing = service.findTask(taskType, id);
        titleEdit.setText(existing.getTitle());
        descEdit.setText(existing.getDesc());
        selectSpinnerItemByValue(timeOfDaySpinner, existing.getTimeOfDay());
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
        timeOfDaySpinner = findViewById(R.id.time_of_day_spinner);
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
        adjustSpinner(this, timeOfDaySpinner, Arrays.asList(TimeOfDay.values()),
                timeOfDay -> setDoneButtonAccessibility());
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
                service.saveIrregularTask(
                        (IrregularTask) existing,
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        (TimeOfDay) timeOfDaySpinner.getSelectedItem(),
                        getDateByString(itDateTextView.getText().toString())
                );
                break;
            case REGULAR:
                service.saveRegularTask(
                        (RegularTask) existing,
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        (TimeOfDay) timeOfDaySpinner.getSelectedItem(),
                        (PeriodType) periodTypeSpinner.getSelectedItem(),
                        datesFragment.getDates(),
                        Integer.parseInt(periodEditText.getText().toString()));
        }
        finish();
    }

    private SpinnerListener<TaskType> taskTypeSpinnerListener = new SpinnerListener<TaskType>() {
        @Override
        public void execute(TaskType selectedItem) {
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
        }
    };

    private void setDoneButtonAccessibility() {
        doneButton.setEnabled(true);

        if (isEmpty(titleEdit) || isEmpty(descEdit)) {
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
