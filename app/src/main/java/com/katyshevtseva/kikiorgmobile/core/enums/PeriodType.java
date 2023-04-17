package com.katyshevtseva.kikiorgmobile.core.enums;

public enum PeriodType {
    DAY(1), WEEK(2), MONTH(3), YEAR(4);

    private final int code;

    PeriodType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PeriodType findByCode(int code) {
        for (PeriodType periodType : PeriodType.values())
            if (periodType.code == code)
                return periodType;
        throw new RuntimeException();
    }
}
