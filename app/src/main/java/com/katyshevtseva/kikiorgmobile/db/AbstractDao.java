package com.katyshevtseva.kikiorgmobile.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.db.AbstractTable.AbstractColumn;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class AbstractDao<T> {
    protected final SQLiteDatabase database;
    private final AbstractTable<T> table;

    void saveNew(T t) {
        ContentValues values = getContentValues(t);
        database.insert(table.getName(), null, values);
    }

    List<T> findAll() {
        List<T> items = new ArrayList<>();
        try (KomCursorWrapper cursor = getCursor()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getT());
                cursor.moveToNext();
            }
        }
        return items;
    }

    T findFirst(String columnName, String value) {
        Cursor cursor = database.query(table.getName(), null, columnName + "=?",
                new String[]{"" + value}, null, null, null, null);

        try (KomCursorWrapper cursorWrapper = new KomCursorWrapper(cursor)) {
            cursorWrapper.moveToFirst();
            if (!cursorWrapper.isAfterLast()) {
                return cursorWrapper.getT();
            }
        }
        return null;
    }

    List<T> find(String columnName, String value) {
        List<T> result = new ArrayList<>();
        Cursor cursor = database.query(table.getName(), null, columnName + "=?",
                new String[]{"" + value}, null, null, null, null);

        try (KomCursorWrapper cursorWrapper = new KomCursorWrapper(cursor)) {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                result.add(cursorWrapper.getT());
                cursor.moveToNext();
            }
        }
        return result;
    }

    void update(T t, String columnName, String value) {
        ContentValues values = getContentValues(t);
        String selection = columnName + "=?";
        String[] selectionArgs = {value};
        database.update(
                table.getName(),
                values,
                selection,
                selectionArgs);
    }

    void delete(String columnName, String value) {
        String selection = columnName + "=?";
        String[] selectionArgs = {value};
        database.delete(table.getName(), selection, selectionArgs);
    }

    private KomCursorWrapper getCursor() {
        Cursor cursor = database.query(table.getName(), null, null, null,
                null, null, null);
        return new KomCursorWrapper(cursor);
    }

    private ContentValues getContentValues(T t) {
        ContentValues values = new ContentValues();
        for (AbstractColumn<T> column : table.getContentColumns()) {
            values.put(column.getName(), column.getDbValueByActualValue(t));
        }
        return values;
    }

    private class KomCursorWrapper extends CursorWrapper {
        KomCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        T getT() {
            T t = table.getNewEmptyObject();
            for (AbstractColumn<T> column : table.getAllColumns()) {
                switch (column.getDbType()) {
                    case STRING:
                        column.setActualValue(t, column.getActualValueByDbValue(getString(getColumnIndex(column.getName()))));
                        break;
                    case LONG:
                        column.setActualValue(t, column.getActualValueByDbValue(getLong(getColumnIndex(column.getName()))));
                }
            }
            return t;
        }
    }
}
