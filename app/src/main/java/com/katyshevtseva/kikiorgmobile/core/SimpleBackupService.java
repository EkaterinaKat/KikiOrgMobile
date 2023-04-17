package com.katyshevtseva.kikiorgmobile.core;

import android.util.Log;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        Log.i(tag, "*** DATELESS TASKS ***");
        for (DatelessTask datelessTask : komDao.getAllDatelessTasks()) {
            Log.i(tag, datelessTask.getBackupString());
        }

        Log.i(tag, "*** LOGS ***");
        List<com.katyshevtseva.kikiorgmobile.core.model.Log> logs = komDao.getAllLogs().stream()
                .sorted(Comparator.comparing(com.katyshevtseva.kikiorgmobile.core.model.Log::getId))
                .collect(Collectors.toList());

        for (com.katyshevtseva.kikiorgmobile.core.model.Log log : logs) {
            Log.i(tag, log.getBackupString());
        }

        if (logs.size() > 100) {
            int numOfLogsToDelete = logs.size() - 30;
            for (int i = 0; i < numOfLogsToDelete; i++) {
                komDao.delete(logs.get(i));
            }
            Log.i(tag, numOfLogsToDelete + " logs were deleted");
        }


        Log.i(tag, "--- END ---");
    }
}
