package com.myx.library.recyclerview;

import android.view.View;

/**
 * Created by mayuxin on 2018/1/25.
 */

public interface IMLoadMore {
    void showLoading();

    void showNormal();

    boolean isLoading();

    View getView();

}
