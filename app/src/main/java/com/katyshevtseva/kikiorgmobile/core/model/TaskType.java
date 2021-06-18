package com.katyshevtseva.kikiorgmobile.core.model;

public enum TaskType {
    REGULAR(1), IRREGULAR(2);

    private int code;

    TaskType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TaskType findByCode(int code) {
        for (TaskType taskType : TaskType.values())
            if (taskType.code == code)
                return taskType;
        throw new RuntimeException();
    }
}
