package com.zen.android.puck.util;

import org.robolectric.util.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    @SuppressWarnings("unchecked")
    public static void callMethod(Object obj, String methodName) {
        Class c = obj.getClass();
        try {
            Method m = c.getDeclaredMethod(methodName);
            if (m != null) {
                m.setAccessible(true);
                m.invoke(obj);
                Logger.error("RxJavaPlugin.reset() call by reflect");
            }
        } catch (NoSuchMethodException e) {
            Logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            Logger.error(e.getMessage());
        } catch (IllegalAccessException e) {
            Logger.error(e.getMessage());
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }

}
