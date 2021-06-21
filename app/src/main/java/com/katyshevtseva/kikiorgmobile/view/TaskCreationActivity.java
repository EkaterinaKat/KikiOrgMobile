package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Core;
import com.katyshevtseva.kikiorgmobile.core.TaskService;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.EditTextListener;
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.SpinnerListener;

import java.util.Arrays;
import java.util.Date;

import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.getDateByString;
import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.isDate;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.selectSpinnerItemByValue;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.setEditTextListener;

public class TaskCreationActivity extends AppCompatActivity {
    private static final String EXTRA_TASK_ID = "task_id";
    private static final String EXTRA_TASK_TYPE = "task_type";
    private TaskService service;
    private Task existing;

    private EditText titleEdit;
    private EditText descEdit;
    private Spinner taskTypeSpinner;
    private LinearLayout irregularLayout;
    private TextView irregularDateTextView;
    private LinearLayout regularLayout;
    private Spinner periodTypeSpinner;
    private EditText periodEditText;
    private TextView refDateTextView;
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
        service = Core.getTaskService(this);

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
        selectSpinnerItemByValue(taskTypeSpinner, existing.getType());
        taskTypeSpinner.setEnabled(false);
        switch (existing.getType()) {
            case REGULAR:
                RegularTask regularTask = (RegularTask) existing;
                selectSpinnerItemByValue(periodTypeSpinner, regularTask.getPeriodType());
                periodEditText.setText("" + regularTask.getPeriod());
                refDateTextView.setText(getDateString(new Date()));
                break;
            case IRREGULAR:
                IrregularTask irregularTask = (IrregularTask) existing;
                irregularDateTextView.setText(getDateString(irregularTask.getDate()));
        }
    }

    private void initializeControls() {
        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        taskTypeSpinner = findViewById(R.id.task_type_spinner);
        doneButton = findViewById(R.id.save_task_button);
        irregularLayout = findViewById(R.id.irregular_layout);
        irregularDateTextView = findViewById(R.id.irregular_date_text_view);
        regularLayout = findViewById(R.id.regular_task_layout);
        periodTypeSpinner = findViewById(R.id.period_type_spinner);
        refDateTextView = findViewById(R.id.ref_date_text_view);
        periodEditText = findViewById(R.id.period_edit_text);
    }

    private void setControlListeners() {
        adjustSpinner(this, taskTypeSpinner, Arrays.asList(TaskType.values()), taskTypeSpinnerListener);
        adjustSpinner(this, periodTypeSpinner, Arrays.asList(PeriodType.values()), new SpinnerListener<PeriodType>() {
            @Override
            public void execute(PeriodType selectedItem) {
                setDoneButtonAccessibility();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
        irregularDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(irregularDateTextView);
            }
        });
        refDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(refDateTextView);
            }
        });
        setEditTextListener(periodEditText, new EditTextListener() {
            @Override
            public void run(String text) {
                setDoneButtonAccessibility();
            }
        });
        setEditTextListener(titleEdit, new EditTextListener() {
            @Override
            public void run(String text) {
                setDoneButtonAccessibility();
            }
        });
        setEditTextListener(descEdit, new EditTextListener() {
            @Override
            public void run(String text) {
                setDoneButtonAccessibility();
            }
        });
    }

    public void openDatePicker(final TextView textViewToChange) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textViewToChange.setText(getDateString(year, month + 1, day));
                setDoneButtonAccessibility();
            }
        });
        datePickerDialog.show();
    }

    private void saveTask() {
        switch ((TaskType) taskTypeSpinner.getSelectedItem()) {
            case IRREGULAR:
                service.saveNewIrregularTask(
                        (IrregularTask) existing,
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        getDateByString(irregularDateTextView.getText().toString())
                );
                break;
            case REGULAR:
                service.saveNewRegularTask(
                        (RegularTask) existing,
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        (PeriodType) periodTypeSpinner.getSelectedItem(),
                        getDateByString(refDateTextView.getText().toString()),
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
                doneButton.setEnabled(isDate(irregularDateTextView.getText().toString()));
                break;
            case REGULAR:
                doneButton.setEnabled(
                        isDate(refDateTextView.getText().toString())
                                && !isEmpty(periodEditText)
                                && !periodEditText.getText().toString().equals("0"));
        }
    }
}
