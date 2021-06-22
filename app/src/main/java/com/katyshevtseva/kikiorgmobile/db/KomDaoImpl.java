package com.katyshevtseva.kikiorgmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.dao.KomDao;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KomDaoImpl implements KomDao {
    private IrregularTaskDao irregularTaskDao;
    private RegularTaskDao regularTaskDao;
    private RtDateDao rtDateDao;

    public KomDaoImpl(Context context) {
        SQLiteDatabase database = new DbHelper(context).getWritableDatabase();
        irregularTaskDao = new IrregularTaskDao(database);
        regularTaskDao = new RegularTaskDao(database);
        rtDateDao = new RtDateDao(database);
    }

    @Override
    public void saveNewIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.saveNew(irregularTask);
    }

    @Override
    public List<IrregularTask> getAllIrregularTasks() {
        return irregularTaskDao.findAll();
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
    public IrregularTask findIrregularTaskById(long id) {
        return irregularTaskDao.findFirst(IrregularTaskDao.TableSchema.Cols.ID, "" + id);
    }

    //////////////////////////////////////////////////////////////

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
    public RegularTask findRegularTaskById(long id) {
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
