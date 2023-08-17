package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ACTION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DESC;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.SUBJECT;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.DATE_TIME;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.db.lib.AbstractDao;
import com.katyshevtseva.kikiorgmobile.db.lib.DbTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogDao extends AbstractDao<Log> {
    static final String NAME = "log";

    LogDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), Log::new));
    }

    private static DbTable.Column<Log> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, Log::getId,
                (log, o) -> log.setId((Long) o));
    }

    private static List<DbTable.Column<Log>> createColumns() {
        List<DbTable.Column<Log>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(DESC, STRING, Log::getDesc,
                (log, o) -> log.setDesc((String) o)));
        columns.add(new DbTable.Column<>(DATE, DATE_TIME, Log::getDate,
                (log, o) -> log.setDate((Date) o)));
        columns.add(new DbTable.Column<>(ACTION, STRING, log -> log.getAction().toString(),
                (log, o) -> log.setAction(Log.Action.valueOf((String) o))));
        columns.add(new DbTable.Column<>(SUBJECT, STRING, log -> log.getSubject().toString(),
                (log, o) -> log.setSubject(Log.Subject.valueOf((String) o))));

        return columns;
    }
}
