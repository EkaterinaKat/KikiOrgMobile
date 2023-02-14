package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_END;
import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_START;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;
import com.katyshevtseva.kikiorgmobile.utils.Time;

public class PrefService {
    public static PrefService INSTANCE;
    private final KomDao komDao;

    public static void init(Context context) {
        INSTANCE = new PrefService(context);
    }

    private PrefService(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public Time getStartActivityPeriod() {
        return new Time(komDao.getPrefByTitle(ACTIVITY_PERIOD_START.toString()).getValue());
    }

    public Time getEndActivityPeriod() {
        return new Time(komDao.getPrefByTitle(ACTIVITY_PERIOD_END.toString()).getValue());
    }

    public boolean updateStartActivityPeriodValue(int hour, int minute) {
        if (hour >= getEndActivityPeriod().getHour()) {
            return false;
        }

        PrefEntity prefEntity = komDao.getPrefByTitle(ACTIVITY_PERIOD_START.toString());
        prefEntity.setValue(hour + ":" + minute);
        komDao.updatePref(prefEntity);
        return true;
    }

    public boolean updateEndActivityPeriodValue(int hour, int minute) {
        if (hour <= getStartActivityPeriod().getHour()) {
            return false;
        }

        PrefEntity prefEntity = komDao.getPrefByTitle(ACTIVITY_PERIOD_END.toString());
        prefEntity.setValue(hour + ":" + minute);
        komDao.updatePref(prefEntity);
        return true;
    }
}
