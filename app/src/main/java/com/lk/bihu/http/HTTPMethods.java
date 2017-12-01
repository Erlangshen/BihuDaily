package com.lk.bihu.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lk.bihu.bean.AllThemeMainInfo;
import com.lk.bihu.bean.BihuContent;
import com.lk.bihu.bean.BihuMenu;
import com.lk.bihu.bean.NewsDetail;
import com.lk.bihu.constant.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liu kun on 2017/10/20 0020.
 */

public class HTTPMethods {
    private Retrofit retrofit;
    private BihuService bihuService;
    private final static int TIME_OUT = 20;
    private static HTTPMethods httpMethods;

    private HTTPMethods() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();
        bihuService = retrofit.create(BihuService.class);
    }

    public static HTTPMethods getInstance() {
        if (httpMethods == null)
            httpMethods = new HTTPMethods();
        return httpMethods;
    }


    /**
     * 获取最新数据
     *
     * @param subscriber
     * @param date
     */
    public void getHomeData(Subscriber<BihuMenu> subscriber, String date) {
        Observable observable = bihuService.getHomeData(date);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取新闻详情
     *
     * @param subscriber
     * @param id
     */
    public void getDetails(Subscriber<NewsDetail> subscriber, int id) {
        Observable observable = bihuService.getDetails(id);
        toSubscribe(observable, subscriber);
    }

    /**
     * 过往新闻，后面加日期 20160502请求的是20160501的新闻
     *
     * @param subscriber
     * @param date
     */
    public void getBeforeData(Subscriber<BihuMenu> subscriber, String date) {
        Observable observable = bihuService.getBeforeData(date);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取菜单列表
     *
     * @param subscriber
     */
    public void getMenu(Subscriber<AllThemeMainInfo> subscriber) {
        Observable observable = bihuService.getMenu();
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取新闻列表
     *
     * @param subscriber
     * @param id
     */
    public void getContent(Subscriber<BihuContent> subscriber, int id) {
        Observable<BihuContent> observable = bihuService.getContent(id);
        toSubscribe(observable, subscriber);
    }

    /**
     * 加载图片
     * @param subscriber
     * @param url
     */
    public void loadImage(Subscriber<Bitmap> subscriber, String url) {
        Observable<ResponseBody> observable = bihuService.loadImage(url);
        observable.subscribeOn(Schedulers.newThread())
                .map(new Func1<ResponseBody, Bitmap>() {

                    @Override
                    public Bitmap call(ResponseBody responseBody) {
                        try {
                            byte[] bytes = responseBody.bytes();
                            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void toSubscribe(Observable o, Subscriber s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
