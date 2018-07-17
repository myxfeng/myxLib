package com.myx.library.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.UUID;


/**
 * 屏幕宽高
 * uuid
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * Created by mayuxin on 2018/2/2.
 */

public class DeviceUtils {
    private static final String UUID_KEY="uuid";

    /**
     * 适配
     *
     * @author Administrator
     */
    /**
     * 根据屏幕尺寸区分设备类型 XLARGE[10,7) LARGE[7,4) NORMAL[4,)
     */
    public enum DeviceTypes {
        XLARGE, LARGE, NORMAL
    }


    private static DeviceTypes deviceType = null;
    private static String deviceId = null;
    private static float xdpi = -1f;
    public static final String DEVICE_ID = "device_id";
    public static final String NETWORKTYPE_INVALID = "null";
    /**
     * wap网络
     */
    public static final String NETWORKTYPE_WAP = "wap";
    /**
     * 2G网络
     */
    public static final String NETWORKTYPE_2G = "2G";
    /**
     * 3G
     */
    public static final String NETWORKTYPE_3G = "3G";
    /**
     * 4G
     */
    public static final String NETWORKTYPE_4G = "4G";
    /**
     * wifi网络
     */
    public static final String NETWORKTYPE_WIFI = "wifi";



    public static int getDisplayWidth(Context context) {
        if(Futils.isLand(context)){
            return getDisplayMetrics(context).heightPixels;
        }else{
            return getDisplayMetrics(context).widthPixels;
        }
    }
    public static int getDisplayHeight(Context context) {
        if(Futils.isLand(context)){
            return getDisplayMetrics(context).widthPixels;
        }else{
            return getDisplayMetrics(context).heightPixels;
        }
    }

    public static int getDisplayWidth(Context context, int orientation) {
        int width = 0;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = getDisplayMetrics(context).widthPixels;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            width = getDisplayMetrics(context).heightPixels;
        }
        return width;
    }

    public static int getDisplayHeight(Context context, int orientation) {
        int height = 0;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            height = getDisplayMetrics(context).heightPixels;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = getDisplayMetrics(context).widthPixels ;
        }
        return height;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
    /**
     * 获取手机唯一标识
     *
     * @return
     */
    public static String getDeviceId(Context context) {
        if (deviceId != null) {
            return deviceId;
        }
        if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = System.getString(context.getContentResolver(), Secure.ANDROID_ID);
        }
        return deviceId;
    }

    public static String getAndroid_ID(Context context) {
        return System.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static String getIMEI(Context context) {
        if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } else {
            return "";
        }
    }

    public static String getIMSI(Context context) {
        if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSubscriberId();
        } else {
            return "";
        }

    }


    /**
     * 获取sim卡运营商名称（SP）
     *
     * @return
     */
    public static String getProvidersName(Context context) {
        String IMSI = "";
        if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            // 返回唯一的用户ID;就是这张卡的编号神马的
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            IMSI = telephonyManager.getSubscriberId();
        }
        String ProvidersName = "";

        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。

        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } else {
            ProvidersName = "";
        }

        return ProvidersName;
    }

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

    public static String getNetWorkType(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            String mNetWorkType = NETWORKTYPE_INVALID;
            if (networkInfo != null && networkInfo.isConnected()) {
                String type = networkInfo.getTypeName();
                if (type.equalsIgnoreCase("WIFI")) {
                    mNetWorkType = NETWORKTYPE_WIFI;
                } else if (type.equalsIgnoreCase("MOBILE")) {
                    mNetWorkType = mobileNetworkType(context);
                }
            } else {
                mNetWorkType = NETWORKTYPE_INVALID;
            }

            return mNetWorkType;
        } catch (Exception e) {
        }
        return NETWORKTYPE_INVALID;
    }

    private static String mobileNetworkType(Context context) {
        if (!hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return NETWORKTYPE_INVALID;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return NETWORKTYPE_2G;// ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return NETWORKTYPE_2G;// ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return NETWORKTYPE_2G;// ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return NETWORKTYPE_3G;// ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return NETWORKTYPE_3G;// ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return NETWORKTYPE_2G;// ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return NETWORKTYPE_3G;// ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return NETWORKTYPE_3G;// ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return NETWORKTYPE_3G;// ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return NETWORKTYPE_3G;// ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return NETWORKTYPE_3G;// ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return NETWORKTYPE_3G;// ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NETWORKTYPE_3G;// ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NETWORKTYPE_2G;// ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return NETWORKTYPE_4G;// ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return NETWORKTYPE_INVALID;
                default:
                    return NETWORKTYPE_INVALID;
            }
        }
    }

    /**
     * 获取手机系统版本号
     *
     * @return
     */
    public static String getOSVersion() {
        String version = "0";
        try {
            version = android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {
        }
        return version;
    }

    public static int dip2px(Context context, float dipValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } else {
            return 0;
        }
    }

    public static int px2dip(Context context, float pxValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        } else {
            return 0;
        }
    }

    public static float sp2px(Context context, float sp) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return sp * scale;
        } else {
            return 0;
        }
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getDeviceModel() {
        try {
            return encodeURL(android.os.Build.MODEL);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getDeviceProduct() {
        try {
            return encodeURL(android.os.Build.MANUFACTURER);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * URL编码
     *
     * @param url
     */
    public static String encodeURL(String url) {
        if (null == url) {
            return "";
        }
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
        }
        return url;
    }

    /**
     * 获取sim卡ISOCC码
     *
     * @param context
     * @return
     */
    public static String getIsoCCName(Context context) {
        if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // 返回唯一的用户ID;就是这张卡的编号神马的
            String iso = telephonyManager.getSimCountryIso();

            if (isNoEmptyStr(iso)) {
                try {
                    return iso;
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }

        return "";
    }

    /**
     * 获取手机的mac
     *
     * @return
     */
    public static String getLocalMacAddress(Context appContext) {

        WifiManager wifi = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取手机MCC码
     *
     * @param appContext
     * @return
     */
    public static String getMCCName(Context appContext) {
        if (hasPermission(appContext, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager telephonyManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            // 返回唯一的用户ID;就是这张卡的编号神马的
            String IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。

            if (IMSI != null && IMSI.length() > 0) {
                try {
                    return IMSI.substring(0, 3);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

        }

        return "";
    }

    /**
     * 获取手机MNC码
     *
     * @param appContext
     * @return
     */
    public static String getMNCName(Context appContext) {
        if (hasPermission(appContext, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager telephonyManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            // 返回唯一的用户ID;就是这张卡的编号神马的
            String IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。

            if (IMSI != null && IMSI.length() > 0) {
                try {
                    return IMSI.substring(3, 5);
                } catch (Exception e) {
                    // TODO: handle exception

                }

            }

        }

        return "";
    }

    public static boolean isNoEmptyStr(String str) {
        return !isEmptyStr(str);
    }

    public static boolean isEmptyStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        // 过滤空格
        str = str.trim();
        return str.equals(" ") || str.equals("") || "NULL".equals(str) || "null".equals(str);
    }

    /**
     * @author Johnson 获取手机唯一标示
     */
    private static String getUUIDWithoutSave(Context context) {
        String UUID = getIMEI(context);
        if (isUUIDValid(UUID)) {
            return UUID;
        } else {

            UUID = getLocalMacAddress(context);
            if (isUUIDValid(UUID)) {
                return MD5Util.getMD5(UUID.getBytes());
            } else {
                return CreateUUID(context);
            }
        }
//		Random r=new Random();
//		int a= r.nextInt(100000);
//		return a+"";
    }

    /**
     * @author Johnson 获取手机唯一标示
     */
    public static String getUUID(Context context) {
        String UUID = PreferenceUtils.getStringPreference(DEVICE_ID, "", context);
        if (CheckUtils.isNoEmptyStr(UUID)) {
            return UUID;
        }
        UUID = getUUIDWithoutSave(context);
        PreferenceUtils.saveStringPreference(DEVICE_ID, UUID, context);
        return UUID;
    }

    /**
     * @author Johnson 创建UUID
     */
    public static String CreateUUID(Context context) {
        String uuidStr = null;
        try {
            uuidStr = PreferenceUtils.getStringPreference(UUID_KEY, "", context);
            if (CheckUtils.isEmptyStr(uuidStr)) {
                uuidStr = "S_" + UUID.randomUUID().toString().replaceAll("-", "");
                PreferenceUtils.saveStringPreference(UUID_KEY, uuidStr, context);// 存时间戳
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return uuidStr;
    }

    /**
     * @author Johnson 判断串号是否相同数字
     */
    public static boolean equalStr(String numOrStr) {
        boolean flag = true;
        char str = numOrStr.charAt(0);
        for (int i = 0; i < numOrStr.length(); i++) {
            if (str != numOrStr.charAt(i)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isUUIDValid(String UUID) {
        if (CheckUtils.isEmptyStr(UUID)) {
            return false;
        }
        if (UUID.equals("Unknown")) {
            return false;
        }
        return !equalStr(UUID);
    }

    public static boolean hasPermission( Context context,  String permission) {
        return context != null && (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED);
    }



}
