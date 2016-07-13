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
    private List<String> data;
    private ArrayAdapter<String> adapter;

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
        int id = getArguments().getInt("id");
        if (-1==id){
            new RequestAsyncTask(getActivity(), Constant.HOME_URL,"拼命加载中...", new AsyncTaskCallBack() {
                @Override
                public void post(String rest) {
                    if (!TextUtils.isEmpty(rest)){
                        try {
                            JSONObject object=new JSONObject(rest);
                            Bihu data = com.alibaba.fastjson.JSONObject.parseObject(object.toString(), Bihu.class);
                            if (DateUtils.getSysTime2().equals(data.getDate())){
                                topStories=data.getTop_stories();
                                homeStories=data.getStories();
                                Log.e("AAA",topStories.toString());
                                Log.e("AAA",homeStories.toString());
                            }else{
                                showToast("请求数据失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        showToast("网络错误");
                    }
                }
            }).execute();
        }

        data = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        homeDataListView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_1, R.color.swipe_color_1, R.color.swipe_color_1);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        ;
        swipeRefreshLayout.setProgressBackgroundColor(R.color.swipe_background_color);
        swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        for (int i = 0; i < 20; i++) {
                            data.add("SwipeRefreshLayout下拉刷新" + i);
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }

    };
}
