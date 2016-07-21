package com.lk.bihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.bihu.R;
import com.lk.bihu.bean.ThemeMainInfo;

import java.util.List;

/**
 * 加载菜单列表的adapter
 */
public class MenuAdapter extends BaseAdapter {
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
        ThemeMainInfo info = others.get(position);
        Holder holder=null;
        if (convertView==null){
            holder=new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.menu_item, null);
            convertView= LayoutInflater.from(context).inflate(R.layout.menu_item,null);
            holder.view_top=convertView.findViewById(R.id.black_line_top);
            holder.view_bottom=convertView.findViewById(R.id.black_line_bottom);
            holder.home_icon= (ImageView) convertView.findViewById(R.id.menu_home_icon);
            holder.home_add = (ImageView) convertView.findViewById(R.id.menu_item_add);
            holder.home_tv= (TextView) convertView.findViewById(R.id.menu_item_tv);
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }
        if (position==0){//首页
            holder.view_top.setVisibility(View.VISIBLE);
            holder.view_bottom.setVisibility(View.VISIBLE);
            holder.home_icon.setVisibility(View.VISIBLE);
            holder.home_add.setImageResource(R.drawable.menu_next);
        }else{
            holder.view_top.setVisibility(View.GONE);
            holder.view_bottom.setVisibility(View.GONE);
            holder.home_icon.setVisibility(View.GONE);
            holder.home_add.setImageResource(R.drawable.menu_add);
        }
        holder.home_tv.setText(info.getName());
        return convertView;
    }
    class Holder{
        View view_top,view_bottom;
        ImageView home_icon,home_add;
        TextView home_tv;
    }
}
