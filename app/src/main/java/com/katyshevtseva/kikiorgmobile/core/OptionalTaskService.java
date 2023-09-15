package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.OptionalTask;

import java.util.List;

public class OptionalTaskService {
    public static OptionalTaskService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new OptionalTaskService(komDao);
    }

    private OptionalTaskService(KomDao komDao) {
        this.komDao = komDao;
    }

    public void save(OptionalTask existing, String title) {
        if (existing == null) {
            existing = new OptionalTask();
        }
        existing.setTitle(title);

        if (existing.getId() == 0) {
            komDao.saveNew(existing);
            LogService.INSTANCE.saveLog(Log.Action.CREATION, existing);
        } else {
            komDao.update(existing);
            LogService.INSTANCE.saveLog(Log.Action.EDITING, existing);
        }
    }

    public List<OptionalTask> getOptionalTasks() {
        return komDao.getAllOptionalTasks();
    }

    public void delete(OptionalTask optionalTask) {
        komDao.delete(optionalTask);
        LogService.INSTANCE.saveLog(Log.Action.DELETION, optionalTask);
    }
}
