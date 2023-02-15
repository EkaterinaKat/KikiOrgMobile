package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.utils.Time;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RtSetting {
    private long id;

    private long rtId;

    private Time duration;

    private Time beginTime;

    private boolean absoluteWobs;

    public RtSetting(long rtId, Time duration, Time beginTime, boolean absoluteWobs) {
        this.rtId = rtId;
        this.duration = duration;
        this.beginTime = beginTime;
        this.absoluteWobs = absoluteWobs;
    }
}
