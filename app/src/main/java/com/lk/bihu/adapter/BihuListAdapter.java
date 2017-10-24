package com.lk.bihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lk.bihu.R;
import com.lk.bihu.bean.Story;

import java.util.List;

public class BihuListAdapter extends BaseAdapter {
    private Context context;
    private List<Story> stories;

    public BihuListAdapter(Context context, List<Story> stories) {
        this.context = context;
        this.stories = stories;
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Object getItem(int position) {
        return stories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return stories.get(position).isDateStr() ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Story story = stories.get(position);
        switch (getItemViewType(position)) {
            case 0:
                Holder holder = null;
                if (convertView == null) {
                    holder = new Holder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.bihu_list_item, null);
                    holder.title = (TextView) convertView.findViewById(R.id.titleTv);
                    holder.image = (ImageView) convertView.findViewById(R.id.itemImage);
                    convertView.setTag(holder);
                } else {
                    holder = (Holder) convertView.getTag();
                }
                holder.title.setText(story.getTitle());
                if (story.getImages() != null && story.getImages().size() > 0) {
                    Glide.with(context)
                            .load(story.getImages().get(0))
                            .error(R.drawable.defaultcovers)
                            .into(holder.image);
                } else {
                    holder.image.setVisibility(View.GONE);
                }
                break;
            case 1:
                Holder2 holder2 = null;
                if (convertView == null) {
                    holder2 = new Holder2();
                    convertView = LayoutInflater.from(context).inflate(R.layout.date_str_layout, null);
                    holder2.dateStr = (TextView) convertView.findViewById(R.id.dateStr);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (Holder2) convertView.getTag();
                }
                holder2.dateStr.setText(story.getTitle());
                break;
        }
        return convertView;
    }

    class Holder {
        TextView title;
        ImageView image;
    }

    class Holder2 {
        TextView dateStr;
    }
}
