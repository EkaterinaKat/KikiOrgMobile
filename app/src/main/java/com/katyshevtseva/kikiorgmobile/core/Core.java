package com.katyshevtseva.kikiorgmobile.core;

public class Core {
    private static final Core INSTANCE = new Core();
    private TaskService taskService = new TaskService();

    private Core() {

    }

    public static Core getInstance() {
        return INSTANCE;
    }

    public TaskService taskService() {
        return taskService;
    }
}
