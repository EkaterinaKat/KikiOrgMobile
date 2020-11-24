package com.katyshevtseva.kikiorgmobile.core.date;

import java.util.Date;

public class DateEntity {
    private long id;
    private Date value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
}
