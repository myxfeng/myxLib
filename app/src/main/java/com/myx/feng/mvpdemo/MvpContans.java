package com.myx.feng.mvpdemo;

import com.myx.feng.rxjavademo.NewsResult;
import com.myx.library.mvp.BaseModel;
import com.myx.library.mvp.BasePresenter;
import com.myx.library.mvp.BaseView;

import rx.Observable;

/**
 * Created by mayuxin on 2017/4/6.
 */

public class MvpContans {
    interface Module extends BaseModel {
        Observable<NewsResult> getImage(String articleid, String cache_control,String timestamp);
    }

    interface View extends BaseView {
        void onGetImageSuccess(String imageUrl);
    }

    abstract static class Presenter extends BasePresenter<View, Module> {
        abstract void getImage(String articleid, String cache_control,String timestamp);
    }

}
