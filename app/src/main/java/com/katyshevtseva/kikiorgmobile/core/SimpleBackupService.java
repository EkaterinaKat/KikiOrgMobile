package com.katyshevtseva.kikiorgmobile.core;

import android.util.Log;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

class SimpleBackupService {
    private static final String tag = "KikiBackup";
    private final KomDao komDao;

    SimpleBackupService(KomDao komDao) {
        this.komDao = komDao;
    }

    void execute() {
        Log.i(tag, "*** START ***");
        Log.i(tag, "*** REGULAR TASKS ***");
        for (RegularTask regularTask : komDao.getAllRegularTasks()) {
            Log.i(tag, regularTask.getBackupString());
        }

        Log.i(tag, "*** IRREGULAR TASKS ***");
        for (IrregularTask irregularTask : komDao.getAllIrregularTasks()) {
            Log.i(tag, irregularTask.getBackupString());
        }

        Log.i(tag, "--- END ---");
    }
}
