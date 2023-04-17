package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_END;
import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_START;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ABSOLUTE_WOBS;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ACTION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ARCHIVED;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.BEGIN_TIME;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DESC;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DURATION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.PERIOD;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.PERIOD_TYPE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.SUBJECT;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.URGENCY;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 25;
    private static final String DATABASE_NAME = "kom.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + IrregularTaskDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT, " +
                DESC + " TEXT, " +
                DURATION + " TEXT, " +
                BEGIN_TIME + " TEXT, " +
                URGENCY + " INTEGER, " +
                DATE + " TEXT )");

        database.execSQL("create table " + RegularTaskDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT, " +
                DESC + " TEXT, " +
                PERIOD_TYPE + " INTEGER, " +
                URGENCY + " INTEGER, " +
                PERIOD + " INTEGER, " +
                ARCHIVED + " INTEGER, " +
                DURATION + " TEXT, " +
                BEGIN_TIME + " TEXT, " +
                ABSOLUTE_WOBS + " INTEGER )");

        database.execSQL("create table " + RtDateDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TASK_ID + " INTEGER, " +
                VALUE + " TEXT )");

        database.execSQL("create table " + LogDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                DESC + " TEXT, " +
                DATE + " TEXT, " +
                ACTION + " TEXT, " +
                SUBJECT + " TEXT )");

        database.execSQL("create table " + PrefDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT, " +
                VALUE + " TEXT )");

        database.insert(PrefDao.NAME, null,
                getPrefContentValues(ACTIVITY_PERIOD_START.toString(), "08:00"));
        database.insert(PrefDao.NAME, null,
                getPrefContentValues(ACTIVITY_PERIOD_END.toString(), "22:00"));

        database.execSQL("create table " + OneDaySettingDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TASK_ID + " INTEGER, " +
                DURATION + " TEXT, " +
                BEGIN_TIME + " TEXT, " +
                DATE + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//        database.execSQL("ALTER TABLE irregular_task ADD duration TEXT; ");
//        database.execSQL("ALTER TABLE irregular_task ADD begin_time TEXT; ");
//        database.execSQL("ALTER TABLE regular_task ADD absolute_wobs INTEGER; ");
//        database.execSQL("delete from one_day_setting; ");
    }

    private static ContentValues getPrefContentValues(String title, String value) {
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(VALUE, value);
        return values;
    }
}
