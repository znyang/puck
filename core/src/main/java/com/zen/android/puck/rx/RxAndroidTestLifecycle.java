package com.zen.android.puck.rx;

import com.zen.android.puck.hook.PuckSchedulerHook;

import org.robolectric.DefaultTestLifecycle;

import java.lang.reflect.Method;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 * @author zen
 * @version 2016/11/16
 */
public class RxAndroidTestLifecycle extends DefaultTestLifecycle{

    @Override
    public void beforeTest(Method method) {
        super.beforeTest(method);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new PuckSchedulerHook());

        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Override
    public void afterTest(Method method) {
        super.afterTest(method);

        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }
}
