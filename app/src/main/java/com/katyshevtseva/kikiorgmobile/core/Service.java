package com.katyshevtseva.kikiorgmobile.core;


import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Service {
    private KomDao komDao;

    public Service(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public void saveNewIrregularTask(IrregularTask existing, String title, String desc, TimeOfDay timeOfDay, Date date) {
        if (existing == null) {
            IrregularTask task = new IrregularTask();
            task.setTitle(title);
            task.setDesc(desc);
            task.setDate(date);
            task.setDone(false);
            task.setTimeOfDay(timeOfDay);
            komDao.saveNewIrregularTask(task);
        } else {
            existing.setTitle(title);
            existing.setDesc(desc);
            existing.setTimeOfDay(timeOfDay);
            existing.setDate(date);
            komDao.updateIrregularTask(existing);
        }
    }

    public void saveNewRegularTask(RegularTask existing, String title, String desc, TimeOfDay timeOfDay,
                                   PeriodType periodType, List<Date> dates, int period) {
        if (existing == null) {
            RegularTask task = new RegularTask();
            task.setTitle(title);
            task.setDesc(desc);
            task.setTimeOfDay(timeOfDay);
            task.setPeriodType(periodType);
            task.setPeriod(period);
            task.setDates(dates);
            komDao.saveNewRegularTask(task);
        } else {
            existing.setTitle(title);
            existing.setDesc(desc);
            existing.setTimeOfDay(timeOfDay);
            existing.setPeriodType(periodType);
            existing.setPeriod(period);
            existing.setDates(dates);
            komDao.updateRegularTask(existing);
        }
    }

    public List<RegularTask> getNotArchivedRegularTasks() {
        List<RegularTask> tasks = new ArrayList<>();
        for (RegularTask task : komDao.getAllRegularTasks()) {
            if (!task.isArchived())
                tasks.add(task);
        }
        return tasks;
    }

    public List<RegularTask> getArchivedRegularTasks() {
        List<RegularTask> tasks = new ArrayList<>();
        for (RegularTask task : komDao.getAllRegularTasks()) {
            if (task.isArchived())
                tasks.add(task);
        }
        return tasks;
    }

    public List<IrregularTask> getNotDoneIrregularTasks() {
        List<IrregularTask> tasks = new ArrayList<>();
        for (IrregularTask task : komDao.getAllIrregularTasks()) {
            if (!task.isDone())
                tasks.add(task);
        }
        return tasks;
    }

    public void archiveTask(RegularTask regularTask) {
        regularTask.setArchived(true);
        komDao.updateRegularTask(regularTask);
    }

    public void resumeTask(RegularTask regularTask) {
        regularTask.setArchived(false);
        komDao.updateRegularTask(regularTask);
    }

    public void deleteTask(IrregularTask irregularTask) {
        komDao.deleteIrregularTask(irregularTask);
    }

    public Task findTask(TaskType taskType, long id) {
        switch (taskType) {
            case REGULAR:
                return komDao.getRegularTaskById(id);
            case IRREGULAR:
                return komDao.getIrregularTaskById(id);
        }
        return null;
    }
}
