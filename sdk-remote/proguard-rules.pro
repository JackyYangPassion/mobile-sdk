# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

# android
-dontwarn com.android.**


# network related
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontnote org.apache.http.**

# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature


# retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontnote retrofit2.**

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontnote okhttp3.**
-dontnote okio.**

-keepattributes InnerClasses
-dontoptimize

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


# rx java config
-dontwarn rx.internal.util.unsafe.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
-dontnote io.reactivex.**


# Gson specific classes
-keepattributes Signature, *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# jsoup
-keep class org.jsoup** {
    *;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# okhttp

-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**


-dontwarn androidx.**

-keep class androidx.** { *; }
-keep interface androidx.* { *; }


# keeping the sdk classes
-keep class com.ripbull.ertc.remote.model.** { *; }
-keep class com.ripbull.ertc.remote.NetworkConfig.** { *; }
-keepclassmembers class com.ripbull.ertc.remote.NetworkConfig{
    public *;
}
-keep class com.ripbull.ertc.remote.NetworkConfig$* { *; }
-keepattributes Builder

-keep class com.ripbull.ertc.remote.Constants { *; }
-keep class com.ripbull.ertc.remote.util.** { *; }
-keep class com.ripbull.ertc.remote.NetworkManagerImpl { *; }
-keep interface com.ripbull.ertc.remote.NetworkManager { *; }
-keep interface com.ripbull.ertc.remote.ApiHandler { *; }
-keep interface com.ripbull.ertc.remote.TokenChangeListener { *; }
