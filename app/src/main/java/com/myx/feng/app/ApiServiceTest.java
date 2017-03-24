package com.myx.feng.app;

import com.myx.feng.CollectResult;
import com.myx.feng.NewsResult;
import com.myx.library.rxjava.BaseUrl;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 多种host 的另一种写法
 * Created by mayuxin on 2017/3/24.
 */

public class ApiServiceTest {
    @BaseUrl(host = AppContans.SERVER__USER_API)// 用户中心host
    public static interface UserService{
        @GET("/sports/userinfo/api/v2/favor/sync")
        Observable<CollectResult> syncCollect(@Query("sessionId") String sessionId);
    }
    @BaseUrl(host = AppContans.SERVER_API) // 普通新闻Host
    public static interface ApiService{
        @GET("/sports/content/getdetail")
        Observable<NewsResult> getDetail(@Query("articleid") String articleid);
    }
}
