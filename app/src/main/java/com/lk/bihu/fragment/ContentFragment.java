package com.lk.bihu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lk.bihu.R;
import com.lk.bihu.adapter.BihuListAdapter;
import com.lk.bihu.adapter.HeadAdapter;
import com.lk.bihu.bean.BihuContent;
import com.lk.bihu.bean.BihuMenu;
import com.lk.bihu.bean.Story;
import com.lk.bihu.bean.TopStory;
import com.lk.bihu.constant.Constant;
import com.lk.bihu.http.RequestAsyncTask;
import com.lk.bihu.interfaces.AsyncTaskCallBack;
import com.lk.bihu.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ContentFragment extends BaseFragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView homeDataListView;//首页listview
    private List<TopStory> topStories;//首页广告
    private List<Story> homeStories;//首页列表
    private BihuListAdapter bihuAdapter = null;//列表adapter
    private View headView;//头视图
    private ViewPager headVp;//头视图的轮询广告ViewPager
    private LinearLayout headLinear;//头视图小点
    private List<BaseFragment> headFragments;//头视图的fragment集合
    private HeadAdapter headAdapter;//头视图adapter
    private int headPrePosition = 0;
    private int count = 0;
    private TextView headTv;
    private int id = -2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (topStories.size() > 1)
                headVp.setCurrentItem(count % topStories.size());
        }
    };
    private Timer timer = null;
    private TimerTask task = null;

    @Override
    protected int getLayoutId() {
        return R.layout.content_fragment;
    }

    @Override
    protected void initView(View v) {
        homeDataListView = (ListView) v.findViewById(R.id.contentList);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeLayout);
    }

    @Override
    protected void initData() {
        if (homeStories == null)
            homeStories = new ArrayList<Story>();
        if (topStories == null)
            topStories = new ArrayList<TopStory>();
        if (headFragments == null)
            headFragments = new ArrayList<BaseFragment>();

        initHeadView();

        bihuAdapter = new BihuListAdapter(getActivity(), homeStories);
        homeDataListView.setAdapter(bihuAdapter);
        id = getArguments().getInt("id");
        requestData(id);
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_1, R.color.swipe_color_1, R.color.swipe_color_1);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.swipe_background_color);
        swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(id);
            }
        });
    }

    /**
     * 请求数据
     */
    private void requestData(int id) {
        if (-1 == id) {
            new RequestAsyncTask(getActivity(), Constant.HOME_URL, new AsyncTaskCallBack() {
                @Override
                public void post(String rest) {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    if (!TextUtils.isEmpty(rest)) {
                        try {
                            JSONObject object = new JSONObject(rest);
                            BihuMenu data = com.alibaba.fastjson.JSONObject.parseObject(object.toString(), BihuMenu.class);
                            if (DateUtils.getSysTime2().equals(data.getDate())) {
                                topStories.clear();
                                topStories.addAll(data.getTop_stories());
                                if (topStories.size() > 1) {
                                    headLinear.setVisibility(View.VISIBLE);
                                    getHeadView();
                                } else {
                                    headLinear.setVisibility(View.GONE);
                                }
                                homeStories.clear();
                                homeStories.addAll(data.getStories());
                                bihuAdapter.notifyDataSetChanged();
                            } else {
                                showToast("请求数据失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("网络错误");
                    }
                }
            }).execute();
        } else {
            new RequestAsyncTask(getActivity(), Constant.CONTENT_URL + id, new AsyncTaskCallBack() {
                @Override
                public void post(String rest) {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    if (!TextUtils.isEmpty(rest)) {
                        BihuContent content = com.alibaba.fastjson.JSONObject.parseObject(rest.toString(), BihuContent.class);
                        homeStories.clear();
                        homeStories.addAll(content.getStories());
                        bihuAdapter.notifyDataSetChanged();
                    } else {
                        showToast("网络错误");
                    }
                }
            }).execute();
        }
    }

    /**
     * 加载头视图布局
     */
    private void initHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_view, null);
        headLinear = (LinearLayout) headView.findViewById(R.id.headLinear);
        headVp = (ViewPager) headView.findViewById(R.id.headVp);
        headTv = (TextView) headView.findViewById(R.id.headTv);
        headAdapter = new HeadAdapter(getFragmentManager(), headFragments);
        headVp.setAdapter(headAdapter);
        homeDataListView.addHeaderView(headView);
        timer = new Timer();
    }

    /**
     * 加载或者刷新头视图
     */
    private void getHeadView() {
        if (headLinear.getChildCount() > 0)
            headLinear.removeAllViews();
        headFragments.clear();
        for (int i = 0; i < topStories.size(); i++) {
            ImageView image = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(6), dip2px(6));
            params.leftMargin = dip2px(8);
            image.setLayoutParams(params);
            image.setImageResource(R.drawable.page);
            headLinear.addView(image);

            HeadFragment hFragment = new HeadFragment();
            Bundle bundle = new Bundle();
            bundle.putString("image_url", topStories.get(i).getImage());
            hFragment.setArguments(bundle);
            headFragments.add(hFragment);
        }

        ImageView image = (ImageView) headLinear.getChildAt(0);
        image.setImageResource(R.drawable.page_now);
        headVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (topStories.size() > 0)
                    headTv.setText(topStories.get(position).getTitle());
                ImageView image = (ImageView) headLinear.getChildAt(position);
                ImageView pImage = (ImageView) headLinear.getChildAt(headPrePosition);
                image.setImageResource(R.drawable.page_now);
                pImage.setImageResource(R.drawable.page);
                headPrePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (task != null)
            task.cancel();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(count);
                count++;
            }
        };
        timer.schedule(task, 3000, 3000);
        headAdapter.setFragments(headFragments);
    }

    /**
     * dp转成px
     */
    public int dip2px(float dipValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
