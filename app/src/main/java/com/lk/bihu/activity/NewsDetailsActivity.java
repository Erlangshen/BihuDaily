package com.lk.bihu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lk.bihu.R;
import com.lk.bihu.bean.NewsDetail;
import com.lk.bihu.bean.Story;
import com.lk.bihu.constant.Constant;
import com.lk.bihu.fragment.DetailFragment;
import com.lk.bihu.http.RequestAsyncTask;
import com.lk.bihu.interfaces.AsyncTaskCallBack;

/**
 * 新闻详情
 */
public class NewsDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backIv, downIv, goodIv, shareIv, discussIv;
    private TextView goodNumTv, discussNumTv;
    private String url;
    private NewsDetail newsDetail;
    private FragmentTransaction transaction;

    @Override
    protected int getLayoutId() {
        return R.layout.news_details_layout;
    }

    @Override
    protected void initView() {
        backIv = (ImageView) findViewById(R.id.backIv);
        downIv = (ImageView) findViewById(R.id.nextIv);
        goodIv = (ImageView) findViewById(R.id.goodIv);
        shareIv = (ImageView) findViewById(R.id.shareIv);
        discussIv = (ImageView) findViewById(R.id.discussIv);
        goodNumTv = (TextView) findViewById(R.id.goodNumTv);
        discussNumTv = (TextView) findViewById(R.id.discussNumTv);

        backIv.setOnClickListener(this);
        downIv.setOnClickListener(this);
        goodIv.setOnClickListener(this);
        shareIv.setOnClickListener(this);
        discussIv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        int storyId = getIntent().getIntExtra("story_id",0);
        url = Constant.DETAILS_URL + storyId;
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        requestData(url);

    }

    /**
     * 请求网络数据
     */
    private void requestData(String url) {
        new RequestAsyncTask(NewsDetailsActivity.this, url, "数据加载中...", new AsyncTaskCallBack() {
            @Override
            public void post(String rest) {
                if (!TextUtils.isEmpty(rest)) {
                    newsDetail = JSONObject.parseObject(rest.toString(), NewsDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newsDetail", newsDetail);
                    DetailFragment dFragment = new DetailFragment();
                    dFragment.setArguments(bundle);
                    transaction.replace(R.id.details_ll, dFragment);
                    transaction.commitAllowingStateLoss();
                } else {
                    showShortToast("网络错误，请检查网络！");
                }
            }
        }).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backIv:
                NewsDetailsActivity.this.finish();
                break;
            case R.id.nextIv:
                break;
            case R.id.goodIv:
                break;
            case R.id.shareIv:
                break;
            case R.id.discussIv:
                break;
        }
    }
}
