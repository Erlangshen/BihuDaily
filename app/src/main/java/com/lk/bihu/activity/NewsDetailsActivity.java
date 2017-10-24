package com.lk.bihu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.bihu.R;
import com.lk.bihu.bean.NewsDetail;
import com.lk.bihu.constant.Constant;
import com.lk.bihu.fragment.DetailFragment;
import com.lk.bihu.http.HTTPMethods;
import com.lk.bihu.http.MySubscriber;
import com.lk.bihu.http.SubscriberListener;

import butterknife.Bind;
import butterknife.OnClick;

import static com.lk.bihu.R.id.details_ll;

/**
 * 新闻详情
 */
public class NewsDetailsActivity extends BaseActivity {
    private FragmentTransaction transaction;
    private int storyId;

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
        storyId = getIntent().getIntExtra("story_id", 0);
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        SubscriberListener listener=new SubscriberListener<NewsDetail>(){

            @Override
            public void onNext(NewsDetail newsDetail) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsDetail", newsDetail);
                DetailFragment dFragment = new DetailFragment();
                dFragment.setArguments(bundle);
                transaction.replace(details_ll, dFragment);
                transaction.commitAllowingStateLoss();
            }
        };
        HTTPMethods.getInstance().getDetails(new MySubscriber(listener,NewsDetailsActivity.this),storyId);
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
