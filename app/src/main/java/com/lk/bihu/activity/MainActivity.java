package com.lk.bihu.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.lk.bihu.R;
import com.lk.bihu.adapter.MenuAdapter;
import com.lk.bihu.bean.AllThemeMainInfo;
import com.lk.bihu.bean.ThemeMainInfo;
import com.lk.bihu.constant.Constant;
import com.lk.bihu.fragment.ContentFragment;
import com.lk.bihu.http.RequestAsyncTask;
import com.lk.bihu.interfaces.AsyncTaskCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private Button menu;
    private ListView menuList;//侧滑菜单
    private DrawerLayout drawerLayout;
    private LinearLayout menuLinear;
    private List<ThemeMainInfo> others;
    private MenuAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        menu = (Button) findViewById(R.id.menu);
        menuList = (ListView) findViewById(R.id.menuList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuLinear = (LinearLayout) findViewById(R.id.menuLinear);
    }

    @Override
    protected void initData() {
        initMenu();
        setMenu();
        ContentFragment fragment = new ContentFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);//首页
        fragment.setArguments(bundle);
        transaction.add(R.id.frag_ll, fragment, "home");
        transaction.commit();

    }

    //初始化侧滑菜单
    private void initMenu() {
        if (others == null)
            others = new ArrayList<ThemeMainInfo>();
        mAdapter = new MenuAdapter(MainActivity.this, others);
        menuList.setAdapter(mAdapter);
        new RequestAsyncTask(MainActivity.this, Constant.MENU_URL, new AsyncTaskCallBack() {
            @Override
            public void post(String rest) {
                if (!TextUtils.isEmpty(rest)) {
                    AllThemeMainInfo all = JSONObject.parseObject(rest.toString(), AllThemeMainInfo.class);
                    others.clear();
                    others.addAll(all.getOthers());
                    mAdapter.notifyDataSetChanged();
                } else {
                    showShortToast("网络错误");
                }
            }
        }).execute();
    }

    /**
     * 设置侧滑菜单
     */
    private void setMenu() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BihuApplication app= (BihuApplication) getApplication();
//                app.setNightMode(!app.isNightMode());
//                if (app.isNightMode()){
//                    ChangeToNight();
//                }else{
//                    ChangeToDay();
//                }
//                recreate();
                drawerLayout.openDrawer(menuLinear);
            }
        });
    }
}
