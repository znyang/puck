package com.zen.android.puck.rx;

import com.zen.android.puck.hook.PuckSchedulerHook;
import com.zen.android.puck.util.ReflectUtils;

import org.robolectric.DefaultTestLifecycle;
import org.robolectric.util.Logger;

import java.lang.reflect.Method;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 * @author zen
 * @version 2016/11/16
 */
public class RxTestLifecycle extends DefaultTestLifecycle {

    private static final boolean hasUseRxAndroid = ReflectUtils.hasUseRxAndroid();

    @Override
    public void beforeTest(Method method) {
        super.beforeTest(method);

        resetRxJavaPlugin();
        try {
            RxJavaPlugins.getInstance().registerSchedulersHook(new PuckSchedulerHook());
        } catch (IllegalStateException e) {
            Logger.error("RxJava SchedulersHook has register");
        }

        if (hasUseRxAndroid) {
            rx.android.plugins.RxAndroidPlugins.getInstance().reset();
            try {
                rx.android.plugins.RxAndroidPlugins.getInstance().registerSchedulersHook(
                        new HookBuilder().build());
            } catch (IllegalStateException e) {
                Logger.error("RxAndroid SchedulersHook has register");
            }
        }
    }

    @Override
    public void afterTest(Method method) {
        super.afterTest(method);

        if (hasUseRxAndroid) {
            rx.android.plugins.RxAndroidPlugins.getInstance().reset();
        }
        resetRxJavaPlugin();
    }

    private static class HookBuilder {
        rx.android.plugins.RxAndroidSchedulersHook build() {
            return new rx.android.plugins.RxAndroidSchedulersHook() {
                @Override
                public Scheduler getMainThreadScheduler() {
                    return Schedulers.immediate();
                }
            };
        }
    }

    private static void resetRxJavaPlugin() {
        try {
            RxJavaPlugins.getInstance().reset();
        } catch (IllegalAccessError e) {
            Logger.error("RxJavaPlugin.reset() call failed!");
            ReflectUtils.callMethod(RxJavaPlugins.getInstance(), "reset");
        }
    }

}
