package com.katyshevtseva.kikiorgmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 8;
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
                IrregularTaskDao.TableSchema.Cols.DATE + " TEXT, " +
                IrregularTaskDao.TableSchema.Cols.DONE + " INTEGER )");

        database.execSQL("create table " + RegularTaskDao.TableSchema.NAME + "(" +
                RegularTaskDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                RegularTaskDao.TableSchema.Cols.TITLE + " TEXT, " +
                RegularTaskDao.TableSchema.Cols.DESC + " TEXT, " +
                RegularTaskDao.TableSchema.Cols.PERIOD_TYPE + " INTEGER, " +
                RegularTaskDao.TableSchema.Cols.REF_DATE + " TEXT, " +
                RegularTaskDao.TableSchema.Cols.PERIOD + " INTEGER, " +
                RegularTaskDao.TableSchema.Cols.ARCHIVED + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}
