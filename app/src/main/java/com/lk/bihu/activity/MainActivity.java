package com.lk.bihu.activity;


import android.view.View;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;

public class MainActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void change(View view) {
        BihuApplication app= (BihuApplication) getApplication();
        app.setNightMode(!app.isNightMode());
        if (app.isNightMode()){
            ChangeToNight();
        }else{
            ChangeToDay();
        }
        recreate();
    }
}
