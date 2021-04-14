package com.katyshevtseva.kikiorgmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.katyshevtseva.kikiorgmobile.db.DbSchema.IrregularTaskTable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "kom.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + IrregularTaskTable.NAME + "(" +
                IrregularTaskTable.Cols.ID + " INTEGER primary key autoincrement, " +
                IrregularTaskTable.Cols.TITLE + " TEXT, " +
                IrregularTaskTable.Cols.DESC + " TEXT, " +
                IrregularTaskTable.Cols.DATE + " TEXT, " +
                IrregularTaskTable.Cols.DONE + " INTEGER )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

    }
}
