package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.IrregularTaskService;
import com.katyshevtseva.kikiorgmobile.core.RegularTaskService;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskUrgency;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.utils.knobs.NoArgKnob;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

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
        if (!GeneralUtil.isEmpty(task.getDesc())) {
            TextView textView = itemView.findViewById(R.id.desc_text_view);
            textView.setText(task.getDesc());
            textView.setOnClickListener(view -> {
                ViewUtils.copyToClipboard(context, task.getDesc());
                Toast.makeText(context, "Desc copied", Toast.LENGTH_LONG).show();
            });
        } else
            itemView.findViewById(R.id.desc_text_view).setVisibility(View.GONE);

        itemView.findViewById(R.id.done_button).setOnClickListener(view -> {
            switch (task.getType()) {
                case IRREGULAR:
                    IrregularTaskService.INSTANCE.done((IrregularTask) task);
                    break;
                case REGULAR:
                    RegularTaskService.INSTANCE.done((RegularTask) task, date);
            }
            dismiss();
        });
        itemView.findViewById(R.id.one_day_reschedule_button).setOnClickListener(view -> {
            switch (task.getType()) {
                case IRREGULAR:
                    IrregularTaskService.INSTANCE.rescheduleForOneDay((IrregularTask) task);
                    dismiss();
                    break;
                case REGULAR:
                    DialogFragment dlg1 = new QuestionDialog("Shift all cycle?",
                            answer -> {
                                RegularTaskService.INSTANCE.rescheduleForOneDay((RegularTask) task, date, answer);
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
                    context.startActivity(IrtEditActivity.newIntent(context, (IrregularTask) task));
                    dismiss();
                    break;
                case REGULAR:
                    regularEditListener();
            }
        });

        tuneUrgencyBox(itemView);

        return itemView;
    }

    private void tuneUrgencyBox(View v) {
        LinearLayout linearLayout = v.findViewById(R.id.urgency_setting_box);
        for (TaskUrgency urgency : TaskUrgency.values()) {
            Button button = new Button(getContext());
            button.setBackground(ViewUtils.getBackground(urgency, getContext()));
            button.setOnClickListener(view -> {
                Service.INSTANCE.setUrgency(task, urgency);
                dismiss();
            });
            linearLayout.addView(button);
        }
    }

    private void regularEditListener() {

        DialogFragment dlg1 = new QuestionDialog("Edit all tasks in cycle?",
                answer -> {
                    if (answer) {
                        context.startActivity(RtEditActivity.newIntent(context, (RegularTask) task));
                    } else {
                        RegularTaskService.INSTANCE.done((RegularTask) task, date);
                        context.startActivity(IrtEditActivity.getRegToIrregIntent(context, (RegularTask) task));
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
                    IrregularTaskService.INSTANCE.rescheduleToCertainDate((IrregularTask) task, selectedDate);
                    dismiss();
                    break;
                case REGULAR:
                    DialogFragment dlg1 = new QuestionDialog("Shift all cycle?", answer -> {
                        RegularTaskService.INSTANCE.rescheduleToCertainDate((RegularTask) task, currentDate, selectedDate, answer);
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
