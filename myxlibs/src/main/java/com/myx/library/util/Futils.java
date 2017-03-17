package com.myx.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by mayuxin on 2017/3/16.
 */

public class Futils {
    /**
     * app.name
     *
     * @param context
     * @return String
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }

    }

    /**
     * app.versioncode
     *
     * @param context
     * @return int
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int code = info.versionCode;
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param context
     * @return boolean
     */
    public static synchronized boolean isNetworkConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isConnected = true;
                    }
                }
            }
        }
        return isConnected;
    }

    /**
     * @return boolean
     */
    public static boolean isUpJELLY_BEAN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }


    /**
     * @param context
     * @return boolean
     */
    public static synchronized boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int networkInfoType = networkInfo.getType();
                if (networkInfoType == ConnectivityManager.TYPE_WIFI || networkInfoType == ConnectivityManager.TYPE_ETHERNET) {
                    return networkInfo.isConnected();
                }
            }
        }
        return false;
    }

    /**
     * @param context
     * @return boolean
     */
    public static boolean isMobileNetworkConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int networkInfoType = networkInfo.getType();
                if (networkInfoType == ConnectivityManager.TYPE_MOBILE) {
                    return networkInfo.isConnected();
                }
            }
        }
        return false;
    }

    /**
     * getMD5
     *
     * @param source
     * @return String
     */
    public static String getMD5(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(source.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() < 2) {
                    hex = "0" + hex;
                }
                stringBuilder.append(hex);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * getMD5
     *
     * @param source
     * @return String
     */
    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * get manifest value by key
     *
     * @param context
     * @param metaKey
     * @return String
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (Exception e) {

        }
        return apiKey;
    }

    /**
     * @param activity
     */
    public static void hideKeyBoardByFocus(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @param view
     * @param context
     */
    public static void hideKeyBoard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * @param activity
     */
    public static void exitFullscreen(Activity activity) {
        if ((activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //视频全屏后显示状态栏
            activity.getWindow().getDecorView().setSystemUiVisibility(VISIBLE);
        }
    }

    /**
     * @param activity
     */
    public static void setFullscreen(Activity activity) {
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //视频全屏后隐藏状态栏
            activity.getWindow().getDecorView().setSystemUiVisibility(INVISIBLE);
        }
    }

    /**
     * @param activity
     */
    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * @param activity
     */
    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    /**
     * @return String
     */
    public static String getOSVersion() {
        String version = "0";
        try {
            version = android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {
        }
        return version;
    }

    public static String getNetWorkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
                int networkInfoType = info.getType();
                if (networkInfoType == ConnectivityManager.TYPE_WIFI || networkInfoType == ConnectivityManager.TYPE_ETHERNET) {
                    return "wifi";
                } else if (networkInfoType == ConnectivityManager.TYPE_MOBILE) {
                    return "mobile";
                }

            }

        }
        return "wifi";
    }

    /**
     * @param context
     * @return String
     */
    public static String getIp(Context context) {
        String ip = "";
        if (getNetWorkType(context).equals("wifi")) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            // 判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        } else if (getNetWorkType(context).equals("mobile")) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ip = inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
                return null;
            }
        }
        return ip;
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }
}
