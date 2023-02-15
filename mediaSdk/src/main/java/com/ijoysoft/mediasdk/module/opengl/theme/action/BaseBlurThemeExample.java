package com.ijoysoft.mediasdk.module.opengl.theme.action;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import androidx.annotation.NonNull;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageBackgroundFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * 管控totalTime时间，进停出三个时间的管控统一处理
 * 如果mediaitem的三个时间>mediaItem时间轴，则多余时间放在停留动画
 * 如果三者时间不够，则受到图片切换是则进行图片切换
 */
public class BaseBlurThemeExample extends ImageBackgroundFilter implements IAction {

    public static final int DEFAULT_ENTER_TIME = 1200;
    public static final int DEFAULT_OUT_TIME = 1200;
    public static final int DEFAULT_STAY_TIME = 1500;

    protected float[] projectionMatrix = new float[16];
    protected float[] mViewMatrix = new float[16];
    protected float[] transitionOM = new float[16];

    protected float translate;

    protected int totalTime;//mediaITem的总时间
    protected float[] frames;
    protected int frameIndex;
    protected int frameCount, stayCount, outCount;
    protected int enterTime, outTime, stayTime;
    protected ActionStatus status;
    protected boolean isAlphaDraw;
    protected float alphaValue;
    protected float zView = -3.0f;

    protected BaseEvaluate stayAction;
    protected BaseEvaluate enterAnimation;
    protected BaseEvaluate outAnimation;
    protected boolean isNoZaxis;

    protected float stayRotateX, stayRotateY;

    //用于视频渲染
    private MediaItem prepareMediaItem;

    public BaseBlurThemeExample() {
    }


    public BaseBlurThemeExample(int duration, int enterTime, int stayTime, int outTime) {
        createThemeExample(duration, enterTime, stayTime, outTime);
    }

    public BaseBlurThemeExample(int duration, int enterTime, int stayTime, int outTime, boolean isNoZaxis) {
        this.isNoZaxis = isNoZaxis;
        createThemeExample(duration, enterTime, stayTime, outTime);
    }

    public void createThemeExample(int duration, int enterTime, int stayTime, int outTime) {
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        status = ActionStatus.ENTER;
        frameIndex = 0;
        this.totalTime = duration;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        this.enterTime = enterTime;
        this.outTime = outTime;
        stayCount = enterTime * ConstantMediaSize.FPS / 1000;
        this.stayTime = stayTime;
        //暂时注释，如果出场的快，应该再悬停状态继续停留
        int factStayTime = Math.max(duration - enterTime - outTime, stayTime);
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
        status = ActionStatus.ENTER;
        if (enterAnimation != null) {
            enterAnimation.prepare();
        }
        if (stayAction != null) {
            stayAction.prepare();
        }
        if (outAnimation != null) {
            outAnimation.prepare();
        }
        transitionOM = enterAnimation.prepare();
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
        switch (status) {
            case ENTER:
                if (enterAnimation == null) {
                    NoneStayAction();
                    break;
                }
                if (enterAnimation != null) {
                    transitionOM = enterAnimation.draw();
                    isAlphaDraw = enterAnimation.checkAlphaDraw();
                    if (isAlphaDraw) {
                        alphaValue = enterAnimation.drawAlpha();
                    }
                }
                break;
            case STAY:
                if (stayAction == null) {
                    NoneStayAction();
                    break;
                }
                transitionOM = stayAction.draw();
                isAlphaDraw = false;
                break;
            case OUT:
                if (outAnimation == null) {
                    NoneStayAction();
                    break;
                }
                if (outAnimation != null) {
                    transitionOM = outAnimation.draw();
                    isAlphaDraw = outAnimation.checkAlphaDraw();
                    if (isAlphaDraw) {
                        alphaValue = outAnimation.drawAlpha();
                    }
                }
                break;
        }


        mViewMatrix = transitionOM;
        if (!isNoZaxis) {
            Matrix.multiplyMM(mViewMatrix, 0, projectionMatrix, 0, transitionOM, 0);
        }
        wedThemeSpecialDeal();
        setMatrix(mViewMatrix);
        if (isAlphaDraw) {
            setAlpha(alphaValue);
        }
        //这里实现滤镜的添加
        draw();
        drawWiget();
        frameIndex++;
        if (frameIndex >= stayCount && status == ActionStatus.ENTER) {
            status = ActionStatus.STAY;
        }
        if (frameIndex >= outCount && status == ActionStatus.STAY) {
            status = ActionStatus.OUT;
        }
    }

    @Override
    public void drawVideoFrame(int videoTeture) {
        if (isVideoDraw) {
            videoDataSourcePlay.setTextureId(videoTeture);
//            drawBackgrondPrepare(prepareMediaItem);
        } else {
            /**
             *  原方案不需要每次都重新叠加渲染的，但是出现拉伸问题，一直搞不清问题所在
             *  所以现在图片的渲染方式也采用这种实时动态方式渲染
             */
            setTextureId(originTexture);
        }
        drawBackgrondPrepare(prepareMediaItem);
    }

    /**
     * 精确到帧，跟图片源触发保持一致
     * 因为视频是GL_TEXTURE_EXTERNAL_OES，需转化
     * *GL_TEXTURE_EXTERNAL_OES 转GL_TEXTURE_2D
     *
     * @return
     */
    public void drawFrame() {
        //如果是视频，则动态设置纹理过程，包括背景信息
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
        status = ActionStatus.STAY;
        frameIndex = stayCount;
    }


    @Override
    public void seek(int currentDuration) {
        frameIndex = (int) Math.ceil(currentDuration * ConstantMediaSize.FPS / 1000f);
        status = ActionStatus.ENTER;
        if (frameIndex >= stayCount) {
            status = ActionStatus.STAY;
        }
        if (frameIndex >= outCount && status == ActionStatus.STAY) {
            status = ActionStatus.OUT;
        }
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
        this.bitmap = bitmap;
        if (bitmap == null) {
            return;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            initTexture(bitmap);
        }
        create();
        onSizeChanged(width, height);
        setRotation(bitmapAngle);

    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
    }

    /**
     * 对gif进行初始化和定位
     *
     * @param gifs   gif及其帧bitmap
     * @param width  屏宽
     * @param height 屏高
     */
    public void initGif(@NonNull List<List<GifDecoder.GifFrame>> gifs, int width, int height) {

    }

    @Override
    public void init(MediaItem mediaItem, int width, int height) {
        bitmapAngle = mediaItem.getAfterRotation();
        prepareMediaItem = mediaItem;
        if (mediaItem.getDynamicMitmaps() != null) {
            LogUtils.i(getClass().getSimpleName(), "this is an action with gifs");
            initGif(mediaItem.getDynamicMitmaps(), width, height);
        }
        if (mediaItem.isImage()) {
            init(mediaItem.getBitmap(), mediaItem.getTempBitmap(), mediaItem.getMimapBitmaps(), width, height);
            //设置模糊背景
            drawBackgrondPrepare(mediaItem);
        } else {
            initVideoMediaItem(mediaItem, width, height);
            init(null, null, mediaItem.getMimapBitmaps(), width, height);
            drawBackgrondPrepare(mediaItem);
        }
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
        super.onSizeChanged(width, height);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (curFilter != null) {
//            curFilter.destroy();
//        }
    }

    @Override
    public int getTexture() {
        return getTextureId();
    }


    @Override
    public int getConor() {
        return 0;
    }

    @Override
    public void setPreTeture(List<Integer> listTexture, List<Integer> listCornor, List<float[]> pos, List<GPUImageFilter> listAfilter) {

    }

    protected List<Integer> listTexture;
    protected List<Integer> listConors;


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
        return new float[0];
    }

    @Override
    public float getEnterProgress() {
        return (1f * frameIndex) / (1f * stayCount);
    }

    @Override
    public int getEnterTime() {
        return enterTime;
    }

    @Override
    public void drawFrameIndex() {
        frameIndex++;
        if (frameIndex >= stayCount && status == ActionStatus.ENTER) {
            status = ActionStatus.STAY;
        }
        if (frameIndex >= outCount && status == ActionStatus.STAY) {
            status = ActionStatus.OUT;
        }
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
        }
        setTextureId(originTexture);
        this.bitmap = bitmap;
    }

    public BaseEvaluate getStayAction() {
        return stayAction;
    }

    public void setStayAction(BaseEvaluate stayAction) {
        this.stayAction = stayAction;
    }

    public BaseEvaluate getEnterAnimation() {
        return enterAnimation;
    }

    public void setEnterAnimation(BaseEvaluate enterAnimation) {
        this.enterAnimation = enterAnimation;
    }

    public BaseEvaluate getOutAnimation() {
        return outAnimation;
    }

    public void setOutAnimation(BaseEvaluate outAnimation) {
        this.outAnimation = outAnimation;
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
        float outputWidth = showWidth;
        float outputHeight = showHeight;
        float ratio1 = outputWidth / framewidth;
        float ratio2 = outputHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "outputWidth:" + outputWidth + ",outputHeight:" + outputHeight + ",imageWidthNew:"
                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / outputWidth;
        float ratioHeight = imageHeightNew / outputHeight;
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
        float outputWidth = showWidth;
        float outputHeight = showHeight;
        float ratio1 = outputWidth / framewidth;
        float ratio2 = outputHeight / frameheight;
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "outputWidth:" + outputWidth + ",outputHeight:" + outputHeight + ",imageWidthNew:"
                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / outputWidth;
        float ratioHeight = imageHeightNew / outputHeight;
        // 根据拉伸比例还原顶点
        cube = pos;
        switch (orientation) {
            case LEFT_BOTTOM:
                cube = new float[]{-0.5f - ratioWidth / scale, 1f, -0.5f - ratioWidth / scale, 1 - ratioHeight / scale,
                        -0.5f + ratioWidth / scale, 1, -0.5f + ratioWidth / scale, 1 - ratioHeight / scale};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
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


    public ActionStatus getStatus() {
        return status;
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
//        filter.setRotation(bitmapAngle);
        curFilter = filter;
    }

    @Override
    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public void updateVideoTexture(int texture) {
        setVideoTeture(texture);
    }

    @Override
    public void setIsPureColor(BGInfo bgInfo) {
        this.mBGInfo = bgInfo;
        checkCreateRender();
    }
}
