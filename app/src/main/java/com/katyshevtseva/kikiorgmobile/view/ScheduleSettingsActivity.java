package com.katyshevtseva.kikiorgmobile.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.PrefService;
import com.katyshevtseva.kikiorgmobile.utils.Time;

public class ScheduleSettingsActivity extends AppCompatActivity {
    private TextView apStartView;
    private TextView apEndView;
    Time apStart;
    Time apEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_settings);

        apStartView = findViewById(R.id.activity_period_start_view);
        apEndView = findViewById(R.id.activity_period_end_view);

        setTimeViewsValues();

        apStartView.setOnClickListener(view -> new TimePickerDialog(this,
                (TimePickerDialog.OnTimeSetListener) (timePicker, hour, min) -> {
                    if (!PrefService.INSTANCE.updateStartActivityPeriodValue(hour, min)) {
                        showApWarning();
                    }
                    setTimeViewsValues();
                }, apStart.getHour(), apStart.getMinute(), true).show());

        apEndView.setOnClickListener(view -> new TimePickerDialog(this,
                (TimePickerDialog.OnTimeSetListener) (timePicker, hour, min) -> {
                    if (!PrefService.INSTANCE.updateEndActivityPeriodValue(hour, min)) {
                        showApWarning();
                    }
                    setTimeViewsValues();
                }, apEnd.getHour(), apEnd.getMinute(), true).show());
    }

    private void showApWarning() {
        new AlertDialog.Builder(this).setTitle("Некорректный интервал")
                .setPositiveButton("ОК", (dialog, id) -> dialog.cancel()).create().show();
    }

    private void setTimeViewsValues() {
        apStart = PrefService.INSTANCE.getStartActivityPeriod();
        apEnd = PrefService.INSTANCE.getEndActivityPeriod();

        apStartView.setText(apStart.getS());
        apEndView.setText(apEnd.getS());
    }
}