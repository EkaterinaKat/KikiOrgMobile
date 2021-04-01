package com.katyshevtseva.kikiorgmobile.core.model;

import java.util.Date;

public class IrregularTask {
    private long id;
    private String title;
    private String desc;
    private Date date;
    private boolean done;

    public IrregularTask() {
        done = false;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "IrregularTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", date=" + date +
                '}';
    }
}
