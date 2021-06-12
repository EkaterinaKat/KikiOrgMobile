package com.katyshevtseva.kikiorgmobile.core.model;

import java.util.Date;

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
    private Date refDate;
    private int period;

    @Override
    public String toString() {
        return "RegularTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", periodType=" + periodType +
                ", refDate=" + refDate +
                ", period=" + period +
                '}';
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.REGULAR;
    }
}
