package com.myx.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;


/**
 * 自定义高度的viewpapger
 */
public class BaseViewPager extends ViewPager {

    private ViewGroup parent;
    private float lastX;
    private float lastY;
    public boolean isFixTouch = true;
    private boolean scrollable = true;

    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.parent = (ViewGroup) getParent();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isFixTouch) {
            if (parent == null) {
                return super.dispatchTouchEvent(ev);
            }
            float x = ev.getX();
            float y = ev.getY();

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    parent.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveX = x - lastX;
                    float moveY = y - lastY;
                    if (Math.abs(moveX) / Math.abs(moveY) > 1) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    } else {
                        parent.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }
            lastX = x;
            lastY = y;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置viewpager是否可以滚动
     *
     * @param enable
     */
    public void setScrollable(boolean enable) {
        scrollable = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (scrollable) {
            return super.onInterceptTouchEvent(event);
        } else {
            return false;
        }
    }
}