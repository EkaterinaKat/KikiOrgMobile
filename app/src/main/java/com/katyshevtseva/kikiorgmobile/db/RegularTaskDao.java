package com.katyshevtseva.kikiorgmobile.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.db.DbSchema.RegularTaskTable;

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
        database.insert(RegularTaskTable.NAME, null, values);
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

    RegularTask findById(long id) {
        Cursor cursor = database.query(RegularTaskTable.NAME, null, RegularTaskTable.Cols.ID + "=?",
                new String[]{"" + id}, null, null, null, null);

        try (KomCursorWrapper cursorWrapper = new KomCursorWrapper(cursor)) {
            cursorWrapper.moveToFirst();
            if (!cursorWrapper.isAfterLast()) {
                return cursorWrapper.getRegularTask();
            }
        }
        throw new RuntimeException("Задача не найдена по id");
    }

    void update(RegularTask regularTask) {
        ContentValues values = getContentValues(regularTask);
        String selection = RegularTaskTable.Cols.ID + " = ?";
        String[] selectionArgs = {"" + regularTask.getId()};
        database.update(
                RegularTaskTable.NAME,
                values,
                selection,
                selectionArgs);
    }

    void delete(RegularTask regularTask) {
        String selection = RegularTaskTable.Cols.ID + " = ?";
        String[] selectionArgs = {"" + regularTask.getId()};
        database.delete(RegularTaskTable.NAME, selection, selectionArgs);
    }

    private KomCursorWrapper getRegularTaskCursor() {
        Cursor cursor = database.query(RegularTaskTable.NAME, null, null, null,
                null, null, null);
        return new KomCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(RegularTask regularTask) {
        ContentValues values = new ContentValues();
        values.put(RegularTaskTable.Cols.TITLE, regularTask.getTitle());
        values.put(RegularTaskTable.Cols.DESC, regularTask.getDesc());
        values.put(RegularTaskTable.Cols.PERIOD_TYPE, regularTask.getPeriodType().getCode());
        values.put(RegularTaskTable.Cols.REF_DATE, DATE_FORMAT.format(regularTask.getRefDate()));
        values.put(RegularTaskTable.Cols.PERIOD, regularTask.getPeriod());
        values.put(RegularTaskTable.Cols.ARCHIVED, regularTask.isArchived() ? 1 : 0);
        return values;
    }

    private class KomCursorWrapper extends CursorWrapper {
        KomCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        RegularTask getRegularTask() {
            int id = getInt(getColumnIndex(RegularTaskTable.Cols.ID));
            String title = getString(getColumnIndex(RegularTaskTable.Cols.TITLE));
            String desc = getString(getColumnIndex(RegularTaskTable.Cols.DESC));
            PeriodType periodType = PeriodType.findByCode(getInt(getColumnIndex(RegularTaskTable.Cols.PERIOD_TYPE)));
            Date refDate;
            try {
                refDate = DATE_FORMAT.parse(getString(getColumnIndex(RegularTaskTable.Cols.REF_DATE)));
            } catch (ParseException e) {
                throw new RuntimeException();
            }
            int period = getInt(getColumnIndex(RegularTaskTable.Cols.PERIOD));
            boolean archived = getInt(getColumnIndex(RegularTaskTable.Cols.ARCHIVED)) == 1;
            return new RegularTask(id, title, desc, periodType, refDate, period, archived);
        }
    }
}
