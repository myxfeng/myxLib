package com.myx.feng.rxjavademo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import rx.Subscriber;

/**
 *  为 retrofit 配置文件缓存
 * Created by mayuxin on 2017/3/30.
 */

public abstract class CaheSubscribe<T extends BaseResult> extends Subscriber<T> {
    private Context context;
    private String cacheKey;
    private boolean isGetCache;
    private boolean isGetWeb;
    public CaheSubscribe(Context ctx, String cacheKey,boolean isGetCahce,boolean isGetWeb) {
        this.context = ctx;
        this.cacheKey = cacheKey;
        this.isGetCache=isGetCahce;
        this.isGetWeb=isGetWeb;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context instanceof Activity &&isGetCache) {
            final T t = (T) ACache.get(context).getAsObject(cacheKey);
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (t != null) {
                        t.getResult().setSource("file");
                        Log.i("ttttt",t.toString());
                        onNext(t);
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
    public void onNext(T t) {
        if (t != null&&isGetWeb) {
            superNext(t);
            if (t.getResult().getErrorCode() == 0) {
                ACache.get(context).put(cacheKey, t);
            }
        }
    }

    public abstract void superNext(T t);

    public abstract void superError(Throwable e);

}
