package com.katyshevtseva.kikiorgmobile.utils;

import lombok.Getter;

@Getter
public class Time implements Comparable<Time> {
    private final String s;
    private final int hour;
    private final int minute;

    public Time(String s) {
        this.s = s;

        try {
            String[] strings = s.split(":");
            if (strings.length != 2) {
                throw new RuntimeException();
            }
            hour = Integer.parseInt(strings[0]);
            minute = Integer.parseInt(strings[1]);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге строки времени: " + s);
        }
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        s = hour + ":" + minute;
    }

    public Time(int totalMinutes) {
        this(totalMinutes / 60, totalMinutes % 60);
    }

    public int getTotalMinutes() {
        return hour * 60 + minute;
    }

    @Override
    public String toString() {
        return "Time{" + s + "}";
    }

    @Override
    public int compareTo(Time time) {
        int res = Integer.compare(hour, time.getHour());
        if (res == 0) {
            res = Integer.compare(minute, time.getMinute());
        }
        return res;
    }
}
