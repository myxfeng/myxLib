package com.myx.feng;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.igexin.sdk.PushManager;
import com.myx.feng.app.ApiServiceTest;
import com.myx.feng.getui.DemoIntentService;
import com.myx.feng.getui.Main2Activity;
import com.myx.feng.recyclerviewdemo.RecyclerActivity;
import com.myx.feng.rxjavademo.CaheSubscribe;
import com.myx.feng.rxjavademo.CollectResult;
import com.myx.feng.rxjavademo.NewsResult;
import com.myx.library.image.ImageUtils;
import com.myx.library.rxjava.Api;
import com.myx.library.rxjava.RxSchedulers;
import com.myx.library.util.ACache;
import com.myx.library.util.ToastUtils;


import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.img)
    SimpleDraweeView image;

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.bind(this);

//        image= (SimpleDraweeView) findViewById(R.id.img);
        image.setBackgroundColor(Color.parseColor("#465568"));

    }

    String imgkey = "121231";
    String titlekey = "fdsfsd";

    public void postImg(View view) {
        Api.getDefault(ApiServiceTest.ApiService.class).getDetail("2095260387443712_cms_2095260387443712", Api.CACHE_CONTROL_AGE, "11").compose(RxSchedulers.<NewsResult>io_main()).subscribe(new CaheSubscribe<NewsResult>(this, imgkey, false, true) {
            @Override
            public void superNext(NewsResult newsResult) {
                ToastUtils.showShort(MainActivity.this, newsResult.getData().getCover() + "==" + newsResult.getResult().getSource());
                ImageUtils.loadBitmapOnlyWifi(newsResult.getData().getCover(), image, false, 0);
            }

            @Override
            public void superError(Throwable e) {

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
        Api.getDefault(ApiServiceTest.UserService.class).syncCollect("f145e922e7c24e9cab3e7e457ff30cd4v5HT5f9z", Api.CACHE_CONTROL_AGE).compose(RxSchedulers.<CollectResult>io_main()).subscribe(new Subscriber<CollectResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CollectResult dddd = (CollectResult) ACache.get(MainActivity.this).getAsObject(titlekey);
                onNext(dddd);
            }

            @Override
            public void onNext(CollectResult newsResult) {
                ACache.get(MainActivity.this).put(titlekey, newsResult);

                ToastUtils.showShort(MainActivity.this, newsResult.getData().get(0).getNews_title());
                title.setText(newsResult.getData().get(0).getNews_title());
            }
        });
    }

    public void openpush(View view) {
        ToastUtils.showShort(MainActivity.this, "开启");

        /*新版推送*/
        PushManager.getInstance().initialize(context, null);
        //注册推送服务
        PushManager.getInstance().registerPushIntentService(context, DemoIntentService.class);
        // 绑定别名
        PushManager.getInstance().bindAlias(context, "1111");
        if (!PushManager.getInstance().isPushTurnedOn(context)) {
            PushManager.getInstance().turnOnPush(context);
        }
    }

    public void closepush(View view) {
        ToastUtils.showShort(MainActivity.this, "关闭");
        PushManager.getInstance().turnOffPush(MainActivity.this);
    }

    public void threadNof(View view) {
        Intent intent = new Intent(context, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bitmap btm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        PendingIntent notificationPendingIt = PendingIntent.getActivity(context, new Random().nextInt(1000), intent, 0);


//        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notifation_layout);
//        mRemoteViews.setTextViewText(R.id.title, title);
//        mRemoteViews.setTextViewText(R.id.desc, description);
//        mRemoteViews.setImageViewResource(R.id.icon, R.drawable.ic_launcher);
//        mRemoteViews.setTextViewText(R.id.time, getTime());

        String data = "从子线程发出的notif";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(btm)
                .setTicker(data + "=Ticker")
                .setContentTitle(data + "=Title")
                .setContentText(data + "=Text")
                .setContentIntent(notificationPendingIt).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        n.defaults |= Notification.DEFAULT_SOUND;
        n.defaults |= Notification.DEFAULT_VIBRATE;
        int nextInt = new Random().nextInt(10000);
        notificationManager.notify(nextInt, n);
    }

    public void jumprecyclerview(View view) {
        startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
    }
}
