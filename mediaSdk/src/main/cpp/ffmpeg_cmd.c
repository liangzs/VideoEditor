#include <jni.h>
#include <ffmpeg/ffmpeg.h>
#include <ffmpeg/cmdutils.h>
#include "ffmpeg_cmd.h"
#include "ffmpeg_thread.h"
#include <signal.h>
#include <setjmp.h>




static JavaVM *jvm = NULL;//java虚拟机
static jclass m_clazz = NULL;//当前类(面向java)
static JNIEnv *m_env = NULL;
static char timeStr[10] = "time=00:";
const char* excuteStr;
jboolean showProgress;


void ffmErrorCallback();

// 定义代码跳转锚点
sigjmp_buf JUMP_ANCHOR;
volatile sig_atomic_t error_cnt = 0;
#define RUNTIME_EXCEPTION "java/lang/Exception"

void
JNU_ThrowByName(JNIEnv *env, const char *name, const char *msg)
{
    jclass cls = (*env)->FindClass(env, name);
    /* if cls is NULL, an exception has already been thrown */
    if (cls != NULL) {
        XLOGE("JNU_ThrowByName..");
        (*env)->ThrowNew(env, cls, msg);
    }
    /* free the local ref */
    (*env)->DeleteLocalRef(env, cls);
}

void throw_exception(JNIEnv *env) {
//    XLOGE("throw_exception: '%s'", RUNTIME_EXCEPTION);
    //想尽办法，在java层抛出这个异常
    jthrowable new_exception = (*env)->FindClass(env, RUNTIME_EXCEPTION);
    if (new_exception == NULL) {
        XLOGE("Exception class not found: '%s'", RUNTIME_EXCEPTION);
        return;
    }
//    XLOGE("throw_exception-value: '%s'", excuteStr);
//    (*env)->ThrowNew(env, new_exception,excuteStr);


}



void exception_handler(int signal, siginfo_t *info, void *reserved) {
    error_cnt += 1;
    XLOGE("catch Exception ffmpeg.....");

//    siglongjmp(JUMP_ANCHOR, 1);
//    ffmpeg_cleanup(0);
//    ffmpeg_thread_exit(0);
//    JNIEnv *env;
//    if (jvm == NULL) {
//        XLOGE("callJavaNotifyNativeException start4");
//        XLOGE("JniInterfacexx", "g_jvm == NULL");
//        return;
//    }
//    XLOGE("callJavaNotifyNativeException start5");
//    int getEnvStat = (*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_6);
//    XLOGE("callJavaNotifyNativeException start6");
//    if (getEnvStat == JNI_EDETACHED) {
//        XLOGE("callJavaNotifyNativeException start7");
//        int attachtatus = (*jvm)->AttachCurrentThread(jvm, &env, NULL);
//        if (attachtatus != 0) {
//            XLOGE("callJavaNotifyNativeException start8");
//            XLOGE("JniInterfacexx", "GetEnv Failed to attach");
//            return;
//        }
//        XLOGE("callJavaNotifyNativeException start9");
//    } else if (getEnvStat == JNI_EVERSION) {
//        XLOGE("JniInterfacexx", "GetEnv JNI version not supported");
//        XLOGE("callJavaNotifyNativeException start10");
//        return;
//    }
//    jclass crashHandlerClass = (*env)->FindClass(env,
//                                                 "com/ijoysoft/mediasdk/module/ffmpeg/FFmpegCmd");
//    jmethodID notifyNativeCrash = (*env)->GetMethodID(env, crashHandlerClass, "handleJniException",
//                                                      "()V");
//    (*env)->CallVoidMethod(env, crashHandlerClass, notifyNativeCrash);
}


char *jstringToChar(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte *ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
    return rtn;
}




JNIEXPORT jint

JNICALL
Java_com_ijoysoft_mediasdk_module_ffmpeg_FFmpegCmd_handle
        (JNIEnv *env, jobject obj, jobjectArray commands,jstring commandStr) {

    showProgress = JNI_FALSE;
    (*env)->GetJavaVM(env, &jvm);
    m_clazz = (*env)->NewGlobalRef(env, obj);
    m_env = env;
    int argc = (*env)->GetArrayLength(env, commands);
    char **argv = (char **) malloc(argc * sizeof(char *));
    int i;
    int result;

    for (i = 0; i < argc; i++) {
        jstring jstr = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
        char *temp = (char *) (*env)->GetStringUTFChars(env, jstr, 0);
        argv[i] = malloc(1024);
        strcpy(argv[i], temp);
        (*env)->ReleaseStringUTFChars(env, jstr, temp);
    }
//    excuteStr= jstringToChar(env,commandStr);
    excuteStr = (*env)->GetStringUTFChars(env,commandStr,0);
    //执行ffmpeg命令
    result = run(argc, argv);
    //新建线程 执行ffmpeg 命令
    // ffmpeg_thread_run_cmd(argc, argv);
    //注册ffmpeg命令执行完毕时的回调
    //ffmpeg_thread_callback(ffmpeg_callback);
    //释放内存
    for (i = 0; i < argc; i++) {
        free(argv[i]);
    }
    free(argv);
    return result;
}


JNIEXPORT jint
JNICALL
Java_com_ijoysoft_mediasdk_module_ffmpeg_FFmpegCmd_handleByProgress(JNIEnv *env, jobject instance,
                                                                    jobjectArray commands,jstring commandStr) {
    // 注册需要捕获的异常信号
    /*
     1    HUP Hangup                        33     33 Signal 33
     2    INT Interrupt                     34     34 Signal 34
     3   QUIT Quit                          35     35 Signal 35
     4    ILL Illegal instruction           36     36 Signal 36
     5   TRAP Trap                          37     37 Signal 37
     6   ABRT Aborted                       38     38 Signal 38
     7    BUS Bus error                     39     39 Signal 39
     8    FPE Floating point exception      40     40 Signal 40
     9   KILL Killed                        41     41 Signal 41
    10   USR1 User signal 1                 42     42 Signal 42
    11   SEGV Segmentation fault            43     43 Signal 43
    12   USR2 User signal 2                 44     44 Signal 44
    13   PIPE Broken pipe                   45     45 Signal 45
    14   ALRM Alarm clock                   46     46 Signal 46
    15   TERM Terminated                    47     47 Signal 47
    16 STKFLT Stack fault                   48     48 Signal 48
    17   CHLD Child exited                  49     49 Signal 49
    18   CONT Continue                      50     50 Signal 50
    19   STOP Stopped (signal)              51     51 Signal 51
    20   TSTP Stopped                       52     52 Signal 52
    21   TTIN Stopped (tty input)           53     53 Signal 53
    22   TTOU Stopped (tty output)          54     54 Signal 54
    23    URG Urgent I/O condition          55     55 Signal 55
    24   XCPU CPU time limit exceeded       56     56 Signal 56
    25   XFSZ File size limit exceeded      57     57 Signal 57
    26 VTALRM Virtual timer expired         58     58 Signal 58
    27   PROF Profiling timer expired       59     59 Signal 59
    28  WINCH Window size changed           60     60 Signal 60
    29     IO I/O possible                  61     61 Signal 61
    30    PWR Power failure                 62     62 Signal 62
    31    SYS Bad system call               63     63 Signal 63
    32     32 Signal 32                     64     64 Signal 64
    */

    // 代码跳转锚点
//    if (sigsetjmp(JUMP_ANCHOR, 1) != 0) {
//        return -1;
//    }
//
//    // 注册要捕捉的系统信号量
//    struct sigaction sigact;
//    struct sigaction old_action;
//    sigaction(SIGABRT, NULL, &old_action);
//    if (old_action.sa_handler != SIG_IGN) {
//        sigset_t block_mask;
//        sigemptyset(&block_mask);
//        sigaddset(&block_mask, SIGABRT); // handler处理捕捉到的信号量时，需要阻塞的信号
//        sigaddset(&block_mask, SIGSEGV); // handler处理捕捉到的信号量时，需要阻塞的信号
//
//        sigemptyset(&sigact.sa_mask);
//        sigact.sa_flags = 0;
//        sigact.sa_mask = block_mask;
//        sigact.sa_handler = exception_handler;
//        sigaction(SIGABRT, &sigact, NULL); // 注册要捕捉的信号
//        sigaction(SIGSEGV, &sigact, NULL); // 注册要捕捉的信号
//    }
    showProgress = JNI_TRUE;
    // TODO
    (*env)->GetJavaVM(env, &jvm);
    m_clazz = (*env)->NewGlobalRef(env, instance);
    m_env = env;
    int argc = (*env)->GetArrayLength(env, commands);
    char **argv = (char **) malloc(argc * sizeof(char *));
    int i;
    int result;
//    const char *nativeString = (*env)->GetStringUTFChars(env, commandStr, 0);
//    excuteStr= (char *) nativeString;
    for (i = 0; i < argc; i++) {
        jstring jstr = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
        char *temp = (char *) (*env)->GetStringUTFChars(env, jstr, 0);
        argv[i] = malloc(1024);
        strcpy(argv[i], temp);
        (*env)->ReleaseStringUTFChars(env, jstr, temp);
    }
    excuteStr = (*env)->GetStringUTFChars(env,commandStr,0);
    result = run(argc, argv);
    //新建线程 执行ffmpeg 命令
    //释放内存
    for (i = 0; i < argc; i++) {
        free(argv[i]);
    }
    free(argv);
    return result;

}


JNIEXPORT jint

JNICALL
Java_com_ijoysoft_mediasdk_module_ffmpeg_FFmpegCmd_cancelJni(JNIEnv *env, jobject instance) {
    XLOGE("---------------Java_com_ijoysoft_mediasdk_module_ffmpeg_FFmpegCmd_cancel---------------");
    clear();
    ffmpeg_thread_cancel();
    return 0;
}

void ffmpeg_progress(float progress) {
    jclass class1 = (*m_env)->FindClass(m_env, "com/ijoysoft/mediasdk/module/ffmpeg/FFmpegCmd");
    if (class1 == NULL) {
        XLOGE("---------------clazz isNULL---------------");
        return;
    }
    //获取方法ID (I)V指的是方法签名 通过javap -s -public FFmpegCmd 命令生成
    jmethodID methodID = (*m_env)->GetStaticMethodID(m_env, class1, "progress", "(I)V");
    if (methodID == NULL) {
        XLOGE("---------------methodID isNULL---------------");
        return;
    }
    //调用该java方法
    (*m_env)->CallStaticVoidMethod(m_env, class1, methodID, (int) progress);
    (*jvm)->DetachCurrentThread(jvm);
}


/**
 * 回调执行Java方法
 */
void callJavaMethod(char *ret) {
    if (!showProgress) {
        return;
    }
    int ss = 0;
    char *q = strstr(ret, timeStr);
    if (q != NULL) {
        //LOGE("遇到time=");
        char str[14] = {0};
        strncpy(str, q, 13);
        int h = (str[5] - '0') * 10 + (str[6] - '0');
        int m = (str[8] - '0') * 10 + (str[9] - '0');
        int s = (str[11] - '0') * 10 + (str[12] - '0');
        ss = s + m * 60 + h * 60 * 60;
    } else {
        return;
    }
    //获取方法ID (I)V指的是方法签名 通过javap -s -public FFmpegCmd 命令生成

    jclass class1 = (*m_env)->FindClass(m_env, "com/ijoysoft/mediasdk/module/ffmpeg/FFmpegCmd");
    if (class1 == NULL) {
        XLOGE("---------------clazz isNULL---------------");
        return;
    }

    jmethodID methodID = (*m_env)->GetStaticMethodID(m_env, class1, "progress", "(I)V");
    if (methodID == NULL) {
        XLOGE("---------------methodID isNULL---------------");
        return;
    }
    //调用该java方法
    (*m_env)->CallStaticVoidMethod(m_env, class1, methodID, ss);
}


/**
 * 回调执行Java方法
 */
void ffmErrorCallback() {
//    jclass class1 = (*m_env)->FindClass(m_env, "com/ijoysoft/mediasdk/module/ffmpeg/FFmpegCmd");
//    if (class1 == NULL) {
//        XLOGE("---------------clazz isNULL---------------");
//        return;
//    }
//Ljava/lang/String;
//    jmethodID methodID = (*m_env)->GetStaticMethodID(m_env, class1, "error", "()V");
//    if (methodID == NULL) {
//        XLOGE("---------------methodID isNULL---------------");
//        return;
//    }
    //调用该java方法，不过有的手机会出现问题，所以测试可以打开，上线过滤掉
//    (*m_env)->CallStaticVoidMethod(m_env, class1, methodID,excuteStr);
//    throw_exception(m_env);
    JNU_ThrowByName(m_env,"com/ijoysoft/mediasdk/module/ffmpeg/JNIException",excuteStr);
    JNU_ThrowByName(m_env,"com/ijoysoft/mediasdk/module/ffmpeg/JNIException","repeat");
}


