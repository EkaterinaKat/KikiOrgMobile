package com.katyshevtseva.kikiorgmobile.core.model;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateString;

import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskUrgency;
import com.katyshevtseva.kikiorgmobile.core.enums.TimeOfDay;
import com.katyshevtseva.kikiorgmobile.db.lib.Entity;
import com.katyshevtseva.kikiorgmobile.utils.DateUtils;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IrregularTask implements Task, Entity {
    private long id;
    private String title;
    private String desc;
    private Date date;
    private TaskUrgency urgency;
    private TimeOfDay timeOfDay;

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

    public String getBackupString() {
        return "IrregularTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", timeOfDay=" + timeOfDay +
                ", date=" + date +
                '}';
    }
}
