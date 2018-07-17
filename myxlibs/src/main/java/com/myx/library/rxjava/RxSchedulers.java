package com.myx.library.rxjava;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 自定义RxAndroid调度管理
 *
 * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
 *
 * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
 *
 * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
 * 行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，
 * 可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。
 * 不要把计算工作放在 io() 中，可以避免创建不必要的线程。
 *
 * Schedulers.computation(): 计算所使用的 Scheduler。
 * 这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。
 * 这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。
 * 不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 *
 * 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
 *
 * Created by mayuxin on 2016/11/29.
 */

public class RxSchedulers {

    public static <T> Observable.Transformer<T, T> io_main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()) //指定subscribe()发生在IO线程，
                        .observeOn(AndroidSchedulers.mainThread()); //指定Subscriber 的回调发生在主线程，
            }
        };
    }

}
