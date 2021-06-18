package com.katyshevtseva.kikiorgmobile.core.model;

public interface Task {
    long getId();

    TaskType getType();

    String getTitle();

    String getDesc();
}
