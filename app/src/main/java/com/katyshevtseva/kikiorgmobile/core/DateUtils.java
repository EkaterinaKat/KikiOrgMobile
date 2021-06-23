package com.katyshevtseva.kikiorgmobile.core;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.core.DateUtils.TimeUnit.DAY;

@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public abstract class DateUtils {
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static String getDateString(int year, int month, int day) {
        return String.format("%d.%s.%d", day, month < 10 ? "0" + month : month, year);
    }

    public static Date parse(int year, int month, int day) {
        try {
            return READABLE_DATE_FORMAT.parse(String.format("%d.%s.%d", day, month < 10 ? "0" + month : month, year));
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }

    public static String getDateString(Date date) {
        return READABLE_DATE_FORMAT.format(date);
    }

    public static Date getDateByString(String s) {
        try {
            return READABLE_DATE_FORMAT.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isDate(String s) {
        try {
            READABLE_DATE_FORMAT.parse(s);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getDateString(List<Date> dates) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Date date : dates) {
            stringBuilder.append(getDateString(date)).append("\n");
        }
        return stringBuilder.toString();
    }

    public static String getDateStringWithWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return getDateString(date) + " (Вс)";
            case 2:
                return getDateString(date) + " (Пн)";
            case 3:
                return getDateString(date) + " (Вт)";
            case 4:
                return getDateString(date) + " (Ср)";
            case 5:
                return getDateString(date) + " (Чт)";
            case 6:
                return getDateString(date) + " (Пт)";
            case 7:
                return getDateString(date) + " (Сб)";
        }
        throw new RuntimeException();
    }

    public static Date getProperDate() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        if (hour > 6)
            return new Date();
        return shiftDate(new Date(), DAY, -1);
    }

    public static Date shiftDate(Date date, TimeUnit unit, int numOfUnits) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(unit.getIntRepresentationForCalendar(), numOfUnits);
        return calendar.getTime();
    }

    public enum TimeUnit {
        DAY(Calendar.DATE), MONTH(Calendar.MONTH), YEAR(Calendar.YEAR);

        private int intRepresentationForCalendar;

        TimeUnit(int intRepresentationForCalendar) {
            this.intRepresentationForCalendar = intRepresentationForCalendar;
        }

        public int getIntRepresentationForCalendar() {
            return intRepresentationForCalendar;
        }
    }
}
