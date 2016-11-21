# Puck

<img alt="Puck is an android library" src="https://www.cleveroad.com/public/comercial/label-android.svg" height="20">
[![Build Status](https://travis-ci.org/znyang/puck.svg?branch=master)](https://travis-ci.org/znyang/puck)
[![](https://jitpack.io/v/znyang/puck.svg)](https://jitpack.io/#znyang/puck)
[![codecov](https://codecov.io/gh/znyang/puck/branch/master/graph/badge.svg)](https://codecov.io/gh/znyang/puck)

![logo](/img/logo.jpg)

## Gradle配置

[JitPack](https://jitpack.io/#znyang/puck)

```gradle
allprojects {
	repositories {
		//...
		maven { url "https://jitpack.io" }
	}
}
```

```gradle
dependencies {
	testCompile 'com.github.znyang:puck:0.1-beta2'
}
```

## Dependencies

* Robolectric
* Mockito
* rxjava
* rxandroid

## 特性

### Robolectric

* [解决AndroidManifest.xml文件找不到的问题](/doc/android-manifest-not-found.md)

### RxJava & RxAndroid

* 替换调度器源，解决测试过程中异步无回调问题（异步转同步）

## 用法

使用 `PuckTestRunner` 代替 `RobolectricTestRunner`

```java
@RunWith(PuckTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {
	// test methods
}
```
