package com.myx.feng.app;

import com.myx.feng.rxjavademo.CollectResult;
import com.myx.library.rxjava.BaseUrl;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mayuxin on 2017/3/23.
 */
@BaseUrl(host = AppContans.SERVER__USER_API,port = AppContans.SERVER__USER_PORT)
public  interface UsercenterService {
    @GET("/sports/userinfo/api/v2/favor/sync")
    Observable<CollectResult> syncCollect(@Query("sessionId") String sessionId);
}
