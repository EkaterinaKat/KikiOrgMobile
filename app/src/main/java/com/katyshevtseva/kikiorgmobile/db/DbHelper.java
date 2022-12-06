package com.katyshevtseva.kikiorgmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 16;
    private static final String DATABASE_NAME = "kom.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + IrregularTaskDao.TableSchema.NAME + "(" +
                IrregularTaskDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                IrregularTaskDao.TableSchema.Cols.TITLE + " TEXT, " +
                IrregularTaskDao.TableSchema.Cols.DESC + " TEXT, " +
                IrregularTaskDao.TableSchema.Cols.TIME_OF_DAY + " INTEGER, " +
                IrregularTaskDao.TableSchema.Cols.DATE + " TEXT )");

        database.execSQL("create table " + RegularTaskDao.TableSchema.NAME + "(" +
                RegularTaskDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                RegularTaskDao.TableSchema.Cols.TITLE + " TEXT, " +
                RegularTaskDao.TableSchema.Cols.DESC + " TEXT, " +
                RegularTaskDao.TableSchema.Cols.TIME_OF_DAY + " INTEGER, " +
                RegularTaskDao.TableSchema.Cols.PERIOD_TYPE + " INTEGER, " +
                RegularTaskDao.TableSchema.Cols.PERIOD + " INTEGER, " +
                RegularTaskDao.TableSchema.Cols.ARCHIVED + " INTEGER )");

        database.execSQL("create table " + RtDateDao.TableSchema.NAME + "(" +
                RtDateDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                RtDateDao.TableSchema.Cols.TASK_ID + " INTEGER, " +
                RtDateDao.TableSchema.Cols.VALUE + " TEXT )");

        database.execSQL("create table " + DatelessTaskDao.TableSchema.NAME + "(" +
                DatelessTaskDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                DatelessTaskDao.TableSchema.Cols.TITLE + " TEXT )");

        database.execSQL("create table " + LogDao.TableSchema.NAME + "(" +
                LogDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                LogDao.TableSchema.Cols.DESC + " TEXT, " +
                LogDao.TableSchema.Cols.DATE + " TEXT, " +
                LogDao.TableSchema.Cols.ACTION + " TEXT, " +
                LogDao.TableSchema.Cols.SUBJECT + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
