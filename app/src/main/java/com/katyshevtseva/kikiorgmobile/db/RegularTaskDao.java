package com.katyshevtseva.kikiorgmobile.db;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.BOOLEAN;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.ARCHIVED;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.DESC;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.PERIOD;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.PERIOD_TYPE;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.REF_DATE;
import static com.katyshevtseva.kikiorgmobile.db.RegularTaskDao.TableSchema.Cols.TITLE;

class RegularTaskDao extends AbstractDao<RegularTask> {
    RegularTaskDao(SQLiteDatabase database) {
        super(database, new RegularTaskTable());
    }

    private static class RegularTaskTable extends AbstractTable<RegularTask> {

        RegularTaskTable() {
            super(TableSchema.NAME, createColumns());
        }

        @Override
        RegularTask getNewEmptyObject() {
            return new RegularTask();
        }

        @Override
        long getId(RegularTask regularTask) {
            return regularTask.getId();
        }

        private static List<AbstractColumn<RegularTask>> createColumns() {
            List<AbstractColumn<RegularTask>> columns = new ArrayList<>();
            columns.add(new AbstractColumn<RegularTask>(ID, LONG) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getId();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setId((Long) value);
                }
            });
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
            columns.add(new AbstractColumn<RegularTask>(REF_DATE, DATE) {
                @Override
                Object getActualValue(RegularTask regularTask) {
                    return regularTask.getRefDate();
                }

                @Override
                void setActualValue(RegularTask regularTask, Object value) {
                    regularTask.setRefDate((Date) value);
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
            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "regular_task";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
            static final String DESC = "desc";
            static final String PERIOD_TYPE = "period_type";
            static final String REF_DATE = "ref_date";
            static final String PERIOD = "period";
            static final String ARCHIVED = "archived";
        }
    }
}
