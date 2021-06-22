package com.katyshevtseva.kikiorgmobile.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.DateUtils;
import com.katyshevtseva.kikiorgmobile.view.utils.FragmentUpdateListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatesSelectFragment extends Fragment {
    private LinearLayout datesPane;
    private List<Date> dates = new ArrayList<>();
    private FragmentUpdateListener updateListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates_select, container, false);
        datesPane = view.findViewById(R.id.dates_layout);
        updatePane();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        updateListener = (FragmentUpdateListener) context;
    }

    void setDates(List<Date> dates) {
        this.dates = dates;
        updatePane();
    }

    List<Date> getDates() {
        return dates;
    }

    boolean isEmpty() {
        return dates.isEmpty();
    }

    private void updatePane() {
        if (updateListener != null)
            updateListener.onUpdate();

        if (datesPane == null)
            return;

        datesPane.removeAllViews();
        for (Date date : dates) {
            datesPane.addView(getDateTextView(date));
        }
        datesPane.addView(getAddDateButton());
    }

    private TextView getDateTextView(final Date date) {
        TextView textView = new TextView(getActivity());
        textView.setText(DateUtils.getDateString(date));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dates.remove(date);
                updatePane();
            }
        });
        return textView;
    }

    private TextView getAddDateButton() {
        TextView textView = new TextView(getActivity());
        textView.setText("Add Date");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
        return textView;
    }

    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dates.add(DateUtils.parse(year, month + 1, day));
                updatePane();
            }
        });
        datePickerDialog.show();
    }
}
