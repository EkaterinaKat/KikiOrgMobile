package com.katyshevtseva.kikiorgmobile.core.model;

import static com.katyshevtseva.kikiorgmobile.core.DateUtils.getDateTimeString;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private long id;
    private Date date;
    private Action action;
    private Subject subject;
    private String desc;

    public Log(Date date, Action action, Subject subject, String desc) {
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.desc = desc;
    }

    public enum Action {
        CREATION, DELETION, RESCHEDULE, COMPLETION, ARCHIVATION, EDITING, RESUME
    }

    public enum Subject {
        REGULAR_TASK, IRREGULAR_TASK, DATELESS_TASK
    }

    public String getFullDesc() {
        return String.format("%d) %s %s \n%s\n%s", id, action, subject, desc, getDateTimeString(date));
    }
}
