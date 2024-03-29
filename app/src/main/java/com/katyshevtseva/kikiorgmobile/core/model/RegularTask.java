package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.core.enums.PeriodType;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskUrgency;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.db.lib.Entity;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularTask implements Task, Entity {
    private long id;
    private String title;
    private String desc;
    private PeriodType periodType;
    private int period;
    private boolean archived;
    private List<Date> dates;
    private TaskUrgency urgency;
    private TimeOfDay timeOfDay;

    public String getAdminTaskListDesk() {
        return String.format("%s\n\n%s\n%s %s\n%s",
                desc, timeOfDay, period, periodType, getLoppedDateListString());
    }

    public String getLogTaskDesk() {
        return String.format("[(%d) %s \n%s \n(%s, %d %s)]", id, title, desc, timeOfDay, period, periodType);
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

    public String getBackupString() {
        return "RegularTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", periodType=" + periodType +
                ", period=" + period +
                ", archived=" + archived +
                ", dates=" + dates +
                '}';
    }

    @Override
    public TaskType getType() {
        return TaskType.REGULAR;
    }
}
