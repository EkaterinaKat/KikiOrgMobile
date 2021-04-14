package com.katyshevtseva.kikiorgmobile.core;

import android.content.Context;

import com.katyshevtseva.kikiorgmobile.db.IrregularTaskDaoImpl;

public class Core {

    public static TaskService getTaskService(Context context) {
        return new TaskService(new IrregularTaskDaoImpl(context));
    }
}
