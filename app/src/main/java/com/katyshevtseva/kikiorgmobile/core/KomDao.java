package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.Date;
import java.util.List;

public interface KomDao {
    void saveNew(IrregularTask irregularTask);

    List<IrregularTask> getAllIrregularTasks();

    List<IrregularTask> getIrregularTasksByDate(Date date);

    void update(IrregularTask irregularTask);

    void delete(IrregularTask irregularTask);

    void saveNew(RegularTask regularTask);

    List<RegularTask> getAllRegularTasks();

    List<RegularTask> getRegularTasksByDate(Date date);

    void update(RegularTask regularTask);

    RegularTask getRegularTaskById(long id);

    IrregularTask getIrregularTaskById(long id);

    void saveNew(Log log);

    List<Log> getAllLogs();

    void delete(Log log);

    PrefEntity getPrefByTitle(String title);

    void update(PrefEntity pref);

    List<OneDaySetting> findOneDaySetting(long taskId, Date date);

    void saveNew(OneDaySetting setting);

    List<OneDaySetting> getAllOneDaySettings();

    void delete(OneDaySetting setting);
}
