package com.katyshevtseva.kikiorgmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.dao.KomDao;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;

import java.util.List;

public class KomDaoImpl implements KomDao {
    private IrregularTaskDao irregularTaskDao;

    public KomDaoImpl(Context context) {
        SQLiteDatabase database = new DbHelper(context).getWritableDatabase();
        irregularTaskDao = new IrregularTaskDao(database);
    }

    @Override
    public void saveNewIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.saveNewIrregularTask(irregularTask);
    }

    @Override
    public List<IrregularTask> getAllIrregularTasks() {
        return irregularTaskDao.getAllIrregularTasks();
    }

    @Override
    public void updateIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.updateIrregularTask(irregularTask);
    }

    @Override
    public void deleteIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.deleteIrregularTask(irregularTask);
    }
}
