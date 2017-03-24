package com.myx.feng.app;

import com.myx.feng.NewsResult;
import com.myx.library.rxjava.BaseUrl;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by mayuxin on 2017/3/23.
 */
@BaseUrl(host = AppContans.SERVER_API,port =AppContans.SERVER_PORT)
public interface ApiService  {
    @GET("/sports/content/getdetail")
    Observable<NewsResult> getDetail(@Query("articleid") String articleid);
}
