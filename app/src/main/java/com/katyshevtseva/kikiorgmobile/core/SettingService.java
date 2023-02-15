package com.katyshevtseva.kikiorgmobile.core;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;
import com.katyshevtseva.kikiorgmobile.utils.Time;

public class SettingService {
    public static SettingService INSTANCE;
    private final KomDao komDao;

    public static void init(Context context) {
        INSTANCE = new SettingService(context);
    }

    private SettingService(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public RtSetting getRgSettingById(long id) {
        return komDao.getRtSettingById(id);
    }

    public void saveNewRgSetting(RegularTask regularTask, Time duration, Time beginTime, boolean absoluteWobs) {
        komDao.saveNewRtSetting(new RtSetting(regularTask.getId(), duration, beginTime, absoluteWobs));
    }
}
