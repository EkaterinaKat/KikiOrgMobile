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
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.view.utils.NoArgKnob;

public class DatelessTaskEditDialog extends DialogFragment {
    private DatelessTask datelessTask;
    private NoArgKnob activityUpdateKnob;
    private Service service;
    private EditText editText;

    DatelessTaskEditDialog(DatelessTask datelessTask, Service service, NoArgKnob activityUpdateKnob) {
        this.datelessTask = datelessTask;
        this.service = service;
        this.activityUpdateKnob = activityUpdateKnob;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_dateless_task_edit, null);
        service = new Service(getContext());

        editText = v.findViewById(R.id.edit_text);
        v.findViewById(R.id.ok_button).setOnClickListener(view -> {
            service.saveDatelessTask(datelessTask, editText.getText().toString());
            dismiss();
        });

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        activityUpdateKnob.execute();
    }
}

