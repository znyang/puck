package com.zen.android.puck.runner;

import org.robolectric.annotation.Config;
import org.robolectric.internal.GradleManifestFactory;
import org.robolectric.internal.ManifestIdentifier;
import org.robolectric.res.FileFsFile;
import org.robolectric.util.Logger;
import org.robolectric.util.ReflectionHelpers;

import java.io.File;
import java.net.URL;

import static org.robolectric.annotation.Config.DEFAULT_MANIFEST;

/**
 * PuckManifestFactory
 *
 * @author znyang 2017/1/24 0024
 */

public class PuckManifestFactory extends GradleManifestFactory {

    @Override
    public ManifestIdentifier identify(Config config) {
        if (config.constants() == Void.class) {
            Logger.error("Field 'constants' not specified in @Config annotation");
            Logger.error("This is required when using Robolectric with Gradle!");
            throw new RuntimeException("No 'constants' field in @Config annotation!");
        }

        final String buildOutputDir = getBuildOutputDir(config);
        final String type = getType(config);
        final String flavor = getFlavor(config);
        final String abiSplit = getAbiSplit(config);
        final String packageName = config.packageName().isEmpty()
                ? config.constants().getPackage().getName()
                : config.packageName();

        final FileFsFile res;
        final FileFsFile assets;
        final FileFsFile manifest;

        if (FileFsFile.from(buildOutputDir, "data-binding-layout-out").exists()) {
            // Android gradle plugin 1.5.0+ puts the merged layouts in data-binding-layout-out.
            // https://github.com/robolectric/robolectric/issues/2143
            res = FileFsFile.from(buildOutputDir, "data-binding-layout-out", flavor, type);
        } else if (FileFsFile.from(buildOutputDir, "res", "merged").exists()) {
            // res/merged added in Android Gradle plugin 1.3-beta1
            res = FileFsFile.from(buildOutputDir, "res", "merged", flavor, type);
        } else if (FileFsFile.from(buildOutputDir, "res").exists()) {
            res = FileFsFile.from(buildOutputDir, "res", flavor, type);
        } else {
            res = FileFsFile.from(buildOutputDir, "bundles", flavor, type, "res");
        }

        if (FileFsFile.from(buildOutputDir, "assets").exists()) {
            assets = FileFsFile.from(buildOutputDir, "assets", flavor, type);
        } else {
            assets = FileFsFile.from(buildOutputDir, "bundles", flavor, type, "assets");
        }

        String manifestName = config.manifest();
        if (DEFAULT_MANIFEST.equals(manifestName)) {
            manifestName = DEFAULT_MANIFEST_NAME;
        }
        URL manifestUrl = getClass().getClassLoader().getResource(manifestName);

        if (manifestUrl != null && manifestUrl.getProtocol().equals("file")) {
            manifest = FileFsFile.from();
        } else if (FileFsFile.from(buildOutputDir, "manifests", "full").exists()) {
            manifest = FileFsFile.from(buildOutputDir, "manifests", "full", flavor, abiSplit, type, manifestName);
        } else if (FileFsFile.from(buildOutputDir, "manifests", "aapt").exists()) {
            // Android gradle plugin 2.2.0+ can put library manifest files inside of "aapt" instead of "full"
            manifest = FileFsFile.from(buildOutputDir, "manifests", "aapt", flavor, abiSplit, type, manifestName);
        } else {
            manifest = FileFsFile.from(buildOutputDir, "bundles", flavor, abiSplit, type, manifestName);
        }

        return new ManifestIdentifier(manifest, res, assets, packageName, null);
    }

    private static String getBuildOutputDir(Config config) {
        return config.buildDir() + File.separator + "intermediates";
    }

    private static String getType(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "BUILD_TYPE");
        } catch (Throwable e) {
            return null;
        }
    }

    private static String getFlavor(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "FLAVOR");
        } catch (Throwable e) {
            return null;
        }
    }

    private static String getAbiSplit(Config config) {
        try {
            return config.abiSplit();
        } catch (Throwable e) {
            return null;
        }
    }

}
