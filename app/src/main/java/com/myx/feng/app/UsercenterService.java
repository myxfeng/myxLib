package com.myx.feng.app;

import com.myx.library.rxjava.BaseUrl;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mayuxin on 2017/3/23.
 */
@BaseUrl(host = AppContans.SERVER__USER_API,port = AppContans.SERVER__USER_PORT)
public  interface UsercenterService {
    @GET("/getname")
    Observable<String> getName(@Query("userid") String userid);
}
