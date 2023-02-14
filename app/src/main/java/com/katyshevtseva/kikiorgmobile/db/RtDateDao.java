package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.RtDateDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.RtDateDao.TableSchema.Cols.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.RtDateDao.TableSchema.Cols.VALUE;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class RtDateDao extends AbstractDao<RtDate> {

    RtDateDao(SQLiteDatabase database) {
        super(database, new RtDateTable());
    }

    private static class RtDateTable extends AbstractTable<RtDate> {

        RtDateTable() {
            super(TableSchema.NAME, createIdColumn(), createColumns());
        }

        @Override
        RtDate getNewEmptyObject() {
            return new RtDate();
        }

        private static Column<RtDate> createIdColumn() {
            return new Column<>(ID, LONG, RtDate::getId,
                    (rtDate, o) -> rtDate.setId((Long) o));
        }

        private static List<Column<RtDate>> createColumns() {
            List<Column<RtDate>> columns = new ArrayList<>();

            columns.add(new Column<>(TASK_ID, LONG, RtDate::getRegularTaskId,
                    (rtDate, o) -> rtDate.setRegularTaskId((Long) o)));
            columns.add(new Column<>(VALUE, DATE, RtDate::getValue,
                    (rtDate, o) -> rtDate.setValue((Date) o)));

            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "rt_dates";

        static final class Cols {
            static final String ID = "id";
            static final String TASK_ID = "task_id";
            static final String VALUE = "value";
        }
    }
}
