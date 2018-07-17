package com.myx.library.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myx.library.R;

/**
 * Created by mayuxin on 2018/1/25.
 */

public class MDefaultLoadMoreView extends FrameLayout implements IMLoadMore{

    private ProgressBar mPbLoad;
    private TextView mTvLoadText;

    private boolean isLoading = false;

    public MDefaultLoadMoreView(Context context) {
        this(context, null);
    }

    public MDefaultLoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDefaultLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.def_load_more, this);
        mPbLoad = (ProgressBar) findViewById(R.id.pbLoad);
        mTvLoadText = (TextView) findViewById(R.id.tvLoadText);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void showNormal() {
        isLoading = false;
        mPbLoad.setVisibility(GONE);
        mTvLoadText.setVisibility(GONE);
    }

    @Override
    public void showLoading() {
        isLoading = true;
        mPbLoad.setVisibility(VISIBLE);
        mTvLoadText.setText("正在加载中...");
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

}
