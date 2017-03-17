package com.myx.library.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mayuxin on 2017/3/17.
 */

public class PreferenceUtils {
    public static final String DEFAULT_CONFIG_FILE_NAME = "config";

    /**
     * @param keyName
     * @param value
     * @param context
     */
    public static void saveStringPreference(String keyName, String value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(keyName, value);
        editor.commit();
    }

    /**
     * @param keyName
     * @param defValue
     * @param context
     * @return
     */
    public static String getStringPreference(String keyName, String defValue, Context context) {

        return context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).getString(keyName, defValue);
    }

    /**
     * @param keyName
     * @param context
     * @return
     */
    public static boolean getBoolPreference(String keyName, Context context) {
        return context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).getBoolean(keyName, false);
    }

    /**
     * @param keyName
     * @param defaultValue
     * @param context
     * @return
     */
    public static boolean getBoolPreference(String keyName, boolean defaultValue, Context context) {
        return context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).getBoolean(keyName, defaultValue);
    }

    /**
     * @param keyName
     * @param value
     * @param context
     */
    public static void saveBoolPreference(String keyName, boolean value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(keyName, value);
        editor.commit();
    }

    /**
     * @param keyName
     * @param defautValue
     * @param context
     * @return
     */
    public static float getFloatPreference(String keyName, float defautValue, Context context) {
        return context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).getFloat(keyName, defautValue);
    }

    /**
     * @param keyName
     * @param value
     * @param context
     */
    public static void saveFloatPreference(String keyName, float value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(keyName, value);
        editor.commit();
    }

    /**
     * @param keyName
     * @param defautValue
     * @param context
     * @return
     */
    public static int getIntPreference(String keyName, int defautValue, Context context) {
        return context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).getInt(keyName, defautValue);
    }

    /**
     * @param keyName
     * @param defautValue
     * @param context
     * @return
     */
    public static long getLongPreference(String keyName, int defautValue, Context context) {
        return context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).getLong(keyName, defautValue);
    }

    /**
     * @param keyName
     * @param value
     * @param context
     */
    public static void saveLongPreference(String keyName, long value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(keyName, value);
        editor.commit();
    }

    /**
     * @param keyName
     * @param value
     * @param context
     */
    public static void saveIntPreference(String keyName, int value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(keyName, value);
        editor.commit();
    }

    /**
     * @param keyName
     * @param context
     */
    public static void removePreference(String keyName, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(keyName);
        editor.commit();
    }

    /**
     * @param context
     */
    public static void removeAllPreference(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
