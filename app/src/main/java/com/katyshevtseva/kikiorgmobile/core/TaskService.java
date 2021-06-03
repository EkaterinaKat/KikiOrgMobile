package com.katyshevtseva.kikiorgmobile.core;


import com.katyshevtseva.kikiorgmobile.core.dao.KomDao;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.Date;
import java.util.List;


public class TaskService {
    private KomDao komDao;

    TaskService(KomDao komDao) {
        this.komDao = komDao;
    }

    public void saveNewIrregularTask(String title, String desc, Date date) {
        IrregularTask task = new IrregularTask();
        task.setTitle(title);
        task.setDesc(desc);
        task.setDate(date);
        task.setDone(false);
        komDao.saveNewIrregularTask(task);
    }

    public void saveNewRegularTask(String title, String desc, PeriodType periodType, Date refDate, int period) {
        RegularTask task = new RegularTask();
        task.setTitle(title);
        task.setDesc(desc);
        task.setPeriodType(periodType);
        task.setRefDate(refDate);
        task.setPeriod(period);
        komDao.saveNewRegularTask(task);
    }

    public List<IrregularTask> getAllIrregularTasks() {
        return komDao.getAllIrregularTasks();
    }

    public List<RegularTask> getAllRegularTasks() {
        return komDao.getAllRegularTasks();
    }
}
