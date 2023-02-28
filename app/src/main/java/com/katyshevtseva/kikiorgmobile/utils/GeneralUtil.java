package com.katyshevtseva.kikiorgmobile.utils;

import android.view.View;
import android.view.Window;

import com.katyshevtseva.kikiorgmobile.core.model.Task;

public abstract class GeneralUtil {

    public static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    public static void setImmersiveStickyMode(Window window) {
        View decorView = window.getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static boolean taskFilter(Task task, String s) {
        s = s == null ? null : s.toLowerCase();
        return isEmpty(s) || (task.getTitle().toLowerCase().contains(s) || task.getDesc().toLowerCase().contains(s));
    }
}
