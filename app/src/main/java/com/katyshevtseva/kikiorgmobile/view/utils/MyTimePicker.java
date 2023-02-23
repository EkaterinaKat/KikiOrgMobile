package com.katyshevtseva.kikiorgmobile.view.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.katyshevtseva.kikiorgmobile.utils.Time;
import com.katyshevtseva.kikiorgmobile.utils.TwoInKnob;

public class MyTimePicker {
    private static final String empty = "---";
    private final TextView textView;
    private Time time;
    private final TwoInKnob<Integer, Integer> onTimeSetListener;

    public MyTimePicker(@NonNull TextView textView, @NonNull Context context,
                        @Nullable TwoInKnob<Integer, Integer> onTimeSetListener,
                        @Nullable Time time, @Nullable View container) {
        this.textView = textView;
        this.onTimeSetListener = onTimeSetListener;

        setTime(time);

        if (container != null) {
            container.setOnClickListener(view -> openTimePicker(context));
        } else {
            textView.setOnClickListener(view -> openTimePicker(context));
        }
    }

    private void openTimePicker(Context context) {
        new TimePickerDialog(context,
                (timePicker, hour, min) -> {
                    setTime(new Time(hour, min));
                    onTimeSetListener.execute(hour, min);
                }, time == null ? 0 : time.getHour(), time == null ? 0 : time.getMinute(), true).show();
    }

    public void setTime(Time time) {
        this.time = time;
        textView.setText(time == null ? empty : time.getS());
    }

    public Time getTime() {
        return time;
    }

    public void clear() {
        setTime(null);
    }

    public boolean isFilled() {
        return time != null;
    }
}
