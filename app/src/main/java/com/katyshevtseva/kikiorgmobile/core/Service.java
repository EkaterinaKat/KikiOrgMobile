package com.katyshevtseva.kikiorgmobile.core;

import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Task;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Service {
    public static Service INSTANCE;
    private final KomDao komDao;

    public static void init(KomDao komDao) {
        INSTANCE = new Service(komDao);
    }

    private Service(KomDao komDao) {
        this.komDao = komDao;
    }

    public List<Task> getTasksForMainList(Date date) {
        List<Task> tasks = new ArrayList<>();

        tasks.addAll(komDao.getIrregularTasksByDate(date));
        tasks.addAll(komDao.getRegularTasksByDate(date).stream()
                .filter(regularTask -> !regularTask.isArchived()).collect(Collectors.toList()));

        return tasks;
    }

    public Date getEarliestTaskDate() {
        Stream<Date> irtDatesStream = komDao.getAllIrregularTasks().stream()
                .map(IrregularTask::getDate);

        Stream<Date> rtDatesStream = RegularTaskService.INSTANCE.getNotArchivedRt(null).stream()
                .flatMap(task -> task.getDates().stream());

        return Stream.concat(irtDatesStream, rtDatesStream).sorted().findFirst().orElse(null);
    }

    public boolean overdueTasksExist() {
        return DateUtils.beforeIgnoreTime(getEarliestTaskDate(), new Date());
    }

    public IrregularTask makeIrregularTaskFromRegular(long regularTaskId) {
        RegularTask regularTask = komDao.getRegularTaskById(regularTaskId);
        IrregularTask task = new IrregularTask();
        task.setTitle(regularTask.getTitle());
        task.setDesc(regularTask.getDesc());
        task.setDate(new Date());
        task.setDuration(regularTask.getDuration());
        task.setBeginTime(ScheduleService.INSTANCE.getAbsoluteBeginTime(regularTask));
        return task;
    }
}
