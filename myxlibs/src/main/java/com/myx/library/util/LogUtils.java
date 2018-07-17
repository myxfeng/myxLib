package com.myx.library.util;

import android.util.Log;

/**
 * Created by mayuxin on 2018/1/30.
 */

public class LogUtils {

    private static boolean DEBUG = true;
    private static String TAG = "myx";

    public static void init(boolean debug, String tag) {
        DEBUG = debug;
        TAG = tag;
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (DEBUG) {
            Log.i(TAG, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.v(tag, msg, tr);
        }
    }

    public static void v(String msg) {
        if (DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (DEBUG) {
            Log.v(TAG, msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.v(tag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (DEBUG) {
            Log.e(TAG, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.e(tag, msg, tr);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(TAG, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (DEBUG) {
            Log.w(TAG, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.w(tag, msg, tr);
        }
    }
}
