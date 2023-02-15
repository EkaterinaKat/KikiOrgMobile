package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_END;
import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_START;

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

        database.execSQL("create table " + PrefDao.TableSchema.NAME + "(" +
                PrefDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                PrefDao.TableSchema.Cols.TITLE + " TEXT, " +
                PrefDao.TableSchema.Cols.VALUE + " TEXT )");

        database.insert(PrefDao.TableSchema.NAME, null,
                getPrefContentValues(ACTIVITY_PERIOD_START.toString(), "08:00"));
        database.insert(PrefDao.TableSchema.NAME, null,
                getPrefContentValues(ACTIVITY_PERIOD_END.toString(), "22:00"));

        database.execSQL("create table " + RtSettingDao.TableSchema.NAME + "(" +
                RtSettingDao.TableSchema.Cols.ID + " INTEGER primary key autoincrement, " +
                RtSettingDao.TableSchema.Cols.RG_ID + " INTEGER, " +
                RtSettingDao.TableSchema.Cols.DURATION + " TEXT, " +
                RtSettingDao.TableSchema.Cols.BEGIN_TIME + " TEXT, " +
                RtSettingDao.TableSchema.Cols.ABSOLUTE_WOBS + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }

    private static ContentValues getPrefContentValues(String title, String value) {
        ContentValues values = new ContentValues();
        values.put(PrefDao.TableSchema.Cols.TITLE, title);
        values.put(PrefDao.TableSchema.Cols.VALUE, value);
        return values;
    }
}
