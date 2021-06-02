package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Core;
import com.katyshevtseva.kikiorgmobile.core.TaskService;
import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils;
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.SpinnerListener;

import java.util.Arrays;

import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.getDateByString;
import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.isDate;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.setEditTextListener;

public class TaskCreationActivity extends AppCompatActivity {
    private TaskService taskService;

    private EditText titleEdit;
    private EditText descEdit;
    private Spinner typeSpinner;

    private LinearLayout irregularLayout;
    private TextView irregularDateTextView;

    private LinearLayout regularLayout;
    private Spinner periodTypeSpinner;

    private LinearLayout dayPeriodLayout;
    private EditText dayPeriodEditText;
    private TextView dayPeriodDateTextView;

    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        taskService = Core.getTaskService(this);
        initializeControls();
        setDoneButtonAccessibility();
        adjustSpinner(this, typeSpinner, Arrays.asList(TaskType.values()), typeSpinnerListener);
        adjustSpinner(this, periodTypeSpinner, Arrays.asList(PeriodType.values()), periodTypeSpinnerListener);
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
        dayPeriodDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(dayPeriodDateTextView);
            }
        });
        setEditTextListener(dayPeriodEditText, new KomUtils.EditTextListener() {
            @Override
            public void run(String text) {
                setDoneButtonAccessibility();
            }
        });
    }

    private void initializeControls() {
        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        typeSpinner = findViewById(R.id.task_type_spinner);
        doneButton = findViewById(R.id.save_task_button);
        irregularLayout = findViewById(R.id.irregular_layout);
        irregularDateTextView = findViewById(R.id.irregular_date_text_view);
        regularLayout = findViewById(R.id.regular_task_layout);
        periodTypeSpinner = findViewById(R.id.period_type_spinner);
        dayPeriodDateTextView = findViewById(R.id.day_period_date_text_view);
        dayPeriodEditText = findViewById(R.id.day_period_edit_text);
        dayPeriodLayout = findViewById(R.id.day_period_layout);
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
        switch ((TaskType) typeSpinner.getSelectedItem()) {
            case REGULAR:
                switch ((PeriodType) periodTypeSpinner.getSelectedItem()) {
                    case DAY:
                        taskService.saveNewDayPeriodTask(
                                titleEdit.getText().toString(),
                                descEdit.getText().toString(),
                                getDateByString(dayPeriodDateTextView.getText().toString()),
                                Integer.parseInt(dayPeriodEditText.getText().toString()));
                        break;
                    case WEEK:
                        break;
                    case MONTH:
                        break;
                    case YEAR:
                }
                break;
            case IRREGULAR:
                taskService.saveNewIrregularTask(
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        getDateByString(irregularDateTextView.getText().toString())
                );
        }
        finish();
    }

    private SpinnerListener<TaskType> typeSpinnerListener = new SpinnerListener<TaskType>() {
        @Override
        public void execute(TaskType selectedItem) {
            irregularLayout.setVisibility(View.GONE);
            regularLayout.setVisibility(View.GONE);
            switch ((TaskType) typeSpinner.getSelectedItem()) {
                case REGULAR:
                    regularLayout.setVisibility(View.VISIBLE);
                    break;
                case IRREGULAR:
                    irregularLayout.setVisibility(View.VISIBLE);
            }
            setDoneButtonAccessibility();
        }
    };

    private SpinnerListener<PeriodType> periodTypeSpinnerListener = new SpinnerListener<PeriodType>() {
        @Override
        public void execute(PeriodType selectedItem) {
            dayPeriodLayout.setVisibility(View.GONE);
            switch ((PeriodType) periodTypeSpinner.getSelectedItem()) {
                case DAY:
                    dayPeriodLayout.setVisibility(View.VISIBLE);
                    break;
                case WEEK:
                    break;
                case MONTH:
                    break;
                case YEAR:
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

        switch ((TaskType) typeSpinner.getSelectedItem()) {
            case REGULAR:
                switch ((PeriodType) periodTypeSpinner.getSelectedItem()) {
                    case DAY:
                        if (isEmpty(dayPeriodEditText) || !isDate(dayPeriodDateTextView.getText().toString()))
                            doneButton.setEnabled(false);
                        break;
                    case WEEK:
                        break;
                    case MONTH:
                        break;
                    case YEAR:
                }
                break;
            case IRREGULAR:

        }
    }
}
