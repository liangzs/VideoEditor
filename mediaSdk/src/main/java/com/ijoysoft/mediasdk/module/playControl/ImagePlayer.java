package com.ijoysoft.mediasdk.module.playControl;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageBackgroundFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.OrientationNoFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.SlideGpuFilterGroup;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

import java.util.List;

/**
 * 用handlerThread提换timer实现暂停，恢复，销毁
 * 亦或用rxsingle实现timer功能
 */
public class ImagePlayer implements IRender {
    private static final String TAG = "ImagePlayer";
    /**
     * 创建缓存对象，离屏buffer
     */
    private int[] fFrame = new int[1];
    // 两张纹理
    private int fTextureSize = 4;
    private int[] fTexture = new int[fTextureSize];

    private HandlerThread handlerThread;
    private Handler handler;
    private final static int RENDER_CODE = 100;
    private int currentDuration;
    private ImageCallback imageCallback;
    private boolean isPlaying;

    // 记录原始图片的纹理id,在create后存储纹理ID
    private MediaItem currentMediaItem;
    private List<MediaItem> mediaItems;
    private int mWidth, mHeight;
    private boolean isTransitionShow = true;
    private boolean isPreviewTransition;
    private ImageBackgroundFilter imageBackgroundFilter;
    // 0表示currentFrame texure,1表示previewTexure,3是结果返回
    private int[] transitionTexure = new int[1];
    /**
     * 用于后台绘制的变换矩阵
     */
    private float[] mediaItemOM;
    /**
     * 旋转矩阵滤镜，撇除和前一帧的转场共同作用，这个矩阵滤镜只用到当前显示
     */
    private AFilter mMatrixShow;

    /**
     * 滤镜组,滤镜由总线切入到支线，各图各自渲染
     */
    private SlideGpuFilterGroup mAfilterGroup;

    public ImagePlayer(ImageCallback imageCallback) {
        this.imageCallback = imageCallback;
        handlerThread = new HandlerThread("imagePlayer");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case RENDER_CODE:
                    startRender();
                    break;
                }
            }
        };
        imageBackgroundFilter = new ImageBackgroundFilter();
        mMatrixShow = new OrientationNoFilter();
        mAfilterGroup = new SlideGpuFilterGroup();
    }

    @Override
    public void onSurfaceCreated() {
        currentDuration = 0;
        imageBackgroundFilter.create();
        mMatrixShow.create();
        mAfilterGroup.init();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height) {
        mWidth = width;
        mHeight = height;
        createFBo(width, height);
        if (currentMediaItem == null) {
            return;
        }
        imageBackgroundFilter.setBitmapPath(currentMediaItem.getFirstFramePath(), currentMediaItem.getRotation());
        imageBackgroundFilter.initTexture();
        Log.i("test","imageplayer->onSurfaceChanged->imageBackgroundFilter.initTexture():"+imageBackgroundFilter.getTextureId());
        imageBackgroundFilter.onSizeChanged(mWidth, mHeight);
        mMatrixShow.onSizeChanged(mWidth, mHeight);
        mAfilterGroup.onSizeChanged(mWidth, mHeight);
    }


    /**
     * 对media进行缩放，平移操作
     */
    public float[] setScaleTranlation(MediaMatrix mediaMatrix) {
        if (mediaMatrix == null) {
            mMatrixShow.setMatrix(MatrixUtils.getOriginalMatrix());
            return null;
        }
        mediaItemOM = MatrixUtils.getOriginalMatrix();
        if (mediaMatrix.getScale() != 0 || mediaMatrix.getOffsetX() != 0 || mediaMatrix.getOffsetY() != 0) {
            float scale = mediaMatrix.getScale();
            int x = mediaMatrix.getOffsetX();
            int y = mediaMatrix.getOffsetY();
            MatrixUtils.scale(mediaItemOM, scale, scale);
            float tranx = (float) x / (float) ConstantMediaSize.showViewWidth;
            float trany = (float) y / (float) ConstantMediaSize.showViewHeight;
            Log.i(TAG, "x:" + x + ",y:" + y + ",scale:" + scale + ",tranx:" + tranx + "," + trany);
            Matrix.translateM(mediaItemOM, 0, tranx, trany, 0f);
        }
        mMatrixShow.setMatrix(mediaItemOM);
        return mediaItemOM;
    }

    /**
     * 图片做旋转时，会有拉伸情况
     *
     * @param r
     */
    public void doRotaion(int r) {
        imageBackgroundFilter.doRotaion(r);
    }

    private void createFBo(int width, int height) {
        // 创建离屏渲染
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // // 创建离屏纹理
        genTextures(width, height);
        // fTexture = FBOManager.getInstance().getFBOforImageplayer(width, height);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        Log.i("test","imageplayer->genTextures:"+fTexture[0]+","+fTexture[1]+","+fTexture[2]+","+fTexture[3]);
        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        }
    }

    private void deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
//        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    /**
     * 对图片的originAfiter进行初始化准备工作
     * 记得在queuEvent中进行执行
     */
    public void playPrepare() {
        // 黑色背景
        GLES20.glViewport(0, 0, ConstantMediaSize.currentScreenWidth, ConstantMediaSize.currentScreenHeight);
        GLES20.glClearColor(0.114f, 0.114f, 0.114f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 显示区背景
        GLES20.glViewport(0, 0, ConstantMediaSize.showViewWidth, ConstantMediaSize.showViewHeight);
        if (currentMediaItem == null) {
            return;
        }
        int cornor = currentMediaItem.getRotation();
        imageBackgroundFilter.setBitmapPath(currentMediaItem.getFirstFramePath(), cornor);
        imageBackgroundFilter.initTexture();
        Log.i("test","imageplayer->playPrepare->imageBackgroundFilter.initTexture():"+imageBackgroundFilter.getTextureId());

        imageBackgroundFilter.setMaxtrix(currentMediaItem.getMediaMatrix());
        imageBackgroundFilter.onSizeChanged(mWidth, mHeight);
        imageBackgroundFilter.drawBackgrondPrepare();
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        imageBackgroundFilter.drawBackground();
        EasyGlUtils.unBindFrameBuffer();
        // 赋值滤镜
        if (currentMediaItem.getAfilter() != null) {
            mAfilterGroup.changeFilter(currentMediaItem.getAfilter());
        }
        mAfilterGroup.onDrawFrame(fTexture[0]);
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
        setScaleTranlation(currentMediaItem.getMediaMatrix());
        mMatrixShow.setTextureId(mAfilterGroup.getOutputTexture());
        mMatrixShow.draw();
        EasyGlUtils.unBindFrameBuffer();
        // 当转场不是空的时候，计算preview模糊纹理
        if (currentMediaItem.getTransitionFilter() != null) {
            currentMediaItem.getTransitionFilter().create();
            currentMediaItem.getTransitionFilter().onSizeChanged(mWidth, mHeight);
            // 传入前张纹理
            cornor = currentMediaItem.getPreRotation();
            MediaItem preMediaItem = getPreMediaItem();
            if (preMediaItem == null) {
                return;
            }
            if (preMediaItem != null && preMediaItem.getMediaMatrix() != null) {
                cornor = (cornor + preMediaItem.getMediaMatrix().getAngle() + 360) % 360;
            }
            if (currentMediaItem.getMediaMatrix() != null) {
                cornor = (cornor - currentMediaItem.getMediaMatrix().getAngle() + 360) % 360;
            }
            imageBackgroundFilter
                    .setBitmapPath(preMediaItem.getMediaType() == MediaType.PHOTO ? preMediaItem.getFirstFramePath()
                            : preMediaItem.getVideolastFramePath(), cornor);
            imageBackgroundFilter.initTexture();
            Log.i("test","imageplayer->playPrepare->TransitionFilter.initTexture():"+imageBackgroundFilter.getTextureId());

            // imageBackgroundFilter.setMaxtrix(preMediaItem == null ? null : preMediaItem.getMediaMatrix());
            imageBackgroundFilter.onSizeChanged(mWidth, mHeight);
            imageBackgroundFilter.drawBackgrondPrepare();
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            imageBackgroundFilter.drawBackground();
            EasyGlUtils.unBindFrameBuffer();
            // 赋值滤镜
            if (preMediaItem.getAfilter() != null) {
                mAfilterGroup.changeFilter(preMediaItem.getAfilter());
            }
            mAfilterGroup.onDrawFrame(fTexture[0]);
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[2]);
            setScaleTranlation(preMediaItem == null ? null : preMediaItem.getMediaMatrix());
            mMatrixShow.setTextureId(mAfilterGroup.getOutputTexture());
            mMatrixShow.draw();
            EasyGlUtils.unBindFrameBuffer();
        }
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
     * 根据currentDuration来是否使用transitionfilter
     */
    @Override
    public void onDrawFrame() {
        // 黑色背景
        GLES20.glViewport(0, 0, ConstantMediaSize.currentScreenWidth, ConstantMediaSize.currentScreenHeight);
        GLES20.glClearColor(0.114f, 0.114f, 0.114f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // 显示区背景
        GLES20.glViewport(0, 0, ConstantMediaSize.showViewWidth, ConstantMediaSize.showViewHeight);
        Log.d("image-onDrawFrame",
                "isTransitionShow==" + isTransitionShow + ", isPreviewTransition==" + isPreviewTransition);
        if ((currentMediaItem.getTransitionFilter() != null && currentDuration < ConstantMediaSize.TRANSITION_DURATION
                && isTransitionShow && currentMediaItem.getDuration() >= ConstantMediaSize.TRANSITION_DURATION)
                || (isPreviewTransition && currentMediaItem.getTransitionFilter() != null)) {
            currentMediaItem.getTransitionFilter().setPreviewTextureId(fTexture[2]);
            currentMediaItem.getTransitionFilter().setTextureId(fTexture[1]);
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[3]);
            currentMediaItem.getTransitionFilter().draw();
            EasyGlUtils.unBindFrameBuffer();
            transitionTexure[0] = fTexture[3];
        } else {
            transitionTexure[0] = fTexture[1];
        }
    }

    public void setDataSource(List<MediaItem> list, MediaItem mediaItem) {
        this.mediaItems = list;
        currentMediaItem = mediaItem;
    }

    /**
     * 检查是否有滤镜
     * @return
     */
    private boolean checkExistsFilter() {
        for (MediaItem mediaItem : mediaItems) {
            if (mediaItem.getAfilter() != null && mediaItem.getAfilter().getmFilterType() != MagicFilterType.NONE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否有转场
     * @return
     */
    private boolean checkExistsTransition() {
        for (MediaItem mediaItem : mediaItems) {
            if (mediaItem.getTransitionFilter() != null
                    && mediaItem.getTransitionFilter().getTransitionType() != TransitionType.NONE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 开始渲染
     */
    public void startRender() {
        if (!isPlaying) {
            return;
        }
        currentDuration += 40;
        if (isPreviewTransition && currentDuration > ConstantMediaSize.TRANSITION_DURATION) {
            isPreviewTransition = false;
            isPlaying = false;
            return;
        }
        Log.i(TAG, "duration---->" + currentDuration);
        if (currentDuration > currentMediaItem.getDuration()) {
            playComplete();
            return;
        }
        Log.d(TAG,
                "isPreviewTransition:" + isPreviewTransition
                        + ", currentDuration > ConstantMediaSize.TRANSITION_DURATION"
                        + (currentDuration > ConstantMediaSize.TRANSITION_DURATION));
        imageCallback.render();
        handler.sendMessageDelayed(handler.obtainMessage(RENDER_CODE), 40);
    }

    /**
     * 控制渲染状态
     */
    public void playPause() {
        isPlaying = false;
        imageCallback.parse();
    }

    /**
     * 控制渲染状态
     */
    public void playTransitionParse() {
        isPlaying = false;
        currentDuration = 0;
        imageCallback.parse();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 恢复播放
     */
    public void playResume() {
        isPlaying = true;
        startRender();
    }

    public void playComplete() {
        Log.i(TAG, "playComplete");
        isPlaying = false;
        currentDuration = 0;
        imageCallback.onComplete();
    }

    /**
     * 跳转到当前播放文件指定位置，duration默认是4s
     *
     * @param seek
     */
    public void seekTo(int seek) {
        Log.i(TAG, "currentDuration-seek:" + seek);
        currentDuration = seek;
        if (currentMediaItem.getTransitionFilter() != null) {
            currentMediaItem.getTransitionFilter().seekTo(seek);
        }
        imageCallback.render();
    }

    /**
     * 归零播放
     */
    public void resetStart() {
        currentDuration = 0;
        if (currentMediaItem.getTransitionFilter() != null) {
            currentMediaItem.getTransitionFilter().seekTo(0);
        }
    }

    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        }
        if (imageBackgroundFilter != null) {
            imageBackgroundFilter.onDestroy();
        }
        deleteFrameBuffer();
    }

    /**
     * 获取当前位置点，默认是时长是4秒
     *
     * @return
     */
    public int getCurrentPostion() {
        return currentDuration;
    }

    public interface ImageCallback {
        void start();

        void onComplete();

        void parse();

        void render();

    }

    public int getOutputTexture() {
        return transitionTexure[0];
    }

    public void setIsPureColor(boolean isPureColor, int[] value) {
        imageBackgroundFilter.setIsPureColor(isPureColor, value);
    }

    /**
     * 开始播放，或者切换播放的时候，记得设置来切换图片源
     *
     * @param currentMediaItem
     */
    public void setCurrentMediaItem(MediaItem currentMediaItem) {
        this.currentMediaItem = currentMediaItem;
    }

    /**
     * 合成的时候，进行更新
     * @param currentDuration
     */
    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    /**
     * 当预览元素片段的时候，直接显示第一帧图
     *
     * @param transitionShow
     */
    public void setTransitionShow(boolean transitionShow) {
        isTransitionShow = transitionShow;
    }

    /**
     * 是否是预览转场效果,如果是预览转场效果，转场播放结束后设置isplaying为false
     */
    public void isPreviewTransition(boolean isPreviewTransition) {
        this.isPreviewTransition = isPreviewTransition;
    }
}
