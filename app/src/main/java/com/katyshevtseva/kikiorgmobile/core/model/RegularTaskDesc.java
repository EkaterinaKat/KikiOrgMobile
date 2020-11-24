package com.katyshevtseva.kikiorgmobile.core.model;

public class RegularTaskDesc {
    private long id;
    private String title;
    private String desc;
    private PeriodType periodType;
    private int period;

    public enum PeriodType{
        DAY, WEEK, MONTH, YEAR
    }

    public RegularTaskDesc(long id, String title, String desc, PeriodType periodType, int period) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.periodType = periodType;
        this.period = period;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public int getPeriod() {
        return period;
    }
}
