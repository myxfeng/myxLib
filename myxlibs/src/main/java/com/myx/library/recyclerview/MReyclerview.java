package com.myx.library.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by mayuxin on 2018/1/25.
 */

public class MReyclerview extends RecyclerView {
    private MRecyclerAdapter mWarpAdapter;
    private RecyclerView.Adapter mReqAdapter;
    private ArrayList<View> mHeaderView = new ArrayList<View>();
    private ArrayList<View> mFooterView = new ArrayList<View>();
    private View mEmptyView;
    private boolean isKeepShowHeadOrFooter;
    private boolean hasShowEmptyView;

    public MReyclerview(Context context) {
        super(context);
    }

    public MReyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MReyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
//        super.setAdapter(adapter);
        // Only once
        mReqAdapter = adapter;
        mWarpAdapter = new MRecyclerAdapter(this, adapter, mHeaderView, mFooterView);


        mReqAdapter.registerAdapterDataObserver(mReqAdapterDataObserver);
        super.setAdapter(mWarpAdapter);
        processEmptyView();

    }

    public void addHeaderView(View v) {
        addHeaderView(v, false);
    }

    public void addHeaderView(View v, boolean isScrollTo) {
        if (mHeaderView.contains(v)) return;

        mHeaderView.add(v);
        if (null != mWarpAdapter) {
            int pos = mHeaderView.size() - 1;
            mWarpAdapter.notifyItemInserted(pos);

            if (isScrollTo) {
                scrollToPosition(pos);
            }
        }
    }

    public boolean removeHeaderView(View v) {
        if (!mHeaderView.contains(v)) return false;

        if (null != mWarpAdapter) {
            mWarpAdapter.notifyItemRemoved(mHeaderView.indexOf(v));
        }
        return mHeaderView.remove(v);
    }

    public void addFooterView(View v) {
        addFooterView(v, false);
    }

    public void addFooterView(View v, boolean isScrollTo) {
        if (mFooterView.contains(v)) return;

        mFooterView.add(v);
        if (null != mWarpAdapter) {
            int pos = (null == mReqAdapter ? 0 : mReqAdapter.getItemCount()) + getHeaderViewsCount() + mFooterView.size() - 1;
            mWarpAdapter.notifyItemInserted(pos);
            if (isScrollTo) {
                scrollToPosition(pos);
            }
        }
    }

    public boolean removeFooterView(View v) {
        if (!mFooterView.contains(v)) return false;

        if (null != mWarpAdapter) {
            int pos = (null == mReqAdapter ? 0 : mReqAdapter.getItemCount()) + getHeaderViewsCount() + mFooterView.indexOf(v);
            mWarpAdapter.notifyItemRemoved(pos);
        }
        return mFooterView.remove(v);
    }


    public int getHeaderViewsCount() {
        return mHeaderView.size();
    }

    private void processEmptyView() {
        if (null != mEmptyView) {
            boolean isShowEmptyView = (null != mReqAdapter ? mReqAdapter.getItemCount() : 0) == 0;

            if (isShowEmptyView == hasShowEmptyView) return;

            if (isKeepShowHeadOrFooter) {
                if (hasShowEmptyView) {
                    mWarpAdapter.notifyItemRemoved(getHeaderViewsCount());
                }
            } else {
                mEmptyView.setVisibility(isShowEmptyView ? VISIBLE : GONE);
//                setVisibility(isShowEmptyView ? GONE : VISIBLE);
            }

            this.hasShowEmptyView = isShowEmptyView;
        }
    }


    /**
     * Set EmptyView (before setAdapter)
     *
     * @param emptyView your EmptyView
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(emptyView, false);
    }

    /**
     * Set EmptyView (before setAdapter)
     *
     * @param emptyView              your EmptyView
     * @param isKeepShowHeadOrFooter is Keep show HeadView or FooterView
     */
    public void setEmptyView(View emptyView, boolean isKeepShowHeadOrFooter) {
        this.mEmptyView = emptyView;
        this.isKeepShowHeadOrFooter = isKeepShowHeadOrFooter;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyViewKeepShowHeadOrFooter(boolean isKeepShowHeadOrFoot) {
        this.isKeepShowHeadOrFooter = isKeepShowHeadOrFoot;
    }

    public boolean isShowEmptyView() {
        return hasShowEmptyView;
    }

    public boolean isKeepShowHeadOrFooter() {
        return isKeepShowHeadOrFooter;
    }

    private AdapterDataObserver mReqAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWarpAdapter.notifyDataSetChanged();
            processEmptyView();
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWarpAdapter.notifyItemInserted(getHeaderViewsCount() + positionStart);
            processEmptyView();
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWarpAdapter.notifyItemRemoved(getHeaderViewsCount() + positionStart);
            processEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWarpAdapter.notifyItemRangeChanged(getHeaderViewsCount() + positionStart, itemCount);
            processEmptyView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWarpAdapter.notifyItemMoved(getHeaderViewsCount() + fromPosition, getHeaderViewsCount() + toPosition);
        }
    };


}
