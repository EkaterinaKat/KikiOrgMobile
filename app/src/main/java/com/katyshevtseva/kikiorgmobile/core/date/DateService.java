package com.katyshevtseva.kikiorgmobile.core.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DateService {
    private DateRepo dateRepo;

    public DateEntity createIfNotExistAndGetDateEntity(Date date) {
        Optional<DateEntity> optionalDateEntity = dateRepo.findByValue(date);
        if (optionalDateEntity.isPresent())
            return optionalDateEntity.get();

        DateEntity dateEntity = new DateEntity();
        dateEntity.setValue(date);
        return dateRepo.save(dateEntity);
    }

    public List<DateEntity> getOnlyExistingDateEntitiesByPeriod(Date start, Date end) {
        List<DateEntity> dateEntities = new ArrayList<>();
        for (Date date : getDateRange(start, end)) {
            Optional<DateEntity> optionalDateEntity = dateRepo.findByValue(date);
            if (optionalDateEntity.isPresent())
                dateEntities.add(optionalDateEntity.get());
        }
        return dateEntities;
    }

    private List<Date> getDateRange(Date start, Date end) {
        Date date = new Date(start.getTime());
        Date oneDayAfterEnd = addOneDay(end);

        List<Date> result = new ArrayList<>();
        while (date.before(oneDayAfterEnd)) {
            result.add(date);
            date = addOneDay(date);
        }
        return result;
    }

    private Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
