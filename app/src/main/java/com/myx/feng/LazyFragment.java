package com.myx.feng;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

/**
 * Created by mayuxin on 2017/3/30.
 */

public abstract class LazyFragment extends Fragment {

    private boolean isVisible = false;
    private boolean isFirstLoad = true;
    private boolean isInitViews = false;
    private View view;
    private ViewStub viewStub;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lazy, container, false);
        viewStub = (ViewStub) view.findViewById(R.id.viwstub);
        viewStub.setLayoutResource(getLayoutResourceId());
        if (savedInstanceState != null) {// 防止fragment 自动销毁后又创建
            initViewWithViewStub();
            lazyLoad();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser) {
            initViewWithViewStub();
            lazyLoad();
        }
    }

    private void initViewWithViewStub() {
        if (viewStub != null && viewStub.getVisibility() != View.VISIBLE && viewStub.getParent() != null && !isInitViews&&isVisible) {
            viewStub.inflate();
            initView(view);
            isInitViews = true;
        }
    }

    private void lazyLoad() {
        if (!isInitViews || !isVisible || !isFirstLoad) {
            return;
        }
        initData();
        isFirstLoad = false;
    }

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract int getLayoutResourceId();

}
