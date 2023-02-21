package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;

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

    void saveNew(DatelessTask datelessTask);

    List<DatelessTask> getAllDatelessTasks();

    void update(DatelessTask datelessTask);

    void delete(DatelessTask datelessTask);

    void saveNew(Log log);

    List<Log> getAllLogs();

    PrefEntity getPrefByTitle(String title);

    void update(PrefEntity pref);

    RtSetting getRtSettingById(long id);

    void saveNew(RtSetting setting);

    void update(RtSetting setting);

    void delete(RtSetting setting);

    List<RtSetting> getAllRtSettings();

    List<RtSetting> getRtSettingsByRtId(Long rtId);

    List<OneDaySetting> findOneDaySetting(long taskId, TaskType taskType, Date date);

    void saveNew(OneDaySetting setting);

    List<OneDaySetting> getAllOneDaySettings();
}
