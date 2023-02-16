package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;

public interface Task {
    long getId();

    TaskType getType();

    String getTitle();

    String getDesc();

    TimeOfDay getTimeOfDay();
}
