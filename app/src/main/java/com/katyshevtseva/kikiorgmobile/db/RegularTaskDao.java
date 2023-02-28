package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ABSOLUTE_WOBS;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ARCHIVED;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.BEGIN_TIME;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DESC;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DURATION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.PERIOD;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.PERIOD_TYPE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.BOOLEAN;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.STRING;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.ArrayList;
import java.util.List;

class RegularTaskDao extends AbstractDao<RegularTask> {
    static final String NAME = "regular_task";

    RegularTaskDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), RegularTask::new));
    }

    long getLastInsertedId() {
        String sql = String.format("SELECT ROWID from %s order by ROWID DESC limit 1", NAME);
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        cursor.close();
        throw new RuntimeException();
    }

    private static DbTable.Column<RegularTask> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, RegularTask::getId,
                (regularTask, o) -> regularTask.setId((Long) o));
    }

    private static List<DbTable.Column<RegularTask>> createColumns() {
        List<DbTable.Column<RegularTask>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(TITLE, STRING, RegularTask::getTitle,
                (regularTask, o) -> regularTask.setTitle((String) o)));
        columns.add(new DbTable.Column<>(DESC, STRING, RegularTask::getDesc,
                (regularTask, o) -> regularTask.setDesc((String) o)));
        columns.add(new DbTable.Column<>(PERIOD_TYPE, LONG, regularTask -> regularTask.getPeriodType().getCode(),
                (regularTask, o) -> regularTask.setPeriodType(PeriodType.findByCode(((Long) o).intValue()))));
        columns.add(new DbTable.Column<>(PERIOD, LONG, RegularTask::getPeriod,
                (regularTask, o) -> regularTask.setPeriod(((Long) o).intValue())));
        columns.add(new DbTable.Column<>(ARCHIVED, BOOLEAN, RegularTask::isArchived,
                (regularTask, o) -> regularTask.setArchived((boolean) o)));

        columns.add(new DbTable.Column<>(DURATION, STRING,
                regularTask -> regularTask.getDuration() != null ? regularTask.getDuration().getS() : null,
                (regularTask, o) -> {
                    if (o != null && !o.equals("null")) {
                        regularTask.setDuration(new Time((String) o));
                    }
                }));

        columns.add(new DbTable.Column<>(BEGIN_TIME, STRING,
                regularTask -> regularTask.getBeginTime() != null ? regularTask.getBeginTime().getS() : null,
                (regularTask, o) -> {
                    if (o != null && !o.equals("null")) {
                        regularTask.setBeginTime(new Time((String) o));
                    }
                }));

        columns.add(new DbTable.Column<>(ABSOLUTE_WOBS, BOOLEAN, RegularTask::isAbsoluteWobs,
                (regularTask, o) -> regularTask.setAbsoluteWobs((boolean) o)));


        return columns;
    }
}
