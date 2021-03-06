package com.lk.bihu.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lk.bihu.R;
import com.lk.bihu.adapter.MenuAdapter;
import com.lk.bihu.bean.AllThemeMainInfo;
import com.lk.bihu.bean.ThemeMainInfo;
import com.lk.bihu.fragment.ContentFragment;
import com.lk.bihu.http.HTTPMethods;
import com.lk.bihu.http.MySubscriber;
import com.lk.bihu.http.SubscriberListener;
import com.lk.bihu.interfaces.MenuItemClickListener;
import com.lk.bihu.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private List<ThemeMainInfo> others;
    private MenuAdapter mAdapter;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private boolean flag = true;//没有添加其他页面-true；添加除首页之外的任何其他页面-false
    private List<Fragment> delList;
    private ContentFragment cacheFragment;//判断当前页面的fragment和将要添加的fragment是否为同一个
    private boolean isNight = false;
    @Bind(R.id.menu)
    Button menu;
    @Bind(R.id.menuList)
    ListView menuList;//侧滑菜单
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.menuLinear)
    LinearLayout menuLinear;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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

    @OnClick(R.id.downLoadLinear)
    void downLoad() {
        showShortToast("尚未完成");
    }

    @OnClick(R.id.nightLinear)
    void night() {
        if (isNight)
            ChangeToDay();
        else
            ChangeToNight();
        isNight = !isNight;
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
        others = SPUtils.getMenuList(MainActivity.this);
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
        SubscriberListener listener=new SubscriberListener<AllThemeMainInfo>(){

            @Override
            public void onNext(AllThemeMainInfo all) {
                others.clear();
                ThemeMainInfo info = new ThemeMainInfo();
                info.setName("首页");
                info.setId(-1);
                others.add(info);
                others.addAll(all.getOthers());
                SPUtils.setMenuList(MainActivity.this, others);
                mAdapter.notifyDataSetChanged();
            }
        };
        HTTPMethods.getInstance().getMenu(new MySubscriber<AllThemeMainInfo>(listener,MainActivity.this));
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
