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

    private OkHttpManager() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                    .build();
        }
    }

    private OkHttpManager(OkHttpClient client) {
        this.mOkHttpClient = client;
    }

    public static OkHttpManager build() {
        return new OkHttpManager();
    }

    public static OkHttpManager build(OkHttpClient client) {
        return new OkHttpManager(client);

    }

    private String url;
    private HashMap<String, Object> params;
    private String method;
    private boolean isAsyn = true;
    private OnHttpListener listener;
    private ArrayList<File> files;
    private String fileKey;
    private String filePath;

    public OkHttpManager post() {
        method = "POST";
        return this;
    }

    public OkHttpManager get() {
        method = "GET";
        return this;
    }

    public OkHttpManager upload() {
        method = "UPLOAD";
        return this;
    }

    public OkHttpManager download() {
        method = "DOWNLOAD";
        return this;
    }

    public OkHttpManager url(String url) {
        this.url = url;
        return this;
    }

    public OkHttpManager params(HashMap<String, Object> params) {
        this.params = params;
        return this;
    }

    public OkHttpManager addFile(ArrayList<File> files) {
        this.files = files;
        return this;
    }

    public OkHttpManager filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public OkHttpManager fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public OkHttpManager asyn(boolean isAsyn) {
        this.isAsyn = isAsyn;
        return this;
    }

    public void excute(OnHttpListener listener) {
        this.listener = listener;
        if ("UPLOAD".equals(method)) {
            postAsynFile(url, params, fileKey, files, listener);
        } else if ("DOWNLOAD".equals(method)) {
            downAsynFile(url, filePath, listener);
        } else {
            request(url, params, method, isAsyn, listener);

        }

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
    private String request(String url, HashMap<String, Object > hashmap, String method, boolean isAsyn, final OnHttpListener listener) {
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

    public void downAsynFile(String url, final String path, final OnHttpListener listener) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onFailure(call, e);
                }
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
                    if (listener != null) {
                        listener.onSuccess(call, null, "");
                    }
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
    public void postAsynFile(String url, HashMap<String, Object > hashMap, String fileKey, ArrayList<File> files, final OnHttpListener listener) {
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
