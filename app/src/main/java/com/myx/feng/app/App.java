package com.myx.feng.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.myx.library.image.ImageUtils;
import com.myx.library.rxjava.Api;
import com.myx.library.util.LogUtils;


//import com.myx.library.image.ImageUtils;

/**
 * Created by mayuxin on 2017/3/17.
 */
public class App extends Application implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onCreate() {
        super.onCreate();
        Debug.startMethodTracing("appstart");
        Api.init(this);
        ImageUtils.initialize(this);
        Debug.stopMethodTracing();
        LogUtils.init(true, "myx");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i("", "");
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
