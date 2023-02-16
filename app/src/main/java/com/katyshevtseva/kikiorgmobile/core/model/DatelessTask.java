package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.db.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatelessTask implements Entity {
    private long id;
    private String title;

    public String getLogTaskDesk() {
        return String.format("[(%d) %s]", id, title);
    }

    public String getIdAndTitleInfo() {
        return id + ") " + title;
    }

    @Override
    public String toString() {
        return "DatelessTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
