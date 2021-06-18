package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.core.CoreUtils;

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
    private boolean archived;

    @Override
    public String toString() {
        return "RegularTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", periodType=" + periodType +
                ", refDate=" + refDate +
                ", period=" + period +
                ", archived=" + archived +
                '}';
    }

    public String getFullDesc() {
        return String.format("%s\nPeriod: %s %s\nNext date: %s",
                desc, period, periodType, CoreUtils.READABLE_DATE_FORMAT.format(refDate));
    }

    @Override
    public TaskType getType() {
        return TaskType.REGULAR;
    }
}
