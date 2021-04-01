package com.katyshevtseva.kikiorgmobile.core;


import com.katyshevtseva.kikiorgmobile.core.model.IrregularTask;


public class TaskService {

    public void saveNewTask(String type, String title, String desc) {
        IrregularTask task = new IrregularTask();
        task.setTitle(title);
        task.setDesc(desc);
        System.out.println("type: " + type);
        System.out.println("save new task: " + task);
    }
}
