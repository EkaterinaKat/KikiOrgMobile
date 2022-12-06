package com.katyshevtseva.kikiorgmobile.core.model;

import static com.katyshevtseva.kikiorgmobile.core.DateUtils.getDateString;

import com.katyshevtseva.kikiorgmobile.core.DateUtils;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IrregularTask implements Task {
    private long id;
    private String title;
    private String desc;
    private TimeOfDay timeOfDay;
    private Date date;

    public String getAdminTaskListDesk() {
        return String.format("%s\n\n%s\n%s",
                desc, timeOfDay, DateUtils.getDateStringWithWeekDay(date));
    }

    public String getLogTaskDesk() {
        return String.format("[(%d) %s \n%s \n(%s, %s)]", id, title, desc, timeOfDay, getDateString(date));
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
                ", timeOfDay=" + timeOfDay +
                ", date=" + date +
                '}';
    }
}
