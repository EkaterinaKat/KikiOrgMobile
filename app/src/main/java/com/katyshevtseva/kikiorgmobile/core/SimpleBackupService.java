package com.katyshevtseva.kikiorgmobile.core;

import android.util.Log;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

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

//        Log.i(tag, "*** IRREGULAR TASKS ***");
//        List<IrregularTask> currentTasks = service.getIrregularTasks(null);
//        for (IrregularTask irregularTask : currentTasks) {
//            Log.i(tag, irregularTask.toString());
//        }

        Log.i(tag, "*** DATELESS TASKS ***");
        for (DatelessTask datelessTask : komDao.getAllDatelessTasks()) {
            Log.i(tag, datelessTask.toString());
        }

        Log.i(tag, "*** LOGS ***");
        for (com.katyshevtseva.kikiorgmobile.core.model.Log log : service.getLogs()) {
            Log.i(tag, log.getFullDesc());
        }

        Log.i(tag, "--- END ---");
    }
}
