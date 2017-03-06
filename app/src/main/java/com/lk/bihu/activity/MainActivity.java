package com.lk.bihu.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
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
import com.lk.bihu.fragment.HeadFragment;
import com.lk.bihu.http.RequestAsyncTask;
import com.lk.bihu.interfaces.AsyncTaskCallBack;
import com.lk.bihu.interfaces.MenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private Button menu;
    private ListView menuList;//侧滑菜单
    private DrawerLayout drawerLayout;
    private LinearLayout menuLinear;
    private List<ThemeMainInfo> others;
    private MenuAdapter mAdapter;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private boolean flag = true;//没有添加其他页面-true；添加除首页之外的任何其他页面-false
    private List<Fragment> delList;
    private ContentFragment cacheFragment;//判断当前页面的fragment和将要添加的fragment是否为同一个

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
        delList = new ArrayList<Fragment>();
        initMenu();
        setMenu();
        cacheFragment = new ContentFragment();
        ThemeMainInfo info = new ThemeMainInfo();
        info.setId(-1);
        initContentFragment(info, "-1");
    }

    /**
     * 加载内容Fragment
     */
    private void initContentFragment(ThemeMainInfo info, String tag) {
        ContentFragment fragment = new ContentFragment();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("themeMainInfo", info);
        bundle.putInt("id", info.getId());
        fragment.setArguments(bundle);
        if (manager.getFragments() == null || manager.getFragments().size() == 0) {
            transaction.add(R.id.frag_ll, fragment, tag);
            transaction.commit();
            cacheFragment = fragment;
            return;
        } else {//得到当前显示的fragment
            for (Fragment fr : manager.getFragments()) {
                if (fr != null && fr.isVisible() && (fr instanceof ContentFragment)) {
                    cacheFragment = (ContentFragment) fr;
                }
            }
        }
        if (!manager.getFragments().contains(fragment)) {
            if (!(fragment instanceof ContentFragment)) {
                return;
            }
            if (fragment.getArguments().getInt("id") != manager.getFragments().get(0).getArguments().getInt("id")) {
                delList.clear();
                for (int i = 1; i < manager.getFragments().size(); i++) {
                    if (manager.getFragments().get(i) instanceof ContentFragment) {
                        flag = false;
                        delList.add(manager.getFragments().get(i));
                    }
                }
                //已经进入首页，从首页进入其他页面（list中已经存在首页，且没有添加过其他页面）
                //当前页面是其他页面，并且切换到其他的（比如从心理学日报切换到电影日报）
                if (!(("-1".equals(manager.getFragments().get(0).getTag())) && flag)) {
                    for (Fragment f : delList) {
                        manager.getFragments().remove(f);
                        transaction.remove(f);
                    }
                }
                transaction.commit();
                transaction = null;
                manager.executePendingTransactions();
                transaction = manager.beginTransaction();
                transaction.hide(manager.getFragments().get(0));
                transaction.add(R.id.frag_ll, fragment, tag);
                transaction.addToBackStack(null);
                transaction.commit();
                //从其他界面回到首页
            } else if (cacheFragment.getArguments().getInt("id") != fragment.getArguments().getInt("id")) {
                super.onBackPressed();
                ContentFragment frag = (ContentFragment) manager.findFragmentByTag("-1");
                frag.timerCallBack();
            } else {//从首页回到首页
                return;
            }
        }
    }

    //初始化侧滑菜单
    private void initMenu() {
        if (others == null)
            others = new ArrayList<ThemeMainInfo>();
        mAdapter = new MenuAdapter(MainActivity.this, others, new MenuItemClickListener() {
            @Override
            public void tvClick(ThemeMainInfo info) {
                initContentFragment(info, String.valueOf(info.getId()));
                drawerLayout.closeDrawer(menuLinear);
            }

            @Override
            public void addClick(ThemeMainInfo info) {

            }
        });
        menuList.setAdapter(mAdapter);
        new RequestAsyncTask(MainActivity.this, Constant.MENU_URL, new AsyncTaskCallBack() {
            @Override
            public void post(String rest) {
                if (!TextUtils.isEmpty(rest)) {
                    try {
                        AllThemeMainInfo all = JSONObject.parseObject(rest.toString(), AllThemeMainInfo.class);
                        others.clear();
                        ThemeMainInfo info = new ThemeMainInfo();
                        info.setName("首页");
                        info.setId(-1);
                        others.add(info);
                        others.addAll(all.getOthers());
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(menuLinear)) {
            drawerLayout.closeDrawer(menuLinear);
        } else {
            super.onBackPressed();
            ContentFragment frag = (ContentFragment) manager.findFragmentByTag("-1");
            frag.timerCallBack();
        }
    }
}
