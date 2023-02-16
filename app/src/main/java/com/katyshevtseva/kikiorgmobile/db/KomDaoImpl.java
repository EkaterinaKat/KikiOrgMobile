package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.RT_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.KomDao;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.PrefEntity;
import com.katyshevtseva.kikiorgmobile.core.model.RegularTask;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KomDaoImpl implements KomDao {
    private final IrregularTaskDao irregularTaskDao;
    private final RegularTaskDao regularTaskDao;
    private final RtDateDao rtDateDao;
    private final DatelessTaskDao datelessTaskDao;
    private final LogDao logDao;
    private final PrefDao prefDao;
    private final RtSettingDao rtSettingDao;

    public KomDaoImpl(Context context) {
        SQLiteDatabase database = new DbHelper(context).getWritableDatabase();
        irregularTaskDao = new IrregularTaskDao(database);
        regularTaskDao = new RegularTaskDao(database);
        rtDateDao = new RtDateDao(database);
        datelessTaskDao = new DatelessTaskDao(database);
        logDao = new LogDao(database);
        prefDao = new PrefDao(database);
        rtSettingDao = new RtSettingDao(database);
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
        datelessTaskDao.update(datelessTask, ID, "" + datelessTask.getId());
    }

    @Override
    public void deleteDatelessTask(DatelessTask datelessTask) {
        datelessTaskDao.delete(ID, "" + datelessTask.getId());
    }

    ////////////////////////////  Log  //////////////////////////////////

    @Override
    public void saveNewLog(Log log) {
        logDao.saveNew(log);
    }

    @Override
    public List<Log> getAllLogs() {
        return logDao.findAll();
    }

    ////////////////////////////  Pref  //////////////////////////////////

    @Override
    public PrefEntity getPrefByTitle(String title) {
        List<PrefEntity> prefs = prefDao.find(TITLE, title);
        if (prefs.size() != 1) {
            throw new RuntimeException("По заданному заголовку найдено более или менее одного преференса");
        }
        return prefs.get(0);
    }

    @Override
    public void updatePref(PrefEntity pref) {
        prefDao.update(pref, ID, "" + pref.getId());
    }

    ////////////////////////////  Setting  //////////////////////////////////

    @Override
    public RtSetting getRtSettingById(long id) {
        return rtSettingDao.findFirst(ID, "" + id);
    }

    @Override
    public void saveNewRtSetting(RtSetting setting) {
        rtSettingDao.saveNew(setting);
    }

    @Override
    public void updateRtSetting(RtSetting setting) {
        rtSettingDao.update(setting, ID, "" + setting.getId());
    }

    @Override
    public void deleteRtSetting(RtSetting setting) {
        rtSettingDao.delete(ID, "" + setting.getId());
    }

    @Override
    public List<RtSetting> getAllRtSettings() {
        return rtSettingDao.findAll();
    }

    @Override
    public List<RtSetting> getRtSettingsByRtId(Long rtId) {
        return rtSettingDao.find(RT_ID, rtId.toString());
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
        return irregularTaskDao.find(DATE, DATE_FORMAT.format(date));
    }

    @Override
    public void updateIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.update(irregularTask, ID, "" + irregularTask.getId());
    }

    @Override
    public void deleteIrregularTask(IrregularTask irregularTask) {
        irregularTaskDao.delete(ID, "" + irregularTask.getId());
    }

    @Override
    public IrregularTask getIrregularTaskById(long id) {
        return irregularTaskDao.findFirst(ID, "" + id);
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
    public void updateRegularTask(RegularTask regularTask) {
        rtDateDao.delete(TASK_ID, "" + regularTask.getId());
        for (Date date : regularTask.getDates()) {
            rtDateDao.saveNew(RtDate.builder().regularTaskId(regularTask.getId()).value(date).build());
        }
        regularTaskDao.update(regularTask, ID, "" + regularTask.getId());
    }

    @Override
    public void deleteRegularTask(RegularTask regularTask) {
        rtDateDao.delete(TASK_ID, "" + regularTask.getId());
        regularTaskDao.delete(ID, "" + regularTask.getId());
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
