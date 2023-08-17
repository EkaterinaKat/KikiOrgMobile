package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.containsIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.countNumberOfDaysBetweenDates;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.removeIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.utils.GeneralUtil.taskFilter;

import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskUrgency;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RegularTaskService {
    public static RegularTaskService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new RegularTaskService(komDao);
    }

    private RegularTaskService(KomDao komDao) {
        this.komDao = komDao;
    }

    public RegularTask findById(long id) {
        return komDao.getRegularTaskById(id);
    }

    public void save(RegularTask existing, String title, String desc, TimeOfDay timeOfDay, PeriodType periodType,
                     List<Date> dates, int period) { //todo рефакторить
        if (existing == null) {
            RegularTask task = new RegularTask();
            task.setUrgency(TaskUrgency.LOW);
            task.setTitle(title);
            task.setDesc(desc);
            task.setPeriodType(periodType);
            task.setTimeOfDay(timeOfDay);
            task.setPeriod(period);
            task.setDates(dates);
            komDao.saveNew(task);
            LogService.INSTANCE.saveLog(Log.Action.CREATION, task);
        } else {
            existing.setTitle(title);
            existing.setDesc(desc);
            existing.setPeriodType(periodType);
            existing.setTimeOfDay(timeOfDay);
            existing.setPeriod(period);
            existing.setDates(dates);
            komDao.update(existing);
            LogService.INSTANCE.saveLog(Log.Action.EDITING, existing);
        }
    }

    public List<RegularTask> getNotArchivedRt(String s) {
        return komDao.getAllRegularTasks().stream()
                .filter(task -> !task.isArchived())
                .filter(task -> taskFilter(task, s))
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<RegularTask> getArchivedRt() {
        return komDao.getAllRegularTasks().stream()
                .filter(RegularTask::isArchived)
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void archive(RegularTask regularTask) {
        regularTask.setArchived(true);
        komDao.update(regularTask);
        LogService.INSTANCE.saveLog(Log.Action.ARCHIVATION, regularTask);
    }

    public void resume(RegularTask regularTask) {
        regularTask.setArchived(false);
        komDao.update(regularTask);
        LogService.INSTANCE.saveLog(Log.Action.RESUME, regularTask);
    }

    public void done(RegularTask regularTask, Date date) {
        if (!containsIgnoreTime(regularTask.getDates(), date))
            throw new RuntimeException();

        regularTask.getDates().remove(date);
        removeIgnoreTime(regularTask.getDates(), date);
        regularTask.getDates().add(shiftDate(regularTask, date));

        komDao.update(regularTask);
        LogService.INSTANCE.saveLog(Log.Action.COMPLETION, regularTask);
    }

    private Date shiftDate(RegularTask regularTask, Date date) {
        switch (regularTask.getPeriodType()) {
            case DAY:
                return DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, regularTask.getPeriod());
            case WEEK:
                return DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, regularTask.getPeriod() * 7);
            case MONTH:
                return DateUtils.shiftDate(date, DateUtils.TimeUnit.MONTH, regularTask.getPeriod());
            case YEAR:
                return DateUtils.shiftDate(date, DateUtils.TimeUnit.YEAR, regularTask.getPeriod());
        }
        throw new RuntimeException();
    }

    public void rescheduleForOneDay(RegularTask regularTask, Date date, boolean shiftAllCycle) {
        rescheduleToCertainDate(regularTask, date,
                DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, 1), shiftAllCycle);
    }

    public void rescheduleToCertainDate(RegularTask regularTask, Date initDate, Date targetDate, boolean shiftAllCycle) {
        if (shiftAllCycle) {
            int diffBetweenDates = countNumberOfDaysBetweenDates(initDate, targetDate);
            List<Date> newDates = regularTask.getDates().stream()
                    .map(date -> DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, diffBetweenDates))
                    .collect(Collectors.toList());
            regularTask.setDates(newDates);
            komDao.update(regularTask);
        } else {
            RegularTaskService.INSTANCE.done(regularTask, initDate);
            IrregularTaskService.INSTANCE.save(null,
                    regularTask.getTitle() + "*",
                    regularTask.getDesc(),
                    regularTask.getTimeOfDay(),
                    targetDate);
        }
        LogService.INSTANCE.saveLog(Log.Action.RESCHEDULE, regularTask,
                String.format("Reschedule from %s to %s \n%s", getDateString(initDate), getDateString(targetDate),
                        shiftAllCycle ? "shift all cycle" : "shift single iteration"));
    }
}
