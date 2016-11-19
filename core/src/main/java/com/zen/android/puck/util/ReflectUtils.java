package com.zen.android.puck.util;

/**
 * @author zen
 * @version 2016/11/19
 */
public class ReflectUtils {

    public static boolean hasClass(String className) {
        try {
            Class cls = Class.forName(className);
            return cls != null;
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            return false;
        }
    }

    public static boolean hasUseRxJava() {
        return hasClass("rx.plugins.RxJavaPlugins");
    }

    public static boolean hasUseRxAndroid() {
        return hasClass("rx.android.plugins.RxAndroidPlugins");
    }

}
