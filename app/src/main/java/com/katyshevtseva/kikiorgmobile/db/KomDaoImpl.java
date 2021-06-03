package com.katyshevtseva.kikiorgmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.dao.KomDao;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;

import java.util.List;

public class KomDaoImpl implements KomDao {
    private IrregularTaskDao irregularTaskDao;
    private RegularTaskDao regularTaskDao;

    public KomDaoImpl(Context context) {
        SQLiteDatabase database = new DbHelper(context).getWritableDatabase();
        irregularTaskDao = new IrregularTaskDao(database);
        regularTaskDao = new RegularTaskDao(database);
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
        irregularTaskDao.update(irregularTask);
    }

    @Override
    public void deleteIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.delete(irregularTask);
    }

    @Override
    public void saveNewRegularTask(RegularTask regularTask) {
        regularTaskDao.saveNew(regularTask);
    }

    @Override
    public List<RegularTask> getAllRegularTasks() {
        return regularTaskDao.findAll();
    }

    @Override
    public void updateRegularTask(RegularTask regularTask) {
        regularTaskDao.update(regularTask);
    }

    @Override
    public void deleteRegularTask(RegularTask regularTask) {
        regularTaskDao.delete(regularTask);
    }
}
