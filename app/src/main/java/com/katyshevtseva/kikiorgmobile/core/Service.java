package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.beforeIgnoreTime;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.Log.Action;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    public static Service INSTANCE;
    private final KomDao komDao;

    public static void init(Context context) {
        INSTANCE = new Service(context);
    }

    private Service(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public void saveDatelessTask(DatelessTask existing, String title) {
        if (existing == null) {
            DatelessTask task = new DatelessTask();
            task.setTitle(title);
            komDao.saveNew(task);
            saveLog(Action.CREATION, task);
        } else {
            existing.setTitle(title);
            komDao.update(existing);
            saveLog(Action.EDITING, existing);
        }
    }

    public List<DatelessTask> getAllDatelessTasks() {
        return komDao.getAllDatelessTasks().stream()
                .sorted(Comparator.comparing(DatelessTask::getId)).collect(Collectors.toList());
    }

    public int countDatelessTasks() {
        return komDao.getAllDatelessTasks().size();
    }

    public void deleteTask(DatelessTask datelessTask) {
        komDao.delete(datelessTask);
        saveLog(Action.DELETION, datelessTask);
    }

    public void moveDatelessTaskToEnd(DatelessTask datelessTask) {
        saveDatelessTask(null, datelessTask.getTitle());
        deleteTask(datelessTask);
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

    public void saveLog(Action action, RegularTask regularTask, String additionalInfo) {
        saveLog(action, Log.Subject.REGULAR_TASK, regularTask.getLogTaskDesk() + "\n" + additionalInfo);
    }

    public void saveLog(Action action, IrregularTask irregularTask, String additionalInfo) {
        saveLog(action, Log.Subject.IRREGULAR_TASK, irregularTask.getLogTaskDesk() + "\n" + additionalInfo);
    }

    public void saveLog(Action action, RegularTask regularTask) {
        saveLog(action, Log.Subject.REGULAR_TASK, regularTask.getLogTaskDesk());
    }

    public void saveLog(Action action, IrregularTask irregularTask) {
        saveLog(action, Log.Subject.IRREGULAR_TASK, irregularTask.getLogTaskDesk());
    }

    public void saveLog(Action action, DatelessTask datelessTask) {
        saveLog(action, Log.Subject.DATELESS_TASK, datelessTask.getLogTaskDesk());
    }

    public void saveLog(Action action, Log.Subject subject, String desc) {
        komDao.saveNew(new Log(new Date(), action, subject, desc));
    }

    public List<Log> getLogs() {
        return komDao.getAllLogs().stream().sorted(Comparator.comparing(Log::getId).reversed()).collect(Collectors.toList());
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
