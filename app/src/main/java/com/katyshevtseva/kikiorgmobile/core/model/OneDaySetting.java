package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.db.Entity;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OneDaySetting implements Entity, Setting {
    private long id;

    private long taskId;

    private Time duration;

    private Time beginTime;

    private Date date;

    public OneDaySetting(long taskId, Time duration, Time beginTime, Date date) {
        this.taskId = taskId;
        this.duration = duration;
        this.beginTime = beginTime;
        this.date = date;
    }

    @Override
    public boolean isAbsoluteWobs() {
        return true;
    }

    @Override
    public boolean isTask() {
        return false;
    }
}
