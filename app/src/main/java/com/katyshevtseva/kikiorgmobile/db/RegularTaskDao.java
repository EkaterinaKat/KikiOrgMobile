package com.katyshevtseva.kikiorgmobile.db;

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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.TimeOfDay;

import java.util.ArrayList;
import java.util.List;

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

        private static Column<RegularTask> createIdColumn() {
            return new Column<>(ID, LONG, RegularTask::getId,
                    (regularTask, o) -> regularTask.setId((Long) o));
        }

        private static List<Column<RegularTask>> createColumns() {
            List<Column<RegularTask>> columns = new ArrayList<>();

            columns.add(new Column<>(TITLE, STRING, RegularTask::getTitle,
                    (regularTask, o) -> regularTask.setTitle((String) o)));
            columns.add(new Column<>(DESC, STRING, RegularTask::getDesc,
                    (regularTask, o) -> regularTask.setDesc((String) o)));
            columns.add(new Column<>(PERIOD_TYPE, LONG, regularTask -> regularTask.getPeriodType().getCode(),
                    (regularTask, o) -> regularTask.setPeriodType(PeriodType.findByCode(((Long) o).intValue()))));
            columns.add(new Column<>(PERIOD, LONG, RegularTask::getPeriod,
                    (regularTask, o) -> regularTask.setPeriod(((Long) o).intValue())));
            columns.add(new Column<>(ARCHIVED, BOOLEAN, RegularTask::isArchived,
                    (regularTask, o) -> regularTask.setArchived((boolean) o)));
            columns.add(new Column<>(TIME_OF_DAY, LONG, regularTask -> regularTask.getTimeOfDay().getCode(),
                    (regularTask, o) -> regularTask.setTimeOfDay(TimeOfDay.findByCode(((Long) o).intValue()))));

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
