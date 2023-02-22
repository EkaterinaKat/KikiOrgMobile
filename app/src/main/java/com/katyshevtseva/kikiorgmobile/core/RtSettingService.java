package com.katyshevtseva.kikiorgmobile.core;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.List;

public class RtSettingService {
    public static RtSettingService INSTANCE;
    private final KomDao komDao;

    public static void init(Context context) {
        INSTANCE = new RtSettingService(context);
    }

    private RtSettingService(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public RtSetting getRgSettingById(long id) {
        return komDao.getRtSettingById(id);
    }

    public void saveNewRgSetting(RegularTask regularTask, Time duration, Time beginTime, boolean absoluteWobs) throws Exception {
        if (komDao.getRtSettingsByRtId(regularTask.getId()).size() > 0) {
            throw new Exception("Настройка для этой задачи уже существует");
        }
        komDao.saveNew(new RtSetting(regularTask.getId(), duration, beginTime, absoluteWobs));
    }

    public RtSetting getRtSettingOrNull(RegularTask task) throws Exception {
        List<RtSetting> settings = komDao.getRtSettingsByRtId(task.getId());
        if (settings.size() > 1) {
            throw new Exception("Найдено более одной регулярной настройки для задачи " + task.getTitle());
        }
        if (settings.size() == 1) {
            return settings.get(0);
        }
        return null;
    }

    public List<RtSetting> getAllRtSettings() {
        return komDao.getAllRtSettings();
    }

    public void editRtSetting(RtSetting setting, Time duration, Time beginTime, boolean absoluteWobs) {
        setting.setDuration(duration);
        setting.setBeginTime(beginTime);
        setting.setAbsoluteWobs(absoluteWobs);
        komDao.update(setting);
    }

    public void deleteRtSetting(RtSetting setting) {
        komDao.delete(setting);
    }

    public String getRtSettingDesc(RtSetting setting) {
        RegularTask task = komDao.getRegularTaskById(setting.getRtId());
        String result = task.getTitle();
        if (setting.getDuration() != null) {
            result += ("\nDuration: " + setting.getDuration().getS());
        }
        if (setting.getBeginTime() != null) {
            result += ("\nBegin: " + setting.getBeginTime().getS() + (setting.isAbsoluteWobs() ? " abs" : " rel"));
        }
        return result;
    }
}
