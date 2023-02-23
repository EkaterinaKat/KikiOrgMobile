package com.katyshevtseva.kikiorgmobile.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

class DbConstants {
    static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    static final String ABSOLUTE_WOBS = "absolute_wobs";
    static final String ACTION = "action";
    static final String ARCHIVED = "archived";
    static final String BEGIN_TIME = "begin_time";
    static final String DATE = "date";
    static final String DESC = "desc";
    static final String DURATION = "duration";
    static final String ID = "id";
    static final String PERIOD = "period";
    static final String PERIOD_TYPE = "period_type";
    static final String RT_ID = "rg_id";
    static final String SUBJECT = "subject";
    static final String TASK_ID = "task_id";
    static final String TASK_TYPE = "task_type";
    static final String TITLE = "title";
    static final String VALUE = "value";
}
