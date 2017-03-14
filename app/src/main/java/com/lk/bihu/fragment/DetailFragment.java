package com.lk.bihu.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.bihu.R;
import com.lk.bihu.bean.NewsDetail;
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
    private TextView tBar_tv,tv_body;
    private Toolbar toolBar;
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
        tBar_iv= (ImageView) v.findViewById(R.id.tBar_iv);
        tBar_tv= (TextView) v.findViewById(R.id.tBar_tv);
        toolBar= (Toolbar) v.findViewById(R.id.toolBar);
        tv_body= (TextView) v.findViewById(R.id.tv_body);
    }

    @Override
    protected void initData() {
        mImageLoaderTools=ImageLoaderTools.getInstance(getActivity());
        Bundle bundle = getArguments();
        newsDetail = (NewsDetail) bundle.getSerializable("newsDetail");
        tv_body.setMovementMethod(LinkMovementMethod.getInstance());
        tv_body.setText(Html.fromHtml(newsDetail.getBody(),new URLImageParser(tv_body),null));
        ctl.setTitle(newsDetail.getTitle());
        ctl.setExpandedTitleTextAppearance(R.style.tv_expanded_style);//设置还没收缩时状态下字体颜色
        ctl.setCollapsedTitleTextAppearance(R.style.tv_collapsed_style);//设置收缩后Toolbar上字体的颜色
        ctl.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        mImageLoaderTools.displayImage(newsDetail.getImage(),tBar_iv);
        tBar_tv.setText(newsDetail.getImage_source());
    }
}
