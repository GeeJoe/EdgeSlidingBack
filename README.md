# EdgeSlidingBack
An Android library help app with edge sliding back like ios

[ ![Download](https://api.bintray.com/packages/geejoe/maven/edgeslidingback/images/download.svg) ](https://bintray.com/geejoe/maven/edgeslidingback/_latestVersion)

## 简介

这是一个仿ios边缘右滑返回的库，效果见动图：

### 普通Activity，内有一个TextView
![](https://github.com/GeeJoe/EdgeSlidingBack/raw/master/gif/2017-06-04_20_09_56.gif)

### 含有ListView和ViewPager的Activity
![](https://github.com/GeeJoe/EdgeSlidingBack/raw/master/gif/2017-06-04_20_28_44.gif)

## 依赖

本项目已同步到Jcenter,可直接引用依赖：
```Java
compile 'com.geejoe:edgeslidingback:1.0.7'
```

## 使用方法

所有需要使用滑动返回的Activity须继承自EdgeSlidingBackActivity
需要关闭右滑返回可以在Activity的super.onCreate()方法之前执行setEnableSlidingBack(false);

另需要设置主题@style/AppTheme.Slide以便滑动时使下层Activity可见
此外还提供了无ActionBar的主题@style/AppTheme.Slide.NoActionBar:

例，如下设置：不需要使用滑动返回的Activity（如MainActivity）,则单独设置主题

```xml
<application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme.Slide">
        <activity android:name=".MainActivity"
            android:theme="@style/AppTheme">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
```

#### 重写Activity的onSlidingBack()方法,处理滑动返回销毁Activity前的工作，例如需要setResult、释放资源等工作
