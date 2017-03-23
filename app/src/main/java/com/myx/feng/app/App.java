package com.myx.feng.app;

import android.app.Application;


//import com.myx.library.image.ImageUtils;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Api.init(this, new Api.ApiCallBack() {
//            @Override
//            public String getHost(Class cls) {
//              if(cls.getName().equals(ApiService.class.getName())){
//                  return "http://api.people.com";
//              }else if(cls.getName().equals(UsercenterService.class.getName())){
//                  return "http://usercenter.people.com";
//              }
//                return null;
//            }
//        });
//
//            Api<ApiService>.retrofit.create()
    }
}
