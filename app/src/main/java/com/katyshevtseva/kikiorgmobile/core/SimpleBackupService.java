package com.katyshevtseva.kikiorgmobile.core;

import android.util.Log;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.List;

class SimpleBackupService {
    private static final String tag = "Backup";
    private final KomDao komDao;
    private final Service service;

    SimpleBackupService(KomDao komDao, Service service) {
        this.komDao = komDao;
        this.service = service;
    }

    void execute() {
        Log.i(tag, "*** START ***");
        Log.i(tag, "*** REGULAR TASKS ***");
        for (RegularTask regularTask : komDao.getAllRegularTasks()) {
            Log.i(tag, regularTask.toString());
        }

        Log.i(tag, "*** CURRENT IRREGULAR TASKS ***");
        List<IrregularTask> currentTasks = service.getNotDoneIrregularTasks(null);
        for (IrregularTask irregularTask : currentTasks) {
            Log.i(tag, irregularTask.toString());
        }

        Log.i(tag, "*** FINISHED IRREGULAR TASKS ***");
        List<IrregularTask> doneTasks = service.getDoneIrregularTasks();
        for (IrregularTask irregularTask : doneTasks) {
            Log.i(tag, irregularTask.toString());
            komDao.deleteIrregularTask(irregularTask);
        }

        Log.i(tag, "*** DATELESS TASKS ***");
        for (DatelessTask datelessTask : komDao.getAllDatelessTasks()) {
            Log.i(tag, datelessTask.toString());
        }

        Log.i(tag, "--- END ---");
    }
}
