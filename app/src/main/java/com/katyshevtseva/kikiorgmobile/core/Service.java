package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.beforeIgnoreTime;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    public static Service INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new Service(komDao);
    }

    private Service(KomDao komDao) {
        this.komDao = komDao;
    }

    public List<Task> getTasksForMainList(Date date) {
        List<Task> tasks = new ArrayList<>();

        tasks.addAll(komDao.getIrregularTasksByDate(date));
        tasks.addAll(komDao.getRegularTasksByDate(date).stream()
                .filter(regularTask -> !regularTask.isArchived()).collect(Collectors.toList()));

        return tasks;
    }

    public boolean overdueTasksExist() {
        for (IrregularTask irregularTask : komDao.getAllIrregularTasks()) {
            if (beforeIgnoreTime(irregularTask.getDate(), new Date()))
                return true;
        }
        for (RegularTask regularTask : komDao.getAllRegularTasks()) {
            if (!regularTask.isArchived()) {
                for (Date date : regularTask.getDates()) {
                    if (beforeIgnoreTime(date, new Date()))
                        return true;
                }
            }
        }
        return false;
    }

    public IrregularTask makeIrregularTaskFromRegular(long regularTaskId) {
        RegularTask regularTask = komDao.getRegularTaskById(regularTaskId);
        IrregularTask task = new IrregularTask();
        task.setTitle(regularTask.getTitle());
        task.setDesc(regularTask.getDesc());
        task.setDate(new Date());
        task.setDuration(regularTask.getDuration());
        task.setBeginTime(ScheduleService.INSTANCE.getAbsoluteBeginTime(regularTask));
        return task;
    }
}
