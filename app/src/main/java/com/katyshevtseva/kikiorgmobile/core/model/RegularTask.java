package com.katyshevtseva.kikiorgmobile.core.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegularTask {
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
                '}';
    }
}
