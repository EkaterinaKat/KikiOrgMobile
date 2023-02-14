package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.DatelessTaskDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.DatelessTaskDao.TableSchema.Cols.TITLE;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;

import java.util.ArrayList;
import java.util.List;

class DatelessTaskDao extends AbstractDao<DatelessTask> {

    DatelessTaskDao(SQLiteDatabase database) {
        super(database, new DatelessTaskTable());
    }

    private static class DatelessTaskTable extends AbstractTable<DatelessTask> {

        DatelessTaskTable() {
            super(TableSchema.NAME, createIdColumn(), createColumns());
        }

        @Override
        DatelessTask getNewEmptyObject() {
            return new DatelessTask();
        }

        private static Column<DatelessTask> createIdColumn() {
            return new Column<>(ID, LONG, DatelessTask::getId,
                    (datelessTask, o) -> datelessTask.setId((Long) o));
        }

        private static List<Column<DatelessTask>> createColumns() {
            List<Column<DatelessTask>> columns = new ArrayList<>();

            columns.add(new Column<>(TITLE, STRING, DatelessTask::getTitle,
                    (datelessTask, o) -> datelessTask.setTitle((String) o)));

            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "dateless_task";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
        }
    }
}
