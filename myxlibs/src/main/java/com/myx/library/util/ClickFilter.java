package com.myx.library.util;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class ClickFilter {

    private long interval = 1000; // 防止连续点击的时间间隔
    private long lastClickTime = 0L; // 上一次点击的时间

    public void setDelayClickTime(long delayClickTime) {
        long time = System.currentTimeMillis();
        lastClickTime = time + delayClickTime;
    }

    private ClickFilter() {

    }

    public ClickFilter(long interval) {
        this.interval = interval;
    }

    public long getLastClickTime() {
        return lastClickTime;
    }

    public void setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
    }

    public long getInterval() {
        return interval;
    }

    public boolean isClickable() {
        long time = System.currentTimeMillis();
        if ((time - lastClickTime) > interval) {
            lastClickTime = time;
            return true;
        }
        return false;
    }
}
