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

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.* { *; }


-keep class com.ripbull.coresdk.data.** { *; }
-dontwarn com.ripbull.coresdk.data.**

# Keep SDK classes
#-keep class com.ripbull.ertc.cache.database.entity.** { *; }
-keep interface com.ripbull.ertc.cache.database.DataSource{ *; }
-keep class com.ripbull.ertc.cache.database.DataSourceImpl { *; }
-keep interface com.ripbull.ertc.cache.preference.PreferenceManager{ *; }