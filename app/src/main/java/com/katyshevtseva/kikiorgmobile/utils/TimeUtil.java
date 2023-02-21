package com.katyshevtseva.kikiorgmobile.utils;

public class TimeUtil {
    private static final int MIN_IN_DAY = 1440;

    public static Time plus(Time time1, Time time2) {
        int totalMinSum = time1.getTotalMinutes() + time2.getTotalMinutes();
        while (totalMinSum >= MIN_IN_DAY) {
            totalMinSum -= MIN_IN_DAY;
        }
        return new Time(totalMinSum);
    }

    public static int minus(Time time1, Time time2) {
        int totalMinSum = time1.getTotalMinutes() - time2.getTotalMinutes();
        while (totalMinSum >= MIN_IN_DAY) {
            totalMinSum -= MIN_IN_DAY;
        }
        while (totalMinSum < 0) {
            totalMinSum += MIN_IN_DAY;
        }
        return totalMinSum;
    }
}
