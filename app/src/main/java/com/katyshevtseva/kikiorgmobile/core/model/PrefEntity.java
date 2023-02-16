package com.katyshevtseva.kikiorgmobile.core.model;

import com.katyshevtseva.kikiorgmobile.db.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrefEntity implements Entity {
    private long id;
    private String title;
    private String value;

    public enum Pref {
        ACTIVITY_PERIOD_START, ACTIVITY_PERIOD_END
    }
}
