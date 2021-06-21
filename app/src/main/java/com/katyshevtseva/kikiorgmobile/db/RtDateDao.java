package com.katyshevtseva.kikiorgmobile.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.RtDateDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.RtDateDao.TableSchema.Cols.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.RtDateDao.TableSchema.Cols.VALUE;

class RtDateDao extends AbstractDao<RtDate> {

    RtDateDao(SQLiteDatabase database) {
        super(database, new RtDateTable());
    }

    private static class RtDateTable extends AbstractTable<RtDate> {

        RtDateTable() {
            super(TableSchema.NAME, createColumns());
        }

        @Override
        RtDate getNewEmptyObject() {
            return new RtDate();
        }

        private static List<AbstractColumn<RtDate>> createColumns() {
            List<AbstractColumn<RtDate>> columns = new ArrayList<>();
            columns.add(new AbstractColumn<RtDate>(ID, LONG) {
                @Override
                Object getActualValue(RtDate rtDate) {
                    return rtDate.getId();
                }

                @Override
                void setActualValue(RtDate rtDate, Object value) {
                    rtDate.setId((Long) value);
                }
            });
            columns.add(new AbstractColumn<RtDate>(TASK_ID, LONG) {
                @Override
                Object getActualValue(RtDate rtDate) {
                    return rtDate.getRegularTaskId();
                }

                @Override
                void setActualValue(RtDate rtDate, Object value) {
                    rtDate.setRegularTaskId((Long) value);
                }
            });
            columns.add(new AbstractColumn<RtDate>(VALUE, DATE) {
                @Override
                Object getActualValue(RtDate rtDate) {
                    return rtDate.getValue();
                }

                @Override
                void setActualValue(RtDate rtDate, Object value) {
                    rtDate.setValue((Date) value);
                }
            });

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
