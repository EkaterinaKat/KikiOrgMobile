package com.katyshevtseva.kikiorgmobile.core.enums;

public enum TaskUrgency {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private final int code;

    TaskUrgency(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TaskUrgency findByCode(int code) {
        for (TaskUrgency urgency : TaskUrgency.values())
            if (urgency.code == code)
                return urgency;
        throw new RuntimeException();
    }
}
