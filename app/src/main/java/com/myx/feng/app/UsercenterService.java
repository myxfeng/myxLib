package com.myx.feng.app;

import com.myx.library.rxjava.URL;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mayuxin on 2017/3/23.
 */
@URL(host = "http://user.people.com",port = ":8080")
public  interface UsercenterService {
    @GET("/getname")
    Observable<String> getName(@Query("userid") String userid);
}
