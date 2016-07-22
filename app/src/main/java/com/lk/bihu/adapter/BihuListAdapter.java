package com.lk.bihu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;
import com.lk.bihu.bean.Story;
import com.lk.bihu.utils.ImageDownLoader;

import java.util.List;

public class BihuListAdapter extends BaseAdapter {
    private Context context;
    private List<Story> stories;
    private ImageDownLoader loader;
    private String imageUrl;

    public BihuListAdapter(Context context, List<Story> stories) {
        this.context = context;
        this.stories = stories;
        loader = BihuApplication.getApp().getImageDownLoaderInstance();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Story story = stories.get(position);
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
            imageUrl = story.getImages().get(0);
        }
        holder.image.setTag(imageUrl);
        if (!TextUtils.isEmpty(imageUrl)) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageResource(R.drawable.defaultcovers);
            Bitmap bitmap = loader.downLoader(holder.image, new ImageDownLoader.ImageLoaderlistener() {

                @Override
                public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                    if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap);
            }
        } else {
            holder.image.setVisibility(View.GONE);
        }
        return convertView;
    }

    class Holder {
        TextView title;
        ImageView image;
    }
}
