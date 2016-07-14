package com.lk.bihu.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lk.bihu.R;
import com.lk.bihu.adapter.BihuListAdapter;
import com.lk.bihu.bean.Bihu;
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

public class ContentFragment extends BaseFragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView homeDataListView;//首页listview
    private List<TopStory> topStories;//首页广告
    private List<Story> homeStories;//首页列表
    private List<Story> homeStoriesCache;//首页列表
    private BihuListAdapter bihuAdapter = null;
    private int id = -2;

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
        if (homeStoriesCache == null)
            homeStoriesCache = new ArrayList<Story>();
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
                            Bihu data = com.alibaba.fastjson.JSONObject.parseObject(object.toString(), Bihu.class);
                            if (DateUtils.getSysTime2().equals(data.getDate())) {
                                topStories = data.getTop_stories();
                                homeStoriesCache.clear();
                                homeStoriesCache.addAll(homeStories);
                                homeStories.clear();
                                homeStories.addAll(data.getStories());
                                if (homeStoriesCache.size() == homeStories.size()) {
                                    showToast("暂无更多数据");
                                } else {
                                    bihuAdapter.notifyDataSetChanged();
                                }
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
        }
    }

}
