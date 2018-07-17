package com.myx.library.http;

import android.text.TextUtils;

import com.facebook.common.file.FileUtils;
import com.google.gson.Gson;
import com.myx.library.rxjava.Api;
import com.myx.library.rxjava.RxBus;
import com.myx.library.util.ACache;
import com.myx.library.util.Futils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by mayuxin on 2018/4/23.
 */

public class ApiManager<T> {
    public interface ApiCallback<T> {
        void onSuccess(String json, T t);

        void onFailure(String e);
    }


    public static void request(String cacheKey, String url,HashMap<String,Object>params, final ApiCallback apiCallback) {
        if (!TextUtils.isEmpty(cacheKey)) {
            apiCallback.onSuccess("",unserializeObject(cacheKey));
        }
        OkHttpManager.getInstance().getAsyn(url, params,new OnHttpListener() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (apiCallback != null) {
                    apiCallback.onFailure(e.toString());
                }
            }
            @Override
            public void onSuccess(Call call, Response response, String json) throws IOException {
                if (apiCallback != null) {
                    Class cls = getT(apiCallback, 0);
                    if (cls != null) {
                        apiCallback.onSuccess(json, new Gson().fromJson(json, cls.getClass()));

                    } else {
                        apiCallback.onSuccess(json, null);
                    }
                }
            }
        });
    }

    void test(){
        request("", "", null, new ApiCallback<Api>() {
            @Override
            public void onSuccess(String json, Api o) {

            }

            @Override
            public void onFailure(String e) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T getT(Object o, int i) {
        try {
            Type type = o.getClass().getGenericSuperclass();
            Type[] actualTypeArguments = ((ParameterizedType) (type)).getActualTypeArguments();
            return ((Class<T>) actualTypeArguments[i]).newInstance();
        } catch (InstantiationException | ClassCastException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 反序列化获取Object对象，文件不存在时返回NULL
     *
     * @return
     * @author liuxiangwei<yava> Date:2011-2-22
     */
    public static Object unserializeObject(String path) {
        File sectionFile = new File(path);
        // 直接反序列化
        if (!sectionFile.exists()) {
            return null;
        }
        ObjectInputStream ois = null;
        Object o = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            o = ois.readObject();
        } catch (Exception e) {
            sectionFile.delete();
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
        }
        return o;
    }

}