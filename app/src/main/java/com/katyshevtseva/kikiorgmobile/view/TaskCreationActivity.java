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
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils;
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.SpinnerListener;

import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.getDateByString;
import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.core.CoreUtils.isDate;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.setEditTextListener;

public class TaskCreationActivity extends AppCompatActivity {
    private TaskService taskService;

    private static final String REGULAR_STRING = "regular";
    private static final String IRREGULAR_STRING = "irregular";
    private static final String[] taskTypes = {"", REGULAR_STRING, IRREGULAR_STRING};

    private static final String BY_DAY_STRING = "by day";
    private static final String BY_WEEK_STRING = "by week";
    private static final String BY_MONTH_STRING = "by month";
    private static final String BY_YEAR_STRING = "by year";
    private static final String[] periodTypes = {"", BY_DAY_STRING, BY_WEEK_STRING, BY_MONTH_STRING, BY_YEAR_STRING};


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
        adjustSpinner(this, typeSpinner, taskTypes, typeSpinnerListener);
        adjustSpinner(this, periodTypeSpinner, periodTypes, periodTypeSpinnerListener);
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
        switch ((String) typeSpinner.getSelectedItem()) {
            case REGULAR_STRING:
                switch ((String) periodTypeSpinner.getSelectedItem()) {
                    case BY_DAY_STRING:
                        taskService.saveNewDayPeriodTask(
                                titleEdit.getText().toString(),
                                descEdit.getText().toString(),
                                getDateByString(dayPeriodDateTextView.getText().toString()),
                                Integer.parseInt(dayPeriodEditText.getText().toString()));
                        break;
                    case BY_WEEK_STRING:
                        break;
                    case BY_MONTH_STRING:
                        break;
                    case BY_YEAR_STRING:
                }
                break;
            case IRREGULAR_STRING:
                taskService.saveNewIrregularTask(
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        getDateByString(irregularDateTextView.getText().toString())
                );
        }
        finish();
    }

    private SpinnerListener typeSpinnerListener = new SpinnerListener() {
        @Override
        public void execute(String selectedItem) {
            irregularLayout.setVisibility(View.GONE);
            regularLayout.setVisibility(View.GONE);
            switch ((String) typeSpinner.getSelectedItem()) {
                case REGULAR_STRING:
                    regularLayout.setVisibility(View.VISIBLE);
                    break;
                case IRREGULAR_STRING:
                    irregularLayout.setVisibility(View.VISIBLE);
            }
            setDoneButtonAccessibility();
        }
    };

    private SpinnerListener periodTypeSpinnerListener = new SpinnerListener() {
        @Override
        public void execute(String selectedItem) {
            dayPeriodLayout.setVisibility(View.GONE);
            switch ((String) periodTypeSpinner.getSelectedItem()) {
                case BY_DAY_STRING:
                    dayPeriodLayout.setVisibility(View.VISIBLE);
                    break;
                case BY_WEEK_STRING:
                    break;
                case BY_MONTH_STRING:
                    break;
                case BY_YEAR_STRING:
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

        switch ((String) typeSpinner.getSelectedItem()) {
            case REGULAR_STRING:
                switch ((String) periodTypeSpinner.getSelectedItem()) {
                    case BY_DAY_STRING:
                        if (isEmpty(dayPeriodEditText) || !isDate(dayPeriodDateTextView.getText().toString()))
                            doneButton.setEnabled(false);
                        break;
                    case BY_WEEK_STRING:
                        break;
                    case BY_MONTH_STRING:
                        break;
                    case BY_YEAR_STRING:
                }
                break;
            case IRREGULAR_STRING:

        }
    }
}
