package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LogService {
    public static LogService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new LogService(komDao);
    }

    private LogService(KomDao komDao) {
        this.komDao = komDao;
    }

    public void saveLog(Log.Action action, RegularTask regularTask, String additionalInfo) {
        saveLog(action, Log.Subject.REGULAR_TASK, regularTask.getLogTaskDesk() + "\n" + additionalInfo);
    }

    public void saveLog(Log.Action action, IrregularTask irregularTask, String additionalInfo) {
        saveLog(action, Log.Subject.IRREGULAR_TASK, irregularTask.getLogTaskDesk() + "\n" + additionalInfo);
    }

    public void saveLog(Log.Action action, RegularTask regularTask) {
        saveLog(action, Log.Subject.REGULAR_TASK, regularTask.getLogTaskDesk());
    }

    public void saveLog(Log.Action action, IrregularTask irregularTask) {
        saveLog(action, Log.Subject.IRREGULAR_TASK, irregularTask.getLogTaskDesk());
    }

    public void saveLog(Log.Action action, DatelessTask datelessTask) {
        saveLog(action, Log.Subject.DATELESS_TASK, datelessTask.getLogTaskDesk());
    }

    public void saveLog(Log.Action action, Log.Subject subject, String desc) {
        komDao.saveNew(new Log(new Date(), action, subject, desc));
    }

    public List<Log> getLogs() {
        return komDao.getAllLogs().stream().sorted(Comparator.comparing(Log::getId).reversed()).collect(Collectors.toList());
    }
}
