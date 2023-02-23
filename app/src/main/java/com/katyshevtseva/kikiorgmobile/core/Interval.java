package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.Time;
import com.katyshevtseva.kikiorgmobile.utils.TimeUtils;

import lombok.Getter;

@Getter
public class Interval {
    private final String title;
    private final Task task;
    private final Time start;
    private final Time end;
    private final String color;

    public static Interval taskInterval(Task task, Time start, Time end) {
        return new Interval(task.getTitle(), task, start, end, null);
    }

    public static Interval emptyInterval(Time start, Time end) {
        return new Interval(null, null, start, end, "#FFFFFF");
    }

    public static Interval sleepInterval(Time start, Time end) {
        return new Interval("сон", null, start, end, null);
    }

    private Interval(String title, Task task, Time start, Time end, String color) {
        if (TimeUtils.after(start, end)) {
            throw new RuntimeException();
        }

        this.title = title;
        this.task = task;
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public String getTimeString() {
        return start.getS() + " - " + end.getS();
    }

    public int getLength() {
        return TimeUtils.minus(end, start);
    }
}
