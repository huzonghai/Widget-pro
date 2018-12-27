# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/mackson/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#---------------------------------默认保留区---------------------------------
#继承activity,application,service,broadcastReceiver,contentprovider....不进行混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class android.support.** {*;}


# 不做预校验
-dontpreverify

# 忽略警告
-ignorewarning

#关闭压缩
#-dontshrink


# -keep class com.ck.newssdk.beans.**{*;}

-keep class com.ck.newssdk.**{*;}

#amplitude
-keepnames class com.amplitude.api.AmplitudeClient {
    *;
}

-keepnames class com.amplitude.api.DatabaseHelper {
    *;
}

-keepclassmembers class com.amplitude.api.DeviceInfo$CachedInfo {
    private String getAndCacheGoogleAdvertisingId();
}

-keep interface com.virgo.ads.track.bussness.JSONCon