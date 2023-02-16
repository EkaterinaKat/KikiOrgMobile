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
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.RT_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.SUBJECT;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TIME_OF_DAY;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 19;
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
                TIME_OF_DAY + " INTEGER, " +
                DATE + " TEXT )");

        database.execSQL("create table " + RegularTaskDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT, " +
                DESC + " TEXT, " +
                TIME_OF_DAY + " INTEGER, " +
                PERIOD_TYPE + " INTEGER, " +
                PERIOD + " INTEGER, " +
                ARCHIVED + " INTEGER )");

        database.execSQL("create table " + RtDateDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TASK_ID + " INTEGER, " +
                VALUE + " TEXT )");

        database.execSQL("create table " + DatelessTaskDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT )");

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

        database.execSQL("create table " + RtSettingDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                RT_ID + " INTEGER, " +
                DURATION + " TEXT, " +
                BEGIN_TIME + " TEXT, " +
                ABSOLUTE_WOBS + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }

    private static ContentValues getPrefContentValues(String title, String value) {
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(VALUE, value);
        return values;
    }
}
