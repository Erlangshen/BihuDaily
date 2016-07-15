package com.lk.bihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lk.bihu.fragment.BaseFragment;

import java.util.List;

/**
 * 加载头视图的Adapter
 */
public class HeadAdapter extends FragmentPagerAdapter{
    private List<BaseFragment> headList;
    public HeadAdapter(FragmentManager fm,List<BaseFragment> headList) {
        super(fm);
        this.headList=headList;
    }

    @Override
    public Fragment getItem(int position) {
        return headList.get(position);
    }

    @Override
    public int getCount() {
        return headList.size();
    }
}
