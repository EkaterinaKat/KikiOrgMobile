package com.katyshevtseva.kikiorgmobile.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;

class RegularTaskDao {
    private SQLiteDatabase database;

    RegularTaskDao(SQLiteDatabase database) {
        this.database = database;
    }

    void saveNew(RegularTask regularTask) {
        ContentValues values = getContentValues(regularTask);
        database.insert(DbSchema.RegularTaskTable.NAME, null, values);
    }

    List<RegularTask> findAll() {
        List<RegularTask> tasks = new ArrayList<>();
        try (KomCursorWrapper cursor = getRegularTaskCursor()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getRegularTask());
                cursor.moveToNext();
            }
        }
        return tasks;
    }

    void update(RegularTask regularTask) {
        ContentValues values = getContentValues(regularTask);
        String selection = DbSchema.RegularTaskTable.Cols.ID + " = ?";
        String[] selectionArgs = {"" + regularTask.getId()};
        database.update(
                DbSchema.RegularTaskTable.NAME,
                values,
                selection,
                selectionArgs);
    }

    void delete(RegularTask regularTask) {
        String selection = DbSchema.RegularTaskTable.Cols.ID + " = ?";
        String[] selectionArgs = {"" + regularTask.getId()};
        database.delete(DbSchema.RegularTaskTable.NAME, selection, selectionArgs);
    }

    private KomCursorWrapper getRegularTaskCursor() {
        Cursor cursor = database.query(DbSchema.RegularTaskTable.NAME, null, null, null,
                null, null, null);
        return new KomCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(RegularTask regularTask) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.RegularTaskTable.Cols.TITLE, regularTask.getTitle());
        values.put(DbSchema.RegularTaskTable.Cols.DESC, regularTask.getDesc());
        values.put(DbSchema.RegularTaskTable.Cols.PERIOD_TYPE, regularTask.getPeriodType().getCode());
        values.put(DbSchema.RegularTaskTable.Cols.REF_DATE, DATE_FORMAT.format(regularTask.getRefDate()));
        values.put(DbSchema.RegularTaskTable.Cols.PERIOD, regularTask.getPeriod());
        return values;
    }

    private class KomCursorWrapper extends CursorWrapper {
        KomCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        RegularTask getRegularTask() {
            int id = getInt(getColumnIndex(DbSchema.RegularTaskTable.Cols.ID));
            String title = getString(getColumnIndex(DbSchema.RegularTaskTable.Cols.TITLE));
            String desc = getString(getColumnIndex(DbSchema.RegularTaskTable.Cols.DESC));
            PeriodType periodType = PeriodType.findByCode(getInt(getColumnIndex(DbSchema.RegularTaskTable.Cols.PERIOD_TYPE)));
            Date refDate;
            try {
                refDate = DATE_FORMAT.parse(getString(getColumnIndex(DbSchema.RegularTaskTable.Cols.REF_DATE)));
            } catch (ParseException e) {
                throw new RuntimeException();
            }
            int period = getInt(getColumnIndex(DbSchema.RegularTaskTable.Cols.PERIOD));
            return new RegularTask(id, title, desc, periodType, refDate, period);
        }
    }
}
