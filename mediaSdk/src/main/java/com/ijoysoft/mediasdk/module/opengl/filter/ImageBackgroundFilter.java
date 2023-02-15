package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.view.BackgroundType;

import java.util.Arrays;

/**
 * 图片层的背景渲染，逻辑从imageplayer中抽出
 * 目的是把背景封装，暴露纹理接口，即外部调用的时候只需传入纹理id便可
 */
public class ImageBackgroundFilter extends ImageOriginFilter {
    private PureColorFilter pureColorFilter;
    //    private ImageOriginFilter customImagefilter;
    private int customImageTexture;
    private GaussianBlurBackgroundFilter widthBlurFilter;
    private GaussianBlurBackgroundFilter heightBlurFilter;
    //存储滤镜信息
    protected GPUImageFilter curFilter;
    /**
     * 创建缓存对象，离屏buffer
     */
    protected int[] fFrame = new int[1];
    private int fTextureSize = 2;
    protected int[] fTexture = new int[fTextureSize];
    private float offsetSigma;
    protected BGInfo mBGInfo;
    private int blurLevel;
    private float[] zoomMatrix;


    public ImageBackgroundFilter() {
        mBGInfo = new BGInfo();
    }

    /**
     * 处理旋转角度
     *
     * @param rotation
     */
    @Override
    public void setRotation(int rotation) {
        super.setRotation(rotation);
    }

    @Override
    public void doRotaion(int r) {
        super.doRotaion(r);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        this.width = width;
        this.height = height;
        createFBo(width, height, fFrame, fTextureSize, fTexture);
    }


    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            LogUtils.e(TAG, op + ": glError " + error);
            // throw new RuntimeException(op + ": glError " + error);
        }
    }


    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
        GLES20.glDeleteTextures(1, new int[]{originTexture}, 0);
        originTexture = -1;
        if (widthBlurFilter != null) {
            widthBlurFilter.onDestroy();
        }
        if (heightBlurFilter != null) {
            heightBlurFilter.onDestroy();
        }
        if (pureColorFilter != null) {
            pureColorFilter.onDestroy();
        }
        if (customImageTexture != 0) {
            GLES20.glDeleteTextures(1, new int[]{customImageTexture}, 0);
        }
        deleteFrameBuffer(fFrame, fTextureSize, fTexture);
    }

    public void setIsPureColor(BGInfo bgInfo, int width, int height) {
        this.mBGInfo = bgInfo;
        this.width = width;
        this.height = height;
        if (pureColorFilter == null && widthBlurFilter == null) {
            checkCreateRender();
        }
    }

    private void checkBlurBgVertext() {
        if (widthBlurFilter != null && widthBlurFilter.getCube() == null) {
            if (bitmap != null) {
                widthBlurFilter.adjustImageScalingCrop(bitmap.getWidth(), bitmap.getHeight(),
                        (bitmapAngle) % 360, width, height);
                heightBlurFilter.setVertex(widthBlurFilter.getCube());
            }
        }
        if (mBGInfo.getCustomBitmap() != null && widthBlurFilter != null) {
            widthBlurFilter.adjustImageScalingCrop(mBGInfo.getCustomBitmap().getWidth(), mBGInfo.getCustomBitmap().getHeight(),
                    (bitmapAngle) % 360, width, height);
            heightBlurFilter.setVertex(widthBlurFilter.getCube());
        }
    }

    /**
     * 检测创建
     */
    protected void checkCreateRender() {
        switch (mBGInfo.getBackroundType()) {
            case BackgroundType.COLOR:
            case BackgroundType.NONE:
                createColorBackground();
                float[] rgba = new float[4];
                rgba[0] = ((float) mBGInfo.getRgbs()[0] / 255f);
                rgba[1] = ((float) mBGInfo.getRgbs()[1] / 255f);
                rgba[2] = ((float) mBGInfo.getRgbs()[2] / 255f);
                rgba[3] = ((float) mBGInfo.getRgbs()[3] / 255f);
                pureColorFilter.setIsPureColor(rgba);
                break;
            case BackgroundType.SELF:
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
        createSelfBlurBackground();
        if (mBGInfo.getCustomBitmap() != null) {
            widthBlurFilter.adjustImageScalingCrop(mBGInfo.getCustomBitmap().getWidth(), mBGInfo.getCustomBitmap().getHeight(),
                    (bitmapAngle) % 360, width, height);
            heightBlurFilter.adjustImageScalingCrop(mBGInfo.getCustomBitmap().getWidth(), mBGInfo.getCustomBitmap().getHeight(),
                    (bitmapAngle) % 360, width, height);
        }
    }

    /**
     * 用自身做背景
     */
    private void createSelfBlurBackground() {
        if (mBGInfo.getBlurLevel() < 0 || mBGInfo.getBlurLevel() > 100) {
            return;
        }
        //10- 100  10  30  50 70  90
        // 5 15  25  35 45
        //散发：1-5
        blurLevel = (int) (mBGInfo.getBlurLevel() * 0.5f);//1-50程度系数可改,偏移量offset则从1-5过渡;
//                blurLevel = 45;
        blurLevel = blurLevel == 0 ? 1 : blurLevel;
        offsetSigma = 0.06f * blurLevel;
        offsetSigma = offsetSigma > 2 ? 2 : offsetSigma;

        if (widthBlurFilter != null) {
            widthBlurFilter.onDestroy();
            widthBlurFilter = null;
        }
        if (heightBlurFilter != null) {
            heightBlurFilter.onDestroy();
            heightBlurFilter = null;
        }
        if (width == 0 || height == 0) {
            LogUtils.e("ImageBackgroundFilter", "createSelfBlurBackground:w,h:" + width + "," + height);
            return;
        }
//        LogUtils.v("ImageBackgroundFilter", "width:" + width + ",height:" + height);
        widthBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(blurLevel).setTexelWidthOffset(offsetSigma).setScale(true);
        heightBlurFilter =
                GaussianBlurBackgroundFilter.initWithBlurRadiusInPixels(blurLevel).setTexelHeightOffset(offsetSigma).setScale(true);
        widthBlurFilter.create();
        widthBlurFilter.onSizeChanged(width, height);
        heightBlurFilter.create();
        heightBlurFilter.onSizeChanged(width, height);
        if (videoHeight != 0) {
            int rotation = (bitmapAngle) % 360;
            widthBlurFilter.adjustImageScalingStretch(videoWidth, videoHeight, rotation, width, height);
        }
        //根据mediaitem中的图形不一致进行等比例缩放
        if (bitmap != null) {
            widthBlurFilter.adjustImageScalingCrop(bitmap.getWidth(), bitmap.getHeight(),
                    (bitmapAngle) % 360, width, height);
            heightBlurFilter.adjustImageScalingCrop(bitmap.getWidth(), bitmap.getHeight(),
                    (bitmapAngle) % 360, width, height);
        }
    }


    /**
     * 这里一个很重要的点就是经过    setTextureId(fTexture[1]);之后是整个glview是一张纹理了
     * BackgroundType.IMAGE 当自定义图的时候，模糊用预处理进行，不必多走两个模糊纹理
     */
    public void drawBackgrondPrepare(MediaItem mediaItem) {
        if (pureColorFilter == null && widthBlurFilter == null) {
            if (mediaItem != null && mediaItem.isVideo()) {
                videoWidth = mediaItem.getWidth();
                videoHeight = mediaItem.getHeight();
            }
            setIsPureColor(mBGInfo, width, height);
        }
        checkBlurBgVertext();
        switch (mBGInfo.getBackroundType()) {
            case BackgroundType.COLOR:
            case BackgroundType.NONE:
                if (isVideoDraw) {
                    EasyGlUtils.bindFrameTexture(videoFrame[0], videoFteture[0]);
                    videoDataSourcePlay.draw();
                    EasyGlUtils.unBindFrameBuffer();
                    setTextureId(videoFteture[0]);
                }
                EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
                pureColorFilter.draw();
                drawZoomScale();
                EasyGlUtils.unBindFrameBuffer();
                break;
            case BackgroundType.SELF:
                drawSelfBackground(mediaItem);
                break;
            case BackgroundType.IMAGE:
                drawCustomImageBackground();
                break;
        }
        drawTheEnd(mediaItem);
    }

    /**
     * 因为视频是GL_TEXTURE_EXTERNAL_OES，需转化
     * GL_TEXTURE_EXTERNAL_OES 转GL_TEXTURE_2D
     * 改变视频类的纹理类型
     */
    private void exchangeVideoTexture(int texture) {

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
        LogUtils.v("ImageBackgroundFilter", ":cube" + Arrays.toString(cube));
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        draw();
    }

    /**
     * 背景模糊为自己的渲染帧
     * 即用自己当前纹理，经过模糊背景绘画后，在上面叠加它自己的缓存帧，再赋值给当前filter对象
     */
    private void drawSelfBackground(MediaItem mediaItem) {
        setTextureId(originTexture);
        /**
         * 转化成照片普通类型纹理,视频外部纹理为es类型
         */
        if (isVideoDraw) {
            EasyGlUtils.bindFrameTexture(videoFrame[0], videoFteture[0]);
            videoDataSourcePlay.draw();
            EasyGlUtils.unBindFrameBuffer();
            setTextureId(videoFteture[0]);
        }
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        widthBlurFilter.setTextureId(getTextureId());
        widthBlurFilter.draw();
        EasyGlUtils.unBindFrameBuffer();

        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
        heightBlurFilter.setTextureId(fTexture[0]);
        heightBlurFilter.setRotation(bitmapAngle);
        heightBlurFilter.draw();
//        setRotation(bitmapAngle);
        drawZoomScale();
        EasyGlUtils.unBindFrameBuffer();
    }


    /**
     * 自定义背景
     */
    private void drawCustomImageBackground() {
        if (isVideoDraw) {
            EasyGlUtils.bindFrameTexture(videoFrame[0], videoFteture[0]);
            videoDataSourcePlay.draw();
            EasyGlUtils.unBindFrameBuffer();
            setTextureId(videoFteture[0]);
        }
        if (customImageTexture == 0) {
            customImageTexture = OpenGlUtils.loadTexture(mBGInfo.getCustomBitmap());
        }

        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        widthBlurFilter.setTextureId(customImageTexture);
        widthBlurFilter.draw();
        EasyGlUtils.unBindFrameBuffer();

        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
        heightBlurFilter.setTextureId(fTexture[0]);
        heightBlurFilter.setRotation(bitmapAngle);
        heightBlurFilter.draw();
        drawZoomScale();
        EasyGlUtils.unBindFrameBuffer();

    }


    /**
     * 把绘画好的缓存帧，再赋值给当前filter对象
     *
     * @param mediaItem
     */
    private void drawTheEnd(MediaItem mediaItem) {
        //这里删除原始纹理，因为后面已经用不到了
//        GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);
        int texture = fTexture[1];
        pos = new float[]{
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f,};
        if (curFilter != null && curFilter.getmFilterType() != MagicFilterType.NONE) {
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            curFilter.onDrawFrame(texture);
            EasyGlUtils.unBindFrameBuffer();
            texture = fTexture[0];
        }
        setTextureId(texture);
        //重新设置顶点，因为设置过模糊的纹理已经是匹配了坐标了，所以现在把坐标设置成全屏就好了
        setRotation(0);
        mVerBuffer.clear();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
    }


}
