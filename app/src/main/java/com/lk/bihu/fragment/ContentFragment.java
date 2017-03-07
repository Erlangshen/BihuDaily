package com.lk.bihu.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;
import com.lk.bihu.activity.NewsDetailsActivity;
import com.lk.bihu.adapter.BihuListAdapter;
import com.lk.bihu.adapter.HeadAdapter;
import com.lk.bihu.bean.BihuContent;
import com.lk.bihu.bean.BihuMenu;
import com.lk.bihu.bean.Story;
import com.lk.bihu.bean.ThemeMainInfo;
import com.lk.bihu.bean.TopStory;
import com.lk.bihu.constant.Constant;
import com.lk.bihu.http.RequestAsyncTask;
import com.lk.bihu.interfaces.AsyncTaskCallBack;
import com.lk.bihu.interfaces.OnLoadListener;
import com.lk.bihu.interfaces.TimerCallBack;
import com.lk.bihu.utils.DateUtils;
import com.lk.bihu.utils.ImageDownLoader;
import com.lk.bihu.view.SwipeRefreshView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ContentFragment extends BaseFragment implements TimerCallBack {
    private SwipeRefreshView swipeRefreshLayout;
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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (topStories.size() > 1 && (count % topStories.size() < topStories.size())) {
                headVp.setCurrentItem(count % topStories.size());
            }
        }
    };
    static Timer timer = null;
    static TimerTask task = null;
    private TextView titleTv;//标题
    private ImageView headIv;//除首页之外的头视图
    private ThemeMainInfo info;
    private String date;//日期

    @Override
    protected int getLayoutId() {
        return R.layout.content_fragment;
    }

    @Override
    protected void initView(View v) {
        homeDataListView = (ListView) v.findViewById(R.id.contentList);
        swipeRefreshLayout = (SwipeRefreshView) v.findViewById(R.id.swipeLayout);
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
        date = DateUtils.getSysTime2();

        bihuAdapter = new BihuListAdapter(getActivity(), homeStories);
        homeDataListView.setAdapter(bihuAdapter);
        info = (ThemeMainInfo) getArguments().getSerializable("themeMainInfo");
        requestData(0);
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_1, R.color.swipe_color_1, R.color.swipe_color_1);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.swipe_background_color);
        swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(0);
            }
        });
        swipeRefreshLayout.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad() {
                requestData(1);
            }
        });

        homeDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story story = (Story) parent.getAdapter().getItem(position);
                if (!story.isDateStr()) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), NewsDetailsActivity.class);
                    intent.putExtra("story", story);
                    startActivityForResult(intent, 222);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 请求数据
     *
     * @param isLoadMore 是否是上拉加载更多（0-否，1-是）
     */
    private void requestData(int isLoadMore) {
        int id = info.getId();
        if (-1 == id) {
            if (isLoadMore == 0) {
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
                                        getHeadView(null);
                                    } else {
                                        headLinear.setVisibility(View.GONE);
                                    }
                                    homeStories.clear();
                                    homeStories.addAll(data.getStories());
                                    bihuAdapter.notifyDataSetChanged();
                                } else {
                                    showToast("请求数据失败");
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            showToast("网络错误");
                        }
                    }
                }).execute();
            } else {
                new RequestAsyncTask(getActivity(), Constant.BEFORE_NEWS_PATH + date, new AsyncTaskCallBack() {
                    @Override
                    public void post(String rest) {
                        try {
                            JSONObject object = new JSONObject(rest);
                            BihuMenu data = com.alibaba.fastjson.JSONObject.parseObject(object.toString(), BihuMenu.class);
                            if (DateUtils.getBeforeDay(date).equals(data.getDate())) {
                                Story story = new Story();
                                story.setDateStr(true);
                                story.setTitle(DateUtils.getDateStr(DateUtils.getBeforeDay(date)));
                                homeStories.add(story);
                                homeStories.addAll(data.getStories());
                                bihuAdapter.notifyDataSetChanged();
                                date = DateUtils.getBeforeDay(date);
                            } else {
                                showToast("请求数据失败");
                            }
                            swipeRefreshLayout.setLoading(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).execute();
            }
        } else {
            new RequestAsyncTask(getActivity(), Constant.NEWSLIST_URL + id, new AsyncTaskCallBack() {
                @Override
                public void post(String rest) {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    if (!TextUtils.isEmpty(rest)) {
                        BihuContent content = com.alibaba.fastjson.JSONObject.parseObject(rest.toString(), BihuContent.class);
                        homeStories.clear();
                        homeStories.addAll(content.getStories());
                        bihuAdapter.notifyDataSetChanged();
                        getHeadView(info);
                    } else {
                        showToast("网络错误");
                    }
                    swipeRefreshLayout.setRefreshing(false);
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
        titleTv = (TextView) headView.findViewById(R.id.titleTv);
        headIv = (ImageView) headView.findViewById(R.id.headIv);
        headAdapter = new HeadAdapter(getFragmentManager(), headFragments);
        headVp.setAdapter(headAdapter);
        homeDataListView.addHeaderView(headView);
    }

    /**
     * 加载或者刷新头视图
     */
    private void getHeadView(ThemeMainInfo tMi) {
        if (tMi == null) {
            headIv.setVisibility(View.GONE);
            headVp.setVisibility(View.VISIBLE);
            headLinear.setVisibility(View.VISIBLE);

            titleTv.setText("今日热闻");
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
                hFragment.setImageUrl(topStories.get(i).getImage());
                headFragments.add(hFragment);
            }

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
            headAdapter.setFragments(headFragments);
            stopTimer();
            startTimer();
        } else {
            stopTimer();
            headVp.setVisibility(View.GONE);
            headIv.setVisibility(View.VISIBLE);
            headLinear.setVisibility(View.GONE);
            ImageDownLoader loader = BihuApplication.getApp().getImageDownLoaderInstance();
            headIv.setTag(info.getThumbnail());
            Bitmap bitmap = loader.downLoader(headIv, new ImageDownLoader.ImageLoaderlistener() {
                @Override
                public void onImageLoader(Bitmap bitmap, ImageView imageView) {
                    if (imageView.getTag() != null && imageView.getTag().equals(info.getThumbnail())) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
            if (bitmap != null) {
                headIv.setImageBitmap(bitmap);
            }
            titleTv.setText(tMi.getName());
            headTv.setText(tMi.getDescription());
        }
    }

    /**
     * 开始Timer任务
     */
    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(count);
                    count++;
                }
            };
        }
        if (timer != null && task != null) {
            timer.schedule(task, 3000, 3000);
        }
    }

    /**
     * 暂停Timer任务
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        count = 0;
    }

    /**
     * dp转成px
     */
    public int dip2px(float dipValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void timerCallBack() {
        stopTimer();
        startTimer();
    }

}
