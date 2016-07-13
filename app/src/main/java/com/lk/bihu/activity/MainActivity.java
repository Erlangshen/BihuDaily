package com.lk.bihu.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;
import com.lk.bihu.fragment.ContentFragment;

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
        setMenu();
        ContentFragment fragment=new ContentFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putInt("id",-1);//首页
        fragment.setArguments(bundle);
        transaction.add(R.id.frag_ll,fragment,"home");
        transaction.commit();

    }
/**设置侧滑菜单*/
    private void setMenu() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BihuApplication app= (BihuApplication) getApplication();
                app.setNightMode(!app.isNightMode());
                if (app.isNightMode()){
                    ChangeToNight();
                }else{
                    ChangeToDay();
                }
                recreate();
            }
        });
    }
}
