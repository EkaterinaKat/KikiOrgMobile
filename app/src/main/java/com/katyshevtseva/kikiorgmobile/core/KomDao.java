package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.Date;
import java.util.List;

public interface KomDao {
    void saveNewIrregularTask(IrregularTask irregularTask);

    List<IrregularTask> getAllIrregularTasks();

    List<IrregularTask> getIrregularTasksByDate(Date date);

    void updateIrregularTask(IrregularTask irregularTask);

    void deleteIrregularTask(IrregularTask irregularTask);

    void saveNewRegularTask(RegularTask regularTask);

    List<RegularTask> getAllRegularTasks();

    List<RegularTask> getRegularTasksByDate(Date date);

    void updateRegularTask(RegularTask regularTask);

    void deleteRegularTask(RegularTask regularTask);

    RegularTask getRegularTaskById(long id);

    IrregularTask getIrregularTaskById(long id);

    void saveNewDatelessTask(DatelessTask datelessTask);

    List<DatelessTask> getAllDatelessTasks();

    void updateDatelessTask(DatelessTask datelessTask);

    void deleteDatelessTask(DatelessTask datelessTask);

    void saveNewLog(Log log);

    List<Log> getAllLogs();

    PrefEntity getPrefByTitle(String title);

    void updatePref(PrefEntity pref);
}
