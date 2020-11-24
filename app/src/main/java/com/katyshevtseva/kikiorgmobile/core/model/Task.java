package com.katyshevtseva.kikiorgmobile.core.model;

import java.util.Date;

public class Task {
    private long id;
    private long desc_id;
    private Date date;
    private boolean done;

    public Task(long id, long desc_id, Date date, boolean done) {
        this.id = id;
        this.desc_id = desc_id;
        this.date = date;
        this.done = done;
    }

    public long getId() {
        return id;
    }

    public long getDesc_id() {
        return desc_id;
    }

    public Date getDate() {
        return date;
    }

    public boolean isDone() {
        return done;
    }
}
