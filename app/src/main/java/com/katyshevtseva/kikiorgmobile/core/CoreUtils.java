package com.katyshevtseva.kikiorgmobile.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CoreUtils {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static String getDateString(int year, int month, int day) {
        return String.format("%d.%s.%d", day, month < 10 ? "0" + month : month, year);
    }

    public static Date getDateByString(String s) {
        try {
            return DATE_FORMAT.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
