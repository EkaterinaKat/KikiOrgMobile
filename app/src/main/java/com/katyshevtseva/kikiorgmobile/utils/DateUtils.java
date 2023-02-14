package com.katyshevtseva.kikiorgmobile.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public abstract class DateUtils {
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final DateFormat READABLE_DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

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

    public static String getDateTimeString(Date date) {
        return READABLE_DATE_TIME_FORMAT.format(date);
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

    public static boolean containsIgnoreTime(List<Date> dates, Date date) {
        for (Date date1 : dates)
            if (equalsIgnoreTime(date1, date))
                return true;
        return false;
    }

    public static void removeIgnoreTime(List<Date> dates, Date date) {
        Iterator<Date> iterator = dates.iterator();
        while (iterator.hasNext()) {
            if (equalsIgnoreTime(iterator.next(), date))
                iterator.remove();
        }
    }

    public static boolean equalsIgnoreTime(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }

    public static boolean beforeIgnoreTime(Date date1, Date date2) {
        return countNumberOfDaysBetweenDates(date1, date2) > 0;
    }

    /**
     * @return положительное число если date1 раньше чем date2 и наоборот
     */
    public static int countNumberOfDaysBetweenDates(Date date1, Date date2) {
        boolean date1BeforeDate2 = date1.before(date2);

        Date earlierDate = date1BeforeDate2 ? new Date(date1.getTime()) : new Date(date2.getTime());
        Date laterDate = date1BeforeDate2 ? new Date(date2.getTime()) : new Date(date1.getTime());

        int count = 0;
        while (!equalsIgnoreTime(earlierDate, laterDate)) {
            count++;
            earlierDate = shiftDate(earlierDate, TimeUnit.DAY, 1);
        }

        if (!date1BeforeDate2)
            count = count * (-1);

        return count;
    }
}
