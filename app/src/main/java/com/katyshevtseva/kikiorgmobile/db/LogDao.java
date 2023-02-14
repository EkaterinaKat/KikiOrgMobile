package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.DATE_TIME;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.LogDao.TableSchema.Cols.DESC;
import static com.katyshevtseva.kikiorgmobile.db.LogDao.TableSchema.Cols.ID;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogDao extends AbstractDao<Log> {

    LogDao(SQLiteDatabase database) {
        super(database, new LogTable());
    }

    private static class LogTable extends AbstractTable<Log> {

        LogTable() {
            super(TableSchema.NAME, createIdColumn(), createColumns());
        }

        @Override
        Log getNewEmptyObject() {
            return new Log();
        }

        private static Column<Log> createIdColumn() {
            return new Column<>(ID, LONG, Log::getId,
                    (log, o) -> log.setId((Long) o));
        }

        private static List<Column<Log>> createColumns() {
            List<Column<Log>> columns = new ArrayList<>();

            columns.add(new Column<>(DESC, STRING, Log::getDesc,
                    (log, o) -> log.setDesc((String) o)));
            columns.add(new Column<>(TableSchema.Cols.DATE, DATE_TIME, Log::getDate,
                    (log, o) -> log.setDate((Date) o)));
            columns.add(new Column<>(TableSchema.Cols.ACTION, STRING, log -> log.getAction().toString(),
                    (log, o) -> log.setAction(Log.Action.valueOf((String) o))));
            columns.add(new Column<>(TableSchema.Cols.SUBJECT, STRING, log -> log.getSubject().toString(),
                    (log, o) -> log.setSubject(Log.Subject.valueOf((String) o))));

            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "log";

        static final class Cols {
            static final String ID = "id";
            static final String DESC = "desc";
            static final String DATE = "date";
            static final String ACTION = "action";
            static final String SUBJECT = "subject";
        }
    }
}
