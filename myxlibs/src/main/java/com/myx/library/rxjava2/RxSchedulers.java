package com.myx.library.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * http 线程调度，请求在io 线程（子线程）, 响应回调Ui线程
 * Created by mayuxin on 2018/6/1.
 */

public class RxSchedulers {
    /**
     * 跟compose()配合使用,比如ObservableUtils.wrap(obj).compose(toMain())
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_Main() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
