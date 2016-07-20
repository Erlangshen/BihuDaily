package com.lk.bihu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.lk.bihu.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载头视图的Adapter
 */
public class HeadAdapter extends FragmentPagerAdapter{
    private List<BaseFragment> headList;
    private FragmentManager fm;
    public HeadAdapter(FragmentManager fm,List<BaseFragment> headList) {
        super(fm);
        this.fm = fm;
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

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    public void setFragments(List<BaseFragment> headList) {
        if(this.headList != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.headList){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.headList = headList;
        notifyDataSetChanged();
    }
}
