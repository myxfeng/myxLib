package com.myx.feng;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.facebook.drawee.view.SimpleDraweeView;
import com.myx.feng.app.ApiService;
import com.myx.feng.myxlib.R;
import com.myx.library.image.ImageUtils;
import com.myx.library.rxjava.Api;
import com.myx.library.rxjava.BaseUrl;
import com.myx.library.rxjava.RxSchedulers;
import com.myx.library.util.Futils;
import com.myx.library.util.ToastUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img)
    SimpleDraweeView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        image= (SimpleDraweeView) findViewById(R.id.img);
        image.setBackgroundColor(Color.parseColor("#465568"));
    }


    public void postImg(View view) {
        Api.getDefault(ApiService.class).getDetail("2095260387443712_cms_2095260387443712").compose(RxSchedulers.<NewsResult>io_main()).subscribe(new Action1<NewsResult>() {
            @Override
            public void call(NewsResult newsResult) {
                ToastUtils.showShort(MainActivity.this, newsResult.getData().getCover());
                ImageUtils.loadBitmapOnlyWifi(newsResult.getData().getCover(), image, false, 0);
            }
        });
    }
}
