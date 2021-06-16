package com.katyshevtseva.kikiorgmobile.core.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IrregularTask {
    private long id;
    private String title;
    private String desc;
    private Date date;
    private boolean done;

    @Override
    public String toString() {
        return "IrregularTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", date=" + date +
                ", done=" + done +
                '}';
    }
}
