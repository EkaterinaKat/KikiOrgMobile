package com.katyshevtseva.kikiorgmobile.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.TimeOfDay;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.BOOLEAN;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.ARCHIVED;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.DESC;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.PERIOD;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.PERIOD_TYPE;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.TIME_OF_DAY;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.TITLE;

class RegularTaskDao extends AbstractDao<RegularTask> {
    RegularTaskDao(SQLiteDatabase database) {
        super(database, new RegularTaskTable());
    }

    long getLastInsertedId() {
        String sql = String.format("SELECT ROWID from %s order by ROWID DESC limit 1", TableSchema.NAME);
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        cursor.close();
        throw new RuntimeException();
    }

    private static class RegularTaskTable extends AbstractTable<RegularTask> {

        RegularTaskTable() {
            super(TableSchema.NAME, createIdColumn(), createColumns());
        }

        @Override
        RegularTask getNewEmptyObject() {
            return new RegularTask();
        }

        private static AbstractColumn<RegularTask> createIdColumn() {
            return new AbstractColumn<RegularTask>(ID, LONG) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getId();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setId((Long) value);
                }
            };
        }

        private static List<AbstractColumn<RegularTask>> createColumns() {
            List<AbstractColumn<RegularTask>> columns = new ArrayList<>();
            columns.add(new AbstractColumn<RegularTask>(TITLE, STRING) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getTitle();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setTitle((String) value);
                }
            });
            columns.add(new AbstractColumn<RegularTask>(DESC, STRING) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getDesc();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setDesc((String) value);
                }
            });
            columns.add(new AbstractColumn<RegularTask>(PERIOD_TYPE, LONG) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getPeriodType().getCode();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setPeriodType(PeriodType.findByCode(((Long) value).intValue()));
                }
            });
            columns.add(new AbstractColumn<RegularTask>(PERIOD, LONG) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getPeriod();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setPeriod(((Long) value).intValue());
                }
            });
            columns.add(new AbstractColumn<RegularTask>(ARCHIVED, BOOLEAN) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.isArchived();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setArchived((boolean) value);
                }
            });
            columns.add(new AbstractColumn<RegularTask>(TIME_OF_DAY, LONG) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getTimeOfDay().getCode();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setTimeOfDay(TimeOfDay.findByCode(((Long) value).intValue()));
                }
            });
            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "regular_task";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
            static final String DESC = "desc";
            static final String TIME_OF_DAY = "time_of_day";
            static final String PERIOD_TYPE = "period_type";
            static final String PERIOD = "period";
            static final String ARCHIVED = "archived";
        }
    }
}
