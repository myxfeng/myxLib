package com.myx.library.recyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by mayuxin on 2018/1/25.
 */

public class MRefreshRecyclerView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener{

    private Context mContext;
    private MReyclerview mReyclerview;
    private IMLoadMore mLoadMoreView;

    private OnPullRefreshListener mOnPullRefreshListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private MRecyclerViewOnScrollListener mFamiliarRecyclerViewOnScrollListener;
    private boolean isPullRefreshEnabled = true;
    private boolean isLoadMoreEnabled = false;

    public MRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        mReyclerview = new MReyclerview(getContext(), attrs);
        addView(mReyclerview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setOnRefreshListener(this);
        setLoadMoreView(new MDefaultLoadMoreView(mContext));
    }

    @Override
    public void onRefresh() {
        if (!isPullRefreshEnabled) return;

        callOnPullRefresh();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mReyclerview.setAdapter(adapter);
    }

    public void setLoadMoreView(IMLoadMore loadMoreView) {
        if (null == loadMoreView) {
            if (null != mLoadMoreView) {
                mReyclerview.removeFooterView(mLoadMoreView.getView());
                mReyclerview.removeOnScrollListener(mFamiliarRecyclerViewOnScrollListener);
                mLoadMoreView = null;
            }
            return;
        }

        mLoadMoreView = loadMoreView;
        initializeLoadMoreView();
    }

    /**
     * Get FamiliarRecyclerView
     *
     * @return FamiliarRecyclerView
     */
    public RecyclerView getRecyclerView() {
        return mReyclerview;
    }

    /**
     * Automatic pull refresh
     */
    public void autoRefresh() {
        if (!isPullRefreshEnabled) return;

        setRefreshing(true);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callOnPullRefresh();
            }
        }, 1000);
    }

    public void setPullRefreshEnabled(boolean enabled) {
        if (isPullRefreshEnabled == enabled) return;

        setEnabled(enabled);

        if (!enabled) {
            setRefreshing(false);
        }

        this.isPullRefreshEnabled = enabled;
    }

    public void setLoadMoreEnabled(boolean enabled) {
        if (isLoadMoreEnabled == enabled) return;

        if (!enabled) {
            mReyclerview.removeFooterView(mLoadMoreView.getView());
        } else {
            mReyclerview.addFooterView(mLoadMoreView.getView());
        }

        isLoadMoreEnabled = enabled;
    }

    public void pullRefreshComplete() {
        setRefreshing(false);
    }

    public void loadMoreComplete() {
        mLoadMoreView.showNormal();
    }

    private void initializeLoadMoreView() {
        if (null == mFamiliarRecyclerViewOnScrollListener) {
            mFamiliarRecyclerViewOnScrollListener = new MRecyclerViewOnScrollListener(mReyclerview) {
                @Override
                public void onScrolledToTop() {
                }

                @Override
                public void onScrolledToBottom() {
                    if (!isLoadMoreEnabled || mLoadMoreView.isLoading()) return;

                    mLoadMoreView.showLoading();
                    callOnLoadMore();
                }
            };
        }
        mReyclerview.addOnScrollListener(mFamiliarRecyclerViewOnScrollListener);
        mLoadMoreView.getView().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
    }

    private void callOnPullRefresh() {
        if (null != mOnPullRefreshListener) {
            mOnPullRefreshListener.onPullRefresh();
        }
    }

    private void callOnLoadMore() {
        if (null != mOnLoadMoreListener) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void setOnPullRefreshListener(OnPullRefreshListener onPullRefreshListener) {
        this.mOnPullRefreshListener = onPullRefreshListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }



    public interface OnPullRefreshListener {
        void onPullRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


}
