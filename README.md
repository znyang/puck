# Puck

<img alt="Puck is an android library" src="https://www.cleveroad.com/public/comercial/label-android.svg" height="20">
[![Build Status](https://travis-ci.org/znyang/puck.svg?branch=master)](https://travis-ci.org/znyang/puck)
[![](https://jitpack.io/v/znyang/puck.svg)](https://jitpack.io/#znyang/puck)
[![codecov](https://codecov.io/gh/znyang/puck/branch/master/graph/badge.svg)](https://codecov.io/gh/znyang/puck)

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
	testCompile 'com.github.znyang:puck:0.1-beta1'
}
```

## Dependencies

* Robolectric
* Mockito
* rxjava
* rxandroid

## 特性

### Robolectric

* 解决AndroidManifest.xml文件找不到的问题

### RxJava & RxAndroid

* 替换调度器源，解决测试过程中异步无回调问题（异步转同步）
