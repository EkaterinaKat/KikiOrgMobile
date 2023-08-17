package com.katyshevtseva.kikiorgmobile.core.enums;

public enum TimeOfDay {
    MORNING(1), AFTERNOON(2), EVENING(3);

    private int code;

    TimeOfDay(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static TimeOfDay findByCode(int code) {
        for (TimeOfDay timeOfDay : TimeOfDay.values())
            if (timeOfDay.code == code)
                return timeOfDay;
        throw new RuntimeException();
    }
}
