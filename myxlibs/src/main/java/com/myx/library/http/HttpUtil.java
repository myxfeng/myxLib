package com.myx.library.http;

import com.myx.library.util.CheckUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class HttpUtil {
    /**
     * @param url
     * @param map
     * @return String
     */
    public static String setUrlParameter(String url, Map<String, Object> map) {
        if (!CheckUtils.isEmptyStr(url)) {
            if (map != null && map.size() > 0) {
                Set<String> set = map.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = map.get(key).toString();
                    url = setUrlParameter(url, key, value);
                }
            }
        }
        return url;
    }

    /**
     * @param str
     * @param key
     * @param value
     * @return String
     */
    public static String setUrlParameter(String str, String key, String value) {
        if (!CheckUtils.isEmptyStr(str) && !CheckUtils.isEmptyStr(key)) {
            try {
                str = addUrlSeparator(str) + key + "=" + URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * @param str
     * @return String
     */
    public static String addUrlSeparator(String str) {
        if (!CheckUtils.isEmptyStr(str)) {
            int a = str.indexOf("?");
            if (a != -1) {
                str = str + "&";
            } else {
                str = str + "?";
            }
        }
        return str;
    }

    public static RequestBody hasMapToBody(HashMap<String, Object> hashmap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (hashmap != null) {
            Iterator<Map.Entry<String, Object>> iterator = hashmap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                builder.add(entry.getKey().toString(), entry.getValue().toString());
            }
            return builder.build();
        }
        return null;
    }

    public static OkHttpClient.Builder setCertificates(OkHttpClient.Builder builder,InputStream... certificates) {
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
           return  builder.sslSocketFactory(sslContext.getSocketFactory());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;

    }
}
