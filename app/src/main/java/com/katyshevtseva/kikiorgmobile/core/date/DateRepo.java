package com.katyshevtseva.kikiorgmobile.core.date;

import java.util.Date;
import java.util.Optional;

public interface DateRepo {
    Optional<DateEntity> findByValue(Date value);
    DateEntity save(DateEntity dateEntity);
}
