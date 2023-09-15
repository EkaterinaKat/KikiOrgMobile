package com.katyshevtseva.kikiorgmobile.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.OptionalTaskService;
import com.katyshevtseva.kikiorgmobile.core.model.OptionalTask;
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.utils.knobs.NoArgKnob;

public class OtDialog extends DialogFragment {
    private final OptionalTask optionalTask;
    private final NoArgKnob activityUpdateKnob;

    public OtDialog(OptionalTask optionalTask, NoArgKnob activityUpdateKnob) {
        this.optionalTask = optionalTask;
        this.activityUpdateKnob = activityUpdateKnob;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_ot_edit, null);
        EditText editText = v.findViewById(R.id.edit_text);
        Button saveButton = v.findViewById(R.id.save_button);
        Button deleteButton = v.findViewById(R.id.delete_button);

        if (optionalTask != null) {
            editText.setText(optionalTask.getTitle());
        }
        deleteButton.setVisibility(optionalTask == null ? View.GONE : View.VISIBLE);

        saveButton.setOnClickListener(view -> {
            OptionalTaskService.INSTANCE.save(optionalTask, editText.getText().toString());
            dismiss();
        });
        deleteButton.setOnClickListener(view -> {
            DialogFragment dlg1 = new QuestionDialog("Delete?",
                    answer -> {
                        if (answer) {
                            OptionalTaskService.INSTANCE.delete(optionalTask);
                            dismiss();
                        }
                    });
            dlg1.show(getActivity().getSupportFragmentManager(), "dialog2");
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
