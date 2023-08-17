package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.LONG;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.db.lib.AbstractDao;
import com.katyshevtseva.kikiorgmobile.db.lib.DbTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class RtDateDao extends AbstractDao<RtDate> {
    static final String NAME = "rt_dates";

    RtDateDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), RtDate::new));
    }

    private static DbTable.Column<RtDate> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, RtDate::getId,
                (rtDate, o) -> rtDate.setId((Long) o));
    }

    private static List<DbTable.Column<RtDate>> createColumns() {
        List<DbTable.Column<RtDate>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(TASK_ID, LONG, RtDate::getRegularTaskId,
                (rtDate, o) -> rtDate.setRegularTaskId((Long) o)));
        columns.add(new DbTable.Column<>(VALUE, DATE, RtDate::getValue,
                (rtDate, o) -> rtDate.setValue((Date) o)));

        return columns;
    }
}
