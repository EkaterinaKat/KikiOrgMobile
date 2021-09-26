package com.katyshevtseva.kikiorgmobile.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatelessTask {
    private long id;
    private String title;

    @Override
    public String toString() {
        return "DatelessTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
