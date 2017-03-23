package com.myx.library.mvp;

/**
 * Created by mayuxin on 2017/3/23.
 */

public interface BaseView {
    void showLoading(String title);
    void stopLoading();
    void showErrorTip(String msg);
}
