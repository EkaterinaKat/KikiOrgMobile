package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.Date;
import java.util.List;

public class OneDaySettingService {
    public static OneDaySettingService INSTANCE;
    private final KomDao komDao;

    public static void init(Context context) {
        INSTANCE = new OneDaySettingService(context);
    }

    private OneDaySettingService(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public void saveNew(Task task, Time duration, Time beginTime, Date date) throws Exception {
        if (duration == null && beginTime == null && date == null && task == null) {
            throw new Exception("Предоставлены не все необходимые параметры");
        }

        if (komDao.findOneDaySetting(task.getId(), task.getType(), date).size() > 0) {
            throw new Exception("Настройка для этой задачи на данную дату уже существует");
        }
        komDao.saveNew(new OneDaySetting(task.getId(), task.getType(), duration, beginTime));
    }

    public List<OneDaySetting> getAll() {
        return komDao.getAllOneDaySettings();
    }

    public OneDaySetting getSettingOrNull(Task task, Date date) throws Exception {
        List<OneDaySetting> settings = komDao.findOneDaySetting(task.getId(), task.getType(), date);

        if (settings.size() > 1)
            throw new Exception(String.format("Найдено более одной настройки для задачи %s на дату %s",
                    task.getTitle(), getDateString(date)));

        if (settings.size() == 1)
            return settings.get(0);

        return null;
    }

}
