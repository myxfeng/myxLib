//package com.myx.feng.myxlib;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.google.gson.Gson;
//import com.myx.library.image.ImageUtils;
//import com.myx.library.util.CheckUtils;
//import com.myx.library.util.OkHttpUtil;
//
//import java.io.IOException;
//import java.util.HashMap;
//
//import okhttp3.Call;
//import okhttp3.Response;
//
//public class TestActivity extends AppCompatActivity {
//    String url = "http://newsapi.people.cn/sports/content/getdetail";
//    private SimpleDraweeView img;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        img = (SimpleDraweeView) findViewById(R.id.img);
//
//    }
//
//    public void postImg(View view) {
//        test();
//    }
//
//    void test() {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("adcode", "");
//        hashMap.put("isoCC", "cn");
//        hashMap.put("city", "city");
//        hashMap.put("device_product", "samsung");
//        hashMap.put("timestamp", "");
//        hashMap.put("province", "北京");
//        hashMap.put("userid", "1000552");
//        hashMap.put("network_state", "wifi");
//        hashMap.put("MNC", "03");
//        hashMap.put("client_ver", "1.0.0");
//        hashMap.put("client_code", "1");
//        hashMap.put("articleid", "2095260387443712_cms_2095260387443712");
//        hashMap.put("udid", "A000004817BFB5");
//        hashMap.put("MCC", "460");
//        hashMap.put("visit_id", "1489727795222");
//        hashMap.put("device_os", "5.0");
//        hashMap.put("longitude", "");
//        hashMap.put("sp", "");
//        hashMap.put("visit_start_time", "1489728268977");
//        hashMap.put("ctime", "1489728268977");
//        hashMap.put("sessionId", "f145e922e7c24e9cab3e7e457ff30cd4v5HT5f9z");
//        hashMap.put("platform", "android");
//        hashMap.put("app_key", "10_2016_12_89");
//        hashMap.put("device_size", "1080.0x1920.0");
//        hashMap.put("securitykey", "c9121528a489233a97a840d083ae5606");
//        OkHttpUtil.postSync(url, hashMap, new OkHttpUtil.OnHttpListener() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onSuccess(Call call, Response response, String json) throws IOException {
//                if (CheckUtils.isNoEmptyStr(json)) {
//                    NewsResult result = new Gson().fromJson(json, NewsResult.class);
//                    if (result != null) {
//                        ImageUtils.loadBitmapOnlyWifi(result.getData().getCover(), img, false, 0);
//                    }
//                }
//            }
//        });
//    }
//}
