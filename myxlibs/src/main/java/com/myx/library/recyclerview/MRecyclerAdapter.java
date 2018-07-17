package com.myx.library.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by mayuxin on 2018/1/25.
 */

public class MRecyclerAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_HEADER = -1;
    private static final int VIEW_TYPE_FOOTER = -2;
    private static final int VIEW_TYPE_EMPTY_VIEW = -3;

    private ArrayList<View> mHeaderView;
    private ArrayList<View> mFooterView;
    private RecyclerView.Adapter mReqAdapter;
    private int curHeaderOrFooterPos;
    private MReyclerview mReyclerview;


    public MRecyclerAdapter(MReyclerview mReyclerview, RecyclerView.Adapter reqAdapter, ArrayList<View> mHeaderView, ArrayList<View> mFooterView) {
        this.mReyclerview = mReyclerview;
        this.mReqAdapter = reqAdapter;
        this.mHeaderView = mHeaderView;
        this.mFooterView = mFooterView;
    }

    public int getHeadersCount() {
        return null != mHeaderView ? mHeaderView.size() : 0;
    }

    public int getFootersCount() {
        return null != mFooterView ? mFooterView.size() : 0;
    }

    private int getReqAdapterCount() {
        return null != mReqAdapter ? mReqAdapter.getItemCount() : 0;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutManager layoutManager=mReyclerview.getLayoutManager();
        switch (viewType) {
            case VIEW_TYPE_HEADER: {
                // create header view
                EmptyHeaderOrFooterViewHolder headerViewHolder;
                View tempHeadView = mHeaderView.get(curHeaderOrFooterPos);
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    FrameLayout mContainerView = new FrameLayout(tempHeadView.getContext());
                    mContainerView.addView(tempHeadView);
                    headerViewHolder = new EmptyHeaderOrFooterViewHolder(mContainerView);
                    StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setFullSpan(true);
                    headerViewHolder.itemView.setLayoutParams(layoutParams);
                } else {
                    headerViewHolder = new EmptyHeaderOrFooterViewHolder(tempHeadView);
                }

                // fix multiple headerView disorder
                if (mHeaderView.size() > 2) {
                    headerViewHolder.setIsRecyclable(false);
                }

                return headerViewHolder;
            }
            case VIEW_TYPE_FOOTER: {
                // create footer view
                EmptyHeaderOrFooterViewHolder footerViewHolder;
                View tempFooterView;

                // fix fast delete IndexOutOfBoundsException
                int footerViewCount = mFooterView.size();
                if (curHeaderOrFooterPos >= footerViewCount) {
                    curHeaderOrFooterPos = footerViewCount - 1;
                }

                tempFooterView = mFooterView.get(curHeaderOrFooterPos);
                ViewParent tempFooterViewParent = tempFooterView.getParent();
                if (null != tempFooterViewParent) {
                    ((ViewGroup)tempFooterViewParent).removeView(tempFooterView);
                }

                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    FrameLayout mContainerView = new FrameLayout(tempFooterView.getContext());
                    mContainerView.addView(tempFooterView);
                    footerViewHolder = new EmptyHeaderOrFooterViewHolder(mContainerView);
                    StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setFullSpan(true);
                    footerViewHolder.itemView.setLayoutParams(layoutParams);
                } else {
                    footerViewHolder = new EmptyHeaderOrFooterViewHolder(tempFooterView);
                }

                // fix multiple footerView disorder
                if (mFooterView.size() > 2) {
                    footerViewHolder.setIsRecyclable(false);
                }

                return footerViewHolder;
            }
            case VIEW_TYPE_EMPTY_VIEW: {
                EmptyHeaderOrFooterViewHolder emptyViewHolder;
                View emptyView = mReyclerview.getEmptyView();
                emptyView.setVisibility(View.VISIBLE);
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    FrameLayout mContainerView = new FrameLayout(emptyView.getContext());
                    mContainerView.addView(emptyView);
                    emptyViewHolder = new EmptyHeaderOrFooterViewHolder(mContainerView);
                    StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setFullSpan(true);
                    emptyViewHolder.itemView.setLayoutParams(layoutParams);
                } else {
                    emptyViewHolder = new EmptyHeaderOrFooterViewHolder(emptyView);
                }

                return emptyViewHolder;
            }
            default:
                // create default item view
                RecyclerView.ViewHolder itemViewHolder = mReqAdapter.onCreateViewHolder(parent, viewType);
                // add click listener

                return itemViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int curItemViewType = getItemViewType(position);

        if (curItemViewType == VIEW_TYPE_HEADER ) {
            // head view
//            boolean isInitializeInvoke = false;
//            int tempHeadViewHashCode = holder.itemView.hashCode();
//            if (!mHeadOrFooterInitInvokeViewBindViewFlag.contains(tempHeadViewHashCode)) {
//                mHeadOrFooterInitInvokeViewBindViewFlag.add(tempHeadViewHashCode);
//                isInitializeInvoke = true;
//            }
//            mOnHeadViewBindViewHolderListener.onHeadViewBindViewHolder(holder, position, isInitializeInvoke);
        } else if (curItemViewType == VIEW_TYPE_FOOTER ) {
            // footer view
//            boolean isInitializeInvoke = false;
//            int tempFooterViewHashCode = holder.itemView.hashCode();
//            if (!mHeadOrFooterInitInvokeViewBindViewFlag.contains(tempFooterViewHashCode)) {
//                mHeadOrFooterInitInvokeViewBindViewFlag.add(tempFooterViewHashCode);
//                isInitializeInvoke = true;
//            }
//            mOnFooterViewBindViewHolderListener.onFooterViewBindViewHolder(holder, position - getHeadersCount() - getReqAdapterCount(), isInitializeInvoke);
        } else if (curItemViewType >= 0) {
            // item view
            final int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (mReqAdapter != null) {
                adapterCount = mReqAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    mReqAdapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

    }
    @Override
    public int getItemViewType(int position) {
        // header view
        if (isHeaderView(position)) {
            this.curHeaderOrFooterPos = position;
            return VIEW_TYPE_HEADER;
        }

        int headersCount = getHeadersCount();
        // item view
        int adapterCount = 0;
        if (getReqAdapterCount() > 0 && position >= headersCount) {
            int adjPosition = position - headersCount;
            adapterCount = mReqAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mReqAdapter.getItemViewType(adjPosition);
            }
        } else if (mReyclerview.isKeepShowHeadOrFooter()) {
            // empty view

            if (position == headersCount) return VIEW_TYPE_EMPTY_VIEW;
        }

        // footer view
        this.curHeaderOrFooterPos = position - headersCount - adapterCount;
        return VIEW_TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        int tempItemCount = mReqAdapter.getItemCount();
        if (mReyclerview.isKeepShowHeadOrFooter()) {
            count += tempItemCount == 0 ? 1 : tempItemCount;
        } else {
            count += tempItemCount;
        }

        if (null != mHeaderView && mHeaderView.size() > 0) {
            count += mHeaderView.size();
        }

        if (null != mFooterView && mFooterView.size() > 0) {
            count += mFooterView.size();
        }

        return count;

    }
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        int position = holder.getAdapterPosition();
        if (null == mReqAdapter || isHeaderView(position) || isFooterView(position)) {
            return ;
        }

        mReqAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        int position = holder.getAdapterPosition();
        if (null == mReqAdapter || isHeaderView(position) || isFooterView(position)) {
            return ;
        }

        mReqAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if (null == mReqAdapter) {
            return ;
        }

        mReqAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        if (null == mReqAdapter) {
            return ;
        }

        mReqAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        int position = holder.getAdapterPosition();
        if (null == mReqAdapter || isHeaderView(position) || isFooterView(position)) {
            return ;
        }

        mReqAdapter.onViewRecycled(holder);
    }

    private boolean isHeaderView(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterView(int position) {
        return getFootersCount() > 0 && position - getHeadersCount() - getReqAdapterCount() >= 0;
    }

    class EmptyHeaderOrFooterViewHolder extends RecyclerView.ViewHolder {
        public EmptyHeaderOrFooterViewHolder(View itemView) {
            super(itemView);
        }
    }


}
