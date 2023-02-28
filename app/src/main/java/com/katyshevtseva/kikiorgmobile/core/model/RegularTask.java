package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.db.Entity;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularTask implements Task, Entity, Setting {
    private long id;
    private String title;
    private String desc;
    private PeriodType periodType;
    private int period;
    private boolean archived;
    private List<Date> dates;
    private Time duration;
    private Time beginTime;
    private boolean absoluteWobs;

    public String getAdminTaskListDesk() {
        String result = String.format("%s\n\n%s %s\n%s",
                desc, period, periodType, getLoppedDateListString());
        if (duration != null) {
            result += ("\nDuration: " + duration.getS());
        }
        if (beginTime != null) {
            result += ("\nBegin: " + beginTime.getS() + (absoluteWobs ? " abs" : " rel"));
        }
        return result;
    }

    public String getLogTaskDesk() {
        return String.format("[(%d) %s \n%s \n(%d %s)]", id, title, desc, period, periodType);
    }

    private String getLoppedDateListString() {
        boolean dateListIsTooBig = dates.size() > 5;
        List<Date> loppedList = dateListIsTooBig ? dates.subList(0, 5) : new ArrayList<>(dates);

        StringBuilder stringBuilder = new StringBuilder();
        for (Date date : loppedList) {
            stringBuilder.append(DateUtils.getDateStringWithWeekDay(date)).append("\n");
        }

        if (dateListIsTooBig)
            stringBuilder.append("...");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public TaskType getType() {
        return TaskType.REGULAR;
    }

    @Override
    public TaskType getTaskType() { //todo дублируется метод
        return TaskType.REGULAR;
    }

    @Override
    public long getTaskId() {
        return id;
    }
}
