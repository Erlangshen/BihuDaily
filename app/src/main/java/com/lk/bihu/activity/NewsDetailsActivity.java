package com.lk.bihu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lk.bihu.R;
import com.lk.bihu.bean.NewsDetail;
import com.lk.bihu.constant.Constant;
import com.lk.bihu.fragment.DetailFragment;
import com.lk.bihu.http.RequestAsyncTask;
import com.lk.bihu.interfaces.AsyncTaskCallBack;

import butterknife.Bind;
import butterknife.OnClick;

import static com.lk.bihu.R.id.details_ll;

/**
 * 新闻详情
 */
public class NewsDetailsActivity extends BaseActivity {
    private String url;
    private NewsDetail newsDetail;
    private FragmentTransaction transaction;
    @Bind(R.id.backIv)
    ImageView backIv;
    @Bind(R.id.nextIv)
    ImageView downIv;
    @Bind(R.id.goodIv)
    ImageView goodIv;
    @Bind(R.id.shareIv)
    ImageView shareIv;
    @Bind(R.id.discussIv)
    ImageView discussIv;
    @Bind(R.id.goodNumTv)
    TextView goodNumTv;
    @Bind(R.id.discussNumTv)
    TextView discussNumTv;

    @Override
    protected int getLayoutId() {
        return R.layout.news_details_layout;
    }

    @Override
    protected void initData() {
        int storyId = getIntent().getIntExtra("story_id", 0);
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
                    transaction.replace(details_ll, dFragment);
                    transaction.commitAllowingStateLoss();
                } else {
                    showShortToast("网络错误，请检查网络！");
                }
            }
        }).execute();
    }

    @OnClick(R.id.backIv)
    void backIvOnClick() {
        NewsDetailsActivity.this.finish();
    }

    @OnClick(R.id.nextIv)
    void nextIvOnClick() {
    }

    @OnClick(R.id.goodIv)
    void goodIvOnClick() {
    }

    @OnClick(R.id.shareIv)
    void shareIvOnClick() {
    }

    @OnClick(R.id.discussIv)
    void discussIvOnClick() {
    }

}
