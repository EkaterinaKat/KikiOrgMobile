package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatelessTaskService {
    public static DatelessTaskService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new DatelessTaskService(komDao);
    }

    private DatelessTaskService(KomDao komDao) {
        this.komDao = komDao;
    }

    public void saveDatelessTask(DatelessTask existing, String title) {
        if (existing == null) {
            DatelessTask task = new DatelessTask();
            task.setTitle(title);
            komDao.saveNew(task);
            LogService.INSTANCE.saveLog(Log.Action.CREATION, task);
        } else {
            existing.setTitle(title);
            komDao.update(existing);
            LogService.INSTANCE.saveLog(Log.Action.EDITING, existing);
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
        LogService.INSTANCE.saveLog(Log.Action.DELETION, datelessTask);
    }

    public void moveDatelessTaskToEnd(DatelessTask datelessTask) {
        saveDatelessTask(null, datelessTask.getTitle());
        deleteTask(datelessTask);
    }
}
