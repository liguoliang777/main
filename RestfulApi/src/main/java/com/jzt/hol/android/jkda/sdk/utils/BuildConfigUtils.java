package com.jzt.hol.android.jkda.sdk.utils;

import java.lang.reflect.Field;

/**
 * Created by tangkun on 16/9/9.
 */
public class BuildConfigUtils {

    private BuildConfigUtils() {
        throw new IllegalArgumentException("工具类不能实例化");
    }

    /**
     * 获取主应用的BuildConfig相关字段
     */
    public static <T> T getBuildConfigValue(String fieldName, T defaultValue) {
        try {
//            final Class<?> activityThread = Class.forName("android.app.ActivityThread");
//            final Method currentPackage = activityThread.getMethod("currentPackageName");
//            final String packageName = (String) currentPackage.invoke(null, (Object[]) null);
            Class<?> clazz = Class.forName("com.jzt.hol.android.jkda.sdk.BuildConfig");
            Field field = clazz.getField(fieldName);
            return (T)field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
        return defaultValue;
    }
}
