package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.BOOLEAN;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.LONG;
import static com.katyshevtseva.kikiorgmobile.db.AbstractTable.ColumnActualType.STRING;
import static com.katyshevtseva.kikiorgmobile.db.RtSettingDao.TableSchema.Cols.ABSOLUTE_WOBS;
import static com.katyshevtseva.kikiorgmobile.db.RtSettingDao.TableSchema.Cols.BEGIN_TIME;
import static com.katyshevtseva.kikiorgmobile.db.RtSettingDao.TableSchema.Cols.DURATION;
import static com.katyshevtseva.kikiorgmobile.db.RtSettingDao.TableSchema.Cols.ID;
import static com.katyshevtseva.kikiorgmobile.db.RtSettingDao.TableSchema.Cols.RG_ID;

import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.ArrayList;
import java.util.List;

public class RtSettingDao extends AbstractDao<RtSetting> {

    RtSettingDao(SQLiteDatabase database) {
        super(database, new RgSettingTable());
    }

    private static class RgSettingTable extends AbstractTable<RtSetting> {

        RgSettingTable() {
            super(TableSchema.NAME, createIdColumn(), createColumns());
        }

        @Override
        RtSetting getNewEmptyObject() {
            return new RtSetting();
        }

        private static Column<RtSetting> createIdColumn() {
            return new Column<>(ID, LONG, RtSetting::getId,
                    (rgSetting, o) -> rgSetting.setId((Long) o));
        }

        private static List<Column<RtSetting>> createColumns() {
            List<Column<RtSetting>> columns = new ArrayList<>();

            columns.add(new Column<>(RG_ID, LONG, RtSetting::getRtId,
                    (rgSetting, o) -> rgSetting.setRtId((Long) o)));

            columns.add(new Column<>(DURATION, STRING, RtSetting::getDuration,
                    (rgSetting, o) -> {
                        if (o != null)
                            rgSetting.setDuration(new Time((String) o));
                    }));

            columns.add(new Column<>(BEGIN_TIME, STRING, RtSetting::getBeginTime,
                    (rgSetting, o) -> {
                        if (o != null)
                            rgSetting.setBeginTime(new Time((String) o));
                    }));

            columns.add(new Column<>(ABSOLUTE_WOBS, BOOLEAN, RtSetting::isAbsoluteWobs,
                    (rgSetting, o) -> rgSetting.setAbsoluteWobs((boolean) o)));

            return columns;
        }
    }

    static final class TableSchema {
        static final String NAME = "rg_setting";

        static final class Cols {
            static final String ID = "id";
            static final String RG_ID = "rg_id";
            static final String DURATION = "duration";
            static final String BEGIN_TIME = "begin_time";
            static final String ABSOLUTE_WOBS = "absolute_wobs";
        }
    }
}