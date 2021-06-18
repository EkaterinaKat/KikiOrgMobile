package com.katyshevtseva.kikiorgmobile.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.db.DbSchema.IrregularTaskTable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;

class IrregularTaskDao {
    private SQLiteDatabase database;

    IrregularTaskDao(SQLiteDatabase database) {
        this.database = database;
    }

    void saveNew(IrregularTask irregularTask) {
        ContentValues values = getContentValues(irregularTask);
        database.insert(IrregularTaskTable.NAME, null, values);
    }

    List<IrregularTask> findAll() {
        List<IrregularTask> tasks = new ArrayList<>();
        try (KomCursorWrapper cursor = getIrregularTaskCursor()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getIrregularTask());
                cursor.moveToNext();
            }
        }
        return tasks;
    }

    IrregularTask findById(long id) {
        Cursor cursor = database.query(IrregularTaskTable.NAME, null, IrregularTaskTable.Cols.ID + "=?",
                new String[]{"" + id}, null, null, null, null);

        try (KomCursorWrapper cursorWrapper = new KomCursorWrapper(cursor)) {
            cursorWrapper.moveToFirst();
            if (!cursorWrapper.isAfterLast()) {
                return cursorWrapper.getIrregularTask();
            }
        }
        throw new RuntimeException("Задача не найдена по id");
    }

    void update(IrregularTask irregularTask) {
        ContentValues values = getContentValues(irregularTask);
        String selection = IrregularTaskTable.Cols.ID + " = ?";
        String[] selectionArgs = {"" + irregularTask.getId()};
        database.update(
                IrregularTaskTable.NAME,
                values,
                selection,
                selectionArgs);
    }

    void delete(IrregularTask irregularTask) {
        String selection = IrregularTaskTable.Cols.ID + " = ?";
        String[] selectionArgs = {"" + irregularTask.getId()};
        database.delete(IrregularTaskTable.NAME, selection, selectionArgs);
    }

    private KomCursorWrapper getIrregularTaskCursor() {
        Cursor cursor = database.query(IrregularTaskTable.NAME, null, null, null,
                null, null, null);
        return new KomCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(IrregularTask irregularTask) {
        ContentValues values = new ContentValues();
        values.put(IrregularTaskTable.Cols.TITLE, irregularTask.getTitle());
        values.put(IrregularTaskTable.Cols.DESC, irregularTask.getDesc());
        values.put(IrregularTaskTable.Cols.DATE, DATE_FORMAT.format(irregularTask.getDate()));
        values.put(IrregularTaskTable.Cols.DONE, irregularTask.isDone() ? 1 : 0);
        return values;
    }

    private class KomCursorWrapper extends CursorWrapper {
        KomCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        IrregularTask getIrregularTask() {
            int id = getInt(getColumnIndex(IrregularTaskTable.Cols.ID));
            String title = getString(getColumnIndex(IrregularTaskTable.Cols.TITLE));
            String desc = getString(getColumnIndex(IrregularTaskTable.Cols.DESC));
            Date date;
            try {
                date = DATE_FORMAT.parse(getString(getColumnIndex(IrregularTaskTable.Cols.DATE)));
            } catch (ParseException e) {
                throw new RuntimeException();
            }
            boolean done = getInt(getColumnIndex(IrregularTaskTable.Cols.DONE)) == 1;
            return new IrregularTask(id, title, desc, date, done);
        }

    }
}
