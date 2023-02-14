package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.PrefDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.PrefDao.TableSchema.Cols.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.PrefDao.TableSchema.Cols.VALUE;
import static com.katyshevtseva.kikiorgmobile.db.PrefDao.TableSchema.NAME;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;

import java.util.ArrayList;
import java.util.List;

public class PrefDao extends AbstractDao<PrefEntity> {

    PrefDao(SQLiteDatabase database) {
        super(database, new PrefDao.PrefTable());
    }

    private static class PrefTable extends AbstractTable<PrefEntity> {

        PrefTable() {
            super(NAME, createIdColumn(), createColumns());
        }

        @Override
        PrefEntity getNewEmptyObject() {
            return new PrefEntity();
        }

        private static Column<PrefEntity> createIdColumn() {
            return new Column<>(ID, LONG, PrefEntity::getId,
                    (prefEntity, o) -> prefEntity.setId((Long) o));
        }

        private static List<Column<PrefEntity>> createColumns() {
            List<Column<PrefEntity>> columns = new ArrayList<>();

            columns.add(new Column<>(TITLE, STRING, PrefEntity::getTitle,
                    (log, o) -> log.setTitle((String) o)));
            columns.add(new Column<>(VALUE, STRING, PrefEntity::getValue,
                    (log, o) -> log.setValue((String) o)));

            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "pref";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
            static final String VALUE = "value";
        }
    }
}