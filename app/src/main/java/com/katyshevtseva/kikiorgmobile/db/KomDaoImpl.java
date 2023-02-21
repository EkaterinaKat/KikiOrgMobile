package com.katyshevtseva.kikiorgmobile.db;

import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.DATE_FORMAT;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.RT_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_ID;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TASK_TYPE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.TITLE;
import static com.katyshevtseva.kikiorgmobile.db.DbConstants.VALUE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.katyshevtseva.kikiorgmobile.core.KomDao;
import com.katyshevtseva.kikiorgmobile.core.enums.TaskType;
import com.katyshevtseva.kikiorgmobile.core.model.DatelessTask;
import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;
import com.katyshevtseva.kikiorgmobile.core.model.Log;
import com.katyshevtseva.kikiorgmobile.core.model.OneDaySetting;
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
    private final OneDaySettingDao oneDaySettingDao;

    public KomDaoImpl(Context context) {
        SQLiteDatabase database = new DbHelper(context).getWritableDatabase();
        irregularTaskDao = new IrregularTaskDao(database);
        regularTaskDao = new RegularTaskDao(database);
        rtDateDao = new RtDateDao(database);
        datelessTaskDao = new DatelessTaskDao(database);
        logDao = new LogDao(database);
        prefDao = new PrefDao(database);
        rtSettingDao = new RtSettingDao(database);
        oneDaySettingDao = new OneDaySettingDao(database);
    }

    ////////////////////////////  DatelessTask  //////////////////////////////////

    @Override
    public void saveNew(DatelessTask datelessTask) {
        datelessTaskDao.saveNew(datelessTask);
    }

    @Override
    public List<DatelessTask> getAllDatelessTasks() {
        return datelessTaskDao.findAll();
    }

    @Override
    public void update(DatelessTask datelessTask) {
        datelessTaskDao.update(datelessTask);
    }

    @Override
    public void delete(DatelessTask datelessTask) {
        datelessTaskDao.delete(datelessTask);
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
    public void update(PrefEntity pref) {
        prefDao.update(pref);
    }

    ////////////////////////////  RtSetting  //////////////////////////////////

    @Override
    public RtSetting getRtSettingById(long id) {
        return rtSettingDao.findFirst(ID, "" + id);
    }

    @Override
    public void saveNew(RtSetting setting) {
        rtSettingDao.saveNew(setting);
    }

    @Override
    public void update(RtSetting setting) {
        rtSettingDao.update(setting);
    }

    @Override
    public void delete(RtSetting setting) {
        rtSettingDao.delete(setting);
    }

    @Override
    public List<RtSetting> getAllRtSettings() {
        return rtSettingDao.findAll();
    }

    @Override
    public List<RtSetting> getRtSettingsByRtId(Long rtId) {
        return rtSettingDao.find(RT_ID, rtId.toString());
    }

    ////////////////////////////  OneDaySetting  //////////////////////////////////

    @Override
    public List<OneDaySetting> findOneDaySetting(long taskId, TaskType taskType, Date date) {
        return oneDaySettingDao.find(
                new String[]{TASK_ID, TASK_TYPE, DATE},
                new String[]{"" + taskId, "" + taskType.getCode(), DATE_FORMAT.format(date)});
    }

    @Override
    public void saveNew(OneDaySetting setting) {
        oneDaySettingDao.saveNew(setting);
    }

    @Override
    public List<OneDaySetting> getAllOneDaySettings() {
        return oneDaySettingDao.findAll();
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
