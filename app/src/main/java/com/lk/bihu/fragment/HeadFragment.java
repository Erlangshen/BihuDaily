package com.lk.bihu.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;
import com.lk.bihu.utils.ImageDownLoader;

/**
 * 轮询视图
 */
public class HeadFragment extends BaseFragment{
    private ImageView image;
    private String imageUrl="";
    private ImageDownLoader loader;
    @Override
    protected int getLayoutId() {
        return R.layout.head_fragment;
    }

    @Override
    protected void initView(View v) {
        image= (ImageView) v.findViewById(R.id.headFragmentImage);
    }

    @Override
    protected void initData() {
        imageUrl= getArguments().getString("image_url");
        loader= BihuApplication.getApp().getImageDownLoaderInstance();
        image.setTag(imageUrl);
        Bitmap bitmap = loader.downLoader(image, new ImageDownLoader.ImageLoaderlistener() {
            @Override
            public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
        if (bitmap!=null){
            image.setImageBitmap(bitmap);
        }
    }
}
