package com.katyshevtseva.kikiorgmobile.core.model;

import java.util.Date;

public class IrregularTaskDesc {
    private long id;
    private String title;
    private String desc;
    private Date date;

    public IrregularTaskDesc(long id, String title, String desc, Date date) {
        this.id = id;
        this.title = title;
        this.desc = desc;
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
}
