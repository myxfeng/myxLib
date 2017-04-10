package com.myx.library.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.myx.library.util.CheckUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayuxin on 2017/4/6.
 */

public abstract class CycleViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List converData = new ArrayList();// 转换后的数据 前后各加一个
    private List srcData;// 原数据
    private View indicatorView;
    private BaseViewPager viewPager;
    private BaseViewPager parentViewPager;
    private ViewPagerAdapter adapter;
    private CycleViewPagerHandler handler;
    private int time = 5000; // 默认轮播时间
    private int currentPosition = 0; // 轮播当前位置
    private boolean isScrolling = false; // 滚动框是否滚动着
    private boolean isCycle = false; // 是否循环
    private boolean isWheel = false; // 是否轮播
    private long releaseTime = 0; // 手指松开、页面不滚动时间，防止手机松开后短时间进行切换
    private int WHEEL = 100; // 转动
    private int WHEEL_WAIT = 101; // 等待
    private ImageCycleViewListener mImageCycleViewListener;
    public Context context;

    /**
     * @param context
     */
    public CycleViewPager(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CycleViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initLayout();
    }

    /**
     * 构造函数，此类需要依赖外部activity来构造
     *
     * @param context
     * @param attrs
     */
    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public void initLayout() {
        viewPager = new BaseViewPager(context);
        viewPager.setBackgroundColor(Color.parseColor("#000000"));
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(viewPager, 0, layoutParams);
        // 初始化指示器View
        indicatorView = getIndicatorView(context);
        if (indicatorView != null) {
            this.addView(indicatorView);
        }
        handler = new CycleViewPagerHandler(context) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int size = converData.size();
                if (msg.what == WHEEL && size != 0) {
                    if (!isScrolling) {
                        int max = size + 1;
                        int position = (currentPosition + 1) % size;
                        viewPager.setCurrentItem(position, true);
                        if (position == max) { // 最后一页时回到第一页
                            viewPager.setCurrentItem(1, false);
                        }
                    }

                    releaseTime = System.currentTimeMillis();
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, time);
                    return;
                }
                if (msg.what == WHEEL_WAIT && size != 0) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, time);
                }
            }
        };

    }


    public void setData(boolean isCycle, List list, ImageCycleViewListener listener) {
        this.isCycle = isCycle;
        List covertData = initImageUrls(list);
        setData(covertData, list, listener);

    }


    private void setData(List covertData, List list, ImageCycleViewListener listener) {
        setData(covertData, list, listener, 0);
    }

    /**
     * 初始化viewpager
     *
     * @param covertData   要显示的imageurl
     * @param showPosition 默认显示位置
     */
    private void setData(List covertData, List list, ImageCycleViewListener listener, int showPosition) {
        mImageCycleViewListener = listener;
        srcData = list;
        if (CheckUtils.isEmptyList(list)) {
            return;
        }
        this.converData.clear();

        if (covertData.size() == 0) {
            return;
        }

        this.converData = covertData;

        int ivSize = covertData.size();

        adapter = new ViewPagerAdapter();

        setIndicator(0, srcData.get(0), indicatorView);


        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        if (showPosition < 0 || showPosition >= ivSize)
            showPosition = 0;
        if (isCycle) {
            showPosition = showPosition + 1;
        }
        viewPager.setCurrentItem(showPosition);
        refreshData();
    }

    private int length;

    /**
     * 初始化图片url
     *
     * @param lists
     * @return
     */
    private List initImageUrls(List lists) {
        List urls = new ArrayList();
        if (CheckUtils.isEmptyList(lists)) {
            return urls;
        }
        int size = lists.size();
        if (size == 1) {//不循环-有几个填加几个
            urls.add(lists.get(0));
            // 设置循环，在调用setData方法前调用,1张图强制不循环
//            setCycle(false);
            this.isCycle = false;
        } else {//循环-头尾各多添加一个
            if (isCycle) {
                urls.add(lists.get(size - 1));
            }
            for (int i = 0; i < size; i++) {
                urls.add(lists.get(i));
            }
            if (isCycle) {
                // 将第一个View添加进来
                urls.add(lists.get(0));
            }
        }
        length = urls.size();
        return urls;
    }

//    /**
//     * 是否循环，默认不开启，开启前，请将views的最前面与最后面各加入一个视图，用于循环
//     *
//     * @param isCycle 是否循环
//     */
//    public void setCycle(boolean isCycle) {
//        this.isCycle = isCycle;
//    }

    /**
     * 是否处于循环状态
     *
     * @return
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 在setData后调用
     * 设置是否轮播，默认不轮播,轮播一定是循环的
     * @param isWheel
     */
    public void setWheel(boolean isWheel) {
        this.isWheel = isWheel;
        isCycle = true;
        if (isWheel && length > 1) {
            handler.postDelayed(runnable, time);
        }
    }

    /**
     * 是否处于轮播状态
     *
     * @return
     */
    public boolean isWheel() {
        return isWheel;
    }

    final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (context != null && !((Activity) context).isFinishing()
                    && isWheel) {
                long now = System.currentTimeMillis();
                // 检测上一次滑动时间与本次之间是否有触击(手滑动)操作，有的话等待下次轮播
                if (now - releaseTime > time - 500) {
                    handler.sendEmptyMessage(WHEEL);
                } else {
                    handler.sendEmptyMessage(WHEEL_WAIT);
                }
            }
        }
    };

    /**
     * 设置轮播暂停时间，即没多少秒切换到下一张视图.默认5000ms
     *
     * @param time 毫秒为单位
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * 设置宽高比
     */
    public void setRatio(float r) {
        LayoutParams params = (LayoutParams) this.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        int width = params.width;
        params.height = (int) (width * r);
        this.setLayoutParams(params);
    }

    /**
     * 刷新数据，当外部视图更新后，通知刷新数据
     */
    public void refreshData() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * 返回内置的viewpager
     *
     * @return viewPager
     */
    public BaseViewPager getViewPager() {
        return viewPager;
    }

    /**
     * 页面适配器 返回对应的view
     *
     * @author Yuedong Li
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return converData.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            Object data = converData.get(position);
            View v = getAdapterItemView(data, context);
            if (mImageCycleViewListener != null) {
                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            if (isCycle) {
                                mImageCycleViewListener.onImageClick(srcData.get(currentPosition - 1), currentPosition - 1, v);
                            } else {
                                mImageCycleViewListener.onImageClick(srcData.get(currentPosition), currentPosition, v);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            container.addView(v);
            return v;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (arg0 == 1) { // viewPager在滚动
            isScrolling = true;
            return;
        } else if (arg0 == 0) { // viewPager滚动结束
            if (parentViewPager != null)
                parentViewPager.setScrollable(true);

            releaseTime = System.currentTimeMillis();

            viewPager.setCurrentItem(currentPosition, false);

        }
        isScrolling = false;
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        int max = converData.size() - 1;
        int position = arg0;
        currentPosition = arg0;
        if (isCycle) {
            if (arg0 == 0) {
                currentPosition = max - 1;
            } else if (arg0 == max) {
                currentPosition = 1;
            }
            position = currentPosition - 1;
        }
        if (position < srcData.size()) {
            setIndicator(position, srcData.get(position), indicatorView);
        }
    }

    /**
     * 设置viewpager是否可以滚动
     *
     * @param enable
     */
    public void setScrollable(boolean enable) {
        viewPager.setScrollable(enable);
    }

    /**
     * 返回当前位置,循环时需要注意返回的position包含之前在views最前方与最后方加入的视图，即当前页面试图在views集合的位置
     *
     * @return
     */
    public int getCurrentPostion() {
        return currentPosition;
    }

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    public abstract void setIndicator(int selectedPosition, Object data, View indicatorView);

//    /**
//     * 如果当前页面嵌套在另一个viewPager中，为了在进行滚动时阻断父ViewPager滚动，可以 阻止父ViewPager滑动事件
//     * 父ViewPager需要实现ParentViewPager中的setScrollable方法
//     */
//    public void disableParentViewPagerTouchEvent(BaseViewPager parentViewPager) {
//        if (parentViewPager != null)
//            parentViewPager.setScrollable(false);
//    }


    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param position
         * @param view
         */
        void onImageClick(Object info, int position, View view);
    }

    public abstract View getAdapterItemView(Object info, Context context);

    public abstract View getIndicatorView(Context context);
}
