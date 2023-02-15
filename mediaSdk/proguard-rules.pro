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
-keep class com.ijoysoft.mediasdk.module.ffmpeg.FFmpegCmd{
static void progress(int);
}
-keep class com.ijoysoft.mediasdk.module.ffmpeg.** { *; }
-keep public class com.ijoysoft.mediasdk.module.mediacodec.MediaClipper
# 主题构造器
-keepclassmembers class com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common*.*Action* {
 public <init>(int, int, int);
}
-keepclassmembers class com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday*.*Action* {
 public <init>(int, int, int);
}
-keepclassmembers class com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear*.*Action* {
 public <init>(int, int, int);
}
#这三行混淆不生效，在类上面加了@Keep
-keepclassmembers class com.ijoysoft.mediasdk.module.opengl.theme.action.EmptyBlurAction {
 public <init>(int, int, int);
}
# PAG粒子构造器
-keepclassmembers class com.ijoysoft.mediasdk.module.opengl.particle.PAG* {
 public <init>(boolean, java.lang.String);
}