package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

class SimpleBackupService {
    private KomDao komDao;

    SimpleBackupService(KomDao komDao) {
        this.komDao = komDao;
    }

    void execute() {
        System.out.println("*** REGULAR TASKS ***");
        for (RegularTask regularTask : komDao.getAllRegularTasks()) {
            System.out.println(regularTask);
        }

        System.out.println("*** IRREGULAR TASKS ***");
        for (IrregularTask irregularTask : komDao.getAllIrregularTasks()) {
            System.out.println(irregularTask);
        }

        System.out.println("*** DATELESS TASKS ***");
        for (DatelessTask datelessTask : komDao.getAllDatelessTasks()) {
            System.out.println(datelessTask);
        }

        System.out.println("--- END ---");
    }
}
