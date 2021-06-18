package com.katyshevtseva.kikiorgmobile.core.dao;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.List;

public interface KomDao {
    void saveNewIrregularTask(IrregularTask irregularTask);

    List<IrregularTask> getAllIrregularTasks();

    void updateIrregularTask(IrregularTask irregularTask);

    void deleteIrregularTask(IrregularTask irregularTask);

    void saveNewRegularTask(RegularTask regularTask);

    List<RegularTask> getAllRegularTasks();

    void updateRegularTask(RegularTask regularTask);

    void deleteRegularTask(RegularTask regularTask);

    RegularTask findRegularTaskById(long id);

    IrregularTask findIrregularTaskById(long id);
}