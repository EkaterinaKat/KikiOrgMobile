package com.katyshevtseva.kikiorgmobile.db;

import java.util.Date;

import lombok.Data;

@Data
class RtDate {
    private long id;
    private long regularTaskId;
    private Date value;
}
