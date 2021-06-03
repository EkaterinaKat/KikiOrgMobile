package com.katyshevtseva.kikiorgmobile.core;


import com.katyshevtseva.kikiorgmobile.core.dao.IrregularTaskDao;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;

import java.util.Date;
import java.util.List;


public class TaskService {
    private IrregularTaskDao irregularTaskDao;

    TaskService(IrregularTaskDao irregularTaskDao) {
        this.irregularTaskDao = irregularTaskDao;
    }

    public void saveNewIrregularTask(String title, String desc, Date date) {
        IrregularTask task = new IrregularTask();
        task.setTitle(title);
        task.setDesc(desc);
        task.setDate(date);
        task.setDone(false);
        irregularTaskDao.saveNewIrregularTask(task);
    }

    public void saveNewRegularTask(String title, String desc, PeriodType periodType, Date startingDate, int period) {
        System.out.println(title + " " + desc + " " + startingDate + " " + period);
    }

    public List<IrregularTask> getAllIrregularTasks() {
        return irregularTaskDao.getAllIrregularTasks();
    }
}
