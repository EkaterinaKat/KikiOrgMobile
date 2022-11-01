package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.List;

class SimpleBackupService {
    private KomDao komDao;
    private Service service;

    SimpleBackupService(KomDao komDao, Service service) {
        this.komDao = komDao;
        this.service = service;
    }

    void execute() {
        System.out.println("*** START ***");
        System.out.println("*** REGULAR TASKS ***");
        for (RegularTask regularTask : komDao.getAllRegularTasks()) {
            System.out.println(regularTask);
        }

        System.out.println("*** CURRENT IRREGULAR TASKS ***");
        List<IrregularTask> currentTasks = service.getNotDoneIrregularTasks(null);
        for (IrregularTask irregularTask : currentTasks) {
            System.out.println(irregularTask);
        }

        System.out.println("*** FINISHED IRREGULAR TASKS ***");
        List<IrregularTask> doneTasks = service.getDoneIrregularTasks();
        for (IrregularTask irregularTask : doneTasks) {
            System.out.println(irregularTask);
            komDao.deleteIrregularTask(irregularTask);
        }

        System.out.println("*** DATELESS TASKS ***");
        for (DatelessTask datelessTask : komDao.getAllDatelessTasks()) {
            System.out.println(datelessTask);
        }

        System.out.println("--- END ---");
    }
}
