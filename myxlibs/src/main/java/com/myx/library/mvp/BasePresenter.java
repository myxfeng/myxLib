package com.myx.library.mvp;

import android.content.Context;


/**
 * Created by mayuxin on 2017/3/23.
 */

public class BasePresenter<T extends BaseView, E extends BaseModel> {
    public Context mContext;
    public T mView;
    public E mModel;

    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart() {
    }

    ;

    public void onDestroy() {
    }
}
