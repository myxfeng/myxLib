package com.myx.library.http;

import com.myx.library.util.CheckUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
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
    public static String setUrlParameter(String url, Map<String, String> map) {
        if (!CheckUtils.isEmptyStr(url)) {
            if (map != null && map.size() > 0) {
                Set<String> set = map.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = map.get(key);
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

    public static RequestBody hasMapToBody(HashMap<String, String> hashmap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (hashmap != null) {
            Iterator<Map.Entry<String, String>> iterator = hashmap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                builder.add(entry.getKey().toString(), entry.getValue().toString());
            }
            return builder.build();
        }
        return null;
    }
}
