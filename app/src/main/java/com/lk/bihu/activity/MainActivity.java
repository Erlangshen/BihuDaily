package com.lk.bihu.activity;


import android.view.View;
import android.widget.Button;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;

public class MainActivity extends BaseActivity {
    private Button menu;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        menu= (Button) findViewById(R.id.menu);
    }

    @Override
    protected void initData() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShortToast("此处应该有个目录弹出");
            }
        });
    }

//    public void change(View view) {
//        BihuApplication app= (BihuApplication) getApplication();
//        app.setNightMode(!app.isNightMode());
//        if (app.isNightMode()){
//            ChangeToNight();
//        }else{
//            ChangeToDay();
//        }
//        recreate();
//    }
}
