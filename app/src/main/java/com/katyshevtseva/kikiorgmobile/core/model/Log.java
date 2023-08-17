package com.katyshevtseva.kikiorgmobile.core.model;

import static com.katyshevtseva.kikiorgmobile.utils.DateUtils.getDateTimeString;

import com.katyshevtseva.kikiorgmobile.db.lib.Entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log implements Entity {
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
        CREATION("#5CCBFF"),
        DELETION("#FF685C"),
        RESCHEDULE("#FFEC5C"),
        COMPLETION("#67FF5C"),
        ARCHIVATION("#999999"),
        EDITING("#CB5CFF"),
        RESUME("#1F75FE");

        private final String color;

        Action(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }

    public enum Subject {
        REGULAR_TASK, IRREGULAR_TASK, DATELESS_TASK
    }

    public String getFullDesc() {
        return String.format("%d) %s %s \n%s\n%s", id, action, subject, desc, getDateTimeString(date));
    }

    public String getBackupString() {
        return "Log{" +
                "id=" + id +
                ", date=" + date +
                ", action=" + action +
                ", subject=" + subject +
                ", desc='" + desc + '\'' +
                '}';
    }
}
