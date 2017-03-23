package com.myx.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by mayuxin on 2017/3/23.
 */

public class ScrollViewPager extends ViewPager{
    private Context context;
    private boolean isScrollable = false;

    public ScrollViewPager(Context context) {
        super(context);
        this.context = context;
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable) {
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {
        if (isScrollable) {
            super.setCurrentItem(item);
        } else {
            setCurrentItem(item, false);
        }
    }

    public void setDuration(long duration) {
        ViewPagerScroller scroller = new ViewPagerScroller(context);
        scroller.setScrollDuration(0);
        scroller.initViewPagerScroll(this);
    }
}
