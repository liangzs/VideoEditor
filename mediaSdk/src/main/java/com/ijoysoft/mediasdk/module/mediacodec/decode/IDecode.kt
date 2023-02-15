package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.MediaFormat
import android.view.Surface
import kotlinx.coroutines.Job

/**
 * 解码动作
 */
interface IDecode {
    fun pause();
    fun resume();
    fun stop();
    fun isDecoding(): Boolean
    fun isSeeking(): Boolean
    fun isStop(): Boolean;

    /**
     * 退出销毁资源
     */
    fun release()

    /**
     * 播放结束重置
     */
    fun reset()

    /**
     * 获取格式参数
     */
    fun getMediaFormat(): MediaFormat? {
        return null;
    }

    /**
     * 获取音视频轨道
     */
    fun getTrack(): Int {
        return 0;
    }

    /**
     * 设置状态变化监听
     */
    fun setStateListener(listenr: IDecodeStateListenr) {}

    /**
     * 传入surface初始化解码资源
     */
    fun initSurface(surface: Surface) {}

    //fun seek(curPos: Long){}
    //
    ///**
    // * 带任务task的seek
    // */
    //fun seek(curPos: Long, taskList: ArrayDeque<String>?,job: Job): Boolean

    fun getCurrentPosition(): Long {
        return 0;
    }

    fun updateNextFrame() {}

    fun setCurrentMurPts(pts: Long?) {}
    fun interruptSeeking() {

    }


}