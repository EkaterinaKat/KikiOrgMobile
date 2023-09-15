package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.OptionalTask;
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

    void saveNew(OptionalTask irregularTask);

    List<OptionalTask> getAllOptionalTasks();

    void update(OptionalTask optionalTask);

    void delete(OptionalTask optionalTask);
}
