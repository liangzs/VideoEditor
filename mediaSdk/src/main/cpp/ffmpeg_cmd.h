//
// Created by DELL on 2019/8/12.
//在java进行管理线程分配，jni监控异常发生
//

#ifndef VIDEOEDITORAS_FFMPEG_CMD_H
#define VIDEOEDITORAS_FFMPEG_CMD_H

#endif //VIDEOEDITORAS_FFMPEG_CMD_H

#ifdef ANDROID

#include <android/log.h>

#ifndef LOG_TAG
#define  LOG_TAG    "FFMPEG"
#endif
#define  XLOGD(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  XLOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#else
#include <stdio.h>
#define XLOGE(format, ...)  fprintf(stdout, LOG_TAG ": " format "\n", ##__VA_ARGS__)
#define XLOGI(format, ...)  fprintf(stderr, LOG_TAG ": " format "\n", ##__VA_ARGS__)
#endif  //ANDROID

void callJavaMethod(char*);
void ffmErrorCallback();
void ffmpeg_progress(float progress);