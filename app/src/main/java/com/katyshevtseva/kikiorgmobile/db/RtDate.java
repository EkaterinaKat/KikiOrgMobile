package com.katyshevtseva.kikiorgmobile.db;

import com.katyshevtseva.kikiorgmobile.db.lib.Entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RtDate implements Entity {
    private long id;
    private long regularTaskId;
    private Date value;
}
