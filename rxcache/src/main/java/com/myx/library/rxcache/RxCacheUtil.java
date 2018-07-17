package com.myx.library.rxcache;


import android.app.Application;


import com.myx.library.rxcache.converter.GsonDiskConverter;
import com.myx.library.rxcache.model.CacheMode;

import java.io.File;

import io.reactivex.Observable;

/**
 * @author mayuxin
 * @date 2018/6/13
 */
public class RxCacheUtil {
    /**
     * DEFAULT：不使用缓存，该模式下,cacheKey,cacheTime 等参数均无效
     * FIRSTREMOTE：先请求网络，请求网络失败后再加载缓存
     * FIRSTCACHE：先加载缓存，缓存没有再去请求网络
     * ONLYREMOTE：仅加载网络，但数据依然会被缓存
     * ONLYCACHE：只读取缓存
     * CACHEANDREMOTE:先使用缓存，不管是否存在，仍然请求网络，CallBack会回调两次.
     * CACHEANDREMOTEDISTINCT:先使用缓存，不管是否存在，仍然请求网络，CallBack回调不一定是两次，如果发现请求的网络数据和缓存数据是相同的，就不会再返回网络的回调，即只回调一次。若不相同仍然会回调两次。（目的是为了防止数据没有发生变化，也需要回调两次导致界面无用的重复刷新）
     */

    /**
     * 仅加载缓存
     */
    public static int ONLY_CACHE = 0;

    /**
     * 仅加载网络
     */
    public static int ONLY_NET = 1;
    /**
     * 缓存 网络 先后加载
     */
    public static int CACHE_NET = 2;
    /**
     * 缓存网络 网络数据与缓存数据不同时在加载网络
     */
    public static int CACHE_NET_DISTINCT = 3;
    /**
     * 网络、缓存  网洛请求失败时加载缓存
     */
    public static int NET_CACHE = 4;

    /**
     * 先加载缓存 缓存没有在加载网络
     */
    public static int FIRST_CACHE=5;

    public static void init(Application application) {
        RxCacheProvider.getInstance()
                .setCacheDirectory(new File(application.getApplicationContext().getExternalCacheDir(), "rxcache")) // 缓存目录，必须
                .setCacheDiskConverter(new GsonDiskConverter()) // 设置数据转换器默认使用GsonDiskConverter
                .setCacheMaxSize(Integer.MAX_VALUE) // 最大缓存空间，默认50M
                .setCacheVersion(1)// 缓存版本为1
                .setCacheTime(-1) // 缓存时间，默认-1表示永久缓存
                .build();
    }


    public static <T> RequestApi cache(Observable<T> observable, int cacheMode, String cacheKey) {
        return RxCacheProvider.api(observable).cacheKey(cacheKey)
                .cacheMode(getCacheMode(cacheMode));
    }

    private static CacheMode getCacheMode(int cacheMode) {
        if (cacheMode == ONLY_CACHE) {
            return CacheMode.ONLYCACHE;
        } else if (cacheMode == ONLY_NET) {
            return CacheMode.ONLYREMOTE;
        } else if (cacheMode == CACHE_NET) {
            return CacheMode.CACHEANDREMOTE;
        } else if (cacheMode == CACHE_NET_DISTINCT) {
            return CacheMode.CACHEANDREMOTEDISTINCT;
        } else if (cacheMode == NET_CACHE) {
            return CacheMode.FIRSTREMOTE;
        } else if (cacheMode==FIRST_CACHE){
            return CacheMode.FIRSTCACHE;
        } else{
            return CacheMode.FIRSTREMOTE;
        }
    }


}
