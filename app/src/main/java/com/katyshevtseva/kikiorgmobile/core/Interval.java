package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.Time;
import com.katyshevtseva.kikiorgmobile.utils.TimeUtil;

import lombok.Getter;

public class Interval {
    private final String title;
    private final Task task;
    @Getter
    private final Time start;
    @Getter
    private final Time end;
    @Getter
    private final String color;

    public static Interval taskInterval(Task task, Time start, Time end) {
        return new Interval(null, task, start, end, "#FF66D9");
    }

    public static Interval emptyInterval(Time start, Time end) {
        return new Interval(null, null, start, end, "#FFFFFF");
    }

    public static Interval sleepInterval(Time start, Time end) {
        return new Interval("сон", null, start, end, "#75BCFF");
    }

    private Interval(String title, Task task, Time start, Time end, String color) {
        this.title = title;
        this.task = task;
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public String getTitle() {
        if (task != null)
            return task.getTitle();
        return title;
    }

    public String getTimeString() {
        return start.getS() + " - " + end.getS();
    }

    public int getLength() {
        return TimeUtil.minus(end, start);
    }
}
