package com.zen.android.puck.rx;

import com.zen.android.puck.hook.PuckSchedulerHook;
import com.zen.android.puck.util.ReflectUtils;

import org.robolectric.DefaultTestLifecycle;

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

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new PuckSchedulerHook());

        if (hasUseRxAndroid) {
            rx.android.plugins.RxAndroidPlugins.getInstance().reset();
            rx.android.plugins.RxAndroidPlugins.getInstance().registerSchedulersHook(
                    new HookBuilder().build());
        }
    }

    @Override
    public void afterTest(Method method) {
        super.afterTest(method);

        if (hasUseRxAndroid) {
            rx.android.plugins.RxAndroidPlugins.getInstance().reset();
        }
        RxJavaPlugins.getInstance().reset();
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
}
