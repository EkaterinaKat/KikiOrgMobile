package com.katyshevtseva.kikiorgmobile.core.model;

public enum PeriodType {
    DAY(1), WEEK(2), MONTH(3), YEAR(4);

    private int code;

    PeriodType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
