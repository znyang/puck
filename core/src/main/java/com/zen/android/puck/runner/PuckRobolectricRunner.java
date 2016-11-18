package com.zen.android.puck.runner;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.util.Logger;
import org.robolectric.util.ReflectionHelpers;

/**
 * fixed manifest not found error.
 * <p>
 * Created by zenyang on 2016/11/9.
 */
public class PuckRobolectricRunner extends RobolectricTestRunner {

    private AndroidManifest mAndroidManifest;

    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public PuckRobolectricRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    private static final String BUILD_OUTPUT = "build/intermediates";

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        if (mAndroidManifest != null) {
            return mAndroidManifest;
        }

        if (config.constants() == Void.class) {
            Logger.error("Field 'constants' not specified in @Config annotation");
            Logger.error("This is required when using RobolectricGradleTestRunner!");
            throw new RuntimeException("No 'constants' field in @Config annotation!");
        }

        final String type = getType(config);
        final String flavor = getFlavor(config);
        final String packageName = getPackageName(config);

        AndroidManifest mft = super.getAppManifest(config);

        final FileFsFile res = FileFsFile.from(mft.getResDirectory().getPath());
        final FileFsFile assets = getAssets(type, flavor);
        final FileFsFile manifest = getManifestFile(type, flavor);

        Logger.debug("Robolectric manifest path: " + manifest.getPath());
        Logger.debug("Robolectric package name: " + packageName);

        mAndroidManifest = new AndroidManifest(manifest, res, assets, packageName);
        return mAndroidManifest;
    }

    private FileFsFile getAssets(String type, String flavor) {
        final FileFsFile assets;
        if (FileFsFile.from(BUILD_OUTPUT, "assets").exists()) {
            assets = FileFsFile.from(BUILD_OUTPUT, "assets", flavor, type);
        } else {
            assets = FileFsFile.from(BUILD_OUTPUT, "bundles", flavor, type, "assets");
        }
        return assets;
    }

    private FileFsFile getManifestFile(String type, String flavor) {
        String[] dirs = {"full", "aapt"};
        FileFsFile manifest = null;

        if (FileFsFile.from(BUILD_OUTPUT, "manifests").exists()) {
            for (String dir : dirs) {
                FileFsFile f = FileFsFile.from(BUILD_OUTPUT, "manifests", dir, flavor, type, "AndroidManifest.xml");
                if (f.exists()) {
                    manifest = f;
                    break;
                }
            }
        }
        if (manifest == null || !manifest.exists()) {
            manifest = FileFsFile.from(BUILD_OUTPUT, "bundles", flavor, type, "AndroidManifest.xml");
        }
        return manifest;
    }

    private String getType(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "BUILD_TYPE");
        } catch (Throwable e) {
            return null;
        }
    }

    private String getFlavor(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "FLAVOR");
        } catch (Throwable e) {
            return null;
        }
    }

    private String getPackageName(Config config) {
        try {
            final String packageName = config.packageName();
            if (!packageName.isEmpty()) {
                return packageName;
            } else {
                return ReflectionHelpers.getStaticField(config.constants(), "APPLICATION_ID");
            }
        } catch (Throwable e) {
            return null;
        }
    }
}
