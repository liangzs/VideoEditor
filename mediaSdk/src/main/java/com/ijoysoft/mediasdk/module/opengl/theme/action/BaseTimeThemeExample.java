package com.ijoysoft.mediasdk.module.opengl.theme.action;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.OesFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.IBaseTimeCusFragmeng;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EmbeddedFilter;

import java.util.List;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 只关注当前动画值变化，当前baseElaluate是啥它渲啥
 * 旋转的时候，支持动感模糊，这样切换片段的显得自然
 */
public class BaseTimeThemeExample extends ImageOriginFilter implements IAction {
    public static final String FRAGMENT_HEAD = "#version 100\n" +
            "#define PITwo 6.28318530718//2pi\n" +
            "#define PI 3.141592653589793\n" +
            "precision mediump float;\n" +
            "varying vec2 textureCoordinate;\n" +
            "varying float vAlpha;\n" +
            "uniform sampler2D vTexture;\n" +
            "uniform int blurType;\n" +
            "uniform vec2 moveStrength;   //0-10 半径\n" +
            "uniform float radioStrength;//0-0.1f  \n";
    public static final String FRAGMENT_MAIN = "void main(){ \n" +
            " vec4 resultColor;\n" +
            " vec2 coord=textureCoordinate;\n";
    public static final String FRAGMENT_BODY =
            "  if(blurType==0){\n" +
                    "\t  resultColor= texture2D(vTexture, coord);\n" +
                    " }else if(blurType==1){\n" +
                    "  float Directions = 10.0;\n" +
                    "  float Quality = 3.0; \n" +
                    "  vec4 Color = texture2D(vTexture, coord);\n" +
                    "  // Blur calculations\n" +
                    "  for( float d=0.0; d<PITwo; d+=PITwo/Directions)\n" +
                    "  {\n" +
                    "  Color += texture2D( vTexture, coord+vec2(cos(d),sin(d))*moveStrength);\n" +
                    "  }\n" +
                    "  Color /= Quality * Directions - 20.0;\n" +
                    "  resultColor =  Color;\n" +
                    " }else if(blurType==2){\n" +
                    "  vec2 center = vec2(.5, .5);\n" +
                    "  vec3 color = vec3(0.0);\n" +
                    "  float total = 0.0;\n" +
                    "  vec2 toCenter = center - coord;\n" +
                    "  for (float t = 0.0; t <= 20.0; t++) {\n" +
                    "   float percent = (t) / 20.0;\n" +
                    "   float weight = 1.0 * (percent - percent * percent);\n" +
                    "   color += texture2D(vTexture, coord + toCenter * percent * radioStrength).rgb * weight;\n" +
                    "   total += weight;\n" +
                    "  }\n" +
                    "  resultColor = vec4(color / total, 1.0);\n" +
                    " }\n";


    public static final String FRAGMENT_END_BLACKET = "gl_FragColor=resultColor*vAlpha;\n" +
            "}";
    private String fragmentStr;
    protected float[] projectionMatrix = new float[16];
    protected float[] mViewMatrix = new float[16];
    protected float[] transitionOM = new float[16];
    protected float translate;
    protected int totalTime;//mediaITem的总时间
    protected float[] frames;
    protected int frameIndex;
    protected int frameCount;
    protected float alphaValue;
    protected float zView = -3f;

    protected boolean isNoZaxis;
    protected Bitmap bitmap;
//    protected int rotation;

    protected float stayRotateX, stayRotateY;
    protected GPUImageFilter curFilter;
    protected int originTexture;
    protected int width, height;
    private Object object = new Object();

    private List<BaseEvaluate> evaluates;
    private BaseEvaluate currentAnimation;
    //播放结束跳转到起点
    private boolean isDrawPreview;

    /**
     * 一张图片中evaluates的下标
     */
    private int currentIndex;
    //    //模糊半径
    private int blurTypeLocation;
    //动感模糊
    private int moveBlurLocation;
    //径向模糊
    private int ratioBlurLocation;

    private IBaseTimeCusFragmeng baseTimeCusFragmeng;

    //当前片段是否为video渲染
    protected boolean isVideoRender;

    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                fragmentStr);
        blurTypeLocation = GLES20.glGetUniformLocation(mProgram, "blurType");
        moveBlurLocation = GLES20.glGetUniformLocation(mProgram, "moveStrength");
        ratioBlurLocation = GLES20.glGetUniformLocation(mProgram, "radioStrength");
        if (baseTimeCusFragmeng != null) {
            baseTimeCusFragmeng.initLocation(mProgram);
        }
    }

    public BaseTimeThemeExample(int duration) {
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        frameIndex = 0;
        //LogUtils.v(getClass().getSimpleName() + "Constructor", "frameIndex reset");
        this.totalTime = duration;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        initWidget();
        fragmentStr = FRAGMENT_HEAD + FRAGMENT_MAIN + FRAGMENT_BODY + FRAGMENT_END_BLACKET;
    }

    public BaseTimeThemeExample(int duration, boolean isNoZaxis) {
        this(duration);
        this.isNoZaxis = isNoZaxis;
    }

    public BaseTimeThemeExample() {
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        this.isNoZaxis = true;
        fragmentStr = FRAGMENT_HEAD + FRAGMENT_MAIN + FRAGMENT_BODY + FRAGMENT_END_BLACKET;
    }

    public void setFragment(String fragmentStr) {
        this.fragmentStr = fragmentStr;
    }

    public void setStayRoateCoord(float x, float y) {
        this.stayRotateX = x;
        this.stayRotateY = y;
    }

    /**
     * 针对滤镜预览，那么直接预览stay状态的帧就好了
     */
    @Override
    public void drawFramePreview() {
        preViewStayEvaluates();
    }

    /**
     * 绘制某一时刻的预览
     *
     * @param duration
     */
    public void drawCertainTimePreview(int duration) {
        if (ObjectUtils.isEmpty(evaluates)) {
            return;
        }
        //当前帧数
        frameIndex = duration * ConstantMediaSize.FPS / 1000;
        drawCertainFramePreview(frameIndex);
    }

    /**
     * 绘制某一帧的预览
     *
     * @param frameIndex 第几帧
     */
    public void drawCertainFramePreview(int frameIndex) {

        if (frameIndex == 0) {
            currentIndex = 0;
        } else {
            for (currentIndex = 0; currentIndex < evaluates.size() && frameIndex > 0; currentIndex++) {
                if (evaluates.get(currentIndex).frameCount <= frameIndex) {
                    frameIndex -= evaluates.get(currentIndex).frameCount;
                }
            }
            currentIndex--;
        }
        this.currentAnimation = evaluates.get(currentIndex);
        this.frameCount = currentAnimation.frameCount;

    }

    @Override
    public void prepare() {
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

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniform1i(blurTypeLocation, currentAnimation.getBlurType());
        GLES20.glUniform1f(ratioBlurLocation, currentAnimation.getBlurRange());
        GLES20.glUniform2f(moveBlurLocation, currentAnimation.getBlurRange() / (float) width, currentAnimation.getBlurRange() / (float) height);
        if (baseTimeCusFragmeng != null) {
            baseTimeCusFragmeng.drawLocation(currentAnimation);
        }
    }

    private void drawFrameImpl() {
        if (currentAnimation == null) {
            return;
        }
        synchronized (object) {
            //嵌入式Filter绘制
            if (currentAnimation instanceof EmbeddedFilterEvaluate) {
                if (this instanceof BaseTimeBlurThemeExample) {
                    BaseTimeBlurThemeExample me = (BaseTimeBlurThemeExample) this;
                    me.drawBlur();
                }

                EmbeddedFilter filter = ((EmbeddedFilterEvaluate) currentAnimation).getEmbeddedFilter();
                float[] cube = getCube();
                filter.setScale(-cube[0], cube[1]);
                ((EmbeddedFilterEvaluate) currentAnimation).drawEmbeddedFilter(getTextureId());
            } else {
                transitionOM = currentAnimation.draw();
                mViewMatrix = transitionOM;
                if (!isNoZaxis) {
                    Matrix.multiplyMM(mViewMatrix, 0, projectionMatrix, 0, transitionOM, 0);
                }
                setMatrix(mViewMatrix);
                if (currentAnimation.checkAlphaDraw()) {
                    alphaValue = currentAnimation.drawAlpha();
                    setAlpha(alphaValue);
                } else {
                    setAlpha(1);
                }
                //这里实现滤镜的添加
                if (curFilter != null && curFilter.getmFilterType() != MagicFilterType.NONE) {
                    if (this instanceof BaseTimeBlurThemeExample) {
                        BaseTimeBlurThemeExample me = (BaseTimeBlurThemeExample) this;
                        me.drawBlur();
                    }
                    curFilter.setMatrix(mViewMatrix);
                    curFilter.onDrawFrame(getTextureId());
                } else {
                    draw();
                }
            }
            //绘制粒子系统或其他效果
            //TODO 有bug
//            if (currentAnimation instanceof TimeThemeEvaluate) {
//                ((TimeThemeEvaluate) currentAnimation).onRenderDrawFrame();
//            }
//            Log.v(getClass().getSimpleName(), "frameIndex: " + frameIndex + "  frameCount: " + frameCount + " currentIndex: " + currentIndex);
            frameIndex++;
            checkNextEvaluate();
        }
    }

    /**
     * 精确到帧，跟图片源触发保持一致
     *
     * @return
     */
    @Override
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


    @Override
    public void seek(int currentDuration) {
        frameIndex = (int) Math.ceil(currentDuration * ConstantMediaSize.FPS / 1000f);
//        LogUtils.v(getClass().getSimpleName() + "seek", "frameIndex:" + frameIndex);
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
        if (mediaItem.isImage()) {
            this.bitmapAngle = mediaItem.getAfterRotation();
            init(mediaItem.getBitmap(), mediaItem.getTempBitmap(), mediaItem.getMimapBitmaps(), width, height);
        } else {
            initVideoMediaItem(mediaItem, width, height);
            this.bitmapAngle = mediaItem.getRotation();
        }
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
        return 0;
    }

    @Override
    public int getEnterTime() {
        return 0;
    }


    /**
     * 改变frameIndex进度
     */
    @Override
    public void drawFrameIndex() {
        frameIndex++;
//        LogUtils.v(getClass().getSimpleName() + " drawFrameIndex", "frameIndex: " + frameIndex + "  frameCount: " + frameCount + " currentIndex: " + currentIndex);
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
     * 绑定纹理通道为1，不走OpenGlUtils.loadTexture 方案
     *
     * @param bitmap
     */
    @Override
    public void setBackgroundTexture(Bitmap bitmap) {
        this.bitmap = bitmap;
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);// 开始深度测试
        GLES20.glEnable(GLES20.GL_BLEND);// 开启Blend
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA); // 设置BlendFunc，第一个参数为源混合因子，第二个参数为目的混合因子
        GLES20.glDeleteTextures(1, new int[]{originTexture}, 0);
        originTexture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE, true);
        setTextureId(originTexture);
    }

    public void drawPrepare(MediaItem mediaItem) {
        if (mediaItem.isImage()) {
            bitmapAngle = mediaItem.getAfterRotation();
            isVideoRender = false;
            setBackgroundTexture(mediaItem.getBitmap());
        } else {
            bitmapAngle = mediaItem.getRotation();
            isVideoRender = isVideoDraw = true;
            this.videoWidth = mediaItem.getWidth();
            this.videoHeight = mediaItem.getHeight();
            if (videoDataSourcePlay == null) {
                videoDataSourcePlay = new OesFilter();
                videoDataSourcePlay.onCreate();
            }
            mMediaMatrix = mediaItem.getMediaMatrix();
            if (mMediaMatrix != null) {
                float[] mediaItemOM = MatrixUtils.getOriginalMatrix();
                matrixAngle = mMediaMatrix.getAngle();
                MatrixUtils.rotate(mediaItemOM, matrixAngle);
                getScaleTranlation(mediaItemOM, mMediaMatrix);
                videoDataSourcePlay.setMatrix(mediaItemOM);
            } else {
                videoDataSourcePlay.setMatrix(MatrixUtils.getOriginalMatrix());
            }
            videoDataSourcePlay.setTextureId(mediaItem.getVideoTexture());
            if (isVideoDraw) {
                createFBo(width, height, videoFrame, 1, videoFteture);
            }

        }
    }


    /**
     * 这是预渲染，就是在renderbus帧缓存之外的提前渲染
     *
     * @param videoTeture
     */
    @Override
    public void drawVideoFrame(int videoTeture) {
        if (isVideoRender) {
            videoDataSourcePlay.setTextureId(videoTeture);
            drawBackgrondPrepare();
        } else {
            LogUtils.v("BaseTimeBlurThemeExample", "originTexture:" + originTexture);
            setTextureId(originTexture);
        }
        GLES20.glViewport(0, 0, width, height);//mediadrawer的原因，导致这里背景的预渲染需要重置窗口
    }


    /**
     * 这里一个很重要的点就是经过    setTextureId(fTexture[1]);之后是整个glview是一张纹理了
     */
    public void drawBackgrondPrepare() {
        if (originTexture != 0) {
            setTextureId(originTexture);
        }
        if (isVideoRender) {
            if (videoDataSourcePlay.getTextureId() == -1) {
                return;
            }
            EasyGlUtils.bindFrameTexture(videoFrame[0], videoFteture[0]);
            videoDataSourcePlay.draw();
            EasyGlUtils.unBindFrameBuffer();
            setTextureId(videoFteture[0]);
        }
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
        if (isVideoRender) {
            originW = videoWidth;
            originH = videoHeight;
        } else {
            if (bitmap == null) {
                return;
            }
            originW = bitmap.getWidth();
            originH = bitmap.getHeight();
        }
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

        setRotation(bitmapAngle);
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
        if (isVideoRender) {
            originW = videoWidth;
            originH = videoHeight;
        } else {
            if (bitmap == null) {
                return;
            }
            originW = bitmap.getWidth();
            originH = bitmap.getHeight();
        }
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

        setRotation(bitmapAngle);
    }

    protected void wedThemeSpecialDeal() {

    }


    /**
     * 特殊情形下处理projectView
     */
    protected void dealSpecialView(float[] transitionOM) {

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

    /**
     * 设置当前动画
     */
    public void setEvaluates(List<BaseEvaluate> evaluates) {
        synchronized (object) {
            if (ObjectUtils.isEmpty(evaluates)) {
                return;
            }
            this.evaluates = evaluates;
            currentIndex = 0;
            frameIndex = 0;
//            LogUtils.v(getClass().getSimpleName() + " setEvaluates", "frameIndex: " + frameIndex);
            this.currentAnimation = evaluates.get(currentIndex);
            this.frameCount = currentAnimation.frameCount;
        }
    }

    /**
     * 取当前动画的中间值状态，即悬停状态
     */
    public void preViewStayEvaluates() {
        if (ObjectUtils.isEmpty(evaluates)) {
            return;
        }
//        int size = evaluates.size();
//        currentIndex = size / 2;
//        frameIndex = 0;
//        this.currentAnimation = evaluates.get(currentIndex);
//        this.frameCount = currentAnimation.frameCount;
        int totalFrame = 0;
        for (BaseEvaluate evaluate : evaluates) {
            totalFrame += evaluate.frameCount;
        }
        drawCertainFramePreview(totalFrame / 2);
    }

    private void checkNextEvaluate() {
//            LogUtils.v("bug check", String.format("currentAnimationIndex: %d", currentIndex));
        if (frameIndex >= frameCount) {
            frameIndex = 0;
//            Log.v(getClass().getSimpleName() + "checkNextEvaluate", "frameIndex: " + frameIndex);
            currentIndex++;
            if (currentIndex >= evaluates.size()) {
                Log.v(getClass().getSimpleName(), "No more evaluate");
                return;
            }
            currentAnimation = evaluates.get(currentIndex);
//            LogUtils.v("bug check", String.format("currentAnimationIndex: %d", currentIndex));
            this.frameCount = currentAnimation.frameCount;
            if (currentIndex > 0 && !isDrawPreview) {
                currentAnimation.composeEvaluate(evaluates.get(currentIndex - 1));
            }
            isDrawPreview = false;
        }
    }

    @Override
    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public void updateVideoTexture(int texture) {

    }

    public void setBaseTimeCusFragmeng(IBaseTimeCusFragmeng baseTimeCusFragmeng) {
        this.baseTimeCusFragmeng = baseTimeCusFragmeng;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setVideoDraw(boolean isVideoDraw) {
        this.isVideoDraw = isVideoDraw;
    }
}
