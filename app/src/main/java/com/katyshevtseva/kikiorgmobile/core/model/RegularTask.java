package com.katyshevtseva.kikiorgmobile.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularTask implements Task {
    private long id;
    private String title;
    private String desc;
    private PeriodType periodType;
    private int period;
    private boolean archived;

    public String getFullDesc() {
        return String.format("%s\n%s %s\n%s",
                desc, period, periodType, "место для даты");
    }

    @Override
    public TaskType getType() {
        return TaskType.REGULAR;
    }
}
