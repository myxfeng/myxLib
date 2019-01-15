package com.myx.library.rxjava2;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author mayuxin
 */
public abstract class RxObserver<T> implements Observer<T> {
    private String cache_control = "";

    public RxObserver(String cache_control) {
        this.cache_control = cache_control;
    }

    public RxObserver() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
    }

    public abstract void onError(int errorCode);
}
