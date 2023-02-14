package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.DESC;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.TIME_OF_DAY;
import static com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols.TITLE;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.db.IrregularTaskDao.TableSchema.Cols;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        private static Column<IrregularTask> createIdColumn() {
            return new Column<>(ID, LONG, IrregularTask::getId,
                    (irregularTask, o) -> irregularTask.setId((Long) o));
        }

        private static List<Column<IrregularTask>> createColumns() {
            List<Column<IrregularTask>> columns = new ArrayList<>();

            columns.add(new Column<>(TITLE, STRING, IrregularTask::getTitle,
                    (irregularTask, o) -> irregularTask.setTitle((String) o)));

            columns.add(new Column<>(DESC, STRING, IrregularTask::getDesc,
                    (irregularTask, o) -> irregularTask.setDesc((String) o)));

            columns.add(new Column<>(Cols.DATE, DATE, IrregularTask::getDate,
                    (irregularTask, o) -> irregularTask.setDate((Date) o)));

            columns.add(new Column<>(TIME_OF_DAY, LONG, irregularTask -> irregularTask.getTimeOfDay().getCode(),
                    (irregularTask, o) -> irregularTask.setTimeOfDay(TimeOfDay.findByCode(((Long) o).intValue()))));

            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "irregular_task";

        static final class Cols {
            static final String ID = "id";
            static final String TITLE = "title";
            static final String DESC = "desc";
            static final String TIME_OF_DAY = "time_of_day";
            static final String DATE = "date";
        }
    }
}
