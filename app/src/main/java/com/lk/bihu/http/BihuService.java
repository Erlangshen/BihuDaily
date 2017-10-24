package com.lk.bihu.http;

import com.lk.bihu.bean.AllThemeMainInfo;
import com.lk.bihu.bean.BihuContent;
import com.lk.bihu.bean.BihuMenu;
import com.lk.bihu.bean.NewsDetail;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by liu kun on 2017/10/20 0020.
 */

public interface BihuService {
    @GET("news/{date}")
    Observable<BihuMenu> getHomeData(@Path("date") String date);

    @GET("news/{id}")
    Observable<NewsDetail> getDetails(@Path("id") int id);

    @GET("themes")
    Observable<AllThemeMainInfo> getMenu();

    @GET("news/before/{date}")
    Observable<BihuMenu> getBeforeData(@Path("date") String date);

    @GET("theme/{id}")
    Observable<BihuContent> getContent(@Path("id") int id);
}
