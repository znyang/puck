package com.zen.android.puck.runner;

import com.zen.android.puck.rx.RxTestLifecycle;
import com.zen.android.puck.util.ReflectUtils;

import org.junit.runners.model.InitializationError;
import org.robolectric.TestLifecycle;

/**
 * @author zen
 * @version 2016/11/11
 */

public class PuckTestRunner extends PuckRobolectricRunner {

    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your
     * AndroidManifest.xml file and res directory by default.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public PuckTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        if (ReflectUtils.hasUseRxJava()) {
            return RxTestLifecycle.class;
        }
        return super.getTestLifecycleClass();
    }
}
