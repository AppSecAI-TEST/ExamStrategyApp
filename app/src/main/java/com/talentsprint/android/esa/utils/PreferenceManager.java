package com.talentsprint.android.esa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Anudeep on 02-05-2017.
 */
public final class PreferenceManager {
    /**
     * Name for shared preferences file.
     */
    private static final String PREF_NAME = "TsptintSharedPreferences";

    /**
     * Hidden constructor.
     */
    private PreferenceManager() {
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context
                .MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void clearPreferences(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear().commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        boolean value = getSharedPreferences(context).getBoolean(key, defaultValue);
        return value;
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        float value = getSharedPreferences(context).getFloat(key, defaultValue);
        return value;
    }

    public static void saveFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        int value = getSharedPreferences(context).getInt(key, defaultValue);
        return value;
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        long value = getSharedPreferences(context).getLong(key, defaultValue);
        return value;
    }

    public static void saveLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        String value = getSharedPreferences(context).getString(key, defaultValue);
        return value;
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        Set<String> value = getSharedPreferences(context).getStringSet(key, defaultValue);
        return value;
    }

    public static void saveStringSet(Context context, String key, Set<String> value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static void delete(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).commit();
    }

    public static void deleteAll(Context context) {
        getSharedPreferences(context).edit().clear().commit();
    }
}
