package com.katyshevtseva.kikiorgmobile.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CoreUtils {
    public static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static String getDateString(int year, int month, int day) {
        return String.format("%d.%s.%d", day, month < 10 ? "0" + month : month, year);
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
}
