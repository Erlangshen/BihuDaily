package com.lk.bihu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lk.bihu.bean.ThemeMainInfo;

import java.util.List;

/**
 * 加载菜单列表的adapter
 */
public class MenuAdapter extends BaseAdapter{
    private Context context;
    private List<ThemeMainInfo> others;

    public MenuAdapter(Context context, List<ThemeMainInfo> others) {
        this.context = context;
        this.others = others;
    }

    @Override
    public int getCount() {
        return others.size();
    }

    @Override
    public Object getItem(int position) {
        return others.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
