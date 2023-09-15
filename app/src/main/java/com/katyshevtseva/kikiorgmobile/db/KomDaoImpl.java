package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.KomDao;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.OptionalTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KomDaoImpl implements KomDao {
    private final IrregularTaskDao irregularTaskDao;
    private final RegularTaskDao regularTaskDao;
    private final RtDateDao rtDateDao;
    private final LogDao logDao;
    private final OptionalTaskDao optionalTaskDao;

    public KomDaoImpl(Context context) {
        SQLiteDatabase database = new DbHelper(context).getWritableDatabase();
        irregularTaskDao = new IrregularTaskDao(database);
        regularTaskDao = new RegularTaskDao(database);
        rtDateDao = new RtDateDao(database);
        logDao = new LogDao(database);
        optionalTaskDao = new OptionalTaskDao(database);
    }

    ////////////////////////////  Log  //////////////////////////////////

    @Override
    public void saveNew(Log log) {
        logDao.saveNew(log);
    }

    @Override
    public List<Log> getAllLogs() {
        return logDao.findAll();
    }

    @Override
    public void delete(Log log) {
        logDao.delete(log);
    }

    ////////////////////////////  OptionalTask  //////////////////////////////////

    @Override
    public void saveNew(OptionalTask optionalTask) {
        optionalTaskDao.saveNew(optionalTask);
    }

    @Override
    public List<OptionalTask> getAllOptionalTasks() {
        return optionalTaskDao.findAll();
    }

    @Override
    public void update(OptionalTask optionalTask) {
        optionalTaskDao.update(optionalTask);
    }

    @Override
    public void delete(OptionalTask optionalTask) {
        optionalTaskDao.delete(optionalTask);
    }

    ////////////////////////////  IrregularTask  //////////////////////////////////

    @Override
    public void saveNew(IrregularTask irregularTask) {
        irregularTaskDao.saveNew(irregularTask);
    }

    @Override
    public List<IrregularTask> getAllIrregularTasks() {
        return irregularTaskDao.findAll();
    }

    @Override
    public List<IrregularTask> getIrregularTasksByDate(Date date) {
        return irregularTaskDao.find(DATE, DATE_FORMAT.format(date));
    }

    @Override
    public void update(IrregularTask irregularTask) {
        irregularTaskDao.update(irregularTask);
    }

    @Override
    public void delete(IrregularTask irregularTask) {
        irregularTaskDao.delete(irregularTask);
    }

    @Override
    public IrregularTask getIrregularTaskById(long id) {
        return irregularTaskDao.findFirst(ID, "" + id);
    }

    ////////////////////////////  RegularTask  //////////////////////////////////

    @Override
    public void saveNew(RegularTask regularTask) {
        regularTaskDao.saveNew(regularTask);
        for (Date date : regularTask.getDates()) {
            rtDateDao.saveNew(RtDate.builder().regularTaskId(regularTaskDao.getLastInsertedId()).value(date).build());
        }
    }

    @Override
    public List<RegularTask> getAllRegularTasks() {
        List<RegularTask> tasks = regularTaskDao.findAll();
        for (RegularTask task : tasks) {
            task.setDates(findDatesByRegularTask(task));
        }
        return tasks;
    }

    @Override
    public List<RegularTask> getRegularTasksByDate(Date date) {
        List<RtDate> rtDates = rtDateDao.find(VALUE, DATE_FORMAT.format(date));
        List<RegularTask> regularTasks = new ArrayList<>();
        for (RtDate rtDate : rtDates) {
            regularTasks.addAll(regularTaskDao.find(ID, "" + rtDate.getRegularTaskId()));
        }
        for (RegularTask task : regularTasks) {
            task.setDates(findDatesByRegularTask(task));
        }
        return regularTasks;
    }

    @Override
    public void update(RegularTask regularTask) {
        rtDateDao.delete(TASK_ID, "" + regularTask.getId());
        for (Date date : regularTask.getDates()) {
            rtDateDao.saveNew(RtDate.builder().regularTaskId(regularTask.getId()).value(date).build());
        }
        regularTaskDao.update(regularTask);
    }

    @Override
    public RegularTask getRegularTaskById(long id) {
        RegularTask task = regularTaskDao.findFirst(ID, "" + id);
        task.setDates(findDatesByRegularTask(task));
        return task;
    }

    private List<Date> findDatesByRegularTask(RegularTask regularTask) {
        List<Date> dates = new ArrayList<>();
        for (RtDate rtDate : rtDateDao.find(TASK_ID, "" + regularTask.getId())) {
            dates.add(rtDate.getValue());
        }
        return dates;
    }
}
