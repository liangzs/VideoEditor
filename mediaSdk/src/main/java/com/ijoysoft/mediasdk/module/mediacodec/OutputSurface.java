package com.ijoysoft.mediasdk.module.mediacodec;
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.SurfaceTexture;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.VideoInfo;
import com.ijoysoft.mediasdk.module.opengl.MediaDrawer;
import com.ijoysoft.mediasdk.module.playControl.ImagePlayer;
import com.ijoysoft.mediasdk.module.playControl.MediaConfig;
import com.ijoysoft.mediasdk.module.playControl.VideoPlayer;

import java.util.List;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * Holds state associated with a Surface used for MediaCodec decoder output.
 * <p>
 * The (width,height) constructor for this class will prepare GL, create a SurfaceTexture,
 * and then create a Surface for that SurfaceTexture.  The Surface can be passed to
 * MediaCodec.configure() to receive decoder output.  When a frame arrives, we latch the
 * texture with updateTexImage, then render the texture with GL to a pbuffer.
 * <p>
 * The no-arg constructor skips the GL preparation step and doesn't allocate a pbuffer.
 * Instead, it just creates the Surface and SurfaceTexture, and when a frame arrives
 * we just draw it on whatever surface is current.
 * <p>
 * By default, the Surface will be using a BufferQueue in asynchronous mode, so we
 * can potentially drop frames.
 */

/**
 * 如果是视频文件，则通过SurfaceTexture.updateTexImage()去更新，如果是图片
 * 则设置图片背景滤镜去推动，中途加转场，没有直接采用imageplayer是因为不需要imageplayer去推动图片
 * @Date 20191216 做初始化时，过滤不需要的纹理
 */
public class OutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "OutputSurface";
    private static final boolean VERBOSE = false;
    private EGL10 mEGL;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private Object mFrameSyncObject = new Object(); // guards mFrameAvailable
    private boolean mFrameAvailable;

    private MediaDrawer mDrawer;
    private int mWidth, mHeight;
    private MediaItem currentMediaItem;
    private MediaConfig mediaConfig;
    private List<MediaItem> mediaItems;
    private ImagePlayer imagePlayer;
    private VideoPlayer videoPlayer;

    /**
     * Creates an OutputSurface using the current EGL context.  Creates a Surface that can be
     * passed to MediaCodec.configure().
     */
    public OutputSurface(List<DoodleItem> doodleItems, List<MediaItem> list, MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
        mediaItems = list;
        if (list.isEmpty()) {
            return;
        }
        currentMediaItem = list.get(0);
        Log.i(TAG, currentMediaItem.toString());
        Log.i(TAG, mediaConfig.toString());
        if (currentMediaItem.getWidth() <= 0 || currentMediaItem.getHeight() <= 0) {
            currentMediaItem.setWidth(ConstantMediaSize.showMainViewWidth);
            currentMediaItem.setHeight(ConstantMediaSize.showMainViewHeight);
        }
        // 到时候根据是否有角度，进行兑换宽高
        ConstantMediaSize.currentScreenWidth = mediaConfig.getExportWidth()[0];
        ConstantMediaSize.currentScreenHeight = mediaConfig.getExportWidth()[1];
        calcPreviewRatio(ConstantMediaSize.currentScreenWidth, ConstantMediaSize.currentScreenHeight);
        // 背景颜色
        mDrawer = new MediaDrawer();
        mDrawer.setDoodle(doodleItems);
        mDrawer.onSurfaceCreated();
        mDrawer.onSurfaceChanged(ConstantMediaSize.offsetX, ConstantMediaSize.offsetY, mWidth, mHeight);
        if (checkExistsVideo()) {
            videoPlayer = new VideoPlayer();
            videoPlayer.onSurfaceCreated();
            videoPlayer.onSurfaceChanged(ConstantMediaSize.offsetX, ConstantMediaSize.offsetY, mWidth, mHeight);
            videoPlayer.onVideoChanged(currentMediaItem);
            mSurfaceTexture = videoPlayer.getSurfaceTexture();
            mSurfaceTexture.setOnFrameAvailableListener(this);
            mSurface = new Surface(mSurfaceTexture);
        }

        if (checkExistsPhoto()) {
            imagePlayer = new ImagePlayer(null);
            imagePlayer.setDataSource(list, currentMediaItem);
            imagePlayer.onSurfaceCreated();
            imagePlayer.onSurfaceChanged(0, 0, mWidth, mHeight);
        }

        if (!ObjectUtils.isEmpty(mediaConfig.getRgba())) {
            int[] rgba = ColorUtil.hex2Rgb(mediaConfig.getRgba());
            if (videoPlayer != null) {
                videoPlayer.setPureColor(true, rgba);
            }
            if (imagePlayer != null) {
                imagePlayer.setIsPureColor(true, rgba);
            }

        }

    }

    /**
     * 计算显示区域的宽高比，宽屏(16:9),竖屏(9:16),方块(1:1)
     * 其实如果是图片比是4:3用1:1的展示较合适
     * 如果是图片带角度其实是9:16这样的，采取16:9的方式了
     * 720*1280
     */
    private void calcPreviewRatio(int screenWidth, int screenHeight) {
        if (mediaConfig == null) {
            ConstantMediaSize.showViewWidth = screenWidth;
            ConstantMediaSize.showViewHeight = screenHeight;
            return;
        }
        int showWidth = 0;
        int showHeight = 0;
        int offsetX = 0;
        int offsetY = 0;
        // float ratio1 = outputWidth / framewidth;
        // float ratio2 = outputHeight / frameheight;
        // float ratioMax = Math.min(ratio1, ratio2);
        // // 居中后图片显示的大小
        // float imageWidthNew = Math.round(framewidth * ratioMax);
        // float imageHeightNew = Math.round(frameheight * ratioMax);
        switch (mediaConfig.getRatioType()) {
        case NONE:
            showWidth = showHeight = screenHeight;
            offsetX = offsetY = 0;
            break;
        case _1_1:
            showWidth = showHeight = screenHeight;
            offsetX = offsetY = 0;
            break;
        case _9_16:
            showHeight = screenHeight;
            showWidth = (int) (0.5625 * showHeight);
            offsetX = (screenWidth - showWidth) / 2;
            offsetY = 0;
            break;
        case _16_9:
            showWidth = screenWidth;
            showHeight = (int) (0.5625 * showWidth);
            offsetY = (screenWidth - showHeight) / 2;
            offsetX = 0;
            break;
        case _3_4:
            showHeight = screenHeight;
            showWidth = (int) (0.75 * showHeight);
            offsetX = (screenWidth - showWidth) / 2;
            offsetY = 0;
            break;
        case _4_3:
            showWidth = screenWidth;
            showHeight = (int) (0.75 * showWidth);
            offsetY = (screenWidth - showHeight) / 2;
            offsetX = 0;
            break;
        }
        mWidth = ConstantMediaSize.showViewWidth = showWidth;
        mHeight = ConstantMediaSize.showViewHeight = showHeight;
        ConstantMediaSize.offsetX = offsetX;
        ConstantMediaSize.offsetY = offsetY;
        Log.i(TAG, "calRatio:" + mWidth + "," + mHeight + "," + offsetX + "," + offsetY);
    }

    /**
     * Discard all resources held by this class, notably the EGL context.
     */
    public void release() {
        if (mEGL != null) {
            if (mEGL.eglGetCurrentContext().equals(mEGLContext)) {
                // Clear the current context and surface to ensure they are discarded immediately.
                mEGL.eglMakeCurrent(mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            }
            mEGL.eglDestroySurface(mEGLDisplay, mEGLSurface);
            mEGL.eglDestroyContext(mEGLDisplay, mEGLContext);
            // mEGL.eglTerminate(mEGLDisplay);
        }
        if(mSurface!=null){
            mSurface.release();
        }
        mEGLDisplay = null;
        mEGLContext = null;
        mEGLSurface = null;
        mEGL = null;
        mSurface = null;
        mSurfaceTexture = null;
    }

    /**
     * Returns the Surface that we draw onto.
     */
    public Surface getSurface() {
        return mSurface;
    }

    /**
     * Latches the next buffer into the texture.  Must be called from the thread that created
     * the OutputSurface object, after the onFrameAvailable callback has signaled that new
     * data is available.
     */
    public void awaitNewImage() {
        final int TIMEOUT_MS = 500;
        synchronized (mFrameSyncObject) {
            while (!mFrameAvailable) {
                try {
                    // Wait for onFrameAvailable() to signal us. Use a timeout to avoid
                    // stalling the test if it doesn't arrive.
                    mFrameSyncObject.wait(TIMEOUT_MS);
                    if (!mFrameAvailable) {
                        // TODO: if "spurious wakeup", continue while loop
                        throw new RuntimeException("Surface frame wait timed out");
                    }
                } catch (InterruptedException ie) {
                    // shouldn't happen
                    throw new RuntimeException(ie);
                }
            }
            mFrameAvailable = false;
        }
        // mSurfaceTexture.updateTexImage();
    }

    /**
     * Draws the data from SurfaceTexture onto the current EGL surface.
     * 这里传入一个变量，指定目标是图片还是视频
     *
     * @param mediaPostion 主要是判断涂鸦层的显示时间
     */
    public void drawImage(boolean isVideo, int mediaPostion, int curPostion) {
        mDrawer.setCurrentDuration(mediaPostion);
        if (!isVideo) {
            imagePlayer.setCurrentDuration(curPostion);
            imagePlayer.onDrawFrame();
            mDrawer.setInputTexture(imagePlayer.getOutputTexture());
        } else {
            videoPlayer.setCurrentVideoDuration(curPostion);
            videoPlayer.onDrawFrame();
            mDrawer.setInputTexture(videoPlayer.getOutputTexture());
        }
        mDrawer.onDrawFrame();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture st) {
        if (VERBOSE)
            LogUtils.d(TAG, "new frame available");
        synchronized (mFrameSyncObject) {
            if (mFrameAvailable) {
                return;
//                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
            mFrameAvailable = true;
            mFrameSyncObject.notifyAll();
        }
    }

    /**
     * Che cks for EGL errors.
     */
    private void checkEglError(String msg) {
        boolean failed = false;
        int error;
        while ((error = mEGL.eglGetError()) != EGL10.EGL_SUCCESS) {
            LogUtils.e(TAG, msg + ": EGL error: 0x" + Integer.toHexString(error));
            failed = true;
        }
        if (failed) {
            throw new RuntimeException("EGL error encountered (see log)");
        }
    }

    /**
     * 添加涂鸦成,根据不一样时间长度，显示不同时长的涂鸦
     */
    public void addDoodle(DoodleItem doodleItem) {
        mDrawer.addDoodle(doodleItem);
    }

    public void onVideoSizeChanged(VideoInfo info) {
        mDrawer.onSurfaceChanged(0, 0, info.width, info.height);
    }

    /**
     * 对图片的originAfiter进行初始化准备工作
     * 记得在queuEvent中进行执行
     */
    public void mediaPrepare(MediaItem item) {
        currentMediaItem = item;
        if (currentMediaItem.getMediaType() != MediaType.VIDEO) {
            imagePlayer.setCurrentMediaItem(currentMediaItem);
            imagePlayer.playPrepare();
        } else {
            if (currentMediaItem.getAfilter() != null) {
                videoPlayer.changeFilter(currentMediaItem.getAfilter());
            }
            videoPlayer.onVideoChanged(item);
            videoPlayer.setVideoFrameRatioUpdate(currentMediaItem);
            MediaItem preMediaItem = getPreMediaItem();
            videoPlayer.playPrepare(preMediaItem);
        }
        if (mediaConfig.getRatioType() == RatioType._4_3 || mediaConfig.getRatioType() == RatioType._16_9) {
            mDrawer.preViewRotate(true);
        } else {
            mDrawer.preViewRotate(false);
        }
    }

    /**
     * 对media进行缩放，平移操作
     */
    public float[] getScaleTranlation(float[] matrix, MediaMatrix mediaMatrix) {
        if (mediaMatrix == null) {
            return matrix;
        }
        if (mediaMatrix.getScale() != 0 || mediaMatrix.getOffsetX() != 0 || mediaMatrix.getOffsetY() != 0) {
            float scale = mediaMatrix.getScale();
            int x = mediaMatrix.getOffsetX();
            int y = mediaMatrix.getOffsetY();
            MatrixUtils.scale(matrix, scale, scale);
            float tranx = (float) x / (float) ConstantMediaSize.showViewWidth;
            float trany = (float) y / (float) ConstantMediaSize.showViewHeight;
            Log.i(TAG, "x:" + x + ",y:" + y + ",scale:" + scale + ",tranx:" + tranx + "," + trany);
            Matrix.translateM(matrix, 0, tranx, trany, 0f);
        }
        return matrix;
    }

    private MediaItem getPreMediaItem() {
        int index = 0;
        for (int i = 0; i < mediaItems.size(); i++) {
            if (mediaItems.get(i) == currentMediaItem) {
                index = i - 1;
                break;
            }
        }
        if (index != -1) {
            return mediaItems.get(index);
        }
        return null;
    }

    /**
     * 检查是否有滤镜
     * @return
     */
    private boolean checkExistsPhoto() {
        for (MediaItem mediaItem : mediaItems) {
            if (mediaItem.getMediaType() == MediaType.PHOTO) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否有转场
     * @return
     */
    private boolean checkExistsVideo() {
        for (MediaItem mediaItem : mediaItems) {
            if (mediaItem.getMediaType() == MediaType.VIDEO) {
                return true;
            }
        }
        return false;
    }

}
