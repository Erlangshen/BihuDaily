package com.lk.bihu;

import android.app.Application;

public class BihuApplication extends Application{
    private boolean isNightMode=false;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean isNightMode() {
        return isNightMode;
    }

    public void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
    }
}
