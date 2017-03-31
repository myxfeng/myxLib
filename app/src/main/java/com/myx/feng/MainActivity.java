package com.myx.feng;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.myx.feng.app.ApiServiceTest;
import com.myx.feng.rxjavademo.ACache;
import com.myx.feng.rxjavademo.CaheSubscribe;
import com.myx.feng.rxjavademo.NewsResult;
import com.myx.library.image.ImageUtils;
import com.myx.library.rxjava.Api;
import com.myx.library.rxjava.RxSchedulers;
import com.myx.library.util.ToastUtils;


import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img)
    SimpleDraweeView image;

    @BindView(R.id.title)
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        image= (SimpleDraweeView) findViewById(R.id.img);
        image.setBackgroundColor(Color.parseColor("#465568"));

    }
    String imgkey="121231";
    String titlekey="fdsfsd";

    public void postImg(View view) {
        Api.getDefault(ApiServiceTest.ApiService.class).getDetail("2095260387443712_cms_2095260387443712", Api.CACHE_CONTROL_AGE, "11").compose(RxSchedulers.<NewsResult>io_main()).subscribe(new CaheSubscribe<NewsResult>(this,imgkey,false,true){
            @Override
            public void superNext(NewsResult newsResult) {
                ToastUtils.showShort(MainActivity.this,newsResult.getData().getCover()+"=="+newsResult.getResult().getSource());
                ImageUtils.loadBitmapOnlyWifi(newsResult.getData().getCover(),image,false,0);
            }

            @Override
            public void superError(Throwable e) {

            }

            @Override
            public void onNext(NewsResult newsResult) {



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
        Api.getDefault(ApiServiceTest.UserService.class).syncCollect("f145e922e7c24e9cab3e7e457ff30cd4v5HT5f9z",Api.CACHE_CONTROL_AGE).compose(RxSchedulers.<CollectResult>io_main()).subscribe(new Subscriber<CollectResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
              CollectResult dddd= (CollectResult) ACache.get(MainActivity.this).getAsObject(titlekey);
                onNext(dddd);
            }

            @Override
            public void onNext(CollectResult newsResult) {
                ACache.get(MainActivity.this).put(titlekey,newsResult);

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
        PushManager.getInstance().stopService(MainActivity.this);
    }
}
