package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Core;
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils;
import com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.SpinnerListener;

import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.adjustSpinner;
import static com.katyshevtseva.kikiorgmobile.view.utils.KomUtils.associateButtonWithControls;

public class TaskCreationActivity extends AppCompatActivity {
    private static final String REGULAR_STRING = "regular";
    private static final String IRREGULAR_STRING = "irregular";
    private static final String[] taskTypes = {"", REGULAR_STRING, IRREGULAR_STRING};

    private EditText titleEdit;
    private EditText descEdit;
    private Spinner typeSpinner;

    private LinearLayout irregularLayout;
    private DatePicker irregularDatePicker;

    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        initializeControls();
        associateButtonWithControls(doneButton, titleEdit, descEdit);
        adjustSpinner(this, typeSpinner, taskTypes, typeSpinnerListener);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }

    private void initializeControls() {
        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        typeSpinner = findViewById(R.id.task_type_spinner);
        doneButton = findViewById(R.id.save_task_button);
        irregularLayout = findViewById(R.id.irregular_layout);
        irregularDatePicker = findViewById(R.id.irregular_date_picker);
    }

    private void saveTask() {
        switch ((String) typeSpinner.getSelectedItem()) {
            case REGULAR_STRING:
                break;
            case IRREGULAR_STRING:
                Core.getInstance().taskService().saveNewIrregularTask(
                        titleEdit.getText().toString(),
                        descEdit.getText().toString(),
                        KomUtils.getDateByDatePicker(irregularDatePicker)
                );
                finish();
        }
    }

    private SpinnerListener typeSpinnerListener = new SpinnerListener() {
        @Override
        public void execute(String selectedItem) {
            switch ((String) typeSpinner.getSelectedItem()) {
                case REGULAR_STRING:
                    irregularLayout.setVisibility(View.GONE);
                    break;
                case IRREGULAR_STRING:
                    irregularLayout.setVisibility(View.VISIBLE);
            }
        }
    };
}
