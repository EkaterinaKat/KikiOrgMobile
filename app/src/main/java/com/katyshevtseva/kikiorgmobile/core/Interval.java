package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.Time;
import com.katyshevtseva.kikiorgmobile.utils.TimeUtils;

import lombok.Getter;

@Getter
public class Interval {
    private final Task task;
    private final Time start;
    private final Time end;

    public Interval(Task task, Time start, Time end) {
        if (TimeUtils.after(start, end)) {
            throw new RuntimeException();
        }

        this.task = task;
        this.start = start;
        this.end = end;
    }

    public String getTimeString() {
        return start.getS() + " - " + end.getS();
    }

    public int getLength() {
        return TimeUtils.minus(end, start);
    }

    public String getTitle() {
        if (task != null)
            return task.getTitle();
        return null;
    }

    public boolean isEmpty() {
        return task == null;
    }
}
