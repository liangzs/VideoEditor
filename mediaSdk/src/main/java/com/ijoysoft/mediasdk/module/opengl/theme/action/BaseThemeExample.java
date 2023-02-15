package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
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
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.ZoomByVertex;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.Arrays;
import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 管控totalTime时间，进停出三个时间的管控统一处理
 * 如果mediaitem的三个时间>mediaItem时间轴，则多余时间放在停留动画
 * 如果三者时间不够，则受到图片切换是则进行图片切换
 */
public class BaseThemeExample extends ImageOriginFilter implements IAction {

    /**
     * 进入时间
     */
    public static final int DEFAULT_ENTER_TIME = 1200;

    /**
     * 出场时间
     */
    public static final int DEFAULT_OUT_TIME = 1200;

    /**
     * 保留时间
     */
    public static final int DEFAULT_STAY_TIME = 1800;


    protected float[] projectionMatrix = new float[16];
    protected float[] mViewMatrix = new float[16];
    protected float[] transitionOM = new float[16];

    protected float translate;

    /**
     * mediaITem的总时间
     */
    protected int totalTime;
    protected float[] frames;
    protected int frameIndex;
    protected int frameCount, stayCount, outCount;
    protected int enterTime, outTime, stayTime;
    protected int weightTime;
    protected ActionStatus status;
    protected boolean isAlphaDraw;
    protected float alphaValue;
    protected float zView = -3f;

    protected BaseEvaluate stayAction;
    protected BaseEvaluate enterAnimation;
    protected BaseEvaluate outAnimation;

    /**
     * 没有z轴
     */
    protected boolean isNoZaxis;


    protected float stayRotateX, stayRotateY;

    protected GPUImageFilter curFilter;


    protected int width, height;

    public BaseThemeExample() {
    }

    public BaseThemeExample(int duration, int enterTime, int stayTime, int outTime) {
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        status = ActionStatus.ENTER;
        frameIndex = 0;
        this.totalTime = duration;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        this.enterTime = enterTime;
        this.outTime = outTime;
        stayCount = enterTime * ConstantMediaSize.FPS / 1000;
        this.stayTime = stayTime;
        weightTime = Math.max(duration - enterTime - outTime, stayTime);
        outCount = (enterTime + weightTime) * ConstantMediaSize.FPS / 1000;
        initWidget();
    }

    public BaseThemeExample(int duration, int enterTime, int stayTime, int outTime, boolean isNoZaxis) {
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        status = ActionStatus.ENTER;
        frameIndex = 0;
        this.isNoZaxis = isNoZaxis;
        this.totalTime = duration;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        this.enterTime = enterTime;
        this.outTime = outTime;
        stayCount = enterTime * ConstantMediaSize.FPS / 1000;
        this.stayTime = stayTime;
        //暂时注释，如果出场的快，应该再悬停状态继续停留
        weightTime = Math.max(duration - enterTime - outTime, stayTime);
        outCount = (enterTime + weightTime) * ConstantMediaSize.FPS / 1000;
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
        if (stayCount == 0) {
            status = ActionStatus.STAY;
        }
        if (outCount == 0) {
            status = ActionStatus.OUT;
        }
        switch (status) {
            case ENTER:
                if (enterAnimation == null) {
                    NoneStayAction();
                    break;
                }
                //控件缩放
                if (enterAnimation instanceof ZoomByVertex) {
                    transitionOM = MatrixUtils.getOriginalMatrix();
                    setVertex(enterAnimation.draw());
                    break;
                }
                transitionOM = enterAnimation.draw();
                isAlphaDraw = enterAnimation.checkAlphaDraw();
                if (isAlphaDraw) {
                    alphaValue = enterAnimation.drawAlpha();
                }
                break;
            case STAY:
                if (stayAction == null) {
                    NoneStayAction();
                    break;
                }
                if (stayAction instanceof ZoomByVertex) {
                    transitionOM = MatrixUtils.getOriginalMatrix();
                    setVertex(stayAction.draw());
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
                if (outAnimation instanceof ZoomByVertex) {
                    transitionOM = MatrixUtils.getOriginalMatrix();
                    setVertex(outAnimation.draw());
                    break;
                }
                transitionOM = outAnimation.draw();
                isAlphaDraw = outAnimation.checkAlphaDraw();
                if (isAlphaDraw) {
                    alphaValue = outAnimation.drawAlpha();
                }
                break;
        }

        mViewMatrix = transitionOM;
        if (!isNoZaxis) {
            Matrix.multiplyMM(mViewMatrix, 0, projectionMatrix, 0, transitionOM, 0);
        }
//        LogUtils.i("", "transitionOM:" + Arrays.toString(transitionOM));
        wedThemeSpecialDeal();
        setMatrix(mViewMatrix);
        if (isAlphaDraw) {
            setAlpha(alphaValue);
        }
        //这里实现滤镜的添加
        if (curFilter != null && curFilter.getmFilterType() != MagicFilterType.NONE) {
//            mAfilterGroup.setVertex(getVertex());
            curFilter.setMatrix(mViewMatrix);
            curFilter.onDrawFrame(getTextureId());
        } else {
            draw();
        }
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
            //重新更新纹理数据
            LogUtils.v("render:", "drawVideoFrame:" + videoDataSourcePlay.getTextureId());
            EasyGlUtils.bindFrameTexture(videoFrame[0], videoFteture[0]);
            videoDataSourcePlay.draw();
            EasyGlUtils.unBindFrameBuffer();
            setTextureId(videoFteture[0]);
        }
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
        status = ActionStatus.STAY;
        frameIndex = stayCount;
//        drawFrameImpl();
    }


    /**
     * 进度设置
     *
     * @param currentDuration 当前进度对应的时间
     */
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

    @Override
    public void drawLast() {
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void init(Bitmap bitmap, int width, int height) {
        ImageOriginFilter.init(this, bitmap, width, height);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        if (bitmap == null) {
            return;
        }
        this.bitmap = bitmap;
        init(bitmap, width, height);
    }

    /**
     * 对gif进行初始化和定位
     * init完要自己adjustScaling， 因为是先执行onSizeChange再init的
     *
     * @param gifs   gif及其帧bitmap
     * @param width  屏宽
     * @param height 屏高
     */
    public void initGif(List<List<GifDecoder.GifFrame>> gifs, int width, int height) {

    }

    @Override
    public void init(MediaItem mediaItem, int width, int height) {
        this.bitmapAngle = mediaItem.getAfterRotation();
        setRotation(bitmapAngle);
        if (mediaItem.isImage()) {
            init(mediaItem.getBitmap(), mediaItem.getTempBitmap(), mediaItem.getMimapBitmaps(), width, height);
        } else {
            initVideoMediaItem(mediaItem, width, height);
            init(null, null, mediaItem.getMimapBitmaps(), width, height);
        }
        if (mediaItem.getDynamicMitmaps() != null) {
            LogUtils.i(getClass().getSimpleName(), "this is an action with gifs");
            initGif(mediaItem.getDynamicMitmaps(), width, height);
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
        this.width = width;
        this.height = height;
        super.onSizeChanged(width, height);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (curFilter != null) {
            curFilter.destroy();
        }
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
        if (frameIndex >= stayCount && status == ActionStatus.ENTER) {
            status = ActionStatus.STAY;
        }
        if (frameIndex >= outCount && status == ActionStatus.STAY) {
            status = ActionStatus.OUT;
        }
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
     * 自我缩放
     *
     * @param showWidth  渲染宽度
     * @param showHeight 渲染高度
     * @param centerX    中点x轴相对坐标
     * @param centerY    中点y轴相对坐标
     * @param scalex     x轴缩放的程度（比例的倒数）
     * @param scaley     y轴缩放的程度（比例的倒数）
     */
    public float[] adjustSelfScaling(int showWidth, int showHeight, float centerX, float centerY, float scalex, float scaley) {
        if (bitmap != null) {
            return adjustScaling(showWidth, showHeight, bitmap.getWidth(), bitmap.getHeight(), centerX, centerY, scalex, scaley);
        } else {
            return new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
        }
    }

    /**
     * {@link #adjustScaling(int, int, int, int, float, float, float, float)}
     */
    public float[] adjustSelfScaling(int showWidth, int showHeight, float centerX, float centerY, float scale) {
        return adjustSelfScaling(showWidth, showHeight, centerX, centerY, scale, scale);
    }

    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, float centerX, float centerY, float scale) {
        return adjustScaling(showWidth, showHeight, framewidth, frameheight, centerX, centerY, scale, scale);
    }

    /**
     * float ratioWidth = imageWidthNew / outputWidth; 相当于归一化坐标，然后 ratioWidth / 4,
     * ratioHeight / 8是换算到总的归一化坐标
     *
     * @param showWidth   渲染宽度
     * @param showHeight  渲染高度
     * @param framewidth  图片（控件）宽度
     * @param frameheight 图片（控件）高度
     * @param centerX     中点x轴相对坐标
     * @param centerY     中点y轴相对坐标
     * @param scalex      x轴缩放的程度（比例的倒数）
     * @param scaley      y轴缩放的程度（比例的倒数）
     */
    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, float centerX, float centerY, float scalex, float scaley) {
        //图片宽高相对渲染宽高缩小的程度
        float ratio1 = (float) showWidth / framewidth;
        float ratio2 = (float) showHeight / frameheight;
        //哪一个轴缩放的程度最小，就说明等比例拉伸后该轴长先达到渲染(宽/高)的长度
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        /**
         * @author hayring将该图片等比拉伸至刚才测量的值，即某一方向上的长度先达到与渲染（长/宽）相等
         */
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
     * 边框控件快速拉伸
     */
    public float[] adjustPaddingScaling() {
        // 根据拉伸比例还原顶点
        cube = new float[]{
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f,
        };
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }


    /**
     * 替换方案为
     *
     * @see BaseThemeExample#adjustScaling(int, int, int, int, AnimateInfo.ORIENTATION, float, float)
     * @deprecated 该适配方法只固定用于上下边的-0.5,0,0.5三个固定位置
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
     * 上下左右靠边缩放，四个方向和四个角落都支持
     *
     * @param orientation T,B,L,R,LT,LB,RT,RB
     * @param coord       自定义x/y的位置 当orientation = L,T,R,B
     */
    public float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation, float coord, float scale) {
        return ImageOriginFilter.adjustScaling(this, showWidth, showHeight, framewidth, frameheight, orientation, coord, scale);
    }

    /**
     * 上下左右靠边缩放，四个方向和四个角落都支持
     *
     * @param orientation T,B,L,R,LT,LB,RT,RB
     * @param coord       自定义x/y的位置 当orientation = L,T,R,B
     */
    public float[] adjustScalingWithoutSettingCube(int showWidth, int showHeight, int framewidth, int frameheight, AnimateInfo.ORIENTATION orientation, float coord, float scale) {
        return ImageOriginFilter.adjustScalingWithoutSettingCube(this, showWidth, showHeight, framewidth, frameheight, orientation, coord, scale);
    }

    public float[] adjustScalingFixX(int showWidth, int showHeight, int framewidth,
                                     int frameheight, AnimateInfo.ORIENTATION orientation) {
        return adjustScalingFixX(showWidth, showHeight, framewidth, frameheight, orientation, 1f);
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
    public float[] adjustScalingFixX(int showWidth, int showHeight, int framewidth,
                                     int frameheight, AnimateInfo.ORIENTATION orientation, float scale) {
        // 根据拉伸比例还原顶点
        cube = pos;
        //根据图片本身选取最长边进行适配
        float maxRatio = Math.max((float) framewidth / (float) showWidth, (float) frameheight / (float) showHeight);
        if (orientation == AnimateInfo.ORIENTATION.BOTTOM || orientation == AnimateInfo.ORIENTATION.TOP) {
            maxRatio = (float) showWidth / framewidth;
        }
        int imageHeightNew = Math.round(frameheight * maxRatio);
        int imageWidthNew = Math.round(framewidth * maxRatio);
        float ratioHeight = scale * 2f * imageHeightNew / showHeight;//-1到1所以为2
        float ratioWidth = scale * 2f * imageWidthNew / showWidth;//-1到1所以为2
        switch (orientation) {
            case BOTTOM:
                cube = new float[]{-1f, 1f,
                        -1f, 1 - ratioHeight,
                        1f, 1,
                        1f, 1 - ratioHeight};
                //频繁打印的日志放至-verbose级别中
                LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
                break;
            case TOP:
                cube = new float[]{-1f, ratioHeight - 1f,
                        -1f, -1f,
                        1f, ratioHeight - 1,
                        +1f, -1f};
                break;
            case RIGHT_TOP:
                cube = new float[]{-1f + ratioWidth, ratioHeight - 1f,
                        ratioWidth - 1f, -1f,
                        1f, ratioHeight - 1,
                        +1f, -1f};
                LogUtils.v("BaseThemeExample", Arrays.toString(cube));
                break;
            case LEFT_BOTTOM:
                //根据图片本身选取最长边进行适配
                cube = new float[]{-1f, 1f,
                        -1f, 1f - ratioHeight,
                        -1f + ratioWidth, 1f,
                        -1f + ratioWidth, 1f - ratioHeight};
                LogUtils.v("BaseThemeExample", Arrays.toString(cube));
                break;
        }
        mVerBuffer.clear();
        mVerBuffer.put(cube).position(0);
        return cube;
    }


//    @Override
//    public void adjustImageScaling(int width, int height) {
//        if (bitmap == null) {
//            return;
//        }
//        int framewidth = bitmap.getWidth();
//        int frameheight = bitmap.getHeight();
//        int rotation = (this.bitmapAngle) % 360;
//        if ((rotation == 90 || rotation == 270)) {
//            framewidth = bitmap.getHeight();
//            frameheight = bitmap.getWidth();
//        }
//        float ratio1 = (1f * width) / (1f * framewidth);
//        float ratio2 = (1f * height) / (1f * frameheight);
//        float ratioMax = Math.min(ratio1, ratio2);
//        // 居中后图片显示的大小
//        int imageWidthNew = Math.round(framewidth * ratioMax);
//        int imageHeightNew = Math.round(frameheight * ratioMax);
//
//        //频繁打印的日志放至-verbose级别中
//        LogUtils.v(TAG, "width:" + width + ",height:" + height + ",imageWidthNew:"
//                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
//        // 图片被拉伸的比例,以及显示比例时的偏移量
//        float ratioWidth = (1f * width) / (1f * imageWidthNew);
//        float ratioHeight = (1f * height) / (1f * imageHeightNew);
//        // 根据拉伸比例还原顶点
//        cube = new float[]{pos[0] / ratioWidth, pos[1] / ratioHeight, pos[2] / ratioWidth, pos[3] / ratioHeight,
//                pos[4] / ratioWidth, pos[5] / ratioHeight, pos[6] / ratioWidth, pos[7] / ratioHeight,};
//        mVerBuffer.clear();
//        mVerBuffer.put(cube).position(0);
//    }

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

    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public void updateVideoTexture(int texture) {
        setVideoTeture(texture);
    }

    public int getOutTime() {
        return outTime;
    }

    public int getStayTime() {
        return stayTime;
    }

    public float getzView() {
        return zView;
    }

    public String printOutString() {
        return "";
    }

    /**
     * 修复使控件正好在屏幕边缘之外
     * 在 {@link BaseBlurThemeTemplate#init(Bitmap, Bitmap, List, int, int) 初始化方法} 后获取到空间的长和宽，对控件的动画进行修复
     */
    public void fixEnterOutAnimationStartEndAtScreenEdge() {
        if (enterAnimation != null) {
            if (widgetRelayed == null) {
                enterAnimation.fixEnterOutAnimationStartEndAtScreenEdge(cube);
            } else {
                //根据依赖的控件位置进行修复
                enterAnimation.fixEnterOutAnimationStartEndAtScreenEdge(widgetRelayed.getCube());
            }
        }
        if (outAnimation != null) {
            if (widgetRelayed == null) {
                outAnimation.fixEnterOutAnimationStartEndAtScreenEdge(cube);
            } else {
                //根据依赖的控件位置进行修复
                outAnimation.fixEnterOutAnimationStartEndAtScreenEdge(widgetRelayed.getCube());
            }
        }
    }


    /**
     * 合为一体的目标控件
     */
    private BaseThemeExample widgetRelayed = null;


    /**
     * 与某一个控件合为一体
     *
     * @param widget 要组合的控件
     */
    public void animateWith(BaseThemeExample widget) {
        widgetRelayed = widget;
        setEnterAnimation(widget.getEnterAnimation().clone());
        setOutAnimation(widget.getOutAnimation().clone());
    }

    public void setWidgetRelayed(BaseThemeExample widgetRelayed) {
        this.widgetRelayed = widgetRelayed;
    }

    public void reset() {
        status = ActionStatus.ENTER;
        frameIndex = 0;
        setAlpha(1.0f);
        if (getEnterAnimation() != null) {
            getEnterAnimation().reset();
        }
        if (getOutAnimation() != null) {
            getOutAnimation().reset();
        }
        if (getStayAction() != null) {
            getStayAction().reset();
        }
    }

    @Override
    public void setVertex(float[] vertex) {
        super.setVertex(vertex);
        cube = vertex;
    }


    /**
     * 等比缩放图片展示，只显示窗口区域
     *
     * @param width
     * @param height
     */
    public void adjustImageScalingCrop(int width, int height) {
        int originW;
        int originH;
        if (bitmap == null) {
            return;
        }
        originW = bitmap.getWidth();
        originH = bitmap.getHeight();
        int rotation = (this.bitmapAngle) % 360;
        if ((rotation == 90 || rotation == 270)) {
            int temp = originW;
            originW = originH;
            originH = temp;
        }
        float ratio1 = (1f * width) / (1f * originW);
        float ratio2 = (1f * height) / (1f * originH);
        float ratioMax = Math.max(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(originW * ratioMax);
        int imageHeightNew = Math.round(originH * ratioMax);
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

    /**
     * 等比缩放图片展示，只显示窗口区域
     *
     * @param width
     * @param height
     */
    public void adjustImageScalingInside(int width, int height) {
        int originW;
        int originH;
        if (bitmap == null) {
            return;
        }
        originW = bitmap.getWidth();
        originH = bitmap.getHeight();
        int rotation = (this.bitmapAngle) % 360;
        if ((rotation == 90 || rotation == 270)) {
            int temp = originW;
            originW = originH;
            originH = temp;
        }
        float ratio1 = (1f * width) / (1f * originW);
        float ratio2 = (1f * height) / (1f * originH);
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        int imageWidthNew = Math.round(originW * ratioMax);
        int imageHeightNew = Math.round(originH * ratioMax);
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



    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
