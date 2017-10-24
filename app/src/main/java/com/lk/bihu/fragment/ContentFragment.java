package com.lk.bihu.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lk.bihu.R;
import com.lk.bihu.activity.NewsDetailsActivity;
import com.lk.bihu.adapter.BihuListAdapter;
import com.lk.bihu.adapter.HeadAdapter;
import com.lk.bihu.bean.BihuContent;
import com.lk.bihu.bean.BihuMenu;
import com.lk.bihu.bean.Story;
import com.lk.bihu.bean.ThemeMainInfo;
import com.lk.bihu.bean.TopStory;
import com.lk.bihu.http.HTTPMethods;
import com.lk.bihu.http.MySubscriber;
import com.lk.bihu.http.SubscriberListener;
import com.lk.bihu.interfaces.OnLoadListener;
import com.lk.bihu.interfaces.TimerCallBack;
import com.lk.bihu.utils.DateUtils;
import com.lk.bihu.view.SwipeRefreshView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

public class ContentFragment extends BaseFragment implements TimerCallBack {
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
    private int mTouchSlop;//viewpager滑动距离临界值
    @Bind(R.id.contentList)
    ListView homeDataListView;//首页listview
    @Bind(R.id.swipeLayout)
    SwipeRefreshView swipeRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.content_fragment;
    }

    @Override
    protected void initData() {
        if (homeStories == null)
            homeStories = new ArrayList<Story>();
        if (topStories == null)
            topStories = new ArrayList<TopStory>();
        if (headFragments == null)
            headFragments = new ArrayList<BaseFragment>();
        ViewConfiguration configuration = ViewConfiguration.get(getActivity());
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

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
                try {
                    Story story = (Story) parent.getAdapter().getItem(position);
                    if (story != null && !story.isDateStr()) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), NewsDetailsActivity.class);
                        intent.putExtra("story_id", story.getId());
                        startActivityForResult(intent, 222);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                SubscriberListener listener = new SubscriberListener<BihuMenu>() {
                    @Override
                    public void onNext(BihuMenu bihuMenu) {
                        if (DateUtils.getSysTime2().equals(bihuMenu.getDate())) {
                            topStories.clear();
                            topStories.addAll(bihuMenu.getTop_stories());
                            if (topStories.size() > 1) {
                                headLinear.setVisibility(View.VISIBLE);
                                getHeadView(null);
                            } else {
                                headLinear.setVisibility(View.GONE);
                            }
                            homeStories.clear();
                            homeStories.addAll(bihuMenu.getStories());
                            bihuAdapter.notifyDataSetChanged();
                        } else {
                            showToast("请求数据失败");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                };
                HTTPMethods.getInstance().getHomeData(new MySubscriber<BihuMenu>(listener, getActivity()), "latest");
            } else {
                SubscriberListener listener = new SubscriberListener<BihuMenu>() {
                    @Override
                    public void onNext(BihuMenu bihuMenu) {
                        if (DateUtils.getBeforeDay(date).equals(bihuMenu.getDate())) {
                            Story story = new Story();
                            story.setDateStr(true);
                            story.setTitle(DateUtils.getDateStr(DateUtils.getBeforeDay(date)));
                            homeStories.add(story);
                            homeStories.addAll(bihuMenu.getStories());
                            bihuAdapter.notifyDataSetChanged();
                            date = DateUtils.getBeforeDay(date);
                        } else {
                            showToast("请求数据失败");
                        }
                        swipeRefreshLayout.setLoading(false);
                    }
                };
                HTTPMethods.getInstance().getBeforeData(new MySubscriber<BihuMenu>(listener, getActivity()), date);
            }
        } else {
            SubscriberListener listener = new SubscriberListener<BihuContent>() {
                @Override
                public void onNext(BihuContent bihuContent) {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    homeStories.clear();
                    homeStories.addAll(bihuContent.getStories());
                    bihuAdapter.notifyDataSetChanged();
                    getHeadView(info);
                    swipeRefreshLayout.setRefreshing(false);
                }
            };
            HTTPMethods.getInstance().getContent(new MySubscriber<BihuContent>(listener,getActivity()),id);
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
            headVp.setOnTouchListener(new View.OnTouchListener() {
                int touchFlag = 0;
                float x = 0, y = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchFlag = 0;
                            x = event.getX();
                            y = event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float xDiff = Math.abs(event.getX() - x);
                            float yDiff = Math.abs(event.getY() - y);
                            if (xDiff < mTouchSlop && xDiff >= yDiff)
                                touchFlag = 0;
                            else
                                touchFlag = -1;
                            break;
                        case MotionEvent.ACTION_UP:
                            if (touchFlag == 0) {
                                int currentItem = headVp.getCurrentItem();
                                Intent it = new Intent();
                                it.setClass(getActivity(), NewsDetailsActivity.class);
                                it.putExtra("story_id", topStories.get(currentItem).getId());
                                startActivity(it);
                            }
                            break;
                    }
                    return false;
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
            Glide.with(getActivity())
                    .load(info.getThumbnail())
                    .error(R.drawable.defaultcovers)
                    .into(headIv);
//            ImageDownLoader loader = BihuApplication.getApp().getImageDownLoaderInstance();
//            headIv.setTag(info.getThumbnail());
//            Bitmap bitmap = loader.downLoader(headIv, new ImageDownLoader.ImageLoaderlistener() {
//                @Override
//                public void onImageLoader(Bitmap bitmap, ImageView imageView) {
//                    if (imageView.getTag() != null && imageView.getTag().equals(info.getThumbnail())) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }
//            });
//            if (bitmap != null) {
//                headIv.setImageBitmap(bitmap);
//            }
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

    @Override
    public void timerCallBack() {
        stopTimer();
        startTimer();
    }

}
