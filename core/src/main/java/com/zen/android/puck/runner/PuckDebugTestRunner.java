package com.zen.android.puck.runner;

import org.junit.runners.model.InitializationError;
import org.robolectric.annotation.Config;
import org.robolectric.util.Logger;

/**
 * PuckDebugTestRunner
 *
 * @author znyang 2017/1/23 0023
 */

public class PuckDebugTestRunner extends PuckTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your
     * AndroidManifest.xml file and res directory by default.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public PuckDebugTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

        System.setProperty("robolectric.logging.enabled", "true");
//        System.setProperty("robolectric.dependency.repo.url", "http://nexus.sdp.nd/nexus/content/groups/android-public/");
    }

    @Override
    protected Config buildGlobalConfig() {
        Config config = super.buildGlobalConfig();
        Logger.debug("GlobalConfig Manifest: %s", config == null ? "null" : config.manifest());
        return config;
    }
}
