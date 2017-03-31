package com.myx.feng.rxjavademo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.myx.library.util.ACache;

import rx.Subscriber;

/**
 * 为 retrofit 配置文件缓存
 * 如果使用文件缓存 择请求的Cache-Control :CACHE_CONTROL_AGE
 * 如果客户端所有请求都使用 文件缓存 则可以自定义配置 OkHttpClient.Builder；
 * Created by mayuxin on 2017/3/30.
 */

public abstract class CaheSubscribe<T extends BaseResult> extends Subscriber<T> {
    private Context context;
    private String cacheKey;
    private boolean isGetFile;
    private boolean isGetWeb;

    public CaheSubscribe(Context ctx, String cacheKey, boolean isGetFile, boolean isGetWeb) {
        this.context = ctx;
        this.cacheKey = cacheKey;
        this.isGetFile = isGetFile;
        this.isGetWeb = isGetWeb;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context instanceof Activity && isGetFile) {
            final T t = (T) ACache.get(context).getAsObject(cacheKey);
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (t != null) {
                        t.getResult().setSource("file");
                        superNext(t);
                    }
                }
            });
        }

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        superError(e);
    }

    @Override
    public void onNext(T t) {// 非文件的数据回调
        if (t != null && isGetWeb) {
            t.getResult().setSource("web");
            superNext(t);
            if (t.getResult().getErrorCode() == 0) {
                ACache.get(context).put(cacheKey, t);
            }
        }
    }

    public abstract void superNext(T t);

    public abstract void superError(Throwable e);

}
