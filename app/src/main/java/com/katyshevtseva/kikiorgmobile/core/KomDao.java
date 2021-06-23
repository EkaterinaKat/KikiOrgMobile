package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
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
}
