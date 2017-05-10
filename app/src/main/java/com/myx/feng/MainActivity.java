package com.myx.feng;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.igexin.sdk.PushManager;
import com.myx.feng.app.ApiServiceTest;
import com.myx.feng.app.App;
import com.myx.feng.getui.DemoIntentService;
import com.myx.feng.getui.DemoPushService;
import com.myx.library.image.ImageUtils;
import com.myx.library.rxjava.Api;
import com.myx.library.rxjava.RxSchedulers;
import com.myx.library.util.Futils;
import com.myx.library.util.ToastUtils;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img)
    SimpleDraweeView image;

    @BindView(R.id.title)
    TextView title;
//    PUSH_APPID
//    PUSH_APPKEY
    // PUSH_APPSECRET
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        image= (SimpleDraweeView) findViewById(R.id.img);
        image.setBackgroundColor(Color.parseColor("#465568"));

//        Futils.getMetaValue();
        String aa="id:"+Futils.getMetaValue(this,"PUSH_APPID")+"\n key:"+Futils.getMetaValue(this,"PUSH_APPKEY")+"\n secret："+Futils.getMetaValue(this,"PUSH_APPSECRET");
        title.setText(aa);
    }


    public void postImg(View view) {
        Api.getDefault(ApiServiceTest.ApiService.class).getDetail("2095260387443712_cms_2095260387443712", Api.getCacheControl(), "11").compose(RxSchedulers.<NewsResult>io_main()).subscribe(new Subscriber<NewsResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewsResult newsResult) {

//                ARouter.getInstance().build("").with().navigation()

                ToastUtils.showShort(MainActivity.this, newsResult.getData().getCover());
//                ImageUtils.loadBitmapOnlyWifi(newsResult.getData().getCover(), image, false, 0);
                ImageUtils.test(newsResult.getData().getCover(), new ImageUtils.ImageCallBack() {
                    @Override
                    public void onSuccess(String imgaeUrl, File file) {
                        image.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                    }
                });
            }
        });
    }

    //    ToastUtils.showShort(MainActivity.this, newsResult.getData().getCover());
////                ImageUtils.loadBitmapOnlyWifi(newsResult.getData().getCover(), image, false, 0);
//    ImageUtils.test(newsResult.getData().getCover(), new ImageUtils.ImageCallBack() {
//        @Override
//        public void onSuccess(String imgaeUrl, File file) {
//            image.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
//        }
//    });
    public void getTitle(View view) {
        Api.getDefault(ApiServiceTest.UserService.class).syncCollect("f145e922e7c24e9cab3e7e457ff30cd4v5HT5f9z").compose(RxSchedulers.<CollectResult>io_main()).subscribe(new Subscriber<CollectResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CollectResult newsResult) {

                ToastUtils.showShort(MainActivity.this, newsResult.getData().get(0).getNews_title());
                title.setText(newsResult.getData().get(0).getNews_title());
            }
        });
    }

    public void openpush(View view){
        PushManager.getInstance().initialize(MainActivity.this, DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(MainActivity.this, DemoIntentService.class);
    }
    public void closepush(View view){
        startActivity(new Intent(this,TestFragmentActivity.class));
        PushManager.getInstance().stopService(MainActivity.this);
    }
}
