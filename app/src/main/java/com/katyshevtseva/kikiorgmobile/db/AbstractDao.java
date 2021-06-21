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
    private final SQLiteDatabase database;
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

    T findById(long id) {
        Cursor cursor = database.query(table.getName(), null, "id=?",
                new String[]{"" + id}, null, null, null, null);

        try (KomCursorWrapper cursorWrapper = new KomCursorWrapper(cursor)) {
            cursorWrapper.moveToFirst();
            if (!cursorWrapper.isAfterLast()) {
                return cursorWrapper.getT();
            }
        }
        throw new RuntimeException("Сущность не найдена по id");
    }

    void update(T t) {
        ContentValues values = getContentValues(t);
        String selection = "id=?";
        String[] selectionArgs = {"" + table.getId(t)};
        database.update(
                table.getName(),
                values,
                selection,
                selectionArgs);
    }

    void delete(T t) {
        String selection = "id=?";
        String[] selectionArgs = {"" + table.getId(t)};
        database.delete(table.getName(), selection, selectionArgs);
    }

    private KomCursorWrapper getCursor() {
        Cursor cursor = database.query(table.getName(), null, null, null,
                null, null, null);
        return new KomCursorWrapper(cursor);
    }

    private ContentValues getContentValues(T t) {
        ContentValues values = new ContentValues();
        for (AbstractColumn<T> column : table.getColumns()) {
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
            for (AbstractColumn<T> column : table.getColumns()) {
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
