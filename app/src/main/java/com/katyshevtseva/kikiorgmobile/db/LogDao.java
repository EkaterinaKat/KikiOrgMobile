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

        private static AbstractColumn<Log> createIdColumn() {
            return new AbstractColumn<Log>(ID, LONG) {
                @Override
                Object getActualValue(Log log) {
                    return log.getId();
                }

                @Override
                void setActualValue(Log log, Object value) {
                    log.setId((Long) value);
                }
            };
        }

        private static List<AbstractColumn<Log>> createColumns() {
            List<AbstractColumn<Log>> columns = new ArrayList<>();


            columns.add(new AbstractColumn<Log>(DESC, STRING) {
                @Override
                Object getActualValue(Log log) {
                    return log.getDesc();
                }

                @Override
                void setActualValue(Log log, Object value) {
                    log.setDesc((String) value);
                }
            });
            columns.add(new AbstractColumn<Log>(TableSchema.Cols.DATE, DATE_TIME) {
                @Override
                Object getActualValue(Log log) {
                    return log.getDate();
                }

                @Override
                void setActualValue(Log log, Object value) {
                    log.setDate((Date) value);
                }
            });
            columns.add(new AbstractColumn<Log>(TableSchema.Cols.ACTION, STRING) {
                @Override
                Object getActualValue(Log log) {
                    return log.getAction().toString();
                }

                @Override
                void setActualValue(Log log, Object value) {
                    log.setAction(Log.Action.valueOf((String) value));
                }
            });
            columns.add(new AbstractColumn<Log>(TableSchema.Cols.SUBJECT, STRING) {
                @Override
                Object getActualValue(Log log) {
                    return log.getSubject().toString();
                }

                @Override
                void setActualValue(Log log, Object value) {
                    log.setSubject(Log.Subject.valueOf((String) value));
                }
            });

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
