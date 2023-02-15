package com.ijoysoft.mediasdk.module.opengl.filter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.text.TextUtils;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;

import java.util.Arrays;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

import java.util.Arrays;

/**
 * 图片源，图片的最原始纹理存储于此，后面的滤镜复用其纹理，而不是根据bitmap再生成纹理
 */
public class ImageOriginFilter extends AFilter {
    protected static final String TAG = "ImageOriginFilter";
    protected String mBitmapPath;
    protected float[] cube = pos;
    protected int bitmapAngle;
    protected int matrixAngle;
    protected boolean isDoRotate;
    /**
     * 图像
     */
    protected Bitmap bitmap;

    protected int videoWidth, videoHeight;
    protected int width;
    protected int height;

    //是否视频绘画
    protected boolean isVideoDraw;
    //用于承载es视频源的纹理
    protected OesFilter videoDataSourcePlay;
    protected int[] videoFrame = new int[1];
    private int fTextureSize = 1;
    protected int[] videoFteture = new int[fTextureSize];
    protected MediaMatrix mMediaMatrix;

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.uRes("shader/base_fragment.sh"));
        float[] coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
        cube = pos;
    }

    @Override
    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
        //LogUtils.v(getClass().getSimpleName(), "width and height: "+width+","+ height);
        if (onSizeChangedListener == null) {
            adjustImageScaling(width, height);
        } else {
            onSizeChangedListener.onSizeChanged(width, height);
        }
        if (isVideoDraw && (videoFteture[0] == 0 || videoFteture[0] == -1)) {
            createFBo(width, height, videoFrame, fTextureSize, videoFteture);
        }

    }


    /**
     * 因为比例方案有三个
     * 方块，宽屏和竖屏，然后根据本身glview的大小设置showview的大小
     * 如果showview中纹理填充不玩，会拿其他颜色进行填充
     * <p>
     * 如果图片为空，则显示showview的大小
     */
    public void adjustImageScaling(int width, int height) {
        if (bitmap != null) {
            adapterVertex(bitmap.getWidth(), bitmap.getHeight(), width, height);
            return;
        }
        if (!TextUtils.isEmpty(mBitmapPath)) {
            Bitmap mBitmap = BitmapFactory.decodeFile(mBitmapPath);
            if (mBitmap == null) {
                return;
            }
            adapterVertex(mBitmap.getWidth(), mBitmap.getHeight(), width, height);
        }

        if (videoHeight != 0 && videoWidth != 0) {
            adapterVertex(videoWidth, videoHeight, width, height);
        }

    }

    /**
     * 适配显示宽高
     */
    private void adapterVertex(int bitmapWidth, int bitmapHeight, int outputWidth, int outputHeight) {
        int rotation = (matrixAngle + bitmapAngle) % 360;
        int framewidth = bitmapWidth;
        int frameheight = bitmapHeight;
        if ((rotation == 90 || rotation == 270)) {
            framewidth = bitmapHeight;
            frameheight = bitmapWidth;
        }
        float ratio1 = (1f * outputWidth) / (1f * framewidth);
        float ratio2 = (1f * outputHeight) / (1f * frameheight);
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = (1f * outputWidth) / (1f * imageWidthNew);
        float ratioHeight = (1f * outputHeight) / (1f * imageHeightNew);
        //频繁打印的日志放至-verbose级别中
        LogUtils.v(TAG, "isDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth + ",ratioHeight:" + ratioHeight);
        if (isDoRotate) {// 这个回路好难理解啊，根据结果推到没有旋转前的状态
            if (ratioWidth > ratioHeight) {
                ratioHeight = ratioWidth;
                ratioWidth = 1;
                //频繁打印的日志放至-verbose级别中
                LogUtils.v(TAG, "ratioWidth>  ratioHeigh----tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                        + ",ratioHeight:" + ratioHeight);
            } else {
                ratioWidth = ratioHeight;
                ratioHeight = 1;
                //频繁打印的日志放至-verbose级别中
                LogUtils.v(TAG, "ratioWidth < ratioHeight---tisDoRotate:" + isDoRotate + ",ratioWidth:" + ratioWidth
                        + ",ratioHeight:" + ratioHeight);
            }
        }
        // 根据拉伸比例还原顶点
        cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
    }

    public void initTexture() {
        if (!TextUtils.isEmpty(mBitmapPath)) {
            GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);
            Bitmap bitmap = BitmapFactory.decodeFile(mBitmapPath);
            int texture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
            if (texture == -1) {
//                LogUtils.e(TAG, "生成纹理失败！！！" + bitmap.getWidth() + "," + bitmap.getHeight());
                texture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
                LogUtils.e(TAG, "生成纹理失败:" + texture);
            } else {
                //频繁打印的日志放到-verbose 级别中
                LogUtils.v(TAG, "生成纹理成功！！！");
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            setTextureId(texture);
        }
    }

    /**
     * 设置背景
     */
    public void setBackgroundTexture(Resources resources, int resId) {
        GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);
        setTextureId(OpenGlUtils.loadTexture(BitmapFactory.decodeResource(resources, resId), NO_TEXTURE));
    }

    /**
     * 设置背景
     */
    public void setBackgroundTexture(Bitmap bitmap) {
        GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);//针对部分机型这里删除上纹理后，显示出现黑色
        setTextureId(OpenGlUtils.loadTexture(bitmap, NO_TEXTURE, false));
    }

    public void setBitmapPath(String path, int angle) {
        this.mBitmapPath = path;
        this.bitmapAngle = angle;
        setRotation(bitmapAngle);
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
     * 旋转操作,根据草稿本记录的矩阵值进行赋值
     *
     * @param rotation
     */
    public void setVertexRotation(int rotation) {
        rotation = (360 - rotation) % 360;
        switch (rotation) {
            case ROT_0:
                pos = new float[]{-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f,};

                break;
            case ROT_90:// 根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
                pos = new float[]{1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f};
                break;
            case ROT_180:
                pos = new float[]{1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f};
                break;
            case ROT_270:
                pos = new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
                break;

        }
        mVerBuffer.clear();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
    }

    /**
     * 旋转操作
     *
     * @param rotation
     */
    public void setRotation(int rotation) {
        rotation = rotation < 0 ? rotation + 360 : rotation;
        float[] coord = getCoord();
        switch (rotation) {
            case ROT_0:
                coord = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,};

                break;
            case ROT_90:// 根据内容的旋转，然后再图纸作画，再反转角度重现得到坐标便可！
                // coord = new float[]{
                // 1.0f, 1.0f,
                // 0.0f, 1.0f,
                // 0.0f, 0.0f,
                // 1.0f, 0.0f
                // };
                coord = new float[]{1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};

                break;
            case ROT_180:
                // coord = new float[]{
                // 1.0f, 1.0f,
                // 1.0f, 0.0f,
                // 0.0f, 1.0f,
                // 0.0f, 0.0f,
                // };
                coord = new float[]{1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
                break;
            case ROT_270:
                coord = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
                break;

        }
        mTexBuffer.clear();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    public int getBitmapAngle() {
        return bitmapAngle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isVideoDraw) {
            deleteFrameBuffer(videoFrame, fTextureSize, videoFteture);
        }
        if (videoDataSourcePlay != null) {
            videoDataSourcePlay.onDestroy();
        }
    }

    /**
     * 获取位置
     *
     * @return cube 位置
     */
    public float[] getCube() {
        return Arrays.copyOf(cube, cube.length);
    }


    /**
     * 上下左右靠边缩放，四个方向和四个角落都支持
     *
     * @param orientation T,B,L,R,LT,LB,RT,RB
     * @param coord       自定义x/y的位置 当orientation = L,T,R,B
     */
    public static float[] adjustScaling(ImageOriginFilter imageOriginFilter, int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation, float coord, float scale) {
        imageOriginFilter.cube = adjustScalingWithoutSettingCube(imageOriginFilter, showWidth, showHeight, framewidth, frameheight, orientation, coord, scale);
        imageOriginFilter.mVerBuffer.clear();
        imageOriginFilter.mVerBuffer.put(imageOriginFilter.cube).position(0);
        return imageOriginFilter.cube;
    }

    public static float[] adjustScalingWithoutSettingCube(ImageOriginFilter imageOriginFilter, int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation, float coord, float scale) {

        float ratio1 = (float) showWidth / framewidth;
        float ratio2 = (float) showHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = 2f * imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点


        float[] cube = imageOriginFilter.pos;
        switch (orientation) {
            case LEFT:
                ratioHeight /= 2f;
                cube = new float[]{-1f, coord + ratioHeight / scale,
                        -1f, coord - ratioHeight / scale,
                        -1f + 2f * ratioWidth / scale, coord + ratioHeight / scale,
                        -1f + 2f * ratioWidth / scale, coord - ratioHeight / scale};
                break;
            case RIGHT:
                ratioHeight /= 2f;
                cube = new float[]{1 - 2f * ratioWidth / scale, coord + ratioHeight / scale,
                        1f - 2f * ratioWidth / scale, coord - ratioHeight / scale,
                        1f, coord + ratioHeight / scale,
                        1f, coord - ratioHeight / scale};
                break;
            case BOTTOM:
                cube = new float[]{coord - ratioWidth / scale, 1f,
                        coord - ratioWidth / scale, 1 - ratioHeight / scale,
                        coord + ratioWidth / scale, 1,
                        coord + ratioWidth / scale, 1 - ratioHeight / scale};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
                break;
            case TOP:
                cube = new float[]{coord - ratioWidth / scale, ratioHeight / scale - 1f,
                        coord - ratioWidth / scale, -1f,
                        coord + ratioWidth / scale, ratioHeight / scale - 1,
                        coord + ratioWidth / scale, -1f};
                break;
            case LEFT_TOP:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{-1.0f, ratioHeight / scale - 1.0f,
                        -1.0f, -1.0f,
                        ratioWidth / scale - 1.0f, ratioHeight / scale - 1.0f,
                        ratioWidth / scale - 1.0f, -1.0f};
                break;
            case LEFT_BOTTOM:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{-1.0f, 1.0f, -1.0f, 1.0f - ratioHeight / scale,
                        ratioWidth / scale - 1.0f, 1.0f, ratioWidth / scale - 1.0f, 1.0f - ratioHeight / scale};
                break;
            case RIGHT_TOP:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{1.0f - ratioWidth / scale, ratioHeight / scale - 1.0f, 1.0f - ratioWidth / scale, -1.0f,
                        1.0f, ratioHeight / scale - 1.0f, 1.0f, -1.0f};
                break;
            case RIGHT_BOTTOM:
                ratioHeight = imageHeightNew / (float) showHeight;
                cube = new float[]{1.0f - ratioWidth / scale, 1.0f, 1.0f - ratioWidth / scale, 1.0f - ratioHeight / scale,
                        1.0f, 1.0f, 1.0f, 1.0f - ratioHeight / scale};
                break;
        }
        return cube;
    }

    /**
     * gl中的textureId
     */
    protected int originTexture;

    public void initTexture(Bitmap bitmap) {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);
        originTexture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
        if (originTexture == -1) {
            LogUtils.e(TAG, "生成纹理失败！！！");
            originTexture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
            LogUtils.e(TAG, "生成纹理失败:" + originTexture);
        } else {
            //频繁打印的日志放到-verbose 级别中
            LogUtils.v(TAG, "生成纹理成功！！！");
        }
        setTextureId(originTexture);
        this.bitmap = bitmap;
    }


    public static void init(ImageOriginFilter imageOriginFilter, Bitmap bitmap, int width, int height) {
        if (bitmap == null || bitmap.isRecycled()) {
            LogUtils.e(TAG, "bitmap = null or recyle");
            return;
        }
        if (!bitmap.isRecycled()) {
            imageOriginFilter.initTexture(bitmap);
        }
        imageOriginFilter.create();
        imageOriginFilter.onSizeChanged(width, height);
    }


    public Bitmap getBitmap() {
        return bitmap;
    }


    /**
     * 设置视频类的纹理初始化
     *
     * @param width
     * @param height
     */
    public void initVideoMediaItem(MediaItem mediaItem, int width, int height) {
        isVideoDraw = true;
        this.videoWidth = mediaItem.getWidth();
        this.videoHeight = mediaItem.getHeight();
        bitmapAngle = mediaItem.getRotation();
        this.width = width;
        this.height = height;
        videoDataSourcePlay = new OesFilter();
        videoDataSourcePlay.setTextureId(mediaItem.getVideoTexture());
        videoDataSourcePlay.onCreate();
        mMediaMatrix = mediaItem.getMediaMatrix();
        float[] mediaItemOM = MatrixUtils.getOriginalMatrix();
        if (mediaItem.getRotation() != 0) {
            MatrixUtils.rotate(mediaItemOM, mediaItem.getRotation());
        }
        if (mMediaMatrix != null) {
            matrixAngle = mMediaMatrix.getAngle();
            MatrixUtils.rotate(mediaItemOM, matrixAngle);
            getScaleTranlation(mediaItemOM, mMediaMatrix);
        }
        videoDataSourcePlay.setMatrix(mediaItemOM);
        create();
        setSize(width, height);

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


    public void setVideoWH(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
    }

    /**
     * 移动控件
     *
     * @param ratioDeltaX x轴移动相对自己宽度的多少倍
     * @param ratioDeltaY y轴移动相对自己宽度的多少倍
     */
    public static float[] transitionRatioPosArray(float[] pos, float ratioDeltaX, float ratioDeltaY) {
        float deltaX = (pos[4] - pos[0]) * ratioDeltaX;
        float deltaY = (pos[1] - pos[3]) * ratioDeltaY;
        pos[0] = pos[2] = pos[0] + deltaX;
        pos[4] = pos[6] = pos[4] + deltaX;
        pos[1] = pos[5] = pos[1] + deltaY;
        pos[3] = pos[7] = pos[3] + deltaY;
        return pos;
    }

    /**
     * 移动控件
     *
     * @param deltaX x轴移动opengl世界坐标
     * @param deltaY y轴移动opengl世界坐标
     */
    public static float[] transitionOpenglPosArray(float[] pos, float deltaX, float deltaY) {
        pos[0] = pos[2] = pos[0] + deltaX;
        pos[4] = pos[6] = pos[4] + deltaX;
        pos[1] = pos[5] = pos[1] + deltaY;
        pos[3] = pos[7] = pos[3] + deltaY;
        return pos;
    }

    /**
     * 等比缩放图片展示，只显示窗口区域
     *
     * @param width
     * @param height
     */
    public void adjustImageScalingStretch(int rotation, int width, int height) {
        if (bitmap == null) {
            return;
        }
        int framewidth = bitmap.getWidth();
        int frameheight = bitmap.getHeight();
        if ((rotation == 90 || rotation == 270)) {
            framewidth = bitmap.getHeight();
            frameheight = bitmap.getWidth();
        }
        float ratio1 = (1f * width) / (1f * framewidth);
        float ratio2 = (1f * height) / (1f * frameheight);
        float ratioMax = Math.max(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = (1f * width) / (1f * imageWidthNew);
        float ratioHeight = (1f * height) / (1f * imageHeightNew);
        // 根据拉伸比例还原顶点

        float[] cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
    }


    /**
     * 创建离屏渲染
     *
     * @param width
     * @param height
     * @param fFrame
     * @param fTextureSize
     * @param fTexture
     */
    protected void createFBo(int width, int height, int[] fFrame, int fTextureSize, int[] fTexture) {
        // 创建离屏渲染
        deleteFrameBuffer(fFrame, fTextureSize, fTexture);
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // 创建离屏纹理
        genTextures(width, height, fFrame, fTextureSize, fTexture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height, int[] fFrame, int fTextureSize, int[] fTexture) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
//        if (fTexture[0] == -1 || fTexture[1] == -1) {
//            deleteFrameBuffer(fFrame, fTextureSize, fTexture);
//            GLES20.glGenTextures(fTextureSize, fTexture, 0);
//        }
        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            // GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        }
    }

    protected void deleteFrameBuffer(int[] fFrame, int fTextureSize, int[] fTexture) {
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    public void setVideoTeture(int texture) {
        if (videoDataSourcePlay != null) {
            videoDataSourcePlay.setTextureId(texture);
        }
    }

    public void adjustScalingByCube(float[] cube) {
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
    }
}
