package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.BEGIN_TIME;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DURATION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OneDaySettingDao extends AbstractDao<OneDaySetting> {
    static final String NAME = "one_day_setting";

    OneDaySettingDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), OneDaySetting::new));
    }

    private static DbTable.Column<OneDaySetting> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, OneDaySetting::getId,
                (setting, o) -> setting.setId((Long) o));
    }

    private static List<DbTable.Column<OneDaySetting>> createColumns() {
        List<DbTable.Column<OneDaySetting>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(TASK_ID, LONG, OneDaySetting::getTaskId,
                (setting, o) -> setting.setTaskId((Long) o)));

        columns.add(new DbTable.Column<>(DURATION, STRING, setting -> setting.getDuration().getS(),
                (setting, o) -> setting.setDuration(new Time((String) o))));

        columns.add(new DbTable.Column<>(BEGIN_TIME, STRING, setting -> setting.getBeginTime().getS(),
                (setting, o) -> setting.setBeginTime(new Time((String) o))));

        columns.add(new DbTable.Column<>(DbConstants.DATE, DATE, OneDaySetting::getDate,
                (setting, o) -> setting.setDate((Date) o)));

        return columns;
    }
}
