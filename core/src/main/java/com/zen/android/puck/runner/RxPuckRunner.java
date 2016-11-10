package com.zen.android.puck.runner;

import com.zen.android.puck.rule.PuckRxJavaRule;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 * @author zen
 * @version 2016/11/11
 */

public class RxPuckRunner extends PuckRobolectricRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public RxPuckRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

//        RxJavaHooks.reset();
//        RxJavaHooks.setOnIOScheduler(t -> Schedulers.immediate());
//        RxJavaHooks.setOnNewThreadScheduler(t -> Schedulers.immediate());
//        RxJavaHooks.setOnComputationScheduler(t -> Schedulers.immediate());

//        RxJavaPlugins.getInstance().reset();
//        RxJavaPlugins.getInstance().registerSchedulersHook(new PuckSchedulerHook());

//        rx.android.plugins.RxAndroidPlugins.getInstance().reset();
//        rx.android.plugins.RxAndroidPlugins.getInstance().registerSchedulersHook(new rx.android.plugins.RxAndroidSchedulersHook() {
//            @Override
//            public Scheduler getMainThreadScheduler() {
//                return Schedulers.immediate();
//            }
//        });
    }

}
