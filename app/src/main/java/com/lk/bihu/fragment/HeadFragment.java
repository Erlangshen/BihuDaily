package com.lk.bihu.fragment;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lk.bihu.R;

import butterknife.Bind;

/**
 * 轮询视图
 */
public class HeadFragment extends BaseFragment {
    private String imageUrl = "";
    @Bind(R.id.headFragmentImage)
    ImageView image;

    @Override
    protected int getLayoutId() {
        return R.layout.head_fragment;
    }

    @Override
    protected void initData() {
        imageUrl = getImageUrl();
        Glide.with(getActivity())
                .load(imageUrl)
                .error(R.drawable.defaultcovers)
                .into(image);
//        loader= BihuApplication.getApp().getImageDownLoaderInstance();
//        image.setTag(imageUrl);
//        Bitmap bitmap = loader.downLoader(image, new ImageDownLoader.ImageLoaderlistener() {
//            @Override
//            public void onImageLoader(Bitmap bitmap, ImageView imageView) {
//                if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
//                    imageView.setImageBitmap(bitmap);
//                }
//            }
//        });
//        if (bitmap!=null){
//            image.setImageBitmap(bitmap);
//        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
