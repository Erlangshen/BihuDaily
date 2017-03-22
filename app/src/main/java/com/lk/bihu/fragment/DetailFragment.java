package com.lk.bihu.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;
import com.lk.bihu.bean.NewsDetail;
import com.lk.bihu.utils.ImageDownLoader;
import com.lk.bihu.utils.ImageLoaderTools;
import com.lk.bihu.utils.URLImageParser;


/**
 * 详情Fragment
 */
public class DetailFragment extends BaseFragment {
    private NewsDetail newsDetail;
    private CoordinatorLayout cl;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout ctl;
    private ImageView tBar_iv;
    private TextView tBar_tv, tv_body;
    private Toolbar toolBar;
    private ImageDownLoader loader;
    private String imageUrl = "";
    private ImageLoaderTools mImageLoaderTools;

    @Override
    protected int getLayoutId() {
        return R.layout.detail_fragment_layout;
    }

    @Override
    protected void initView(View v) {
        cl = (CoordinatorLayout) v.findViewById(R.id.cl);
        appBar = (AppBarLayout) v.findViewById(R.id.appBar);
        ctl = (CollapsingToolbarLayout) v.findViewById(R.id.ctl);
        tBar_iv = (ImageView) v.findViewById(R.id.tBar_iv);
        tBar_tv = (TextView) v.findViewById(R.id.tBar_tv);
        toolBar = (Toolbar) v.findViewById(R.id.toolBar);
        tv_body = (TextView) v.findViewById(R.id.tv_body);
    }

    @Override
    protected void initData() {
        mImageLoaderTools = ImageLoaderTools.getInstance(getActivity());
        loader = BihuApplication.getApp().getImageDownLoaderInstance();
        Bundle bundle = getArguments();
        newsDetail = (NewsDetail) bundle.getSerializable("newsDetail");
        tv_body.setMovementMethod(LinkMovementMethod.getInstance());
        tv_body.setText(Html.fromHtml(newsDetail.getBody(), new URLImageParser(tv_body), null));
        ctl.setTitle(newsDetail.getTitle());
        ctl.setExpandedTitleTextAppearance(R.style.tv_expanded_style);//设置还没收缩时状态下字体颜色
        ctl.setCollapsedTitleTextAppearance(R.style.tv_collapsed_style);//设置收缩后Toolbar上字体的颜色
        ctl.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        imageUrl = newsDetail.getImage();
        if (imageUrl != null) {
            tBar_iv.setTag(imageUrl);
            Bitmap bitmap = loader.downLoader(tBar_iv, new ImageDownLoader.ImageLoaderlistener() {
                @Override
                public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                    if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
            if (bitmap != null) {
                tBar_iv.setImageBitmap(bitmap);
            }
        } else {
            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 0);
            appBar.setLayoutParams(params);
        }
        tBar_tv.setText(newsDetail.getImage_source());

        tv_body.setOnTouchListener(new View.OnTouchListener() {
            float xDiff = 0.0f;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDiff = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        xDiff = event.getX() - xDiff;
                        if (xDiff > dip2px(30))
                            getActivity().finish();
                        break;
                }
                return false;
            }
        });
    }
}
