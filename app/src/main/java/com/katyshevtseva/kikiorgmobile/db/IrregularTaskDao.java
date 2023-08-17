package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DESC;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TIME_OF_DAY;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.URGENCY;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.lib.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskUrgency;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.db.lib.AbstractDao;
import com.katyshevtseva.kikiorgmobile.db.lib.DbTable;

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

        columns.add(new DbTable.Column<>(URGENCY, LONG, irregularTask -> irregularTask.getUrgency().getCode(),
                (irregularTask, o) -> irregularTask.setUrgency(TaskUrgency.findByCode(((Long) o).intValue()))));

        columns.add(new DbTable.Column<>(TIME_OF_DAY, LONG, irregularTask -> irregularTask.getTimeOfDay().getCode(),
                (irregularTask, o) -> irregularTask.setTimeOfDay(TimeOfDay.findByCode(((Long) o).intValue()))));

        return columns;
    }
}
