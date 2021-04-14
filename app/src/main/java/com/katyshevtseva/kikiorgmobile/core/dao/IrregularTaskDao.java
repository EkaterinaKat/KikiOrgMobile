package com.katyshevtseva.kikiorgmobile.core.dao;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;

import java.util.List;

public interface IrregularTaskDao {
    void saveNewIrregularTask(IrregularTask irregularTask);

    List<IrregularTask> getAllIrregularTasks();

    void updateIrregularTask(IrregularTask irregularTask);

    void deleteIrregularTask(IrregularTask irregularTask);
}
