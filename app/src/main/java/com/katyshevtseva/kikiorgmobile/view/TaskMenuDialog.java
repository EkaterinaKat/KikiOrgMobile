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
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.utils.NoArgKnob;

import java.util.Date;

public class TaskMenuDialog extends DialogFragment {
    private final Task task;
    private final NoArgKnob activityUpdateKnob;
    private final Date date;
    private final AppCompatActivity context;

    public TaskMenuDialog(Task task, NoArgKnob activityUpdateKnob, Date date, AppCompatActivity context) {
        this.task = task;
        this.activityUpdateKnob = activityUpdateKnob;
        this.date = date;
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.dialog_task_menu, null);

        ((TextView) itemView.findViewById(R.id.title_text_view)).setText(task.getTitle());
        if (!GeneralUtil.isEmpty(task.getDesc()))
            ((TextView) itemView.findViewById(R.id.desc_text_view)).setText(task.getDesc());
        else
            itemView.findViewById(R.id.desc_text_view).setVisibility(View.GONE);

        itemView.findViewById(R.id.done_button).setOnClickListener(view -> {
            switch (task.getType()) {
                case IRREGULAR:
                    Service.INSTANCE.done((IrregularTask) task);
                    break;
                case REGULAR:
                    Service.INSTANCE.done((RegularTask) task, date);
            }
            dismiss();
        });
        itemView.findViewById(R.id.one_day_reschedule_button).setOnClickListener(view -> {
            switch (task.getType()) {
                case IRREGULAR:
                    Service.INSTANCE.rescheduleForOneDay((IrregularTask) task);
                    dismiss();
                    break;
                case REGULAR:
                    DialogFragment dlg1 = new QuestionDialog("Shift all cycle?",
                            answer -> {
                                Service.INSTANCE.rescheduleForOneDay((RegularTask) task, date, answer);
                                dismiss();
                            });
                    dlg1.show(context.getSupportFragmentManager(), "dialog2");
            }
        });
        itemView.findViewById(R.id.more_days_reschedule_button).setOnClickListener(
                view -> rescheduleWithDatePicker(task, date));

        itemView.findViewById(R.id.edit_task_button).setOnClickListener(view -> {
            switch (task.getType()) {
                case IRREGULAR:
                    irregularEditListener();
                    break;
                case REGULAR:
                    regularEditListener();
            }
        });

        itemView.findViewById(R.id.add_setting_button).setOnClickListener(view -> {
            new OneDaySettingCreationDialog(task, activityUpdateKnob, date)
                    .show(context.getSupportFragmentManager(), "dialog2");
            dismiss();
        });

        return itemView;
    }

    private void irregularEditListener() {
        context.startActivity(TaskCreationActivity.newIntent(context, task));
        dismiss();
    }

    private void regularEditListener() {

        DialogFragment dlg1 = new QuestionDialog("Edit all tasks in cycle?",
                answer -> {
                    if (answer) {
                        context.startActivity(TaskCreationActivity.newIntent(context, task));
                    } else {
                        Service.INSTANCE.done((RegularTask) task, date);
                        context.startActivity(TaskCreationActivity.getRegToIrregIntent(context, (RegularTask) task));
                    }
                    dismiss();
                });
        dlg1.show(context.getSupportFragmentManager(), "dialog2");
    }

    void rescheduleWithDatePicker(Task task, Date currentDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
            Date selectedDate = DateUtils.parse(year, month + 1, day);
            switch (task.getType()) {
                case IRREGULAR:
                    Service.INSTANCE.rescheduleToCertainDate((IrregularTask) task, selectedDate);
                    dismiss();
                    break;
                case REGULAR:
                    DialogFragment dlg1 = new QuestionDialog("Shift all cycle?", answer -> {
                        Service.INSTANCE.rescheduleToCertainDate((RegularTask) task, currentDate, selectedDate, answer);
                        dismiss();
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
        GeneralUtil.setImmersiveStickyMode(getActivity().getWindow());
    }
}
