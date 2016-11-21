# AndroidManifest.xml文件找不到的问题

错误如下：

```
java.io.FileNotFoundException: build/intermediates/manifests/full/debug/AndroidManifest.xml (No such file or directory)
	at java.io.FileInputStream.open0(Native Method)
	at java.io.FileInputStream.open(FileInputStream.java:195)
	at java.io.FileInputStream.<init>(FileInputStream.java:138)
	at org.robolectric.res.FileFsFile.getInputStream(FileFsFile.java:78)
	at org.robolectric.manifest.AndroidManifest.parseAndroidManifest(AndroidManifest.java:130)
	at org.robolectric.manifest.AndroidManifest.getPackageName(AndroidManifest.java:458)
	at org.robolectric.manifest.AndroidManifest.getResourcePath(AndroidManifest.java:503)
	at org.robolectric.manifest.AndroidManifest.getIncludedResourcePaths(AndroidManifest.java:508)
	at org.robolectric.RobolectricTestRunner.getAppResourceLoader(RobolectricTestRunner.java:422)
	at org.robolectric.internal.ParallelUniverse.setUpApplicationState(ParallelUniverse.java:80)
	at org.robolectric.RobolectricTestRunner$2.evaluate(RobolectricTestRunner.java:237)
	at org.robolectric.RobolectricTestRunner.runChild(RobolectricTestRunner.java:174)
```

这个问题发生在使用`com.android.tools.build.gradle:2.2.+`的情况下，而在2.2之前测试正常。

测试代码：

```java
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private MainActivity mMainActivity;

    @Before
    public void setUp() throws Exception {
        mMainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testStart() throws Exception {
        System.out.println(RuntimeEnvironment.application.getPackageName());
    }
}
```

运行测试失败后，可以看到`build/intermediates/manifests/`目录下只生成了aapt，没有full目录。这是怎么回事呢？

## `com.android.tools.build:gradle`的版本变化

manifest应当是由某个gradle task产生的，又是android相关文件，因此锁定`com.android.tools.build:gradle`是生成者。果然，2.2以前的版本能正常测试，也并没有生成aapt目录。

通过查找gradle plugin源码，发现gradle-core这个库的`VariantScopeImpl.java`发生了变化，以2.2.0和1.5.0的版本对比：

1.5.0
```java
@NonNull
@Override
public File getAaptFriendlyManifestOutputFile() {
        // DIR_BUNDLES = "bundles"
        return FileUtils.join(globalScope.getIntermediatesDir(), DIR_BUNDLES,
                getVariantConfiguration().getDirName(), "aapt", "AndroidManifest.xml");

}
```
2.2.0
```java
@NonNull
@Override
public File getAaptFriendlyManifestOutputFile() {
    return FileUtils.join(
            globalScope.getIntermediatesDir(),
            "manifests",
            "aapt",
            getVariantConfiguration().getDirName(),
            "AndroidManifest.xml");
}
```
结果就是，生成AndroidManifest.xml的目录，从bundles/aapt移到了manifests/aapt下。

## Robolectric中的处理

那么，robolectric中是怎么获取AndroidManifest.xml文件的？

请看RobolectricTestRunner.java中的方法：

```java
// 获取Manifest, Config对象由getConfig()方法获取
protected AndroidManifest getAppManifest(Config config) {
  return ManifestFactory.newManifestFactory(config).create();
}

public Config getConfig(Method method) {
  // 默认实现
  Config config = new Config.Implementation(new int[0], Config.DEFAULT_MANIFEST, Config.DEFAULT_QUALIFIERS, Config.DEFAULT_PACKAGE_NAME,
          Config.DEFAULT_ABI_SPLIT, Config.DEFAULT_RES_FOLDER, Config.DEFAULT_ASSET_FOLDER, Config.DEFAULT_BUILD_FOLDER, new Class[0],
          new String[0], Application.class, new String[0], Void.class);

  // 全局配置，来自system.Property
  Config globalConfig = Config.Implementation.fromProperties(getConfigProperties());
  if (globalConfig != null) {
    config = new Config.Implementation(config, globalConfig);
  }

  // 测试方法method所在类的Config注解
  Config methodClassConfig = method.getDeclaringClass().getAnnotation(Config.class);
  if (methodClassConfig != null) {
    config = new Config.Implementation(config, methodClassConfig);
  }

  // 测试类及其父类的Config注解
  ArrayList<Class> testClassHierarchy = new ArrayList<>();
  Class testClass = getTestClass().getJavaClass();

  while (testClass != null) {
    testClassHierarchy.add(0, testClass);
    testClass = testClass.getSuperclass();
  }

  for (Class clazz : testClassHierarchy) {
    Config classConfig = (Config) clazz.getAnnotation(Config.class);
    if (classConfig != null) {
      config = new Config.Implementation(config, classConfig);
    }
  }

  // 当前测试方法的Config注解
  Config methodConfig = method.getAnnotation(Config.class);
  if (methodConfig != null) {
    config = new Config.Implementation(config, methodConfig);
  }

  return config;
}
```

可以看到，这里的Config是有一个优先顺序的，高优先的同名配置会覆盖低优先级配置。

注意这里getAppManifest()中创建AndroidManifest的方法：

```java
ManifestFactory.newManifestFactory(config).create();
```

在Gradle项目中，由GradleManifestFactory工厂类来完成创建，在`GradleManifestFactory.java`处理manifest的代码如下：

**GradleManifestFactory.java**

```java
if (FileFsFile.from(buildOutputDir, "manifests").exists()) {
   manifest = FileFsFile.from(buildOutputDir, "manifests", "full", flavor, abiSplit, type, DEFAULT_MANIFEST_NAME);
} else {
   manifest = FileFsFile.from(buildOutputDir, "bundles", flavor, abiSplit, type, DEFAULT_MANIFEST_NAME);
}
```
根据这段逻辑就可以知道为啥使用2.2.+ 就会报错。(判断manifests目录存在就认为有manifests/full）

## 解决方案

robolectric官方issues上有很多解决该问题的脚本
比如插入任务的方式来复制文件：
[robolectric-issues1925](https://github.com/robolectric/robolectric/issues/1925)
通过自定义Runner：
[robolectric-issues2581](https://github.com/robolectric/robolectric/issues/2581)
我比较赞同使用Runner的方式解决，要考虑到不同变种版本的因素。

**在robolectric v3.2版本中已经修复这个问题：**[commit](https://github.com/robolectric/robolectric/commit/24973cd7fa61401b3af1069f01d516e40080221c)

目前还只有快照版本。相关代码：

```java
if (FileFsFile.from(buildOutputDir, "manifests", "full").exists()) {
    manifest = FileFsFile.from(buildOutputDir, "manifests", "full", flavor, abiSplit, type, DEFAULT_MANIFEST_NAME);
} else if (FileFsFile.from(buildOutputDir, "manifests", "aapt").exists()) {
    // Android gradle plugin 2.2.0+ can put library manifest files inside of "aapt" instead of "full"
    manifest = FileFsFile.from(buildOutputDir, "manifests", "aapt", flavor, abiSplit, type, DEFAULT_MANIFEST_NAME);
} else {
    manifest = FileFsFile.from(buildOutputDir, "bundles", flavor, abiSplit, type, DEFAULT_MANIFEST_NAME);
}
```

因此，如果出现这个问题时，3.2已经发布，可以直接使用高版本，否则考虑自定义Runner进行处理。
