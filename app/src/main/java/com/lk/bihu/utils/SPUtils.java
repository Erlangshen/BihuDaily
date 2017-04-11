package com.lk.bihu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.lk.bihu.bean.MenuList;
import com.lk.bihu.bean.ThemeMainInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 缓存辅助类
 */

public class SPUtils {
    private final static String FILENAME = "bihuCache";

    public static void setMenuList(Context con, List<ThemeMainInfo> others) {
        SharedPreferences sp = con.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        MenuList menuList = new MenuList();
        menuList.setOthers(others);
        String menuListStr = JSONObject.toJSONString(menuList);
        editor.putString("menuList", menuListStr);
        SharedPreferencesCompat.apply(editor);
    }

    public static List<ThemeMainInfo> getMenuList(Context con) {
        SharedPreferences sp = con.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        String jsonStr = sp.getString("menuList", null);
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        } else {
            MenuList menuList = JSONObject.parseObject(jsonStr, MenuList.class);
            return menuList.getOthers();
        }
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
