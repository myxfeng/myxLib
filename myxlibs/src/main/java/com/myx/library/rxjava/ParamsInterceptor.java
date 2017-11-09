package com.myx.library.rxjava;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mayuxin on 2017/3/21.
 */

public abstract class ParamsInterceptor implements Interceptor {

    //    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request original = chain.request();
//        HttpUrl httpUrl = original.url();
//       String pjCode= httpUrl.queryParameter("pjCode");
//        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
//        RequestBody requestBody = original.body();
//        if ("GET".equals(original.method())) {//GET请求的参数是挂在url 上
//            HashMap<String, String> params = getParams();
//            if (params != null && params.size() > 0) {
//                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, String> entry = iterator.next();
//                    if(CheckUtils.isNoEmptyStr(pjCode)&&!entry.getValue().equals(pjCode)){
//                        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
//                    }
////                    if (urlBuilder.query("pjCode") != null) {
////                        MLog.e("test:::you code");
////                    } else {
////                        MLog.e("meiyou  code");
////                    }
//                }
//            }
//        } else if ("POST".equals(original.method())) {// POST 请求的参数是放在Form表单body 体中
//            if (requestBody instanceof FormBody) {
//                FormBody.Builder formBuilder = new FormBody.Builder();
//                // 添加已有参数
//                FormBody originalFormBody = (FormBody) requestBody;
//                int size = originalFormBody.size();
//                for (int i = 0; i < size; i++) {
//                    formBuilder.addEncoded(originalFormBody.encodedName(i), originalFormBody.encodedValue(i));
//                }
//                // 添加公共参数
//                FormBody paramsFormBody = (FormBody) HttpUtil.hasMapToBody(getParams());
//                int paramsSize = paramsFormBody.size();
//                for (int i = 0; i < paramsSize; i++) {
//                    formBuilder.addEncoded(paramsFormBody.encodedName(i), paramsFormBody.encodedValue(i));
//                }
//                requestBody = formBuilder.build();
//
//            } else if (requestBody instanceof MultipartBody) {
//                MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
//                // 添加已有参数
//                multiBuilder.addPart(requestBody);
//                // 添加公共参数
//                multiBuilder.addPart(HttpUtil.hasMapToBody(getParams()));
//                requestBody = multiBuilder.build();
//            } else {
//                HashMap<String, String> params = getParams();
//                if (params != null && params.size() > 0) {
//                    Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
//                    while (iterator.hasNext()) {
//                        Map.Entry<String, String> entry = iterator.next();
//                        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
//                    }
//                }
//            }
//
//
//        }
//
//        //添加请求头
//        Request request = original.newBuilder()
////                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
////                .addHeader("Connection", "keep-alive")
//                .method(original.method(), requestBody)
//                .url(urlBuilder.build())
//                .build();
//        return chain.proceed(request);
//    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl httpUrl = original.url();
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        RequestBody requestBody = original.body();
        HashMap<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (!isExist(entry.getKey(), httpUrl)) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        //添加请求头
        Request request = original.newBuilder()
//                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                .addHeader("Connection", "keep-alive")
                .method(original.method(), requestBody)
                .url(urlBuilder.build())
                .build();
        return chain.proceed(request);
    }

    public abstract HashMap<String, String> getParams();

    private boolean isExist(String key, HttpUrl httpUrl) {
        String values = httpUrl.queryParameter(key);
        if (values != null) {
            return true;
        }
        return false;
    }
}
