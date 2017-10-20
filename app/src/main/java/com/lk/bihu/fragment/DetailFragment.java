package com.lk.bihu.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lk.bihu.R;
import com.lk.bihu.bean.NewsDetail;
import com.lk.bihu.http.RequestImageAsyncTask;
import com.lk.bihu.interfaces.ImageAsyncTaskCallBack;
import com.lk.bihu.utils.ImageDownLoader;
import com.lk.bihu.utils.ImageLoaderTools;
import com.lk.bihu.utils.MessageSpan;
import com.lk.bihu.utils.MyLinkMovementMethod;
import com.lk.bihu.utils.URLImageParser;
import com.lk.bihu.view.ZoomImageView;

import java.lang.ref.WeakReference;

import butterknife.Bind;


/**
 * 详情Fragment
 */
public class DetailFragment extends BaseFragment {
    private NewsDetail newsDetail;
    private ImageDownLoader loader;
    private String imageUrl = "";
    private ImageLoaderTools mImageLoaderTools;
    private boolean isImageShow = false;
    @Bind(R.id.cl)
    CoordinatorLayout cl;
    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.ctl)
    CollapsingToolbarLayout ctl;
    @Bind(R.id.tBar_iv)
    ImageView tBar_iv;
    @Bind(R.id.tBar_tv)
    TextView tBar_tv;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.tv_body)
    TextView tv_body;
    @Bind(R.id.detailIv)
    ZoomImageView detailIv;
    @Bind(R.id.detailRl)
    RelativeLayout detailRl;
    @Bind(R.id.imageBack)
    ImageView imageBack;

    private static class MyHandler extends Handler {
        WeakReference<DetailFragment> mWeakReference;

        public MyHandler(DetailFragment fragment) {
            mWeakReference = new WeakReference<DetailFragment>(fragment);
        }

        public void handleMessage(Message msg) {
            int what = msg.what;
            String imageSource = "";
            if (what == 200) {
                MessageSpan ms = (MessageSpan) msg.obj;
                Object[] spans = (Object[]) ms.getObj();
                for (Object span : spans) {
                    if (span instanceof ImageSpan) {
                        if (span != null) {
                            imageSource = ((ImageSpan) span).getSource();
                        } else {
                            mWeakReference.get().setIsImageShow(false);
                        }
                        if (!TextUtils.isEmpty(imageSource)) {
                            if (!mWeakReference.get().isImageShow())
                                mWeakReference.get().loadImage(imageSource);
                        } else {
                            mWeakReference.get().setIsImageShow(false);
                        }
                    }
                }
            }
        }
    }

    public void setIsImageShow(boolean isImageShow) {
        this.isImageShow = isImageShow;
        showToast("加载图片出错");
    }

    public boolean isImageShow() {
        return isImageShow;
    }

    public void loadImage(final String imageSource) {
        try {
            new RequestImageAsyncTask(getActivity(), imageSource, "正在加载大图...", new ImageAsyncTaskCallBack() {
                @Override
                public void callBack(Bitmap bm) {
                    detailIv.setImage(bm);
                }
            }).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        detailRl.setVisibility(View.VISIBLE);
        isImageShow = true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.detail_fragment_layout;
    }

    @Override
    protected void initData() {
        mImageLoaderTools = ImageLoaderTools.getInstance(getActivity());
//        loader = BihuApplication.getApp().getImageDownLoaderInstance();
        Bundle bundle = getArguments();
        newsDetail = (NewsDetail) bundle.getSerializable("newsDetail");
        MyHandler handler = new MyHandler(DetailFragment.this);
        tv_body.setMovementMethod(MyLinkMovementMethod.getInstance(handler, ImageSpan.class));
        tv_body.setText(Html.fromHtml(newsDetail.getBody(), new URLImageParser(tv_body, getActivity()), null));
        ctl.setTitle(newsDetail.getTitle());
        ctl.setExpandedTitleTextAppearance(R.style.tv_expanded_style);//设置还没收缩时状态下字体颜色
        ctl.setCollapsedTitleTextAppearance(R.style.tv_collapsed_style);//设置收缩后Toolbar上字体的颜色
        ctl.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        imageUrl = newsDetail.getImage();
        if (imageUrl != null) {
            Glide.with(getActivity())
                    .load(imageUrl)
                    .error(R.drawable.defaultcovers)
                    .into(tBar_iv);
//            tBar_iv.setTag(imageUrl);
//            Bitmap bitmap = loader.downLoader(tBar_iv, new ImageDownLoader.ImageLoaderlistener() {
//                @Override
//                public void onImageLoader(Bitmap bitmap, ImageView imageView) {
//                    if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }
//            });
//            if (bitmap != null) {
//                tBar_iv.setImageBitmap(bitmap);
//            }
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
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageShow) {
                    detailRl.setVisibility(View.GONE);
                    isImageShow = false;
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        isImageShow = false;
    }
}
