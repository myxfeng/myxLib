package com.myx.library.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.myx.library.util.Futils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * /**1.mvp模式
 * public class SampleActivity extends BaseActivity<NewsChanelPresenter, NewsChannelModel>implements NewsChannelContract.View {
 *
 * @Override public int getLayoutId() {
 * return R.layout.activity_news_channel;
 * }
 * @Override public void initPresenter() {
 * mPresenter.setVM(this, mModel);
 * }
 * @Override public void initView() {
 * }
 * }
 * 2.普通模式
 * public class SampleActivity extends BaseActivity {
 * @Override public int getLayoutId() {
 * return R.layout.activity_news_channel;
 * }
 * @Override public void initPresenter() {
 * }
 * @Override public void initView() {
 * }
 * }
 * Created by mayuxin on 2017/3/23.
 */

public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends AppCompatActivity {
    public T mPresenter;
    public E mModel;
    public Context mContext;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        mContext = this;
        mPresenter = Futils.getT(this, 0);
        mModel = Futils.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        this.initPresenter();
        this.initView();
    }


    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
    }

    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    public abstract void initView();

    /**
     * 设置主题
     */
    private void initTheme() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
        unbinder.unbind();
    }
}
