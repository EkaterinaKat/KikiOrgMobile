package com.katyshevtseva.kikiorgmobile.view.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class KomUtils {
    public static void adjustSpinner(Context context, Spinner spinner, final String[] items, final SpinnerListener spinnerListener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerListener.execute(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @FunctionalInterface
    public interface SpinnerListener {
        void execute(String selectedItem);
    }

    public static void associateButtonWithControls(final Button button, final View... views) {
        button.setEnabled(false);
        for (final View view : views) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        setButtonAccessibility(button, Arrays.asList(views));
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            } else if (view instanceof Spinner) {
                final Spinner spinner = (Spinner) view;
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setButtonAccessibility(button, Arrays.asList(views));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            } else {
                throw new RuntimeException("unknown view");
            }
        }
    }

    private static void setButtonAccessibility(Button button, List<View> views) {
        boolean enableButton = true;
        for (View view : views) {
            if (view instanceof EditText && ((EditText) view).getText().toString().trim().equals("")) {
                enableButton = false;
            } else if (view instanceof Spinner && ((Spinner) view).getSelectedItem().toString().isEmpty()) {
                enableButton = false;
            }
            button.setEnabled(enableButton);
        }
    }
}
