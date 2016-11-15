package com.zen.android.puck.runner;

import com.zen.android.puck.hook.PuckObservableExecutionHook;
import com.zen.android.puck.rx.RxAndroidTestLifecycle;

import org.junit.runners.model.InitializationError;
import org.robolectric.TestLifecycle;

import rx.plugins.RxJavaPlugins;

/**
 * @author zen
 * @version 2016/11/11
 */

public class RxPuckRunner extends PuckRobolectricRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your
     * AndroidManifest.xml file and res directory by default.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public RxPuckRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

        RxJavaPlugins.getInstance().registerObservableExecutionHook(new PuckObservableExecutionHook());
    }

    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return RxAndroidTestLifecycle.class;
    }
}
