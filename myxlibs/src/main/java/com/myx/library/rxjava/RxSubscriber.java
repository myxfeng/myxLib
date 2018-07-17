package com.myx.library.rxjava;

import android.content.Context;


import com.myx.library.util.Futils;

import rx.Subscriber;

/**
 * 对Subscriber的简单封装，在网络请求过程中实现加载时，请求成功时，请求失败时的简单处理
 *
 * Created by mayuxin on 2016/12/12.
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    public RxSubscriber(Context context, String msg,boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog=showDialog;
    }

    public RxSubscriber(Context context,boolean showDialog){
        this(context,"加载中....",showDialog);
    }

    public RxSubscriber(Context context) {
        this(context, "加载中....",true);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T t) {
        OnNext(t);
    }

    @Override
    public void onError(Throwable e) {
        //网络
        if (!Futils.isNetConnected(mContext)) {
            OnError("");
        }
        //服务器
        else if (e instanceof Exception) {
            OnError(e.getMessage());
        }
        //其它
        else {
            OnError("网络连接错误");
        }
    }

    protected abstract void OnNext(T t);

    protected abstract void OnError(String message);
}
