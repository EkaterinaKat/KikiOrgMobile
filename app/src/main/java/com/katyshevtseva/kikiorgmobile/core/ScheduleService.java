package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.TimeUtils.beforeIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.utils.TimeUtils.equalsIgnoreTime;
import static com.katyshevtseva.kikiorgmobile.utils.TimeUtils.plus;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.core.model.Setting;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.db.KomDaoImpl;
import com.katyshevtseva.kikiorgmobile.utils.Time;
import com.katyshevtseva.kikiorgmobile.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ScheduleService {
    private static final Time startOfDay = new Time(0, 0);
    private static final Time endOfDay = new Time(23, 59);
    public static ScheduleService INSTANCE;
    private final KomDao komDao;

    public static void init(Context context) {
        INSTANCE = new ScheduleService(context);
    }

    private ScheduleService(Context context) {
        this.komDao = new KomDaoImpl(context);
    }

    public Schedule getSchedule(Date date) throws Exception {
        List<Task> tasks = Service.INSTANCE.getTasksForMainList(date);
        List<Setting> settings = new ArrayList<>();
        List<Task> notScheduledTasks = new ArrayList<>();

        for (Task task : tasks) {
            Setting setting = getSettingOrNullByTask(task, date);
            if (setting != null)
                settings.add(setting);
            else
                notScheduledTasks.add(task);
        }

        return formSchedule(settings, notScheduledTasks, date);
    }

    private Schedule formSchedule(List<Setting> settings, List<Task> notScheduledTasks, Date date) {
        settings = settings.stream().sorted(beginTimeComparator).collect(Collectors.toList());
        Time activityStart = PrefService.INSTANCE.getActivityStart();
        Time activityEnd = PrefService.INSTANCE.getActivityEnd();
        Date today = new Date();
        List<Interval> intervals = new ArrayList<>();
        String warnings = "";

        // Создаем интервалы сна и выполнения задач
        if (morningSleepIntervalIsNeeded(date, today, activityStart)) {
            intervals.add(Interval.sleepInterval(startOfDay, activityStart));
        }
        settings.forEach(setting -> intervals.add(getIntervalBySetting(setting)));
        if (eveningSleepIntervalIsNeeded(date, today)) {
            intervals.add(Interval.sleepInterval(activityEnd, endOfDay));
        }

        // Добавляем пустые интервалы и проверяем на предупреждение
        List<Interval> resultIntervals = new ArrayList<>();
        if (!intervals.isEmpty()) {
            Time prevIntervalEnd = intervals.get(0).getStart();
            for (Interval interval : intervals) {
                int comparisonResult = prevIntervalEnd.compareTo(interval.getStart());

                if (comparisonResult > 0) { //prevIntervalEnd > interval.start
                    warnings += (interval.getTitle() + " накладывается на предыдущий интервал\n");
                } else if (comparisonResult < 0) { //prevIntervalEnd < interval.start
                    resultIntervals.add(Interval.emptyInterval(prevIntervalEnd, interval.getStart()));
                }
                resultIntervals.add(interval);
                prevIntervalEnd = interval.getEnd();
            }
        }

        return new Schedule(notScheduledTasks, resultIntervals, warnings.equals("") ? null : warnings);
    }

    private boolean morningSleepIntervalIsNeeded(Date date, Date today, Time activityStart) {
        if (equalsIgnoreTime(date, today)) {
            return TimeUtils.afterNow(activityStart);
        }
        return !beforeIgnoreTime(date, today);
    }

    private boolean eveningSleepIntervalIsNeeded(Date date, Date today) {
        return !beforeIgnoreTime(date, today);
    }

    private Interval getIntervalBySetting(Setting setting) {
        Time absoluteBeginTime = getAbsoluteBeginTime(setting);
        return Interval.taskInterval(getTaskBySetting(setting), absoluteBeginTime,
                TimeUtils.plus(absoluteBeginTime, setting.getDuration()));
    }

    private Task getTaskBySetting(Setting setting) {
        switch (setting.getTaskType()) {
            case REGULAR:
                return komDao.getRegularTaskById(setting.getTaskId());
            case IRREGULAR:
                return komDao.getIrregularTaskById(setting.getTaskId());
        }
        throw new RuntimeException();
    }

    private final Comparator<Setting> beginTimeComparator = Comparator.comparing(this::getAbsoluteBeginTime);

    public Time getAbsoluteBeginTime(Setting setting) {
        return setting.isAbsoluteWobs() ?
                setting.getBeginTime()
                : plus(PrefService.INSTANCE.getActivityStart(), setting.getBeginTime());
    }

    private Setting getSettingOrNullByTask(Task task, Date date) throws Exception {
        OneDaySetting oneDaySetting = OneDaySettingService.INSTANCE.getSettingOrNull(task, date);
        if (oneDaySetting != null) {
            return oneDaySetting;
        }

        if (task.getType() == TaskType.REGULAR) {
            RtSetting setting = RtSettingService.INSTANCE.getRtSettingOrNull((RegularTask) task);
            if (setting != null && setting.getDuration() != null && setting.getBeginTime() != null)
                return setting;
        }
        return null;
    }

    public Setting getAnySettingByTaskOrNull(Task task, Date date) throws Exception {
        OneDaySetting oneDaySetting = OneDaySettingService.INSTANCE.getSettingOrNull(task, date);
        if (oneDaySetting != null) {
            return oneDaySetting;
        }

        if (task.getType() == TaskType.REGULAR) {
            return RtSettingService.INSTANCE.getRtSettingOrNull((RegularTask) task);
        }

        return null;
    }

    @Data
    @AllArgsConstructor
    public static class Schedule {
        private final List<Task> notScheduledTasks;
        private final List<Interval> intervals;
        private final String warning;
    }
}
