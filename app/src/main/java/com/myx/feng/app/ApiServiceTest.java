package com.myx.feng.app;

import com.myx.feng.rxjavademo.CollectResult;
import com.myx.feng.rxjavademo.NewsData;
import com.myx.feng.rxjavademo.NewsResult;
import com.myx.library.rxjava.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 多种host 的另一种写法
 * Created by mayuxin on 2017/3/24.
 */

public class ApiServiceTest {
    @BaseUrl(host = AppContans.SERVER__USER_API)// 用户中心host
    public static interface UserService {
        @GET("/sports/userinfo/api/v2/favor/sync")
        Observable<CollectResult> syncCollect(@Query("sessionId") String sessionId, @Header("Cache-Control") String cache_control);
    }

    @BaseUrl(host = AppContans.SERVER_API) // 普通新闻Host
    public static interface ApiService {
        @GET("/sports/content/getdetail")
        Observable<NewsResult> getDetail(@Query("articleid") String articleid, @Header("Cache-Control") String cache_control, @Query("timestamp") String timestamp);

        @POST("/sports/content/getdetail")
        Observable<NewsResult> getDetail(@Body NewsData data);

        @FormUrlEncoded
        @POST("/sports/content/getdetail")
        Observable<NewsResult> getDetail(@Field("articleid") String articleid,  @Field("timestamp") String timestamp);
        @GET("/sports/content/getList")
        Observable<NewsResult> getList(@Query("tagId") String tagId);
    }
}
