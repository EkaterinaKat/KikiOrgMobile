package com.katyshevtseva.kikiorgmobile.core;

import static com.katyshevtseva.kikiorgmobile.utils.TimeUtils.after;
import static com.katyshevtseva.kikiorgmobile.utils.TimeUtils.getNow;
import static com.katyshevtseva.kikiorgmobile.utils.TimeUtils.plus;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Setting;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
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
    public static ScheduleService INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new ScheduleService(komDao);
    }

    private ScheduleService(KomDao komDao) {
        this.komDao = komDao;
    }

    public Schedule getSchedule(Date date) throws Exception {
        List<Task> tasks = Service.INSTANCE.getTasksForMainList(date);
        List<Setting> settings = new ArrayList<>();
        List<Task> notScheduledTasks = new ArrayList<>();

        for (Task task : tasks) {
            Setting setting = getCompleteSettingForScheduleOrNull(task, date);
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
        List<Interval> intervals = new ArrayList<>();
        String warnings = "";

        // Создаем интервалы выполнения задач
        settings.forEach(setting -> intervals.add(getIntervalBySetting(setting)));

        // Добавляем пустые интервалы и проверяем на предупреждение
        List<Interval> resultIntervals = new ArrayList<>();

        if (intervals.isEmpty()) {
            resultIntervals.add(new Interval(null, activityStart, activityEnd));
        } else {
            Time prevIntervalEnd = getInitPie(date, intervals.get(0), activityStart);
            for (Interval interval : intervals) {
                int comparisonResult = prevIntervalEnd.compareTo(interval.getStart());

                if (TimeUtils.after(activityStart, interval.getStart()) || TimeUtils.after(interval.getEnd(), activityEnd)) {
                    warnings += (interval.getTitle() + " нарушает границы периода активности\n");
                }

                if (comparisonResult > 0) { //prevIntervalEnd > interval.start
                    warnings += (interval.getTitle() + " накладывается на предыдущий интервал\n");
                } else if (comparisonResult < 0) { //prevIntervalEnd < interval.start
                    resultIntervals.add(new Interval(null, prevIntervalEnd, interval.getStart()));
                }
                resultIntervals.add(interval);
                prevIntervalEnd = interval.getEnd();
            }

            if (after(activityEnd, prevIntervalEnd)) {
                resultIntervals.add(new Interval(null, prevIntervalEnd, activityEnd));
            }
        }

        return new Schedule(notScheduledTasks, resultIntervals, warnings.equals("") ? null : warnings);
    }

    private Time getInitPie(Date date, Interval firstInterval, Time activityStart) {
        Date today = new Date();
        Time now = getNow();
        if (TimeUtils.equalsIgnoreTime(today, date)) {
            if (TimeUtils.after(firstInterval.getStart(), now))
                return now;
            else
                return firstInterval.getStart();
        }
        if (TimeUtils.beforeIgnoreTime(date, today)) {
            return firstInterval.getStart();
        }
        return activityStart;
    }

    private Interval getIntervalBySetting(Setting setting) {
        Time absoluteBeginTime = getAbsoluteBeginTime(setting);
        return new Interval(getTaskBySetting(setting), absoluteBeginTime,
                TimeUtils.plus(absoluteBeginTime, setting.getDuration()));
    }

    private Task getTaskBySetting(Setting setting) {
        if (setting.isTask()) {
            return (Task) setting;
        }
        return komDao.getRegularTaskById(setting.getTaskId());
    }

    private final Comparator<Setting> beginTimeComparator = Comparator.comparing(this::getAbsoluteBeginTime);

    public Time getAbsoluteBeginTime(Setting setting) {
        return setting.isAbsoluteWobs() ?
                setting.getBeginTime()
                : plus(PrefService.INSTANCE.getActivityStart(), setting.getBeginTime());
    }

    private Setting getCompleteSettingForScheduleOrNull(Task task, Date date) throws Exception {
        if (task.getType() == TaskType.REGULAR) {
            OneDaySetting ods = OneDaySettingService.INSTANCE.getSettingOrNull((RegularTask) task, date);
            if (ods != null)
                return ods;
        }

        Setting setting = (Setting) task;
        if (setting.getDuration() != null && setting.getBeginTime() != null)
            return setting;

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
