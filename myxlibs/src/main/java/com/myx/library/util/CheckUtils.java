package com.myx.library.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class CheckUtils {
    public static boolean isEmptyList(List arg) {
        return !(arg != null && arg.size() > 0);
    }

    public static boolean isNoEmptyList(List arg) {
        return !isEmptyList(arg);
    }

    public static boolean isEmptyStr(String str) {
        str = str.trim();
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return str.equals(" ") || str.equals("") || "NULL".equals(str) || "null".equals(str);
    }

    public static boolean isNoEmptyStr(String str) {
        return !isEmptyStr(str);
    }
}
