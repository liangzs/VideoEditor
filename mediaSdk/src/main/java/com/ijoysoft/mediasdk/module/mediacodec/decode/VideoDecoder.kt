package com.ijoysoft.mediasdk.module.mediacodec.decode

import android.media.MediaCodec
import android.media.MediaFormat
import android.view.Surface
import com.ijoysoft.mediasdk.common.utils.LogUtils
import kotlinx.coroutines.Job
import java.nio.ByteBuffer

/**
 * 视频mediacodec解码，用于视频显示播放和渲染合成
 * 编辑显示的时候，用的是glsurfaceview，这个view自带了egl环境
 * 在视频合成的时候，需要自己构建egl环境的
 */
class VideoDecoder(control: Triple<String, Float, Boolean>, surface: Surface, listenr: IDecodeStateListenr?, extractor: IExtractor?)
    : BaseDecoder(control, listenr, null) {
    private var mSurface: Surface? = surface;
    override fun initRender(): Boolean {
        return true;
    }


    /**
     * 检查surfaview等环境
     */
    override fun check(): Boolean {
        if (mSurface == null) {
            return false;
        }
        return true;
    }

    override fun initExtractor(path: String): IExtractor {
        return VideoExtractor(path);
    }

    override fun render(outputBuffers: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
    }

    /**
     * 通过surface进行mediacodec的配置
     */
    override fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean {
        if (mSurface != null) {
            codec.configure(format, mSurface, null, 0);
            //notifyDecode()
            LogUtils.v(javaClass.simpleName, "configCodec...");
            return true;
        }
        return false;//这样父类就得wait了，因为surfaview需要初始化
    }

    override fun doneDecode() {
        super.doneDecode()
    }

    override fun initSpecParams(format: MediaFormat) {
    }


    override fun initSurface(surface: Surface) {
    }


    fun seekVideoByLock(curPos: Long, taskList: ArrayDeque<String>?, job: Job?): Boolean {
        //synchronized(seekLock) {
        return seek(curPos, taskList, job)
        //}
    }

    /**
     * extractor进行seek操作
     * 然后mediacode也取最近的帧结果
     *
     * seek不成功时，不进行渲染request操作，防止出现黑屏状态
     * 现象：有些视频的seek是很耗费时间的，所以在这个耗费的时间中，
     * 如果做了手指抬起或者片段切换动作时，要做一个强制seek，即移除当前seek，做一个强制的seek
     */
    fun seek(curPos: Long, taskList: ArrayDeque<String>?, job: Job?): Boolean {
        if (!hadInitCodec) {
            taskList?.clear()
            return true
        }
        return seekImpl(curPos, taskList, job)
    }

    /**
     *seek具体操作
     */
    fun seekImpl(curPos: Long, taskList: ArrayDeque<String>?, job: Job?): Boolean {
        if (state == DecodeState.STOP || extractor == null) {
            taskList?.clear()
            return true
        }
        state = DecodeState.SEEKING;
        val position: Long = curPos * 1000;
        //最多做max次轮询
        extractor?.seek(position)
        var sampleTime = extractor!!.getCurrentTimestamp();
        var count = 0;
        //初心播放结束结果
        bufferInfo.flags = 0
        while (count < SEEK_MAX && job?.isActive == true) {
            extractor?.advance()
            count++
            val tempTime = extractor!!.getCurrentTimestamp();
            if (tempTime != -1L) {
                sampleTime = position.getNearValue(sampleTime, tempTime)
                //seek定位成功 100ms内
                if (Math.abs(position - sampleTime) < 100000) {
                    break
                }
            } else {
                count = SEEK_MAX;
            }
        }
        LogUtils.v(javaClass.simpleName, "seek1111111111111111,count:$count");
        //如果轮询SEEK_MAX后，advance后的值还是目标值差别太大了的话只能在此基础上继续做轮询操作同时做预览更新操作：updateNextFrame
        //定位到指定位置的medicodec帧数据，然后渲染出来,最后用sampleTime进行对比
        var countMax = 0;
        if (count == SEEK_MAX) {
            extractor?.seek(sampleTime)
            while (Math.abs(position - sampleTime) > 200000 && sampleTime != 0L && state != DecodeState.STOP && job?.isActive == true) {//200ms
                sampleTime = bufferInfo.presentationTimeUs
                //预览当前帧
                codec?.let {
                    LogUtils.v(javaClass.simpleName, "count == SEEK_MAX,countMax:" + countMax);
                    if (previewCurrentFrame()) {
                        LogUtils.v(javaClass.simpleName, "count ==  SEEK_MAX seek11111111---222222222222,count:" + count);
                        taskList?.clear()
                        return false
                    }
                } ?: break
                //extractor时间轴已经对的上号了，但是视频图片数据依然对不上好，所以只能是单独取出来,不走advance过程
                if (Math.abs(position - (extractor?.getCurrentTimestamp() ?: 0)) < 50000) {
                    break
                }
                countMax++
                //防止意外
                if (countMax > SEEK_MAX_AGAIN) {
                    break
                }
            }
        } else {//不然就重走一次seek,然后根据sampleTime走正常的advance重走一次过程
            count = 0;
            extractor?.seek(sampleTime)
            while (bufferInfo.presentationTimeUs != sampleTime && state != DecodeState.STOP && job?.isActive == true) {
                //预览当前帧
                codec?.let {
                    LogUtils.v(javaClass.simpleName, "count < SEEK_MAX,count:" + count);
                    if (previewCurrentFrame()) {
                        LogUtils.v(javaClass.simpleName, "count < SEEK_MAX seek11111111222222222222,count:" + count);
                        taskList?.clear()
                        return false
                    }
                } ?: break
                //LogUtils.v(javaClass.simpleName, "fix--seek-sampleTime:" + sampleTime + "extractor!!.getCurrentTimestamp():" + extractor?.getCurrentTimestamp() +
                //        ",bufferInfo.presentationTimeUs:" + bufferInfo.presentationTimeUs);
                count++
                //防止意外
                if (count > SEEK_MAX) {
                    break
                }
            }
        }

        taskList?.clear()
        synPtstime = System.currentTimeMillis() - getCurrentDecodeTime()
        //LogUtils.v(javaClass.simpleName, "seek----end-----,cucurPos:$curPos getCurrentDecodeTime:" + getCurrentDecodeTime());
        return true;
    }

}