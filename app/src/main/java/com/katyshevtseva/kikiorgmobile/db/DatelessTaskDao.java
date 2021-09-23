package com.katyshevtseva.kikiorgmobile.db;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.DatelessTaskDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.DatelessTaskDao.TableSchema.Cols.TITLE;

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

        private static AbstractColumn<DatelessTask> createIdColumn() {
            return new AbstractColumn<DatelessTask>(ID, LONG) {
                @Override
                Object getActualValue(DatelessTask datelessTask) {
                    return datelessTask.getId();
                }

                @Override
                void setActualValue(DatelessTask datelessTask, Object value) {
                    datelessTask.setId((Long) value);
                }
            };
        }

        private static List<AbstractColumn<DatelessTask>> createColumns() {
            List<AbstractColumn<DatelessTask>> columns = new ArrayList<>();
            columns.add(new AbstractColumn<DatelessTask>(TITLE, STRING) {
                @Override
                Object getActualValue(DatelessTask datelessTask) {
                    return datelessTask.getTitle();
                }

                @Override
                void setActualValue(DatelessTask datelessTask, Object value) {
                    datelessTask.setTitle((String) value);
                }
            });
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
