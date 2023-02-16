package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.beforeIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.containsIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.countNumberOfDaysBetweenDates;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.removeIgnoreTime;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.Log.Action;
import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    public static Service INSTANCE;
    private final KomDao komDao;

    public static void init(Context context) {
        INSTANCE = new Service(context);
    }

    private Service(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public void saveIrregularTask(IrregularTask existing, String title, String desc, TimeOfDay timeOfDay, Date date) {
        if (existing == null) {
            existing = new IrregularTask();
        }
        existing.setTitle(title);
        existing.setDesc(desc);
        existing.setTimeOfDay(timeOfDay);
        existing.setDate(date);

        if (existing.getId() == 0) {
            komDao.saveNew(existing);
            saveLog(Action.CREATION, existing);
        } else {
            komDao.update(existing);
            saveLog(Action.EDITING, existing);
        }
    }

    public void saveRegularTask(RegularTask existing, String title, String desc, TimeOfDay timeOfDay,
                                PeriodType periodType, List<Date> dates, int period) {
        if (existing == null) {
            RegularTask task = new RegularTask();
            task.setTitle(title);
            task.setDesc(desc);
            task.setTimeOfDay(timeOfDay);
            task.setPeriodType(periodType);
            task.setPeriod(period);
            task.setDates(dates);
            komDao.saveNew(task);
            saveLog(Action.CREATION, task);
        } else {
            existing.setTitle(title);
            existing.setDesc(desc);
            existing.setTimeOfDay(timeOfDay);
            existing.setPeriodType(periodType);
            existing.setPeriod(period);
            existing.setDates(dates);
            komDao.update(existing);
            saveLog(Action.EDITING, existing);
        }
    }

    public void saveDatelessTask(DatelessTask existing, String title) {
        if (existing == null) {
            DatelessTask task = new DatelessTask();
            task.setTitle(title);
            komDao.saveNew(task);
            saveLog(Action.CREATION, task);
        } else {
            existing.setTitle(title);
            komDao.update(existing);
            saveLog(Action.EDITING, existing);
        }
    }

    public List<RegularTask> getNotArchivedRegularTasks(String s) {
//        new SimpleBackupService(komDao, this).execute();
        return komDao.getAllRegularTasks().stream()
                .filter(task -> !task.isArchived())
                .filter(task -> taskFilter(task, s))
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    public List<RegularTask> getArchivedRegularTasks() {
        return komDao.getAllRegularTasks().stream()
                .filter(RegularTask::isArchived)
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<IrregularTask> getIrregularTasks(String s) {
        return komDao.getAllIrregularTasks().stream()
                .filter(task -> taskFilter(task, s))
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    private boolean taskFilter(Task task, String s) {
        s = s == null ? null : s.toLowerCase();
        return isEmpty(s) || (task.getTitle().toLowerCase().contains(s) || task.getDesc().toLowerCase().contains(s));
    }

    public List<DatelessTask> getAllDatelessTasks() {
        return komDao.getAllDatelessTasks().stream()
                .sorted(Comparator.comparing(DatelessTask::getId)).collect(Collectors.toList());
    }

    public int countDatelessTasks() {
        return komDao.getAllDatelessTasks().size();
    }

    public void archiveTask(RegularTask regularTask) {
        regularTask.setArchived(true);
        komDao.update(regularTask);
        saveLog(Action.ARCHIVATION, regularTask);
    }

    public void resumeTask(RegularTask regularTask) {
        regularTask.setArchived(false);
        komDao.update(regularTask);
        saveLog(Action.RESUME, regularTask);
    }

    public void deleteTask(IrregularTask irregularTask) {
        komDao.delete(irregularTask);
        saveLog(Action.DELETION, irregularTask);
    }

    public void deleteTask(DatelessTask datelessTask) {
        komDao.delete(datelessTask);
        saveLog(Action.DELETION, datelessTask);
    }

    public void moveDatelessTaskToEnd(DatelessTask datelessTask) {
        saveDatelessTask(null, datelessTask.getTitle());
        deleteTask(datelessTask);
    }

    public Task findTask(TaskType taskType, long id) {
        switch (taskType) {
            case REGULAR:
                return komDao.getRegularTaskById(id);
            case IRREGULAR:
                return komDao.getIrregularTaskById(id);
        }
        return null;
    }

    public List<Task> getTasksForMainList(Date date) {
        List<Task> tasks = new ArrayList<>();

        tasks.addAll(komDao.getIrregularTasksByDate(date));
        tasks.addAll(komDao.getRegularTasksByDate(date).stream()
                .filter(regularTask -> !regularTask.isArchived()).collect(Collectors.toList()));

        tasks.sort((t1, t2) -> {
            int result = t1.getTimeOfDay().getCode().compareTo(t2.getTimeOfDay().getCode());
            if (result != 0)
                return result;
            return t1.getTitle().compareToIgnoreCase(t2.getTitle());
        });

        return tasks;
    }

    public void done(IrregularTask irregularTask) {
        komDao.delete(irregularTask);
        saveLog(Action.COMPLETION, irregularTask);
    }

    public void done(RegularTask regularTask, Date date) {
        if (!containsIgnoreTime(regularTask.getDates(), date))
            throw new RuntimeException();

        regularTask.getDates().remove(date);
        removeIgnoreTime(regularTask.getDates(), date);
        regularTask.getDates().add(shiftDate(regularTask, date));

        komDao.update(regularTask);
        saveLog(Action.COMPLETION, regularTask);
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

    public void rescheduleForOneDay(IrregularTask irregularTask) {
        rescheduleToCertainDate(irregularTask,
                DateUtils.shiftDate(irregularTask.getDate(), DateUtils.TimeUnit.DAY, 1));
    }

    public void rescheduleToCertainDate(IrregularTask irregularTask, Date date) {
        Date initDate = irregularTask.getDate();
        irregularTask.setDate(date);
        komDao.update(irregularTask);
        saveLog(Action.RESCHEDULE, irregularTask,
                String.format("Reschedule from %s to %s", getDateString(initDate), getDateString(date)));
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
            done(regularTask, initDate);
            saveIrregularTask(null,
                    String.format("%s (перенесено с %s)", regularTask.getTitle(), getDateString(initDate)),
                    regularTask.getDesc(),
                    regularTask.getTimeOfDay(),
                    targetDate);
        }
        saveLog(Action.RESCHEDULE, regularTask,
                String.format("Reschedule from %s to %s \n%s", getDateString(initDate), getDateString(targetDate),
                        shiftAllCycle ? "shift all cycle" : "shift single iteration"));
    }

    public boolean overdueTasksExist() {
        for (IrregularTask irregularTask : komDao.getAllIrregularTasks()) {
            if (beforeIgnoreTime(irregularTask.getDate(), new Date()))
                return true;
        }
        for (RegularTask regularTask : komDao.getAllRegularTasks()) {
            if (!regularTask.isArchived()) {
                for (Date date : regularTask.getDates()) {
                    if (beforeIgnoreTime(date, new Date()))
                        return true;
                }
            }
        }
        return false;
    }

    private void saveLog(Action action, RegularTask regularTask, String additionalInfo) {
        saveLog(action, Log.Subject.REGULAR_TASK, regularTask.getLogTaskDesk() + "\n" + additionalInfo);
    }

    private void saveLog(Action action, IrregularTask irregularTask, String additionalInfo) {
        saveLog(action, Log.Subject.IRREGULAR_TASK, irregularTask.getLogTaskDesk() + "\n" + additionalInfo);
    }

    private void saveLog(Action action, RegularTask regularTask) {
        saveLog(action, Log.Subject.REGULAR_TASK, regularTask.getLogTaskDesk());
    }

    private void saveLog(Action action, IrregularTask irregularTask) {
        saveLog(action, Log.Subject.IRREGULAR_TASK, irregularTask.getLogTaskDesk());
    }

    private void saveLog(Action action, DatelessTask datelessTask) {
        saveLog(action, Log.Subject.DATELESS_TASK, datelessTask.getLogTaskDesk());
    }

    private void saveLog(Action action, Log.Subject subject, String desc) {
        komDao.saveNew(new Log(new Date(), action, subject, desc));
    }

    public List<Log> getLogs() {
        return komDao.getAllLogs().stream().sorted(Comparator.comparing(Log::getId).reversed()).collect(Collectors.toList());
    }

    public IrregularTask makeIrregularTaskFromRegular(long regularTaskId) {
        RegularTask regularTask = komDao.getRegularTaskById(regularTaskId);
        IrregularTask task = new IrregularTask();
        task.setTitle(regularTask.getTitle());
        task.setDesc(regularTask.getDesc());
        task.setDate(new Date());
        task.setTimeOfDay(TimeOfDay.AFTERNOON);
        return task;

    }
}
