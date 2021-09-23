package com.katyshevtseva.kikiorgmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.KomDao;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;

public class KomDaoImpl implements KomDao {
    private IrregularTaskDao irregularTaskDao;
    private RegularTaskDao regularTaskDao;
    private RtDateDao rtDateDao;
    private DatelessTaskDao datelessTaskDao;

    public KomDaoImpl(Context context) {
        SQLiteDatabase database = new DbHelper(context).getWritableDatabase();
        irregularTaskDao = new IrregularTaskDao(database);
        regularTaskDao = new RegularTaskDao(database);
        rtDateDao = new RtDateDao(database);
        datelessTaskDao = new DatelessTaskDao(database);
    }

    ////////////////////////////  DatelessTask  //////////////////////////////////

    @Override
    public void saveNewDatelessTask(DatelessTask datelessTask) {
        datelessTaskDao.saveNew(datelessTask);
    }

    @Override
    public List<DatelessTask> getAllDatelessTasks() {
        return datelessTaskDao.findAll();
    }

    @Override
    public void updateDatelessTask(DatelessTask datelessTask) {
        datelessTaskDao.update(datelessTask, DatelessTaskDao.TableSchema.Cols.ID, "" + datelessTask.getId());
    }

    @Override
    public void deleteDatelessTask(DatelessTask datelessTask) {
        datelessTaskDao.delete(DatelessTaskDao.TableSchema.Cols.ID, "" + datelessTask.getId());
    }


    ////////////////////////////  IrregularTask  //////////////////////////////////

    @Override
    public void saveNewIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.saveNew(irregularTask);
    }

    @Override
    public List<IrregularTask> getAllIrregularTasks() {
        return irregularTaskDao.findAll();
    }

    @Override
    public List<IrregularTask> getIrregularTasksByDate(Date date) {
        return irregularTaskDao.find(IrregularTaskDao.TableSchema.Cols.DATE, DATE_FORMAT.format(date));
    }

    @Override
    public void updateIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.update(irregularTask, IrregularTaskDao.TableSchema.Cols.ID, "" + irregularTask.getId());
    }

    @Override
    public void deleteIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.delete(IrregularTaskDao.TableSchema.Cols.ID, "" + irregularTask.getId());
    }

    @Override
    public IrregularTask getIrregularTaskById(long id) {
        return irregularTaskDao.findFirst(IrregularTaskDao.TableSchema.Cols.ID, "" + id);
    }

    ////////////////////////////  RegularTask  //////////////////////////////////

    @Override
    public void saveNewRegularTask(RegularTask regularTask) {
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
        List<RtDate> rtDates = rtDateDao.find(RtDateDao.TableSchema.Cols.VALUE, DATE_FORMAT.format(date));
        List<RegularTask> regularTasks = new ArrayList<>();
        for (RtDate rtDate : rtDates) {
            regularTasks.addAll(regularTaskDao.find(RegularTaskDao.TableSchema.Cols.ID, "" + rtDate.getRegularTaskId()));
        }
        for (RegularTask task : regularTasks) {
            task.setDates(findDatesByRegularTask(task));
        }
        return regularTasks;
    }

    @Override
    public void updateRegularTask(RegularTask regularTask) {
        rtDateDao.delete(RtDateDao.TableSchema.Cols.TASK_ID, "" + regularTask.getId());
        for (Date date : regularTask.getDates()) {
            rtDateDao.saveNew(RtDate.builder().regularTaskId(regularTask.getId()).value(date).build());
        }
        regularTaskDao.update(regularTask, RegularTaskDao.TableSchema.Cols.ID, "" + regularTask.getId());
    }

    @Override
    public void deleteRegularTask(RegularTask regularTask) {
        rtDateDao.delete(RtDateDao.TableSchema.Cols.TASK_ID, "" + regularTask.getId());
        regularTaskDao.delete(RegularTaskDao.TableSchema.Cols.ID, "" + regularTask.getId());
    }

    @Override
    public RegularTask getRegularTaskById(long id) {
        RegularTask task = regularTaskDao.findFirst(RegularTaskDao.TableSchema.Cols.ID, "" + id);
        task.setDates(findDatesByRegularTask(task));
        return task;
    }

    private List<Date> findDatesByRegularTask(RegularTask regularTask) {
        List<Date> dates = new ArrayList<>();
        for (RtDate rtDate : rtDateDao.find(RtDateDao.TableSchema.Cols.TASK_ID, "" + regularTask.getId())) {
            dates.add(rtDate.getValue());
        }
        return dates;
    }
}
