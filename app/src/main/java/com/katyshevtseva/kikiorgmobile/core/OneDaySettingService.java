package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;

import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.Date;
import java.util.List;

public class OneDaySettingService {
    public static OneDaySettingService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new OneDaySettingService(komDao);
    }

    private OneDaySettingService(KomDao komDao) {
        this.komDao = komDao;
    }

    public void saveNew(RegularTask task, Time duration, Time beginTime, Date date) throws Exception {
        if (duration == null && beginTime == null && date == null && task == null) {
            throw new Exception("Предоставлены не все необходимые параметры");
        }

        for (OneDaySetting setting : komDao.findOneDaySetting(task.getId(), date)) {
            komDao.delete(setting);
        }
        komDao.saveNew(new OneDaySetting(task.getId(), duration, beginTime, date));
    }

    public List<OneDaySetting> getAll() {
        return komDao.getAllOneDaySettings();
    }

    public OneDaySetting getSettingOrNull(RegularTask task, Date date) throws Exception {
        List<OneDaySetting> settings = komDao.findOneDaySetting(task.getId(), date);

        if (settings.size() > 1)
            throw new Exception(String.format("Найдено более одной настройки для задачи %s на дату %s",
                    task.getTitle(), getDateString(date)));

        if (settings.size() == 1)
            return settings.get(0);

        return null;
    }

}
