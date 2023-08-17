package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.utils.GeneralUtil.taskFilter;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskUrgency;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class IrregularTaskService {
    public static IrregularTaskService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new IrregularTaskService(komDao);
    }

    private IrregularTaskService(KomDao komDao) {
        this.komDao = komDao;
    }

    public IrregularTask findById(long id) {
        return komDao.getIrregularTaskById(id);
    }

    public void save(IrregularTask existing, String title, String desc, TimeOfDay timeOfDay, Date date) {
        if (existing == null) {
            existing = new IrregularTask();
            existing.setUrgency(TaskUrgency.LOW);
        }
        existing.setTitle(title);
        existing.setTimeOfDay(timeOfDay);
        existing.setDesc(desc);
        existing.setDate(date);

        if (existing.getId() == 0) {
            komDao.saveNew(existing);
            LogService.INSTANCE.saveLog(Log.Action.CREATION, existing);
        } else {
            komDao.update(existing);
            LogService.INSTANCE.saveLog(Log.Action.EDITING, existing);
        }
    }

    public List<IrregularTask> getIrregularTasks(String s) {
        return komDao.getAllIrregularTasks().stream()
                .filter(task -> taskFilter(task, s))
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void delete(IrregularTask irregularTask) {
        komDao.delete(irregularTask);
        LogService.INSTANCE.saveLog(Log.Action.DELETION, irregularTask);
    }

    public void done(IrregularTask irregularTask) {
        komDao.delete(irregularTask);
        LogService.INSTANCE.saveLog(Log.Action.COMPLETION, irregularTask);
    }

    public void rescheduleForOneDay(IrregularTask irregularTask) {
        rescheduleToCertainDate(irregularTask,
                DateUtils.shiftDate(irregularTask.getDate(), DateUtils.TimeUnit.DAY, 1));
    }

    public void rescheduleToCertainDate(IrregularTask irregularTask, Date date) {
        Date initDate = irregularTask.getDate();
        irregularTask.setDate(date);
        komDao.update(irregularTask);
        LogService.INSTANCE.saveLog(Log.Action.RESCHEDULE, irregularTask,
                String.format("Reschedule from %s to %s", getDateString(initDate), getDateString(date)));
    }
}
