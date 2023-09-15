package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ACTION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ARCHIVED;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DESC;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.PERIOD;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.PERIOD_TYPE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.SUBJECT;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TIME_OF_DAY;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.URGENCY;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 27;
    private static final String DATABASE_NAME = "kom.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + IrregularTaskDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT, " +
                URGENCY + " INTEGER, " +
                TIME_OF_DAY + " INTEGER, " +
                DATE + " TEXT )");

        database.execSQL("create table " + RegularTaskDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT, " +
                DESC + " TEXT, " +
                PERIOD_TYPE + " INTEGER, " +
                TIME_OF_DAY + " INTEGER, " +
                URGENCY + " INTEGER, " +
                PERIOD + " INTEGER, " +
                ARCHIVED + " INTEGER )");

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

        database.execSQL("create table " + OptionalTaskDao.NAME + "(" +
                ID + " INTEGER primary key autoincrement, " +
                TITLE + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//        database.execSQL("Update irregular_task set time_of_day = 1; ");
//        database.execSQL("Update regular_task set time_of_day = 1; ");
//        database.execSQL("ALTER TABLE irregular_task ADD duration TEXT; ");
//        database.execSQL("ALTER TABLE irregular_task ADD begin_time TEXT; ");
//        database.execSQL("ALTER TABLE regular_task ADD absolute_wobs INTEGER; ");
//        database.execSQL("delete from one_day_setting; ");
    }
}
