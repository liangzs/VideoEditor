package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.view.BackgroundType;

import java.util.Arrays;

/**
 * 处理视频播放的背景问题，其本身的的textureid是视频帧纹理id
 * 经过fbo处理之后，开放textureid过去
 * 原先视频纹理是继承OesFilter的samplerExternalOES vTexture，现兼容sampler2D vTexture
 */

public class VideoBackgroundFilter extends OesFilter {
    private static final String TAG = "VideoBackgroundFilter";
    public static final int ROT_0 = 0;
    public static final int ROT_90 = 90;
    public static final int ROT_180 = 180;
    public static final int ROT_270 = 270;
    private GaussianBlurBackgroundFilter widthBlurFilter;
    private GaussianBlurBackgroundFilter heightBlurFilter;
    private PureColorFilter pureColorFilter;
    private ImageOriginFilter customImagefilter;
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[2];

    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private MediaItem mediaItem;
    private int fTextureSize = 2;
    private float[] cube;
    private boolean isDoRotate;
    private int matrixAngle;
    private float[] mediaItemOM;
    private float offsetSigma;
    private int width, height, canvasWidth, canvasHeight;
    private BGInfo mBGInfo;
    private int blurLevel;
    private float[] zoomMatrix;

    public VideoBackgroundFilter() {
        super();
        offsetSigma = 0.72f;
        mBGInfo = new BGInfo();
    }


    /**
     * 图片旋转时，出现拉伸情况
     *
     * @param r
     */
    public void doRotaion(int r) {
        matrixAngle = r;
        isDoRotate = false;
        if (r == 90 || r == 270) {
            isDoRotate = true;
        }
    }

    /**
     * 旋转视频操作
     *
     * @param rotation
     */
    public void setRotation(int rotation) {
        Log.i(TAG, "setRotationl:" + rotation);
        float[] coord;
        switch (rotation) {
            case ROT_0:
                coord = new float[]{
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f,};
                break;
            case ROT_90:// 根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
                coord = new float[]{1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};

                break;
            case ROT_180:
                coord = new float[]{1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,};
                break;
            case ROT_270:
                coord = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,};
                break;
            default:
                return;
        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void onSurfaceChanged(int offsetX, int offsetY, int viewWidth, int viewHeight, int screenWidth, int screenHeight) {
        this.width = viewWidth;
        this.height = viewHeight;
        this.canvasWidth = screenWidth;
        this.canvasHeight = screenHeight;
        createFBo(width, height);
        adjustVideoScaling();
    }

    private void createFBo(int width, int height) {
        // 创建离屏渲染
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // 创建离屏纹理
        genTextures(width, height);
        // fTexture = FBOManager.getInstance().fboTransactionNoRecyle(fTextureSize);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);

        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        }
    }

    private void deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    public void setIsPureColor(BGInfo bgInfo) {
        this.mBGInfo = bgInfo;
        switch (mBGInfo.getBackroundType()) {
            case BackgroundType.COLOR:
            case BackgroundType.NONE:
                createColorBackground();
                rgba[0] = ((float) mBGInfo.getRgbs()[0] / 255f);
                rgba[1] = ((float) mBGInfo.getRgbs()[1] / 255f);
                rgba[2] = ((float) mBGInfo.getRgbs()[2] / 255f);
                rgba[3] = ((float) mBGInfo.getRgbs()[3] / 255f);
                pureColorFilter.setIsPureColor(rgba);
                break;
            case BackgroundType.SELF:
                //10- 100  10  30  50 70  90
                // 5 15  25  35 45
                //散发：1-5

                createSelfBlurBackground();
                break;
            case BackgroundType.IMAGE:
                createCustomImageFilter();
                break;
        }
    }

    /**
     * 创建纯色背景
     */
    private void createColorBackground() {
        if (pureColorFilter == null) {
            pureColorFilter = new PureColorFilter();
            pureColorFilter.create();
            pureColorFilter.setSize(width, height);
        }
    }

    private void createCustomImageFilter() {
        if (customImagefilter == null) {
            customImagefilter = new ImageOriginFilter();
            customImagefilter.create();
            customImagefilter.setSize(width, height);
        }
        if (mediaItem != null && mBGInfo.getCustomBitmap() != null) {
            customImagefilter.initTexture(mBGInfo.getCustomBitmap());
            customImagefilter.adjustImageScalingStretch(mediaItem.getRotation(), width, height);
        }
    }

    /**
     * 用自身做背景
     */
    private void createSelfBlurBackground() {
        blurLevel = (int) (mBGInfo.getBlurLevel() * 0.5f);//1-50程度系数可改,偏移量offset则从1-5过渡;
        offsetSigma = 0.06f * blurLevel;
        offsetSigma = offsetSigma > 2 ? 2 : offsetSigma;
        if (widthBlurFilter != null) {
            widthBlurFilter.onClear();
        }
        if (heightBlurFilter != null) {
            heightBlurFilter.onDestroy();
        }
        widthBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(blurLevel).setTexelWidthOffset(offsetSigma).setScale(true);
        heightBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(blurLevel).setTexelHeightOffset(offsetSigma).setScale(true);
        widthBlurFilter.create();
        widthBlurFilter.onSizeChanged(width, height);
        heightBlurFilter.create();
        heightBlurFilter.onSizeChanged(width, height);
        if (mediaItem != null) {
            int rotation = (matrixAngle + mediaItem.getRotation()) % 360;
            widthBlurFilter.adjustImageScalingStretch(mediaItem.getWidth(), mediaItem.getHeight(), rotation, width, height);
        }
    }

    /**
     * 显示video的比例
     * MatrixUtils.rotate(SM, 90);和调顶点位置相通
     */
    private void adjustVideoScaling() {
        float outputWidth = width;
        float outputHeight = height;
        if (mediaItem != null) {
            int framewidth = mediaItem.getWidth();
            int frameheight = mediaItem.getHeight();
            LogUtils.d(TAG, "matrixAngle:" + matrixAngle + ",mediaItem.getAfterRotation():" + mediaItem.getRotation());
            int rotation = (matrixAngle + mediaItem.getRotation()) % 360;
            if ((rotation == 90 || rotation == 270)) {
                framewidth = mediaItem.getHeight();
                frameheight = mediaItem.getWidth();
            }
            float ratio1 = outputWidth / framewidth;
            float ratio2 = outputHeight / frameheight;
            float ratioMax = Math.min(ratio1, ratio2);
            // 居中后图片显示的大小
            float imageWidthNew = Math.round(framewidth * ratioMax);
            float imageHeightNew = Math.round(frameheight * ratioMax);

            // 图片被拉伸的比例,以及显示比例时的偏移量
            // float ratioWidth = outputWidth / imageWidthNew;
            // float ratioHeight = outputHeight / imageHeightNew;
            float ratioWidth = outputWidth / imageWidthNew;
            float ratioHeight = outputHeight / imageHeightNew;
            LogUtils.i(TAG,
                    "outputWidth:" + outputWidth + ",outputHeight:" + outputHeight + ",imageWidthNew:" + imageWidthNew
                            + ",imageHeightNew:" + imageHeightNew + ",framewidth:" + framewidth + ",frameheight:"
                            + frameheight);
            LogUtils.i(TAG, "isDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth + ",ratioHeight:" + ratioHeight);
            if (isDoRotate) {// 这个回路好难理解啊，根据结果推到没有旋转前的状态
                if (imageWidthNew > imageHeightNew) {
                    ratioWidth = ratioHeight;
                    ratioHeight = 1;
                    LogUtils.i(TAG, "ratioWidth > ratioHeight---tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                            + ",ratioHeight:" + ratioHeight);
                } else {
                    ratioHeight = ratioWidth;
                    ratioWidth = 1;
                    LogUtils.i(TAG, "ratioWidth < ratioHeigh----tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                            + ",ratioHeight:" + ratioHeight);
                }
            }
            float pos[] = {
                    -1.0f, 1.0f,
                    -1.0f, -1.0f,
                    1.0f, 1.0f,
                    1.0f, -1.0f,
            };
            // 根据拉伸比例还原顶点
            cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                    pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
            mVerBuffer.clear();
            mVerBuffer.put(cube).position(0);

        }
    }


    /**
     * 绘画背景，对外提供接口，因为视频是GL_TEXTURE_EXTERNAL_OES，需转化
     * GL_TEXTURE_EXTERNAL_OES 转GL_TEXTURE_2D
     */
    public void drawBackground() {
        // 黑色背景
        GLES20.glViewport(0, 0, canvasWidth, canvasHeight);
        GLES20.glClearColor(0.114f, 0.114f, 0.114f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 显示区背景
        GLES20.glViewport(0, 0, width, height);
        switch (mBGInfo.getBackroundType()) {
            case BackgroundType.COLOR:
            case BackgroundType.NONE:
                EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
                pureColorFilter.draw();
                drawZoomScale();
                EasyGlUtils.unBindFrameBuffer();
                break;
            case BackgroundType.SELF:
                drawBackgroundSelf();
                break;
            case BackgroundType.IMAGE:
                EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
                customImagefilter.draw();
                drawZoomScale();
                EasyGlUtils.unBindFrameBuffer();
                break;
        }
    }

    /**
     * 缩放展示区域
     */
    private void drawZoomScale() {
        if (mBGInfo.getZoomScale() != 0) {
            zoomMatrix = Arrays.copyOf(cube, cube.length);
            for (int i = 0; i < zoomMatrix.length; i++) {
                zoomMatrix[i] *= mBGInfo.getZoomScale();
            }
            mVerBuffer.clear();
            mVerBuffer.put(zoomMatrix).position(0);
            draw();
            return;
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        draw();
    }

    /**
     * 自身糊度
     */
    private void drawBackgroundSelf() {
        if (widthBlurFilter == null) {
            createSelfBlurBackground();
        }
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        mVerBuffer.clear();
        mVerBuffer.put(pos).position(0);
        draw();// GL_TEXTURE_EXTERNAL_OES 转GL_TEXTURE_2D
        EasyGlUtils.unBindFrameBuffer();
        // 模糊width方向
        widthBlurFilter.setTextureId(fTexture[0]);
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
        widthBlurFilter.draw();
        EasyGlUtils.unBindFrameBuffer();
        // 模糊height方向
        heightBlurFilter.setTextureId(fTexture[1]);

        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        heightBlurFilter.draw();
        drawZoomScale();
        EasyGlUtils.unBindFrameBuffer();
    }


    /**
     * 获取fbo的渲染纹理
     *
     * @return
     */
    public int getOutputTexture() {
        // if (!isPureBg) {
        // return fTexture[1];
        // }
        return fTexture[0];
    }

    /**
     * 视频帧的显示
     */
    public void setVideoMediaItem(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }

    /**
     * 视频帧的显示及更新
     * 这里只负责做旋转的动作，包括旋转后的顶点动态变化，以及纹理的旋转
     * * 缩放和平移放在videodrawer的matrixshow去管理，记得先旋转后缩放
     */
    public void setVideoFrameRatioUpdate(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
        mediaItemOM = MatrixUtils.getOriginalMatrix();
        setMatrix(mediaItemOM);
        doRotaion(0);
        if (mediaItem.getMediaMatrix() != null) {
//            doRotaion(mediaItem.getMediaMatrix().getAngle());
            MatrixUtils.rotate(mediaItemOM, mediaItem.getMediaMatrix().getAngle());
            getScaleTranlation(mediaItemOM, mediaItem.getMediaMatrix());
            setMatrix(mediaItemOM);
        }
        //ratio
//        adjustVideoScaling();
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
            LogUtils.i(TAG, "x:" + x + ",y:" + y + ",scale:" + scale + ",tranx:" + tranx + "," + trany);
            Matrix.translateM(matrix, 0, tranx, trany, 0f);
        }
        return matrix;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (widthBlurFilter != null) {
            widthBlurFilter.onDestroy();
        }
        if (heightBlurFilter != null) {
            heightBlurFilter.onDestroy();
        }
        if (pureColorFilter != null) {
            pureColorFilter.onDestroy();
        }
        deleteFrameBuffer();
    }

}
