package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_END;
import static com.katyshevtseva.kikiorgmobile.core.model.PrefEntity.Pref.ACTIVITY_PERIOD_START;

import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;
import com.katyshevtseva.kikiorgmobile.utils.Time;

public class PrefService {
    public static PrefService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new PrefService(komDao);
    }

    private PrefService(KomDao komDao) {
        this.komDao = komDao;
    }

    public Time getActivityStart() {
        return new Time(komDao.getPrefByTitle(ACTIVITY_PERIOD_START.toString()).getValue());
    }

    public Time getActivityEnd() {
        return new Time(komDao.getPrefByTitle(ACTIVITY_PERIOD_END.toString()).getValue());
    }

    public void updateStartActivityPeriodValue(int hour, int minute) throws Exception {
        if (hour >= getActivityEnd().getHour()) {
            throw new Exception("Некорректный интервал");
        }

        PrefEntity prefEntity = komDao.getPrefByTitle(ACTIVITY_PERIOD_START.toString());
        prefEntity.setValue(hour + ":" + minute);
        komDao.update(prefEntity);
    }

    public void updateEndActivityPeriodValue(int hour, int minute) throws Exception {
        if (hour <= getActivityStart().getHour()) {
            throw new Exception("Некорректный интервал");
        }

        PrefEntity prefEntity = komDao.getPrefByTitle(ACTIVITY_PERIOD_END.toString());
        prefEntity.setValue(hour + ":" + minute);
        komDao.update(prefEntity);
    }
}
