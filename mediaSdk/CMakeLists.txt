# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
set(CURRENT_DIR ${CMAKE_SOURCE_DIR})
add_library( # Sets the name of the library.
        ijoysoft-ffmpeg
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/ffmpeg/cmdutils.c
        src/main/cpp/ffmpeg/ffmpeg.c
        src/main/cpp/ffmpeg/ffmpeg_filter.c
        src/main/cpp/ffmpeg/ffmpeg_opt.c
        src/main/cpp/ffmpeg_cmd.c
        src/main/cpp/ffmpeg_thread.c
        src/main/cpp/ffmpeg/video_player.c
        src/main/cpp/ffmpeg/video_filter.c
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
get_filename_component(PARENT_DIR ${CMAKE_SOURCE_DIR} PATH)
set(LIBRARY_DIR ${PARENT_DIR}/mediaSdk)
#或者用绝对路径
add_library(ffmpeg
        SHARED
        IMPORTED)
set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libffmpeg.so)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
set(CMAKE_VERBOSE_MAKEFILE on)
# 打印当前位置信息
message("Current souce file positon1: " ${CMAKE_CURRENT_SOURCE_DIR})
message("Current souce file positon2: " ../../../../libs/${ANDROID_ABI}/include)
message("Current souce file positon3: " ${LIBRARY_DIR}/libs/${ANDROID_ABI}/include)
include_directories(src/main/cpp)
#动态获取路径
#include_directories(../../../../libs/${ANDROID_ABI}/include)
#include_directories(${LIBRARY_DIR}/libs/${ANDROID_ABI}/include)
include_directories(src/main/cpp/include)
find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        ijoysoft-ffmpeg
        ffmpeg
        -landroid #native_window
        -ljnigraphics #bitmap
        -lOpenSLES #openSLES
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})