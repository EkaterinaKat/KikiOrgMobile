package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.utils.Time;

public interface Setting {
    Time getBeginTime();

    boolean isAbsoluteWobs();

    Time getDuration();

    TaskType getTaskType();

    long getTaskId();
}
