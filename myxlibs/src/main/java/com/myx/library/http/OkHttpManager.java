package com.myx.library.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class OkHttpManager {
    private OkHttpClient mOkHttpClient;
    private static volatile OkHttpManager okhttpmanager;

    private OkHttpManager() {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .readTimeout(10000,TimeUnit.MILLISECONDS)
                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                    .build();

    }

    private OkHttpManager(OkHttpClient client) {
        this.mOkHttpClient = client;
    }

    public OkHttpClient getClient() {
        return mOkHttpClient;
    }

    public void setClient(OkHttpClient client) {
        this.mOkHttpClient = client;
    }

    /**
     * 单例
     *
     * @return
     */
    public static OkHttpManager getInstance() {
        if (okhttpmanager == null) {
            synchronized (OkHttpManager.class) {
                if(okhttpmanager==null){
                    okhttpmanager = new OkHttpManager();
                }
            }
        }
        return okhttpmanager;
    }

    /**
     * 非单例
     *
     * @return
     */
    public static OkHttpManager initOKHttp(OkHttpClient client) {
        okhttpmanager = new OkHttpManager(client);
        return okhttpmanager;
    }


    /**
     * @param url
     * @param hashmap
     * @param listener
     * @param method
     * @param isAsyn
     * @return
     * @throws IOException
     */
    private String request(String url, HashMap<String, Object> hashmap, String method, boolean isAsyn, final OnHttpListener listener) {
        RequestBody requestbody = null;
        if ("GET".equals(method)) {
            url = HttpUtil.setUrlParameter(url, hashmap);
        } else if ("POST".equals(method)) {
            requestbody = HttpUtil.hasMapToBody(hashmap);

        }
        Request request = new Request.Builder().url(url).method(method, requestbody).build();
        if (isAsyn) {
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (listener != null) {
                        listener.onFailure(call, e);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (listener != null) {
                        listener.onSuccess(call, response, response.body().string());
                    }
                }
            });
        } else {
            Response response = null;
            try {
                response = mOkHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * @param url
     * @param hashMap
     * @param listener
     */
    public void getAsyn(String url, HashMap<String, Object> hashMap, final OnHttpListener listener) {
        request(url, hashMap, "GET", true, listener);
    }

    /**
     * @param url
     * @param listener
     */
    public void getAsyn(String url, final OnHttpListener listener) {
        request(url, null, "GET", true, listener);
    }

    /**
     * @param url
     * @param hashMap
     * @return
     */
    public String get(String url, HashMap<String, Object> hashMap) {
        return request(url, hashMap, "GET", false, null);
    }

    /**
     * @param url
     * @return
     */
    public String get(String url) {
        return request(url, null, "GET", false, null);
    }

    /**
     * @param url
     * @param hashMap
     * @param listener
     */
    public void postAsyn(String url, HashMap<String, Object> hashMap, final OnHttpListener listener) {
        request(url, hashMap, "POST", true, listener);
    }

    /**
     * @param url
     * @param listener
     */
    public void postAsyn(String url, final OnHttpListener listener) {
        request(url, null, "POST", true, listener);
    }

    /**
     * @param url
     * @param hashMap
     * @return
     */
    public String post(String url, HashMap<String, Object> hashMap) {
        return request(url, hashMap, "POST", false, null);
    }

    /**
     * @param url
     * @return
     */
    public String post(String url) {
        return request(url, null, "POST", false, null);
    }

    public void downAsynFile(String url, final String path) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                        file.createNewFile();
                    }

                    fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
    }

    /**
     * RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
     * MultipartBody.Part part = MultipartBody.Part.createFormData(UPLOAD_DATA_PARAM, file.getName(), requestFile);
     *
     * @param url
     * @param hashMap
     * @param fileKey
     * @param files
     * @param listener
     */
    public void postAsynFile(String url, HashMap<String, Object> hashMap, String fileKey, ArrayList<File> files, final OnHttpListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().addPart(HttpUtil.hasMapToBody(hashMap));
        if (files != null) {
            for (File file : files) {
                RequestBody filbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart(fileKey + "[]", file.getName(), filbody);
            }
        }
        builder.addPart(HttpUtil.hasMapToBody(hashMap));
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (listener != null) {
                    listener.onSuccess(call, response, response.body().string());
                }
            }
        });
    }

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            mOkHttpClient.newBuilder().sslSocketFactory(sslContext.getSocketFactory()).build();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
