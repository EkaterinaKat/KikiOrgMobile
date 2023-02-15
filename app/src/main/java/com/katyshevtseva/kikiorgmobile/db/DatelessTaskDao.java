package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;

import java.util.ArrayList;
import java.util.List;

class DatelessTaskDao extends AbstractDao<DatelessTask> {
    static final String NAME = "dateless_task";

    DatelessTaskDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), DatelessTask::new));
    }

    private static DbTable.Column<DatelessTask> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, DatelessTask::getId,
                (datelessTask, o) -> datelessTask.setId((Long) o));
    }

    private static List<DbTable.Column<DatelessTask>> createColumns() {
        List<DbTable.Column<DatelessTask>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(TITLE, STRING, DatelessTask::getTitle,
                (datelessTask, o) -> datelessTask.setTitle((String) o)));

        return columns;
    }
}
