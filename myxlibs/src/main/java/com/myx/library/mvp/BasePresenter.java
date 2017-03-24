package com.myx.library.mvp;

import android.content.Context;

import com.myx.library.rxjava.RxManager;


/**
 * Created by mayuxin on 2017/3/23.
 */

public class BasePresenter<T extends BaseView, E extends BaseModel> {
    public Context mContext;
    public T mView;
    public E mModel;
    RxManager rxManager = new RxManager();

    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart() {
    }

    ;

    public void onDestroy() {
        rxManager.clear();
    }
}
