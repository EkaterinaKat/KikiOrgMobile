package com.katyshevtseva.kikiorgmobile.core.model;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.db.Entity;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;
import com.katyshevtseva.kikiorgmobile.utils.Time;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IrregularTask implements Task, Entity, Setting {
    private long id;
    private String title;
    private String desc;
    private Date date;
    private Time duration;
    private Time beginTime;

    public String getAdminTaskListDesk() {
        return String.format("%s\n\n%s",
                desc, DateUtils.getDateStringWithWeekDay(date));
    }

    public String getLogTaskDesk() {
        return String.format("[(%d) %s \n%s \n(%s)]", id, title, desc, getDateString(date));
    }

    @Override
    public TaskType getType() {
        return TaskType.IRREGULAR;
    }

    @Override
    public String toString() {
        return "IrregularTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean isAbsoluteWobs() {
        return true;
    }

    @Override
    public TaskType getTaskType() {//todo дублир
        return TaskType.IRREGULAR;
    }

    @Override
    public long getTaskId() {
        return id;
    }
}
