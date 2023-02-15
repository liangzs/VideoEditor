package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;

import java.util.Arrays;
import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 不在区分进场，悬停，出场的三种动画结构，而是由多种快速动作进行动态组合
 * 每个片段时间默认是250ms
 */
public class MultiActionThemeExample extends ImageOriginFilter implements IAction {


    protected float[] projectionMatrix = new float[16];
    protected float[] mViewMatrix = new float[16];
    protected float[] transitionOM = new float[16];

    protected float translate;

    protected int totalTime;//mediaITem的总时间
    protected float[] frames;
    protected int frameIndex;
    protected int frameCount, stayCount, outCount;
    protected int enterTime, outTime, stayTime;
    protected boolean isAlphaDraw;
    protected float alphaValue;
    protected float zView = -3f;
    protected boolean isNoZaxis;
    protected BaseEvaluate currentEvaluete;
    protected Bitmap bitmap;

    protected float stayRotateX, stayRotateY;

    protected GPUImageFilter curFilter;
    protected int originTexture;
    private int width, height;

    public MultiActionThemeExample() {
    }

    public MultiActionThemeExample(int duration, int enterTime, int stayTime, int outTime) {
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        frameIndex = 0;
        this.totalTime = duration;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        this.enterTime = enterTime;
        this.outTime = outTime;
        stayCount = enterTime * ConstantMediaSize.FPS / 1000;
        this.stayTime = stayTime;
        int factStayTime = duration - enterTime - outTime < stayTime ? stayTime : duration - enterTime - outTime;
        outCount = (enterTime + factStayTime) * ConstantMediaSize.FPS / 1000;
        initWidget();
    }

    public MultiActionThemeExample(int duration, int enterTime, int stayTime, int outTime, boolean isNoZaxis) {
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        frameIndex = 0;
        this.isNoZaxis = isNoZaxis;
        this.totalTime = duration;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        this.enterTime = enterTime;
        this.outTime = outTime;
        stayCount = enterTime * ConstantMediaSize.FPS / 1000;
        this.stayTime = stayTime;
        //暂时注释，如果出场的快，应该再悬停状态继续停留
        int factStayTime = duration - enterTime - outTime < stayTime ? stayTime : duration - enterTime - outTime;
        outCount = (enterTime + factStayTime) * ConstantMediaSize.FPS / 1000;
        initWidget();
    }

    public void setStayRoateCoord(float x, float y) {
        this.stayRotateX = x;
        this.stayRotateY = y;
    }


    @Override
    public void prepare() {
        frameIndex = 0;
        Matrix.multiplyMM(mViewMatrix, 0, projectionMatrix, 0, transitionOM, 0);
    }

    protected void NoneStayAction() {
        transitionOM = MatrixUtils.getOriginalMatrix();
        if (!isNoZaxis) {
            Matrix.translateM(transitionOM, 0, 0, 0, zView);
        }
    }

    public void setZView(float zView) {
        this.zView = zView;
    }

    private void drawFrameImpl() {
        drawFramePre();
    }

    /**
     * 精确到帧，跟图片源触发保持一致
     *
     * @return
     */
    public void drawFrame() {
        drawFrameImpl();
    }

    @Override
    public void drawMatrixFrame() {

    }


    /**
     * 根据durationPostion 进行seek操作
     * 根据具体情况，显示enter，还是stay，还是out
     *
     * @return
     */
    public void drawFrame(int durationPostion) {
        drawFrameImpl();
    }

    /**
     * 针对滤镜预览，那么直接预览stay状态的帧就好了
     */
    @Override
    public void drawFramePreview() {
    }


    @Override
    public void seek(int currentDuration) {
    }

    @Override
    public void drawWiget() {

    }

    public void drawLast() {

    }

    @Override
    public void initWidget() {

    }

    @Override
    public void init(Bitmap bitmap, int width, int height) {
        if (bitmap == null || bitmap.isRecycled()) {
            LogUtils.e(TAG, "bitmap = null or recyle");
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            initTexture(bitmap);
        }
        create();
        setSize(width, height);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
    }

    @Override
    public void init(MediaItem mediaItem, int width, int height) {
        this.bitmapAngle = mediaItem.getAfterRotation();
        init(mediaItem, width, height);
        setRotation(bitmapAngle);
    }

    @Override
    public ActionStatus getStatus() {
        return null;
    }


    public void setScale(float x, float y) {
        MatrixUtils.scale(mViewMatrix, x, y);
    }

    public void setProjectionMatrix(float[] matrix) {
        this.projectionMatrix = matrix;
    }


    @Override
    public void doRotaion(int r) {
        super.doRotaion(r);
    }


    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
        super.onSizeChanged(width, height);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int getTexture() {
        return getTextureId();
    }


    @Override
    public int getConor() {
        return 0;
    }

    protected List<Integer> listTexture;
    protected List<Integer> listConors;
    protected List<float[]> listPos;

    @Override
    public void setPreTeture(List<Integer> listTexture, List<Integer> listConors, List<float[]> pos, List<GPUImageFilter> listAfilter) {
        this.listTexture = listTexture;
        this.listConors = listConors;
        this.listPos = pos;
    }

    @Override
    public void setPreTeture(int texture, AFilter aFilter) {

    }

    @Override
    public void setPreTeture(int texture) {

    }


    @Override
    public void setPreTeture(int texture, int frame, int frameTexture) {

    }


    @Override
    public float[] getPos() {
        return pos;
    }

    @Override
    public float getEnterProgress() {
        return (1f * frameIndex) / (1f * stayCount);
    }

    @Override
    public int getEnterTime() {
        return enterTime;
    }

    /**
     * 改变frameIndex进度
     */
    @Override
    public void drawFrameIndex() {
        frameIndex++;
    }

    public void initTexture(Bitmap bitmap) {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        GLES20.glDeleteTextures(1, new int[]{getTextureId()}, 0);
        originTexture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
        if (originTexture == -1) {
            LogUtils.e(TAG, "生成纹理失败！！！" + bitmap.getWidth() + "," + bitmap.getHeight());
            originTexture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
            LogUtils.e(TAG, "生成纹理失败:" + originTexture);
        } else {
            //频繁打印的日志放到-verbose 级别中
            LogUtils.v(TAG, "生成纹理成功！！！" + bitmap.getWidth() + "," + bitmap.getHeight());
        }
        setTextureId(originTexture);
        this.bitmap = bitmap;
    }


    /**
     * float ratioWidth = imageWidthNew / outputWidth; 相当于归一化坐标，然后 ratioWidth / 4,
     * ratioHeight / 8是换算到总的归一化坐标
     *
     * @param showWidth
     * @param showHeight
     * @param framewidth
     * @param frameheight
     * @param centerX
     * @param centerY
     */
    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, float centerX, float centerY, float scalex, float scaley) {
        float ratio1 = (float) showWidth / framewidth;
        float ratio2 = (float) showHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
//        LogUtils.v("adjustScaling", "outputWidth:" + (float) showWidth + ",outputHeight:" + (float) showHeight + ",imageWidthNew:"
//                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点
        cube = new float[]{centerX - ratioWidth / scalex, centerY + ratioHeight / scaley, centerX - ratioWidth / scalex, centerY - ratioHeight / scaley,
                centerX + ratioWidth / scalex, centerY + ratioHeight / scaley, centerX + ratioWidth / scalex, centerY - ratioHeight / scaley};
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }

    /**
     * 靠上下左右边,是上面方法的固定化
     *
     * @param showWidth
     * @param showHeight
     * @param framewidth
     * @param frameheight
     * @param orientation
     */
    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation) {
        return adjustScaling(showWidth, showHeight, framewidth, frameheight, orientation, 4);
    }

    /**
     * 靠上下左右边
     *
     * @param showWidth
     * @param showHeight
     * @param framewidth
     * @param frameheight
     * @param orientation
     */
    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation, float scale) {
        float ratio1 = (float) showWidth / framewidth;
        float ratio2 = (float) showHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "outputWidth:" + (float) showWidth + ",outputHeight:" + (float) showHeight + ",imageWidthNew:"
                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点
        cube = pos;
        switch (orientation) {
            case LEFT_BOTTOM:
                cube = new float[]{-0.5f - ratioWidth / scale, 1f, -0.5f - ratioWidth / scale, 1 - ratioHeight / scale,
                        -0.5f + ratioWidth / scale, 1, -0.5f + ratioWidth / scale, 1 - ratioHeight / scale};
                break;
            case RIGHT_BOTTOM:
                cube = new float[]{0.5f - ratioWidth / scale, 1f, 0.5f - ratioWidth / scale, 1 - ratioHeight / scale,
                        0.5f + ratioWidth / scale, 1, 0.5f + ratioWidth / scale, 1 - ratioHeight / scale};
                break;
            case BOTTOM:
                cube = new float[]{-ratioWidth / scale, 1f, -ratioWidth / scale, 1 - ratioHeight / scale,
                        ratioWidth / scale, 1, ratioWidth / scale, 1 - ratioHeight / scale};
                break;
            case LEFT_TOP:
                cube = new float[]{-0.5f - ratioWidth / scale, ratioHeight / scale - 1f, -0.5f - ratioWidth / scale, -1f,
                        -0.5f + ratioWidth / scale, ratioHeight / scale - 1, -0.5f + ratioWidth / scale, -1f};
                break;
            case RIGHT_TOP:
                cube = new float[]{0.5f - ratioWidth / scale, ratioHeight / scale - 1f, 0.5f - ratioWidth / scale, -1f,
                        0.5f + ratioWidth / scale, ratioHeight / scale - 1, 0.5f + ratioWidth / scale, -1f};
                break;
            case TOP:
                cube = new float[]{-ratioWidth / scale, ratioHeight / scale - 1f, -ratioWidth / scale, -1f,
                        ratioWidth / scale, ratioHeight / scale - 1f, ratioWidth / scale, -1f};
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }


    /**
     * 上下边，靠边缩放,目前只针对上下边
     *
     * @param coord 自定义x的位置
     */
    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation, float coord, float scale) {
        float ratio1 = (float) showWidth / framewidth;
        float ratio2 = (float) showHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点
        cube = pos;
        switch (orientation) {
            case BOTTOM:
                cube = new float[]{coord - ratioWidth / scale, 1f, coord - ratioWidth / scale, 1 - ratioHeight / scale,
                        coord + ratioWidth / scale, 1, coord + ratioWidth / scale, 1 - ratioHeight / scale};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
                break;
            case TOP:
                cube = new float[]{coord - ratioWidth / scale, ratioHeight / scale - 1f, coord - ratioWidth / scale, -1f,
                        coord + ratioWidth / scale, ratioHeight / scale - 1, coord + ratioWidth / scale, -1f};
                break;
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }

    /**
     * 针对一些部件，比如锦旗，需要铺满屏幕的。做等比例的高拉伸
     *
     * @param showWidth
     * @param showHeight
     * @param framewidth
     * @param frameheight
     * @param orientation {@link #adjustScaling(int, int, int, int, AnimateInfo.ORIENTATION, float)} sacle=1的时候的特殊案例
     * @return
     */
    public float[] adjustScalingFixX(int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation) {
        float ratio1 = (float) showWidth / framewidth;
        // 居中后图片显示的大小
        int imageHeightNew = Math.round(frameheight * ratio1);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioHeight = 2f * imageHeightNew / showHeight;//-1到1所以为2
        // 根据拉伸比例还原顶点
        cube = pos;
        switch (orientation) {
            case BOTTOM:
                cube = new float[]{-1f, 1f, -1f, 1 - ratioHeight,
                        1f, 1, 1f, 1 - ratioHeight};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
                break;
            case TOP:
                cube = new float[]{-1f, ratioHeight - 1f, -1f, -1f,
                        1f, ratioHeight - 1, +1f, -1f};
                break;
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }


    @Override
    public void adjustImageScaling(int width, int height) {
        if (bitmap == null) {
            return;
        }
        int framewidth = bitmap.getWidth();
        int frameheight = bitmap.getHeight();
        int rotation = (this.bitmapAngle) % 360;
        if ((rotation == 90 || rotation == 270)) {
            framewidth = bitmap.getHeight();
            frameheight = bitmap.getWidth();
        }
        float ratio1 = (1f * width) / (1f * framewidth);
        float ratio2 = (1f * height) / (1f * frameheight);
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
        LogUtils.v(TAG, "width:" + width + ",height:" + height + ",imageWidthNew:"
                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = (1f * width) / (1f * imageWidthNew);
        float ratioHeight = (1f * height) / (1f * imageHeightNew);
        // 根据拉伸比例还原顶点
        cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
    }

    /**
     * 等比缩放图片展示，只显示窗口区域
     *
     * @param width
     * @param height
     */
    public void adjustImageScalingStretch(int width, int height) {
        if (bitmap == null) {
            return;
        }
        int framewidth = bitmap.getWidth();
        int frameheight = bitmap.getHeight();
        int rotation = (this.bitmapAngle) % 360;
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
        //频繁打印的日志放至-verbose级别中
        LogUtils.v(TAG, "width:" + width + ",height:" + height + ",imageWidthNew:"
                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = (1f * width) / (1f * imageWidthNew);
        float ratioHeight = (1f * height) / (1f * imageHeightNew);
        // 根据拉伸比例还原顶点
        cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);

        setRotation(0);
    }

    protected void wedThemeSpecialDeal() {

    }


    /**
     * 特殊情形下处理projectView
     */
    protected void dealSpecialView(float[] transitionOM) {

    }

    /**
     * 不适合重写，那么就直接赋值
     *
     * @param projectionMatrix
     */
    public void dealProjectView(float[] projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    protected void drawFramePre() {
    }


    @Override
    public void initFilter(MagicFilterType filterType, int width, int height) {
    }

    /**
     * 设置当前片段的滤镜，这里暴露到最上层
     */
    @Override
    public void setFilter(GPUImageFilter filter) {
        if (ObjectUtils.isEmpty(filter)) {
            return;
        }
        if (curFilter != null) {
            curFilter.destroy();
        }
        filter.init();
        filter.onInputSizeChanged(width, height);
        filter.onDisplaySizeChanged(width, height);
        if (cube != null) {
            filter.setVertex(cube);
        }
        filter.setRotation(bitmapAngle);
        curFilter = filter;
    }


    /**
     * 设置滤镜顶点
     *
     * @param cube
     */
    public void setAfilterVertex(float[] cube) {
        if (curFilter != null) {
            curFilter.setVertex(cube);
        }
    }


    public void setNoZaxis(boolean value) {
        this.isNoZaxis = value;
    }

    public void setOriginTexture(int texture) {
        originTexture = texture;
        setTextureId(originTexture);
    }

    public String printOutString() {
        return "";
    }


    @Override
    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public void updateVideoTexture(int texture) {

    }
}
