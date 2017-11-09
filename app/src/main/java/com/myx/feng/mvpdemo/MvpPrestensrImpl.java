package com.myx.feng.mvpdemo;

import com.myx.feng.rxjavademo.NewsResult;
import com.myx.library.rxjava.RxSchedulers;
import com.myx.library.rxjava.RxSubscriber;

import rx.Subscriber;

/**
 * Created by mayuxin on 2017/4/6.
 */

public class MvpPrestensrImpl extends MvpContans.Presenter {
    @Override
    void getImage(String articleid, String cache_control, String timestamp) {
        mRxManager.add(mModel.getImage(articleid, cache_control, timestamp).subscribe(new Subscriber<NewsResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewsResult newsResult) {
                mView.onGetImageSuccess(newsResult.getData().getCover());
            }
        }));

    }
}
