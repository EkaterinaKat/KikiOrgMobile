package com.katyshevtseva.kikiorgmobile.core.model;

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
    private boolean done;

    public String getFullDesc() {
        return String.format("%s\n%s\n%s",
                desc, timeOfDay, DateUtils.getDateStringWithWeekDay(date));
    }

    @Override
    public TaskType getType() {
        return TaskType.IRREGULAR;
    }
}
