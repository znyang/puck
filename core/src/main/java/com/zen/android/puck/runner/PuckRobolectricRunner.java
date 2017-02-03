package com.zen.android.puck.runner;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ManifestFactory;
import org.robolectric.internal.MavenManifestFactory;
import org.robolectric.manifest.AndroidManifest;

/**
 * fixed manifest not found error. <p> Created by zenyang on 2016/11/9.
 */
public class PuckRobolectricRunner extends RobolectricTestRunner {

    private AndroidManifest mAndroidManifest;

    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your
     * AndroidManifest.xml file and res directory by default. Use the {@link Config} annotation to
     * configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public PuckRobolectricRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected ManifestFactory getManifestFactory(Config config) {
        Class<?> buildConstants = config.constants();
        //noinspection ConstantConditions
        if (buildConstants != null && buildConstants != Void.class) {
            return new PuckManifestFactory();
        } else {
            return new MavenManifestFactory();
        }
    }
}
