package com.myx.feng.mvpdemo;

import com.myx.feng.app.ApiServiceTest;
import com.myx.feng.rxjavademo.NewsResult;
import com.myx.library.rxjava.Api;
import com.myx.library.rxjava.RxSchedulers;

import rx.Observable;

/**
 * Created by mayuxin on 2017/4/6.
 */

public class MvpMoudleImpl implements MvpContans.Module {
    @Override
    public Observable<NewsResult> getImage(String articleid, String cache_control, String timestamp) {
        return Api.getDefault(ApiServiceTest.ApiService.class).getDetail(articleid, cache_control, timestamp).compose(RxSchedulers.<NewsResult>io_main());
    }
}
