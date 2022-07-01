package com.ijoysoft.mediasdk.view;

import android.opengl.EGL14;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.mediacodec.realtime.BaseMediaEncoderRunable;
import com.ijoysoft.mediasdk.module.mediacodec.realtime.MediaVideoEncoderRunable;
import com.ijoysoft.mediasdk.module.mediacodec.realtime.RealTimeMediaMuxerManager;

import java.io.IOException;

public class RecordRealTimeBak {

    /********************************************一套视频保存-test**************************************************/
//    // muxer for audio/video recording
//    private RealTimeMediaMuxerManager mMediaMuxerManager;
//    private MediaVideoEncoderRunable mMediaVideoEncoderRunable;
//    private BaseMediaEncoderRunable.MediaEncoderListener mMediaEncoderListener = new BaseMediaEncoderRunable.MediaEncoderListener() {
//        @Override
//        public void onPrepared(BaseMediaEncoderRunable encoder) {
//            if (encoder instanceof MediaVideoEncoderRunable) {
//                setmMediaVideoEncoderRunable((MediaVideoEncoderRunable) encoder);
//
//            }
//        }
//
//        @Override
//        public void onStopped(BaseMediaEncoderRunable encoder) {
//            setmMediaVideoEncoderRunable(null);
//        }
//    };
//
//    /**
//     * 开始录制视频,实时录制方案
//     */
//    public void startRecordByRealTime() {
//        try {
//            mMediaMuxerManager = new RealTimeMediaMuxerManager();
//            //开始视频录制
//            new MediaVideoEncoderRunable(mMediaMuxerManager, mMediaEncoderListener, ConstantMediaSize.HD_height, ConstantMediaSize.HD_width);
//            // 开启音频录制
////            new MediaAudioEncoderRunable(mMediaMuxerManager, mMediaEncoderListener);
//            // 视频，音频 录制初始化
//            mMediaMuxerManager.prepare();
//            // 视频，音频 开始录制
//            mMediaMuxerManager.startRecording();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * request stop recording
//     */
//    private void stopRecordingTest() {
//        if (mMediaMuxerManager != null) {
//            mMediaMuxerManager.stopRecording();
//            mMediaMuxerManager = null;
//        }
//        if (mediaRecordCallback != null) {
//            mediaRecordCallback.finish();
//        }
//    }


    /**
     //     * 设置是否录制视频
     //     *
     //     * @param mMediaVideoEncoderRunable
     //     */
//    public void setmMediaVideoEncoderRunable(final MediaVideoEncoderRunable mMediaVideoEncoderRunable) {
//        this.mMediaVideoEncoderRunable = mMediaVideoEncoderRunable;
//
//        queueEvent(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (MediaPreviewView.this) {
//                    // 这里是获取了一个GLThread的EGL14.eglGetCurrentContext()
//                    if (mMediaVideoEncoderRunable != null) {
//                        mMediaVideoEncoderRunable.setEglContext(EGL14.eglGetCurrentContext(), mDrawer.getShowTexureId());
//                    }
//                }
//            }
//        });
//    }


    //在ondrawframe中插入
    //这里是视频的写入，每渲染一帧，都把该帧的纹理写入，切换eglcontext，手机屏幕暂停显示，用另外的eglcontext上下文
    //接管当前环境，然后重走渲染的过程，每一帧的渲染都保存下来，视频还是用surfaceteture.updateImage,图片还是用计时器去触发
//    synchronized (this) {
//        if (mMediaVideoEncoderRunable != null) {
//            // notify to capturing thread that the camera frame is available.
//            mMediaVideoEncoderRunable.frameAvailableSoon(null);
//        }
//    }

}
