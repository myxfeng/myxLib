package com.myx.feng;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.myx.feng.cycleviewdemo.CyclerViewActivity;
import com.myx.feng.getui.DemoIntentService;
import com.myx.feng.getui.Main2Activity;
import com.myx.feng.nativeweb.NativeWebActivity;
import com.myx.feng.recyclerviewdemo.RecyclerActivity;
import com.myx.feng.rxjavademo.NewsData;
import com.myx.library.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.img)
    ImageView image;
    private Toolbar toolbar;

    private NavigationView navigationView;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.lisview)
    ListView listView;


    static {
        System.loadLibrary("Jnilib");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSystemBarTransparent();
        setContentView(R.layout.activity_drawleft);
        context = this;
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        View vNavigationHeader = LayoutInflater.from(this).inflate(R.layout.itemview, navigationView, false);
        navigationView.addHeaderView(vNavigationHeader);
        Log.i("myx", "onCreate=" + System.currentTimeMillis());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_item);

        arrayAdapter.add("postImg");
        arrayAdapter.add("getTitle");
        arrayAdapter.add("openpush");
        arrayAdapter.add("closepush");
        arrayAdapter.add("threadNotifaicaition");
        arrayAdapter.add("jumprecyclerview");
        arrayAdapter.add("jumpCycler");
        arrayAdapter.add("jumpMvp");
        arrayAdapter.add("nativeweb");
        arrayAdapter.add("postBody");
        arrayAdapter.add("okhttpget");
        arrayAdapter.add("jsontest");
        arrayAdapter.add("fresco");


        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aaa = arrayAdapter.getItem(position);
                switch (aaa) {
                    case "postImg":
                        postImg(view);
                        break;
                    case "getTitle":
                        getTitle(view);
                        break;
                    case "openpush":
                        openpush(view);
                        break;
                    case "closepush":
                        closepush(view);
                        break;
                    case "threadNotifaicaition":
                        threadNof(view);
                        break;
                    case "jumprecyclerview":
                        jumprecyclerview(view);
                        break;
                    case "jumpCycler":
                        jumpCycler(view);
                        break;
                    case "jumpMvp":
                        jumpMvp(view);
                        break;
                    case "nativeweb":
                        nativeweb(view);
                        break;
                    case "postBody":
                        postBody(view);
                        break;
                    case "okhttpget":
                        okhttpget(view);
                        break;
                    case "jsontest":
                        jsontest();
                        break;
                    case "fresco":
                        goFresco();


                }
            }
        });


    }

    void goFresco() {
        startActivity(new Intent(this, FrescoTestActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("myx", "onPause=" + System.currentTimeMillis());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("myx", "onResume=" + System.currentTimeMillis());

    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void test() {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.bluetooth) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String imgkey = "121231";
    String titlekey = "fdsfsd";

    public void postImg(View view) {

    }

    //    ToastUtils.showShort(MainActivity.this, newsResult.getData().getCover());
////                ImageUtils.loadImageOnlyWifi(newsResult.getData().getCover(), image, false, 0);
//    ImageUtils.test(newsResult.getData().getCover(), new ImageUtils.ImageCallBack() {
//        @Override
//        public void onSuccess(String imgaeUrl, File file) {
//            image.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
//        }
//    });
    public void getTitle(View view) {
//        Api.getDefault(ApiServiceTest.UserService.class, AppContans.SERVER__USER_API).syncCollect("f145e922e7c24e9cab3e7e457ff30cd4v5HT5f9z", Api.CACHE_CONTROL_NET).compose(RxSchedulers.<CollectResult>io_main()).subscribe(new Subscriber<CollectResult>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                CollectResult dddd = (CollectResult) ACache.get(MainActivity.this).getAsObject(titlekey);
//                onNext(dddd);
//            }
//
//            @Override
//            public void onNext(CollectResult newsResult) {
//                ACache.get(MainActivity.this).put(titlekey, newsResult);
//
////                ToastUtils.showShort(MainActivity.this, newsResult.getData().get(0).getNews_title());
////                title.setText(newsResult.getData().get(0).getNews_title());
//
//            }
//        });
    }

    public void openpush(View view) {
        ToastUtils.showShort(MainActivity.this, "开启");
        /*新版推送*/
        PushManager.getInstance().initialize(context, null);
        //  注册推送服务
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

    public void jumpMvp(View view) {
    }

    public void jumpCycler(View view) {
        startActivity(new Intent(this, CyclerViewActivity.class));
    }

    public void nativeweb(View view) {
        startActivity(new Intent(this, NativeWebActivity.class));
    }

    public void postBody(View view) {
        NewsData data = new NewsData();
        data.setArticleid("2095260387443712_cms_2095260387443712");
    }

    public void okhttpget(View view) {

    }

    public void jsontest() {
        String json = "{\n" +
                "    \"cardId\": \"16880\",\n" +
                "    \"type\": \"2\",\n" +
                "    \"imgUrl\": \"http://luckyimgs.peopletech.cn/image/201803/27/86adec53dc1f54d2623686bf75e86c0a?x-oss-process=image/resize,m_mfit,limit_0,w_600,h_445/crop,x_0,y_27,w_600,h_300/resize,m_mfit,limit_0,w_1600,h_800\",\n" +
                "    \"title\": \"香港反对派成员辱国旗区旗罪成 被判监2个月\",\n" +
                "    \"digest\": \"古思尧曾公然叫嚣“我就是故意侮辱国旗”。（图片来源：星岛日报网）\\n海外网3月27日电香港反对派团体“社民连”成员古思尧有过多次侮辱国旗及区旗的案底。2017年7月至2018年1月期间，古思尧参加3次\",\n" +
                "    \"source\": \"海外网\",\n" +
                "    \"pubTime\": \"2018-03-27 17:16:39\",\n" +
                "    \"menuId\": null,\n" +
                "    \"category\": \"政聚焦\",\n" +
                "    \"detailUrl\": \"http://newsdata.peopletech.cn/wap-news-zxb/#/normal/16880\",\n" +
                "    \"tag\": \"国旗,香港,区旗,古思尧,张申,侮辱,香港特区,国徽,湾仔,海外版\",\n" +
                "    \"rgb\": null,\n" +
                "    \"readTime\": null,\n" +
                "    \"text\": \"<p><img src=\\\"http://luckyimgs.peopletech.cn/image/201803/27/86adec53dc1f54d2623686bf75e86c0a\\\" width=\\\"1600\\\" height=\\\"1188\\\"/></p><p>古思尧曾公然叫嚣“我就是故意侮辱国旗”。（图片来源：星岛日报网）</p><p>海外网3月27日电香港反对派团体“社民连”成员古思尧有过多次侮辱国旗及区旗的案底。2017年7月至2018年1月期间，古思尧参加3次不同的游行集会时，均涉嫌展示涂污的五星红旗及香港特别行政区区旗，因而被控3宗侮辱国旗及区旗罪。拒不认罪的古思尧今(27日)被裁定3项控罪全部罪成，判监2个月。</p><p>据香港东网报道，没有律师代表的古思尧叫嚣，“求情就不必了”，裁判官不必同情或对他仁慈。裁判官则称入狱是唯一判刑考虑，参考案例后判古思尧2个月有期徒刑。</p><p>报道还称，古思尧获刑前，还与所谓的“支持者”在庭外喝酒，他预料自己今日（27日）将第6次入狱，还声称每次坐监前都要“饮番杯”（意为喝一杯），以“愉快”心情去坐牢。</p><p><img src=\\\"http://luckyimgs.peopletech.cn/image/201803/27/accbbea1824a008497eb5d7969eac5d6\\\" width=\\\"608\\\" height=\\\"434\\\"/></p><p>古思尧侮辱五星红旗。（港媒资料图）</p><p>古思尧此前曾多次发表狂妄言论。据早前报道，在2017年7月及10月的两次游行中，古思尧涂污、毁坏国旗并上下倒置。同年12月29日，古思尧在湾仔警察总部接受预约拘捕，被控以两项侮辱国旗罪。古思尧否认控罪，对两案的答辩均为“我系故意侮辱国旗，我唔认罪”（我就是故意侮辱国旗，我不认罪）。</p><p>古思尧之前曾有过多次侮辱国旗区旗的案底。2012年6月及2013年1月，古思尧游行示威时，分别焚烧国旗及涂黑国旗与香港特区区旗，经审讯后被裁定3项侮辱国旗及1项侮辱区旗罪成立，共判9个月，他上诉后减刑至4个半月。2015年7月，古思尧又在湾仔焚烧区旗，2016年3月再被裁定“侮辱区旗罪”成立，裁判官认为他已有三次同类前科，判刑6个星期。</p><p>根据《国旗及国徽条例》第七条“保护国旗、国徽”，任何人公开及故意以焚烧、毁损、涂划、玷污、践踏等方式侮辱国旗或国徽，即属犯罪，一经循公诉程序定罪，最高可处以监禁3年及罚款5万港元。</p><p>根据《区旗及区徽条例》第七条“保护区旗、区徽”，任何人公开及故意以焚烧、毁损、涂划、玷污、践踏等方式侮辱区旗或区徽，即属犯罪。一经循公诉程序定罪，可处第五级罚款及监禁3年；一经循简易程序定罪可处第三级罚款及监禁1年。（综编/海外网 张申）</p><p>本文系版权作品，未经授权严禁转载。海外视野，中国立场，登陆人民日报海外版官网——海外网www.haiwainet.cn或“海客”客户端，领先一步获取权威资讯。</p><p>责编：张申、姚凯红</p>\",\n" +
                "    \"categoryId\": null,\n" +
                "    \"images\": null,\n" +
                "    \"videos\": null,\n" +
                "    \"recommend\": null\n" +
                "}";

        try {
            long start = System.currentTimeMillis();
            JSONObject jsonObject = new JSONObject(json);
            ToastUtils.showShort(this, jsonObject.optString("imgUrl"));
            long end = System.currentTimeMillis();
            Log.i("myx", "json=" + (end - start));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
