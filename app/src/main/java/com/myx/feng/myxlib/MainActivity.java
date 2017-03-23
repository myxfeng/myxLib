package com.myx.feng.myxlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.myx.library.rxjava.URL;
import com.myx.library.util.Futils;
import com.myx.library.util.ToastUtils;

import java.util.HashMap;

import static com.myx.feng.myxlib.MainActivity.url;

@URL(host = url ,port = ":8080")
public class MainActivity extends AppCompatActivity {
    public static  final String url="1111";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        OkHttpManager.getInstance().setClient();
    }

    int i = 0;

    public void postImg(View view) {
       ToastUtils.showLong(this, Futils.getClassValue(MainActivity.class,URL.class).get("host").toString()+"===="+ Futils.getClassValue(MainActivity.class,URL.class).get("port"));
////        String url = "http://10.100.10.193:8010/mockjsdata/1/uploadimg.php";
//        String url = "http://newsapi.people.cn/sports/content/getdetail";
//        ArrayList<File> files = new ArrayList<>();
//        files.add(new File(Environment.getExternalStorageDirectory(), "paper.pdf"));
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("111", "长城");
//        hashMap.put("333", "人民");
//        hashMap.put("444", "天安门");
//        hashMap.put("5555", "22222");
////        OkHttpManager.getInstance().postFile(url, hashMap, "uploadfile", null, null);
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new ParamsInterceptor() {
//            @Override
//            public HashMap<String, String> getParams() {
//                HashMap<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("pa1", "公共参数1");
//                hashMap.put("pa1", "公共参数1");
//                hashMap.put("pa1", "公共参数1");
//                hashMap.put("pa1", "公共参数1");
//                return hashMap;
//            }
//        }).build();
//        OkHttpManager.getInstance().setClient(okHttpClient);
//        OkHttpManager.getInstance().postAsyn(url, hashMap, null);


        String url = "http://newsapi.people.cn/sports/content/getdetail";
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("adcode", "");
//        hashMap.put("isoCC", "cn");
//        hashMap.put("city", "city");
//        hashMap.put("device_product", "samsung");
//        hashMap.put("timestamp", "");

            hashMap.put("province","北京");

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

//        OkHttpClient okHttpClient = new OkHttpClient.Builder().
//                addInterceptor(new ParamsInterceptor() {
//                    @Override
//                    public HashMap<String, String> getParams() {
//                        HashMap<String, String> hashMap = new HashMap<String, String>();
//                        hashMap.put("pa1", "公共参数1");
//
//                            hashMap.put("pa2","公共参数2");
//
//                        return hashMap;
//                    }
//                }).
//                build();
//        OkHttpManager.getInstance().setClient(okHttpClient);
//
//
//        OkHttpManager.getInstance().getAsyn(url, hashMap, new OnHttpListener() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onSuccess(Call call, Response response, String json) throws IOException {
//
//            }
//        });
    }
}
