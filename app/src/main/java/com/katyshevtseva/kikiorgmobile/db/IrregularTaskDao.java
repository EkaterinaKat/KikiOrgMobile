package com.katyshevtseva.kikiorgmobile.db;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.BOOLEAN;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.DESC;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.DONE;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.TITLE;

class IrregularTaskDao extends AbstractDao<IrregularTask> {

    IrregularTaskDao(SQLiteDatabase database) {
        super(database, new IrregularTaskTable());
    }

    private static class IrregularTaskTable extends AbstractTable<IrregularTask> {

        IrregularTaskTable() {
            super(TableSchema.NAME, createIdColumn(), createColumns());
        }

        @Override
        IrregularTask getNewEmptyObject() {
            return new IrregularTask();
        }

        private static AbstractColumn<IrregularTask> createIdColumn() {
            return new AbstractColumn<IrregularTask>(ID, LONG) {
                @Override
                Object getActualValue(IrregularTask irregularTask) {
                    return irregularTask.getId();
                }

                @Override
                void setActualValue(IrregularTask irregularTask, Object value) {
                    irregularTask.setId((Long) value);
                }
            };
        }

        private static List<AbstractColumn<IrregularTask>> createColumns() {
            List<AbstractColumn<IrregularTask>> columns = new ArrayList<>();
            columns.add(new AbstractColumn<IrregularTask>(TITLE, STRING) {
                @Override
                Object getActualValue(IrregularTask irregularTask) {
                    return irregularTask.getTitle();
                }

                @Override
                void setActualValue(IrregularTask irregularTask, Object value) {
                    irregularTask.setTitle((String) value);
                }
            });
            columns.add(new AbstractColumn<IrregularTask>(DESC, STRING) {
                @Override
                Object getActualValue(IrregularTask irregularTask) {
                    return irregularTask.getDesc();
                }

                @Override
                void setActualValue(IrregularTask irregularTask, Object value) {
                    irregularTask.setDesc((String) value);
                }
            });
            columns.add(new AbstractColumn<IrregularTask>(Cols.DATE, DATE) {
                @Override
                Object getActualValue(IrregularTask irregularTask) {
                    return irregularTask.getDate();
                }

                @Override
                void setActualValue(IrregularTask irregularTask, Object value) {
                    irregularTask.setDate((Date) value);
                }
            });
            columns.add(new AbstractColumn<IrregularTask>(DONE, BOOLEAN) {
                @Override
                Object getActualValue(IrregularTask irregularTask) {
                    return irregularTask.isDone();
                }

                @Override
                void setActualValue(IrregularTask irregularTask, Object value) {
                    irregularTask.setDone((Boolean) value);
                }
            });
            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "irregular_task";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
            static final String DESC = "desc";
            static final String DATE = "date";
            static final String DONE = "done";
        }
    }
}
