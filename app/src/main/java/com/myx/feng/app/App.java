package com.myx.feng.app;

import android.app.Application;

import com.myx.library.image.ImageUtils;
import com.myx.library.rxjava.Api;


//import com.myx.library.image.ImageUtils;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Api.init(this);
        ImageUtils.initialize(this);
    }
}
