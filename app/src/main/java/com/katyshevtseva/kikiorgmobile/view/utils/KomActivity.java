package com.katyshevtseva.kikiorgmobile.view.utils;

import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.utils.knobs.NoArgKnob;

import lombok.Setter;

@Setter
public abstract class KomActivity extends AppCompatActivity {
    private SwipeManager swipeManager;
    private NoArgKnob onLeftSwipe;
    private NoArgKnob onRightSwipe;
    private NoArgKnob onStart;
    private boolean immersiveStickyMode = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if ((onLeftSwipe != null || onRightSwipe != null) && swipeManager == null) {
            createSwipeManager();
        }

        if (swipeManager != null) {
            Boolean result = swipeManager.dispatchTouchEvent(ev);
            return result == null ? super.dispatchTouchEvent(ev) : result;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void createSwipeManager() {
        swipeManager = new SwipeManager(this);
        if (onLeftSwipe != null)
            swipeManager.setLeftSwipeListener(onLeftSwipe);
        if (onRightSwipe != null)
            swipeManager.setRightSwipeListener(onRightSwipe);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (onStart != null) {
            onStart.execute();
        }
    }

    @Override
    protected void onResume() {
        setImstModeIfNeeded();
        super.onResume();
    }

    public void setImstModeIfNeeded() {
        if (immersiveStickyMode) {
            GeneralUtil.setImmersiveStickyMode(getWindow());
        }
    }
}
