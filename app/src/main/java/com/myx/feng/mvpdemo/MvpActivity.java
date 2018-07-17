package com.myx.feng.mvpdemo;

import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myx.feng.R;
import com.myx.library.image.ImageUtils;
import com.myx.library.mvp.BaseActivity;
import com.myx.library.rxjava.Api;

import butterknife.BindView;

public class MvpActivity extends BaseActivity<MvpPrestensrImpl, MvpMoudleImpl> implements MvpContans.View {
    @BindView(R.id.imageView)
    SimpleDraweeView imageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mvp;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {

    }

    @Override
    public void onGetImageSuccess(String imageUrl) {
        ImageUtils.loadImageOnlyWifi(imageUrl, imageView, false, 0);
    }

    @Override
    public void showLoading(String title) {
        showDialog(11);
    }

    @Override
    public void stopLoading() {
        dismissDialog(11);
    }

    @Override
    public void showErrorTip(String msg) {

    }

    public void getImage(View view) {
        mPresenter.getImage("2095260387443712_cms_2095260387443712", Api.getCacheControl(), "11");
    }
}
