package com.myx.library.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mayuxin on 2017/3/21.
 */

public abstract class ParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        //url
        HttpUrl.Builder urlBuilder = original.url().newBuilder();
        RequestBody requestBody = original.body();
        if ("GET".equals(original.method())) {//GET请求的参数是挂在url 上
            HashMap<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        } else if ("POST".equals(original.method())) {// POST 请求的参数是放在body 体中
            if (requestBody instanceof FormBody) {
                FormBody.Builder formBuilder = new FormBody.Builder();
                // 添加已有参数
                FormBody originalFormBody = (FormBody) requestBody;
                int size = originalFormBody.size();
                for (int i = 0; i < size; i++) {
                    formBuilder.addEncoded(originalFormBody.encodedName(i), originalFormBody.encodedValue(i));
                }
                // 添加公共参数
                FormBody paramsFormBody = (FormBody) HttpUtil.hasMapToBody(getParams());
                int paramsSize = paramsFormBody.size();
                for (int i = 0; i < paramsSize; i++) {
                    formBuilder.addEncoded(paramsFormBody.encodedName(i), paramsFormBody.encodedValue(i));
                }
                requestBody = formBuilder.build();

            } else if (requestBody instanceof MultipartBody) {
                MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
                // 添加已有参数
                multiBuilder.addPart(requestBody);
                // 添加公共参数
                multiBuilder.addPart(HttpUtil.hasMapToBody(getParams()));
                requestBody=multiBuilder.build();
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

}
