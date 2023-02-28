package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.utils.Time;

public interface Setting {
    Time getBeginTime();

    boolean isAbsoluteWobs();

    Time getDuration();

    long getTaskId();

    boolean isTask();
}
