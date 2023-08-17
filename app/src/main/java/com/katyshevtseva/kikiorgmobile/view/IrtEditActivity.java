package com.katyshevtseva.kikiorgmobile.view;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateByString;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.isDate;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.isEmpty;
import static com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils.setEditTextListener;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.IrregularTaskService;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.view.utils.KomActivity;

import java.util.Date;

public class IrtEditActivity extends KomActivity {
    private static final String EXTRA_TASK_ID = "task_id";
    private static final String EXTRA_REG_TO_IRREG = "reg_to_irreg";
    private IrregularTask existing;

    private EditText titleEdit;
    private EditText descEdit;
    private TextView dateTextView;
    private Button doneButton;

    public static Intent newIntent(Context context, @Nullable IrregularTask task) {
        Intent intent = new Intent(context, IrtEditActivity.class);
        if (task != null) {
            intent.putExtra(EXTRA_TASK_ID, task.getId());
            intent.putExtra(EXTRA_REG_TO_IRREG, false);
        }
        return intent;
    }

    public static Intent getRegToIrregIntent(Context context, @NonNull RegularTask regularTask) {
        Intent intent = new Intent(context, IrtEditActivity.class);
        intent.putExtra(EXTRA_TASK_ID, regularTask.getId());
        intent.putExtra(EXTRA_REG_TO_IRREG, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irt_edit);

        initializeControls();
        setControlListeners();
        setInitFieldValues();
        setDoneButtonAccessibility();
    }

    private void setInitFieldValues() {
        boolean intentIsEmpty = getIntent().getLongExtra(EXTRA_TASK_ID, -1) == -1;

        if (intentIsEmpty)
            dateTextView.setText(getDateString(new Date()));
        else
            handleIntent();
    }

    private void handleIntent() {
        boolean regToIrreg = getIntent().getBooleanExtra(EXTRA_REG_TO_IRREG, false);
        long id = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
        if (regToIrreg) {
            existing = Service.INSTANCE.makeIrregularTaskFromRegular(id);
        } else {
            if (id == -1)
                return;
            existing = IrregularTaskService.INSTANCE.findById(id);
        }

        titleEdit.setText(existing.getTitle());
        descEdit.setText(existing.getDesc());
        dateTextView.setText(getDateString(existing.getDate()));
    }

    private void initializeControls() {
        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        doneButton = findViewById(R.id.save_task_button);
        dateTextView = findViewById(R.id.it_date_text_view);
    }

    private void setControlListeners() {
        doneButton.setOnClickListener(view -> saveTask());
        dateTextView.setOnClickListener(view -> openDatePicker(dateTextView));

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
        IrregularTaskService.INSTANCE.save(
                existing,
                titleEdit.getText().toString(),
                descEdit.getText().toString(),
                getDateByString(dateTextView.getText().toString())
        );
        finish();
    }

    private void setDoneButtonAccessibility() {
        doneButton.setEnabled(!isEmpty(titleEdit) && isDate(dateTextView.getText().toString()));
    }
}
