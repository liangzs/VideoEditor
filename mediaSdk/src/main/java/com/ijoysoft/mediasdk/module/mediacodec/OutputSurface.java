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

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.Matrix;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.playControl.MediaConfig;
import com.ijoysoft.mediasdk.module.playControl.MediaRenderBus;

import java.util.List;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import kotlin.Triple;

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
 *
 * @Date 20191216 做初始化时，过滤不需要的纹理
 */
public class OutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "OutputSurface";
    private static final boolean VERBOSE = true;
    private EGL10 mEGL;
    private EGLDisplay mEGLDisplay;
    private EGLContext mEGLContext;
    private EGLSurface mEGLSurface;
    private int mshowWidth, mshowHeight, canvasWidth, canvasHeight, offX, offY;
    private MediaItem currentMediaItem;
    private MediaConfig mediaConfig;
    private List<MediaItem> mediaItems;
    //渲染总线
    private MediaRenderBus renderBus;

    /**
     * Creates an OutputSurface using the current EGL context.  Creates a Surface that can be
     * passed to MediaCodec.configure().
     */
    public OutputSurface(List<DoodleItem> doodleItems, List<MediaItem> list, List<Bitmap> widgetMaps,
                         MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
        mediaItems = list;
        if (list.isEmpty()) {
            return;
        }
        currentMediaItem = list.get(0);
        if (currentMediaItem.getWidth() <= 0 || currentMediaItem.getHeight() <= 0) {
            currentMediaItem.setWidth(ConstantMediaSize.showViewWidth);
            currentMediaItem.setHeight(ConstantMediaSize.showViewHeight);
        }
        // 到时候根据是否有角度，进行兑换宽高
        canvasWidth = mediaConfig.getExportWidth(false)[0];
        canvasHeight = mediaConfig.getExportWidth(false)[1];
        calcPreviewRatio(canvasWidth, canvasHeight);

        renderBus = new MediaRenderBus(null);
        renderBus.setIsMurging(true);
        renderBus.setDataSource(list, doodleItems, mediaConfig);
        renderBus.locationRender(currentMediaItem);
        renderBus.setWidgetDataSource(widgetMaps);
        renderBus.setInnerBorder(mediaConfig.getInnerBorder());
        renderBus.setGlobalParticles(mediaConfig.getGlobalParticles());
        renderBus.setPureColor(mediaConfig.getBGInfo());
        renderBus.onSurfaceCreated();
        renderBus.setInitCreate(false);
        renderBus.onSurfaceChanged(offX, offY, mshowWidth, mshowHeight, canvasWidth, canvasHeight);
        renderBus.checkVideoMurge();

    }

    private MediaItem getFirstPhotoItem() {
        for (MediaItem mediaItem : mediaItems) {
            if (mediaItem.isImage()) {
                return mediaItem;
            }
        }
        return null;
    }

    /**
     * 计算显示区域的宽高比，宽屏(16:9),竖屏(9:16),方块(1:1)
     * 其实如果是图片比是4:3用1:1的展示较合适
     * 如果是图片带角度其实是9:16这样的，采取16:9的方式了
     * 720*1280
     */
    private void calcPreviewRatio(int screenWidth, int screenHeight) {
        if (mediaConfig == null) {
            mshowWidth = screenWidth;
            mshowHeight = screenHeight;
            return;
        }
        RatioType ratioType = mediaConfig.getRatioType();
        int showWidth = 0;
        int showHeight = 0;
        int offsetX = 0;
        int offsetY = 0;
        if (ratioType == null) {
            ratioType = RatioType._1_1;
        }
        float screenRatio = screenWidth * 1f / screenHeight;
        float showRatio = ratioType.getRatioValue();
        if (screenRatio > showRatio) {
            showHeight = screenHeight;
            showWidth = (int) (showHeight * showRatio);
            offsetX = (screenWidth - showWidth) / 2;
            offsetY = 0;
        } else {
            showWidth = screenWidth;
            showHeight = (int) (showWidth / showRatio);
            offsetX = 0;
            offsetY = (screenHeight - showHeight) / 2;
        }
        this.mshowWidth = showWidth;
        this.mshowHeight = showHeight;
        this.offX = offsetX;
        this.offY = offsetY;
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
        mEGLDisplay = null;
        mEGLContext = null;
        mEGLSurface = null;
        mEGL = null;
        renderBus.onDestroy();
    }


    /**
     * Draws the data from SurfaceTexture onto the current EGL surface.
     * 这里传入一个变量，指定目标是图片还是视频
     *
     * @param mediaPostion 主要是判断涂鸦层的显示时间
     */
    public void drawImage(int mediaPostion, long curPostion) {
        renderBus.setCurrentMurPts(curPostion);
        renderBus.onDrawFrame(mediaPostion);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture st) {
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
     * 对图片的originAfiter进行初始化准备工作
     * 记得在queuEvent中进行执行
     */
    public void mediaPrepare(int index, int currentPostion) {
        currentMediaItem = mediaItems.get(index);
        renderBus.switchPlayer(new Triple(index, currentPostion, false));
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
            float tranx = (float) x / (float) mshowWidth;
            float trany = (float) y / (float) mshowHeight;
            Log.i(TAG, "x:" + x + ",y:" + y + ",scale:" + scale + ",tranx:" + tranx + "," + trany);
            Matrix.translateM(matrix, 0, tranx, trany, 0f);
        }
        return matrix;
    }

}
