package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.OptionalTask;
import com.katyshevtseva.kikiorgmobile.db.lib.AbstractDao;
import com.katyshevtseva.kikiorgmobile.db.lib.DbTable;

import java.util.ArrayList;
import java.util.List;

public class OptionalTaskDao extends AbstractDao<OptionalTask> {
    static final String NAME = "optional_task";

    OptionalTaskDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), OptionalTask::new));
    }

    private static DbTable.Column<OptionalTask> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, OptionalTask::getId,
                (ot, o) -> ot.setId((Long) o));
    }

    private static List<DbTable.Column<OptionalTask>> createColumns() {
        List<DbTable.Column<OptionalTask>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(TITLE, STRING, OptionalTask::getTitle,
                (ot, o) -> ot.setTitle((String) o)));

        return columns;
    }
}
