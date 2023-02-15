package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;

import java.util.ArrayList;
import java.util.List;

public class PrefDao extends AbstractDao<PrefEntity> {
    static final String NAME = "pref";

    PrefDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), PrefEntity::new));
    }

    private static DbTable.Column<PrefEntity> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, PrefEntity::getId,
                (prefEntity, o) -> prefEntity.setId((Long) o));
    }

    private static List<DbTable.Column<PrefEntity>> createColumns() {
        List<DbTable.Column<PrefEntity>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(TITLE, STRING, PrefEntity::getTitle,
                (log, o) -> log.setTitle((String) o)));
        columns.add(new DbTable.Column<>(VALUE, STRING, PrefEntity::getValue,
                (log, o) -> log.setValue((String) o)));

        return columns;
    }
}