package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.core.DateUtils.beforeIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.core.DateUtils.containsIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.core.DateUtils.getDateString;
import static com.katyshevtseva.kikiorgmobile.core.DateUtils.getProperDate;
import static com.katyshevtseva.kikiorgmobile.core.DateUtils.removeIgnoreTime;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.Log.Action;
import com.katyshevtseva.kikiorgmobile.core.model.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.core.model.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private final KomDao komDao;

    public Service(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public void saveIrregularTask(IrregularTask existing, String title, String desc, TimeOfDay timeOfDay, Date date) {
        if (existing == null) {
            IrregularTask task = new IrregularTask();
            task.setTitle(title);
            task.setDesc(desc);
            task.setDate(date);
            task.setDone(false);
            task.setTimeOfDay(timeOfDay);
            komDao.saveNewIrregularTask(task);
            saveLog(Action.CREATION, task);
        } else {
            existing.setTitle(title);
            existing.setDesc(desc);
            existing.setTimeOfDay(timeOfDay);
            existing.setDate(date);
            komDao.updateIrregularTask(existing);
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
            komDao.saveNewRegularTask(task);
            saveLog(Action.CREATION, task);
        } else {
            existing.setTitle(title);
            existing.setDesc(desc);
            existing.setTimeOfDay(timeOfDay);
            existing.setPeriodType(periodType);
            existing.setPeriod(period);
            existing.setDates(dates);
            komDao.updateRegularTask(existing);
            saveLog(Action.EDITING, existing);
        }
    }

    public void saveDatelessTask(DatelessTask existing, String title) {
        if (existing == null) {
            DatelessTask task = new DatelessTask();
            task.setTitle(title);
            komDao.saveNewDatelessTask(task);
            saveLog(Action.CREATION, task);
        } else {
            existing.setTitle(title);
            komDao.updateDatelessTask(existing);
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

    public List<IrregularTask> getNotDoneIrregularTasks(String s) {
        return komDao.getAllIrregularTasks().stream()
                .filter(task -> !task.isDone())
                .filter(task -> taskFilter(task, s))
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    private boolean taskFilter(Task task, String s) {
        s = s == null ? null : s.toLowerCase();
        return isEmpty(s) || (task.getTitle().toLowerCase().contains(s) || task.getDesc().toLowerCase().contains(s));
    }

    public List<IrregularTask> getDoneIrregularTasks() {
        return komDao.getAllIrregularTasks().stream()
                .filter(IrregularTask::isDone)
                .sorted(Comparator.comparing(task -> task.getTitle().toLowerCase()))
                .collect(Collectors.toList());
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
        komDao.updateRegularTask(regularTask);
        saveLog(Action.ARCHIVATION, regularTask);
    }

    public void resumeTask(RegularTask regularTask) {
        regularTask.setArchived(false);
        komDao.updateRegularTask(regularTask);
        saveLog(Action.RESUME, regularTask);
    }

    @Deprecated
    public void returnToWorkTask(IrregularTask irregularTask, Date date) {
        irregularTask.setDone(false);
        irregularTask.setDate(date);
        komDao.updateIrregularTask(irregularTask);
    }

    public void deleteTask(IrregularTask irregularTask) {
        komDao.deleteIrregularTask(irregularTask);
        saveLog(Action.DELETION, irregularTask);
    }

    public void deleteTask(DatelessTask datelessTask) {
        komDao.deleteDatelessTask(datelessTask);
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

        tasks.addAll(komDao.getIrregularTasksByDate(date).stream()
                .filter(irregularTask -> !irregularTask.isDone()).collect(Collectors.toList()));
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

    @Deprecated
    public void done(IrregularTask irregularTask) {
        irregularTask.setDone(true);
        komDao.updateIrregularTask(irregularTask);
        saveLog(Action.COMPLETION, irregularTask);
    }

    public void done(RegularTask regularTask, Date date) {
        if (!containsIgnoreTime(regularTask.getDates(), date))
            throw new RuntimeException();

        regularTask.getDates().remove(date);
        removeIgnoreTime(regularTask.getDates(), date);
        regularTask.getDates().add(shiftDate(regularTask, date));

        komDao.updateRegularTask(regularTask);
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
        komDao.updateIrregularTask(irregularTask);
        saveLog(Action.RESCHEDULE, irregularTask,
                String.format("Reschedule from %s to %s", getDateString(initDate), getDateString(date)));
    }

    public void rescheduleForOneDay(RegularTask regularTask, Date date, boolean shiftAllCycle) {
        rescheduleToCertainDate(regularTask, date,
                DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, 1), shiftAllCycle);
    }

    public void rescheduleToCertainDate(RegularTask regularTask, Date initDate, Date targetDate, boolean shiftAllCycle) {
        if (shiftAllCycle) {
            int diffBetweenDates = DateUtils.countNumberOfDaysBetweenDates(initDate, targetDate);
            List<Date> newDates = regularTask.getDates().stream()
                    .map(date -> DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, diffBetweenDates))
                    .collect(Collectors.toList());
            regularTask.setDates(newDates);
            komDao.updateRegularTask(regularTask);
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
            if (!irregularTask.isDone() && beforeIgnoreTime(irregularTask.getDate(), getProperDate()))
                return true;
        }
        for (RegularTask regularTask : komDao.getAllRegularTasks()) {
            if (!regularTask.isArchived()) {
                for (Date date : regularTask.getDates()) {
                    if (beforeIgnoreTime(date, getProperDate()))
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
        komDao.saveNewLog(new Log(new Date(), action, subject, desc));
    }

    public List<Log> getLogs() {
        return komDao.getAllLogs().stream().sorted(Comparator.comparing(Log::getId)).collect(Collectors.toList());
    }
}
