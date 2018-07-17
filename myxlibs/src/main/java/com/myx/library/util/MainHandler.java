package com.myx.library.util;

import android.os.Handler;
import android.os.Looper;

/**
 * 运行UI主线程
 * Created by mayuxin on 2017/6/2.
 */

public class MainHandler extends Handler {
    private static MainHandler instance = new MainHandler(Looper.getMainLooper());

    public MainHandler() {
        this(Looper.getMainLooper());
    }

    private MainHandler(Looper looper) {
        super(looper);
    }

    public static MainHandler getInstance() {
        return instance;
    }

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            runnable.run();
        } else {
            instance.post(runnable);
        }

    }
}
