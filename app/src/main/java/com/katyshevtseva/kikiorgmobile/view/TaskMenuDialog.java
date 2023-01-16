package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.DateUtils;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.view.utils.NoArgKnob;

import java.util.Date;

public class TaskMenuDialog extends DialogFragment {
    private final Task task;
    private final NoArgKnob activityUpdateKnob;
    private final Service service;
    private final Date date;
    private final AppCompatActivity context;

    public TaskMenuDialog(Task task, NoArgKnob activityUpdateKnob, Service service, Date date, AppCompatActivity context) {
        this.task = task;
        this.activityUpdateKnob = activityUpdateKnob;
        this.service = service;
        this.date = date;
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.dialog_task_menu, null);

        ((TextView) itemView.findViewById(R.id.title_text_view)).setText(task.getTitle());

        itemView.findViewById(R.id.done_button).setOnClickListener(view -> {
            switch (task.getType()) {
                case IRREGULAR:
                    service.done((IrregularTask) task);
                    break;
                case REGULAR:
                    service.done((RegularTask) task, date);
            }
            activityUpdateKnob.execute();
        });
        itemView.findViewById(R.id.one_day_reschedule_button).setOnClickListener(view -> {
            switch (task.getType()) {
                case IRREGULAR:
                    service.rescheduleForOneDay((IrregularTask) task);
                    activityUpdateKnob.execute();
                    break;
                case REGULAR:
                    DialogFragment dlg1 = new QuestionDialog("Shift all cycle?",
                            answer -> {
                                service.rescheduleForOneDay((RegularTask) task, date, answer);
                                activityUpdateKnob.execute();
                            });
                    dlg1.show(context.getSupportFragmentManager(), "dialog2");
            }
        });
        itemView.findViewById(R.id.more_days_reschedule_button).setOnClickListener(
                view -> rescheduleWithDatePicker(task, date));

        return itemView;
    }

    void rescheduleWithDatePicker(Task task, Date currentDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
            Date selectedDate = DateUtils.parse(year, month + 1, day);
            switch (task.getType()) {
                case IRREGULAR:
                    service.rescheduleToCertainDate((IrregularTask) task, selectedDate);
                    activityUpdateKnob.execute();
                    break;
                case REGULAR:
                    DialogFragment dlg1 = new QuestionDialog("Shift all cycle?", answer -> {
                        service.rescheduleToCertainDate((RegularTask) task, currentDate, selectedDate, answer);
                        activityUpdateKnob.execute();
                    });
                    dlg1.show(context.getSupportFragmentManager(), "dialog2");
            }
        });
        datePickerDialog.show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        activityUpdateKnob.execute();
    }
}
