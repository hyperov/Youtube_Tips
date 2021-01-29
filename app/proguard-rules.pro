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
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-printusage usage.txt


#-keep class com.noqoush.adfalcon.android.sdk.** {*;}
#
#-keep class com.google.ads.mediation.adfalcon.** {*;}
#
#-keep public class com.google.android.gms.ads.** {
#
#public *;
#
#}
#
#-keep public class com.google.ads.** {
#
#public *;
#
#}

#startapp - start.io
-keep class com.startapp.** {
      *;
}

-keep class com.truenet.** {
      *;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,
LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**

-dontwarn org.jetbrains.annotations.**

#firebase-ads
-keep public class com.google.firebase.analytics.FirebaseAnalytics {
    public *;
}

-keep public class com.google.android.gms.measurement.AppMeasurement {
    public *;
}

#UNITY ADS
# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface
# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}
# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}
# Keep all classes in Unity Services package
-keep class com.unity3d.services.** {
   *;
}
-dontwarn com.google.ar.core.**
-dontwarn com.unity3d.services.**
-dontwarn com.ironsource.adapters.unityads.**



-keep class com.nabil.youtubetips.categories.model.CategoryItem { *; }
-keep class com.nabil.youtubetips.category.model.response.YoutubeCategoryResponse { *; }
-keep class com.nabil.youtubetips.category.model.response.VideoItem { *; }
-keep class com.nabil.youtubetips.category.model.response.Thumbnails { *; }
-keep class com.nabil.youtubetips.category.model.response.Standard { *; }
-keep class com.nabil.youtubetips.category.model.response.Snippet { *; }
-keep class com.nabil.youtubetips.category.model.response.PageInfo { *; }
-keep class com.nabil.youtubetips.category.model.response.Medium { *; }
-keep class com.nabil.youtubetips.category.model.response.Maxres { *; }
-keep class com.nabil.youtubetips.category.model.response.Localized { *; }
-keep class com.nabil.youtubetips.category.model.response.High { *; }
-keep class com.nabil.youtubetips.category.model.response.Default { *; }

-keep class com.pierfrancescosoffritti.androidyoutubeplayer.** { *; }

-keepnames class com.pierfrancescosoffritti.youtubeplayer.*

