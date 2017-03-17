package com.myx.feng.myxlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    int i = 0;

    public void postImg(View view) {
//        String url = "http://10.100.10.193:8010/mockjsdata/1/uploadimg.php";
//        ArrayList<File> files = new ArrayList<>();
//        files.add(new File(Environment.getExternalStorageDirectory(), "paper.pdf"));
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("111", "长城");
//        hashMap.put("333", "人民");
//        hashMap.put("444", "天安门");
//        hashMap.put("5555", "22222");
////        OkHttpManager.getInstance().postFile(url, hashMap, "uploadfile", null, null);
//        OkHttpManager.getInstance().postSync(url, hashMap, null);
//        int aaa = OkHttpManager.getInstance().getClient().connectTimeoutMillis();
//        ToastUtils.showShort(this, aaa + "");
//        OkHttpManager.getInstance().getClient().newBuilder().connectTimeout(5 + 1, TimeUnit.SECONDS);
//        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
//        OkHttpManager.getInstance().setClient(client);


//        String url = "http://newsapi.people.cn/sports/content/getdetail";
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
//        OkHttpManager.getInstance().postSync(url, hashMap, new OnHttpListener() {
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
