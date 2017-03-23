package com.myx.feng.app;

import com.myx.library.rxjava.URL;

import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by mayuxin on 2017/3/23.
 */
@URL(host = "http://api.people.cn",port = ":80")
public interface ApiService  {
    @GET("/getList")
    String getNewsList(@Query("tagId") String tagId);
}
