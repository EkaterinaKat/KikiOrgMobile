package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ABSOLUTE_WOBS;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.BEGIN_TIME;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DURATION;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.RG_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.BOOLEAN;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.DbTable.ColumnActualType.STRING;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.ArrayList;
import java.util.List;

public class RtSettingDao extends AbstractDao<RtSetting> {
    static final String NAME = "rg_setting";

    RtSettingDao(SQLiteDatabase database) {
        super(database, new DbTable<>(NAME, createIdColumn(), createColumns(), RtSetting::new));
    }

    private static DbTable.Column<RtSetting> createIdColumn() {
        return new DbTable.Column<>(ID, LONG, RtSetting::getId,
                (rgSetting, o) -> rgSetting.setId((Long) o));
    }

    private static List<DbTable.Column<RtSetting>> createColumns() {
        List<DbTable.Column<RtSetting>> columns = new ArrayList<>();

        columns.add(new DbTable.Column<>(RG_ID, LONG, RtSetting::getRtId,
                (rgSetting, o) -> rgSetting.setRtId((Long) o)));

        columns.add(new DbTable.Column<>(DURATION, STRING, RtSetting::getDuration,
                (rgSetting, o) -> {
                    if (o != null)
                        rgSetting.setDuration(new Time((String) o));
                }));

        columns.add(new DbTable.Column<>(BEGIN_TIME, STRING, RtSetting::getBeginTime,
                (rgSetting, o) -> {
                    if (o != null)
                        rgSetting.setBeginTime(new Time((String) o));
                }));

        columns.add(new DbTable.Column<>(ABSOLUTE_WOBS, BOOLEAN, RtSetting::isAbsoluteWobs,
                (rgSetting, o) -> rgSetting.setAbsoluteWobs((boolean) o)));

        return columns;
    }
}