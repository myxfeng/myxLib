package com.myx.feng.cycleviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myx.feng.R;
import com.myx.feng.rxjavademo.NewsData;
import com.myx.library.image.ImageUtils;
import com.myx.library.widget.CycleViewPager;


/**
 * Created by mayuxin on 2017/4/6.
 */

public class CustomCycler extends CycleViewPager {

    public CustomCycler(Context context) {
        super(context);
    }

    public CustomCycler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomCycler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View getIndicatorView(Context context) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        TextView textView = new TextView(context);
        textView.setTextColor(Color.RED);
        relativeLayout.addView(textView);
        return relativeLayout;
    }

    @Override
    public void setIndicator(int selectedPosition, Object data, View indicatorView) {
        RelativeLayout relativeLayout = (RelativeLayout) indicatorView;
        TextView textView = (TextView) relativeLayout.getChildAt(0);
        NewsData newsData = (NewsData) data;
        textView.setText("tag=" + selectedPosition + "==" + ((NewsData) data).getNews_title());
    }

    @Override
    public View getAdapterItemView(Object info, Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        NewsData data = (NewsData) info;
        ImageUtils.loadImageOnlyWifi(data.getCover(), imageView, false, 0);
        return view;
    }

}
