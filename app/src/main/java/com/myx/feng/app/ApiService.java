package com.myx.feng.app;

import com.myx.feng.rxjavademo.NewsResult;
import com.myx.library.rxjava.BaseUrl;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * 相同host 的api 放在同一个interface中  即一个host 对应apiservice
 * 为了编写方便 也也可以将2个interface (2个host) 放在同一个文件中（java 最基本的写法）
 * Created by mayuxin on 2017/3/23.
 */
@BaseUrl(host = AppContans.SERVER_API,port =AppContans.SERVER_PORT)
public interface ApiService  {
    @GET("/sports/content/getdetail")
    Observable<NewsResult> getDetail(@Query("articleid") String articleid);
}

