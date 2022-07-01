package com.ijoysoft.mediasdk.module.mediacodec;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.mediacodec.realtime.BaseMediaEncoderRunable;
import com.ijoysoft.mediasdk.module.mediacodec.realtime.MediaVideoEncoderRunable;
import com.ijoysoft.mediasdk.module.mediacodec.realtime.RealTimeMediaMuxerManager;
import com.ijoysoft.mediasdk.view.MediaPreviewView;

import java.io.IOException;

public class VideoMurgeCenter {

    private RealTimeMediaMuxerManager mMediaMuxerManager;
    private MediaVideoEncoderRunable mMediaVideoEncoderRunable;
    private BaseMediaEncoderRunable.MediaEncoderListener mMediaEncoderListener = new BaseMediaEncoderRunable.MediaEncoderListener() {
        @Override
        public void onPrepared(BaseMediaEncoderRunable encoder) {
            if (encoder instanceof MediaVideoEncoderRunable) {
                setmMediaVideoEncoderRunable((MediaVideoEncoderRunable) encoder);

            }
        }

        @Override
        public void onStopped(BaseMediaEncoderRunable encoder) {
            setmMediaVideoEncoderRunable(null);
        }
    };

    /**
     * 开始录制视频
     */
    public void startRecord() {
        // if you record audio only, ".m4a" is also OK.
        try {
            mMediaMuxerManager = new RealTimeMediaMuxerManager();
            //开始视频录制
            new MediaVideoEncoderRunable(mMediaMuxerManager, mMediaEncoderListener, 1280, 720);
            // 开启音频录制
//            new MediaAudioEncoderRunable(mMediaMuxerManager, mMediaEncoderListener);
            // 视频，音频 录制初始化
            mMediaMuxerManager.prepare();
            // 视频，音频 开始录制
            mMediaMuxerManager.startRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * request stop recording
     */
    private void stopRecording() {
        if (mMediaMuxerManager != null) {
            mMediaMuxerManager.stopRecording();
            mMediaMuxerManager = null;
        }
        if (mediaRecordCallback != null) {
            mediaRecordCallback.finish();
        }
    }

    /**
     * 设置是否录制视频
     *
     * @param mMediaVideoEncoderRunable
     */
    public void setmMediaVideoEncoderRunable(final MediaVideoEncoderRunable mMediaVideoEncoderRunable) {
        this.mMediaVideoEncoderRunable = mMediaVideoEncoderRunable;

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
    }

    private MediaPreviewView.MediaRecordCallback mediaRecordCallback;

    public MediaPreviewView.MediaRecordCallback getMediaRecordCallback() {
        return mediaRecordCallback;
    }

    public void setMediaRecordCallback(MediaPreviewView.MediaRecordCallback mediaRecordCallback) {
        this.mediaRecordCallback = mediaRecordCallback;
    }

}
