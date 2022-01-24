package com.unity3d.player;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 */
public final class SPUtils {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "config";

    public static void init(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 保存数据的方法
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        String type = value.getClass().getSimpleName();
        if ("String".equals(type)) {
            editor.putString(key, (String) value);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) value);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) value);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) value);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) value);
        }
        editor.commit();
    }

    /**
     * 获取数据的方法
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(String key, Object defaultValue) {
        String type = defaultValue.getClass().getSimpleName();
        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultValue);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultValue);
        }
        return null;
    }

    public static String getString(String key) {
        return sp.getString(key, "");
    }

    public static Boolean getBoolean(String key) {
        return sp.getBoolean(key, true);
    }

    public static Integer getInteger(String key) {
        return sp.getInt(key, 0);
    }

    public static void clear() {
        editor.clear();
        editor.commit();
    }
}
