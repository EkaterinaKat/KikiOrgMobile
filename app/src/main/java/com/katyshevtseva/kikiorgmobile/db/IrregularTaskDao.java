package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.BEGIN_TIME;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DESC;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DURATION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class IrregularTaskDao extends AbstractDao<IrregularTask> {
    static final String NAME = "irregular_task";

    IrregularTaskDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), IrregularTask::new));
    }

    private static DbTable.Column<IrregularTask> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, IrregularTask::getId,
                (irregularTask, o) -> irregularTask.setId((Long) o));
    }

    private static List<DbTable.Column<IrregularTask>> createColumns() {
        List<DbTable.Column<IrregularTask>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(TITLE, STRING, IrregularTask::getTitle,
                (irregularTask, o) -> irregularTask.setTitle((String) o)));

        columns.add(new DbTable.Column<>(DESC, STRING, IrregularTask::getDesc,
                (irregularTask, o) -> irregularTask.setDesc((String) o)));

        columns.add(new DbTable.Column<>(DbConstants.DATE, DATE, IrregularTask::getDate,
                (irregularTask, o) -> irregularTask.setDate((Date) o)));

        columns.add(new DbTable.Column<>(DURATION, STRING,
                irregularTask -> irregularTask.getDuration() != null ? irregularTask.getDuration().getS() : null,
                (irregularTask, o) -> {
                    if (o != null && !o.equals("null")) {
                        irregularTask.setDuration(new Time((String) o));
                    }
                }));

        columns.add(new DbTable.Column<>(BEGIN_TIME, STRING,
                irregularTask -> irregularTask.getBeginTime() != null ? irregularTask.getBeginTime().getS() : null,
                (irregularTask, o) -> {
                    if (o != null && !o.equals("null")) {
                        irregularTask.setBeginTime(new Time((String) o));
                    }
                }));

        return columns;
    }
}
