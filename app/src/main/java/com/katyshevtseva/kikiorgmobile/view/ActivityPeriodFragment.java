package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.PrefService;
import com.katyshevtseva.kikiorgmobile.view.utils.MyTimePicker;
import com.katyshevtseva.kikiorgmobile.view.utils.ViewUtils;

public class ActivityPeriodFragment extends Fragment {
    private MyTimePicker startTp;
    private MyTimePicker endTp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_period, container, false);
        initTimePickers(view);
        return view;
    }

    private void initTimePickers(View v) {
        startTp = new MyTimePicker(v.findViewById(R.id.activity_period_start_view), getContext(),
                this::startTimeUpdateListener, PrefService.INSTANCE.getActivityStart(), null);
        endTp = new MyTimePicker(v.findViewById(R.id.activity_period_end_view), getContext(),
                this::endTimeUpdateListener, PrefService.INSTANCE.getActivityEnd(), null);
    }

    private void startTimeUpdateListener(int hour, int min) {
        try {
            PrefService.INSTANCE.updateStartActivityPeriodValue(hour, min);
        } catch (Exception e) {
            ViewUtils.showAlertDialog(getContext(), e.getMessage());
        }
        updateTimePickersValues();
    }

    private void endTimeUpdateListener(int hour, int min) {
        try {
            PrefService.INSTANCE.updateEndActivityPeriodValue(hour, min);
        } catch (Exception e) {
            ViewUtils.showAlertDialog(getContext(), e.getMessage());
        }
        updateTimePickersValues();
    }

    private void updateTimePickersValues() {
        startTp.setTime(PrefService.INSTANCE.getActivityStart());
        endTp.setTime(PrefService.INSTANCE.getActivityEnd());
    }
}