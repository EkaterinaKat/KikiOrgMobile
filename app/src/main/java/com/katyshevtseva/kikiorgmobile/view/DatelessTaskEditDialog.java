package com.katyshevtseva.kikiorgmobile.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.DatelessTaskService;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.utils.NoArgKnob;

public class DatelessTaskEditDialog extends DialogFragment {
    private final DatelessTask datelessTask;
    private final NoArgKnob activityUpdateKnob;
    private EditText editText;

    public DatelessTaskEditDialog(DatelessTask datelessTask, NoArgKnob activityUpdateKnob) {
        this.datelessTask = datelessTask;
        this.activityUpdateKnob = activityUpdateKnob;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_dateless_task_edit, null);

        editText = v.findViewById(R.id.edit_text);
        if (datelessTask != null) {
            editText.setText(datelessTask.getTitle());
        }
        v.findViewById(R.id.ok_button).setOnClickListener(view -> {
            DatelessTaskService.INSTANCE.saveDatelessTask(datelessTask, editText.getText().toString());
            dismiss();
        });

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        activityUpdateKnob.execute();
        GeneralUtil.setImmersiveStickyMode(getActivity().getWindow());
    }
}

