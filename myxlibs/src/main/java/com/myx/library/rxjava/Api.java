package com.myx.library.rxjava;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myx.library.util.Futils;
import com.myx.library.util.UseAgentUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/*
    Api使用实例

    Api.init();

    接口编写实例
    import com.myx.library.rxjava.BaseUrl;

    import retrofit2.http.GET;
    import retrofit2.http.Query;


    @BaseUrl(host = "http://api.people.cn",port = ":80") // 配置路径
    public interface ApiService  {
        @GET("/getList")// 配置接口api
        String getNewsList(@Query("tagId") String tagId);
    }
————————————缓存配置——————————————————————————————
    1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/

/**
 * Created by mayuxin on 2017/3/23.
 */
public class Api {

    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 20000;

    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 20000;

    public static final int WRITE_TIME_OUT = 20000;
    private Retrofit retrofit;


    private static HashMap<String, Api> sRetrofitManager = new HashMap<>();

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = Integer.MAX_VALUE;
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;//CacheControl.FORCE_CACHE
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    public static final String CACHE_CONTROL_NET = "max-age=0";

    private static Application application;
    private static OkHttpClient.Builder builder;
    private static ParamsInterceptor paramsInterceptor;


    /**
     * 自定义okhttpclient
     *
     * @param application
     * @param builder
     */
    public static void init(Application application, OkHttpClient.Builder builder) {
        Api.application = application;
        Api.builder = builder;
    }

    /**
     * @param application
     * @param paramsInterceptor 公共参数
     */
    public static void init(Application application, ParamsInterceptor paramsInterceptor) {
        Api.application = application;
        Api.paramsInterceptor = paramsInterceptor;
    }

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        Api.application = application;
    }


    private Api(String baseUrl, int read_time_out, int connect_time_out, int write_time_out) {
        if (application == null) {
            throw new NullPointerException("Api not init at Application onCreate()");
        }
        OkHttpClient okHttpClient;
        if (builder != null) {
            okHttpClient = builder.build();
        } else {
            //开启Log
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //缓存

            File cacheFile = new File(application.getExternalCacheDir(), "cache");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

            //增加头部信息
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request build = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(build);
                }
            };
            OkHttpClient.Builder temp = new OkHttpClient.Builder()
                    .readTimeout(read_time_out, TimeUnit.MILLISECONDS)
                    .connectTimeout(connect_time_out, TimeUnit.MILLISECONDS)
                    .writeTimeout(write_time_out, TimeUnit.MILLISECONDS)
                    .addInterceptor(mRewriteCacheControlInterceptor)
                    .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logInterceptor)
                    .addInterceptor(new UserAgentInterceptor(UseAgentUtils.getUserAgent(application)))
                    .cache(cache);
            if (paramsInterceptor != null) {
                temp.addInterceptor(paramsInterceptor);
            }
            okHttpClient = temp.build();
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }


    public static <T> T getDefault(Class<T> cls, String baseUrl, int read_time_out, int connect_time_out, int write_time_out) {
        String url = baseUrl;
        if (!TextUtils.isEmpty(url)) {
            Api retrofitManager = new Api(url, read_time_out, connect_time_out, write_time_out);
            return retrofitManager.retrofit.create(cls);
        } else {
            Api retrofitManager = new Api(url, read_time_out, connect_time_out, write_time_out);
            return retrofitManager.retrofit.create(cls);
        }

    }

    public static <T> T getDefault(Class<T> cls, String baseUrl) {
        return getDefault(cls, baseUrl, READ_TIME_OUT, CONNECT_TIME_OUT, WRITE_TIME_OUT);
    }


    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    public static String getCacheControl() {
        return Futils.isNetConnected(application) ? CACHE_CONTROL_NET : CACHE_CONTROL_CACHE;
    }


    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if (!Futils.isNetConnected(application)) {
                if (!cacheControl.contains(CACHE_CONTROL_NET)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                } else {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();
                }
            }
            Response originalResponse = chain.proceed(request);
            if (Futils.isNetConnected(application)) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置

                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


}
