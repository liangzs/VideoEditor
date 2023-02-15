package com.ijoysoft.mediasdk.module.opengl;

import static com.ijoysoft.mediasdk.module.opengl.GlobalDrawer.DrawEndTeture.NORMAL;
import static com.ijoysoft.mediasdk.module.opengl.GlobalDrawer.DrawEndTeture.PARTICLE;
import static com.ijoysoft.mediasdk.module.opengl.GlobalDrawer.DrawEndTeture.TRANSITION;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;

import androidx.annotation.IntDef;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaMatrix;
import com.ijoysoft.mediasdk.module.opengl.filter.AFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.DoodleGroupFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.NoFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles;
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle;
import com.ijoysoft.mediasdk.module.opengl.particle.PAGOneBgParticle;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDrawerManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.transition.PAGLocalTwoTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.PAGTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import org.libpag.PAGPlayer;
import org.libpag.PAGSurface;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * 涂鸦层控制类
 * mediaDrawer做最后的输出设置（文字、贴图、水印），以及视频比例的偏移量
 *
 * @date 2022616
 * 1、添加Apgplayer 用于添加转场特效，文字特效等等，
 * <p>
 * 2、imageplayer,videoplayer原先转场的挂靠也放到这里，区分pag转场和原始的opengl转场，
 * ondrawframe返回actionStatus状态作为转场的开始条件
 * <p>
 * 3、后续的片头片尾亦可以采用这种特效方式挂载(把挂载主题上的片头片尾资源，通过时间轴在这里输出)
 */
public class GlobalDrawer implements IRender {
    private static final String TAG = "MediaDrawer";
    //转场放到最底层
    private final int PAG_TRAN_INDEX = 0;
    //粒子放到最上层
    private final int PAG_PARTICLE_INDEX = 1;
    //涂鸦、水印绘制
    private final DoodleGroupFilter mDoodleFilter;
    //控件的长宽
    private int viewWidth, viewHeight, wholeWidth, wholeHeight;
    private int offsetX, offsetY;
    private boolean isPreviewRotate;
    private AFilter mShowFilter;
    private AFilter pagFilter;
    private MediaMatrix mMediaMatrix;
    //粒子系统
    private ParticleDrawerManager particlesDrawer;
    //边框图片绘制
    private ImageOriginFilter imageDrawer;
    //粒子系统
    private GlobalParticles globalParticles = GlobalParticles.NONE;
    //内部边框
    private InnerBorder innerBorder = InnerBorder.Companion.getNONE();
    //创建缓存对象，离屏buffer
    private int[] fFrame = new int[1];
    // 1、2做转场纹理，3作为pag转场，4做pag粒子，5做临时
    private int fTextureSize = 5;
    private int[] fTexture = new int[fTextureSize];
    private float[] normalOm, flipOm, norMirrorOm, flipMirrorOm;
    //pag播放器
    private PAGPlayer mTranPAGPlayer;
    private PAGPlayer mParticlePAGPlayer;
    private AFilter pagParticleFilter;
    //转场迁移到这里，mediapreview走switch切换的时候进行切换操作
    private TransitionFilter transitionFilter;
    //转场状态
    private @TransitionFilter.PreviewStatus
    int transiStatus;
    //pag 粒子
    private PAGNoBgParticle pagParticle;
    /**
     * 主题下的pag文件
     */
    private PAGNoBgParticle themePagFile;
    private int mediaItemIndex;
    private double progress;
    private int inputTexture;

    //pag参数
    private long timeTran = 0;
    private long timeParticle = 0;
    private long pagTranDuration = 0;
    private long pagParticleDuration = 0;

    //最后渲染层
    private @DrawEndTeture
    int lastTeture;

    public GlobalDrawer() {
        mDoodleFilter = new DoodleGroupFilter();
        mShowFilter = new NoFilter();
        pagFilter = new NoFilter();
        normalOm = MatrixUtils.getOriginalMatrix();
        norMirrorOm = Arrays.copyOf(normalOm, normalOm.length);
        MatrixUtils.flip(norMirrorOm, true, false);//矩阵上下翻转

        flipOm = MatrixUtils.getOriginalMatrix();
        MatrixUtils.flip(flipOm, false, true);//矩阵上下翻转

        flipMirrorOm = MatrixUtils.getOriginalMatrix();
        MatrixUtils.flip(flipMirrorOm, true, true);//矩阵上下翻转

    }

    @Override
    public void onSurfaceCreated() {
        if (imageDrawer != null) {
            imageDrawer.onDestroy();
        }
        if (particlesDrawer != null) {
            particlesDrawer.onDestroy();
        }

        initImageOriginFilter();
        particlesDrawer = globalParticles.getCreator().invoke();
        particlesDrawer.onSurfaceCreated();
        mDoodleFilter.create();
        mShowFilter.create();
        pagFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        viewWidth = width;
        viewHeight = height;
        this.wholeWidth = screenWidth;
        this.wholeHeight = screenHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        if (imageDrawer != null) {
            imageDrawer.onSizeChanged(width, height);
        }
        particlesDrawer.onSurfaceChanged(0, 0, width, height, screenWidth, screenHeight);
        mDoodleFilter.onSurfaceChanged(offsetX, offsetY, viewWidth, viewHeight);
        createFBo(width, height);
        if (mTranPAGPlayer != null) {
            mTranPAGPlayer.release();
            mTranPAGPlayer = null;
            if (transitionFilter instanceof PAGTransitionFilter) {
                checkPagTranPlay(width, height);
            }
        }
        if (mParticlePAGPlayer != null) {
            mParticlePAGPlayer.release();
            mParticlePAGPlayer = null;
        }
        if (pagParticle != null) {
            checkPagParticlePlay(viewWidth, viewHeight);
            pagParticle.onSurfaceChanged(offsetX, offsetY, viewWidth, viewHeight, wholeWidth, wholeHeight);
            mParticlePAGPlayer.setComposition(pagParticle.getPAGFile());
            pagParticle.pagFilePrepare();
        }

    }

    /**
     * 创建pag的pagComposite、surface信息
     *
     * @param mWidth
     * @param mHeight
     */
    private void checkPagTranPlay(int mWidth, int mHeight) {
        if (mTranPAGPlayer == null) {
            mTranPAGPlayer = new PAGPlayer();
            PAGSurface pagSurface = PAGSurface.FromTexture(fTexture[2], mWidth, mHeight);
            mTranPAGPlayer.setSurface(pagSurface);
            pagFilter.setTextureId(fTexture[2]);
        }
    }

    /**
     * 创建pag的pagComposite、surface信息
     *
     * @param mWidth
     * @param mHeight
     */
    private void checkPagParticlePlay(int mWidth, int mHeight) {
        if (mParticlePAGPlayer == null) {
            mParticlePAGPlayer = new PAGPlayer();
            PAGSurface pagSurface = PAGSurface.FromTexture(fTexture[3], mWidth, mHeight);
            mParticlePAGPlayer.setSurface(pagSurface);
            if (pagParticleFilter != null) {
                pagParticleFilter.onDestroy();
            }
            pagParticleFilter = new NoFilter();
            pagParticleFilter.create();
            pagParticleFilter.setTextureId(fTexture[3]);
        }
    }


    public void checkGlError(String s) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(s + ": glError " + error);
        }
    }

    /**
     * 创建FBO
     *
     * @param width
     * @param height
     */
    private void createFBo(int width, int height) {
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        genTextures(width, height);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        if (fTexture[0] == -1) {
            GLES20.glGenTextures(fTextureSize, fTexture, 0);
        }
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
        /* 9.0以上的手机问题，纹理不进行回收了！ */
        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
        for (int i = 0; i < fTextureSize; i++) {
            fTexture[i] = -1;
        }
    }

    public void preViewRotate(boolean flag) {
        isPreviewRotate = flag;
    }

    /**
     * 设置外置帧源,从befilter开始绘制
     * 如果是视频，那么就创建一个TexureSurface，并绑定texture的纹理id 和surface
     * surfaceTexture = new SurfaceTexture(texture[0]);
     * mPreFilter.create();
     * mPreFilter.setTextureId(texture[0]);
     * 然后通过 surfaceTexture.updateTexImage();通知视频有帧过去，mPreFilter承载视频的帧，然后放在fbo中进行绘画，然后
     * surfaceTexture.updateTexImage();
     * EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
     * GLES20.glViewport(0, 0, viewWidth, viewHeight);
     * mPreFilter.draw();
     * EasyGlUtils.unBindFrameBuffer();那么就会把prefilter给绘画在fbo中，
     * 然后需要从fbo中读取缓存的帧的数据，那么久直接用这个fTexture[0]就可以使用这个fbo中的帧了
     * <p>
     * 如果对图片进行操作的话，那么需要把图片的纹理id传到prefilter中，但是不走surfaceTexture中的updateTexImage过程，
     * 是由imageplaer主动去推动render的渲染
     */
    public void setInputTexture(int textureId) {
        inputTexture = textureId;
//        mDoodleFilter.setTextureId(textureId);
//        mShowFilter.setTextureId(textureId);
    }


    /**
     * 视频存储的时候，把最后的那个纹理传入给编码器
     *
     * @return
     */
    public int getShowTexureId() {
        if (mShowFilter != null) {
            return mShowFilter.getTextureId();
        }
        return 0;
    }


    public void setDoodle(List<DoodleItem> list, boolean render) {
        LogUtils.i(TAG, "setDoodle");
        mDoodleFilter.setDoodleItemList(list, render);
    }

//    /**
//     * 垂直翻转纹理显示
//     */
//    public void setFlipVertical(boolean isTrue) {
//        float[] OM = MatrixUtils.getOriginalMatrix();
//        if (isTrue) {
//            boolean mirror = mMediaMatrix != null && !mMediaMatrix.isMirror();
//            MatrixUtils.flip(OM, mirror, isTrue);//矩阵上下翻转
//        } else {
//            boolean mirror = mMediaMatrix != null && mMediaMatrix.isMirror();
//            MatrixUtils.flip(OM, mirror, isTrue);//矩阵上下翻转
//        }
//        mShowFilter.setMatrix(OM);
//    }


    /**
     * 用于转场切换显示
     */
    public void drawBufferFrame() {
        mShowFilter.setMatrix(flipOm);
        //检测是否做了镜像操作
//        if (mMediaMatrix != null && mMediaMatrix.isMirror()) {
//            mShowFilter.setMatrix(flipMirrorOm);
//        }
        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mShowFilter.draw();
        if (imageDrawer != null) {
            imageDrawer.draw();
        }
    }

    /**
     * 根据索引位置去更新贴图
     */
    public void updateDoodle(int index, DoodleItem doodleItem) {
        mDoodleFilter.updateDoodle(index, doodleItem);
    }


    public void setMediaMatrix(MediaMatrix mediaMatrix) {
        mMediaMatrix = mediaMatrix;
    }

    /**
     * 初始化图片绘制层
     */
    private void initImageOriginFilter() {
        imageDrawer = new ImageOriginFilter();
        imageDrawer.setOnSizeChangedListener((w1, h1) -> {
            Bitmap src;
            if (InnerBorder.Companion.getNONE().equals(innerBorder)) {
                Bitmap bitmap = Bitmap.createBitmap(w1, h1, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(Color.TRANSPARENT);
                src = bitmap;
            } else if (innerBorder.getSize() == 0) {
                String absolutePath = innerBorder.requirePath(ConstantMediaSize.ratioType, viewWidth, viewHeight);
                src = OpenGlUtils.getImageFromAssetsFile(MediaSdk.getInstance().getContext(), absolutePath.substring(InnerBorder.assetPrefix.length(), absolutePath.length()));
            } else {
                src = BitmapFactory.decodeFile(innerBorder.requirePath(ConstantMediaSize.ratioType, viewWidth, viewHeight));
            }
            imageDrawer.initTexture(src);
            imageDrawer.create();
        });
    }


    /**
     * 在页面已渲染好的情况下设置边框
     *
     * @param innerBorder 边框
     */
    public void setInnerBorderWhenPlaying(InnerBorder innerBorder) {
        this.innerBorder = innerBorder;
        imageDrawer.getBitmap().recycle();
        imageDrawer.onDestroy();
        imageDrawer = null;
        //初始化
        initImageOriginFilter();
        imageDrawer.onSizeChanged(viewWidth, viewHeight);
        imageDrawer.setVertex(imageDrawer.getCube());

    }

    /**
     * 设置全局粒子
     *
     * @param globalParticles
     */
    public void setGlobalParticles(GlobalParticles globalParticles) {
        this.globalParticles = globalParticles;
        //设置pag素材粒子
        PAGNoBgParticle pagNoBgParticle = null;
        if (globalParticles.getTransitionClass() != null) {
            try {
                Constructor<? extends PAGNoBgParticle> pagInstance = globalParticles.getTransitionClass().getConstructor(boolean.class, String.class);
                pagNoBgParticle = (PAGNoBgParticle) pagInstance.newInstance(globalParticles.isOnline(),
                        globalParticles.getSavePath());
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            themePagFile = null;
            this.pagParticle = pagNoBgParticle;
        }
    }

    /**
     * 设置主题内部pag粒子
     */
    public void setThemeParticlePag(PAGNoBgParticle pag) {
        pagParticle = themePagFile = pag;
        checkPagParticlePlay(viewWidth, viewHeight);
        themePagFile.onSurfaceChanged(offsetX, offsetY, viewWidth, viewHeight, wholeWidth, wholeHeight);
        mParticlePAGPlayer.setComposition(themePagFile.getPAGFile());
        themePagFile.pagFilePrepare();
    }

    public void setInnerBorder(InnerBorder innerBorder) {
        this.innerBorder = innerBorder;
    }

    @Override
    public void onDestroy() {
        if (imageDrawer != null) {
            imageDrawer.onDestroy();
            imageDrawer = null;
        }
        if (particlesDrawer != null) {
            particlesDrawer.onDestroy();
        }
        if (mShowFilter != null) {
            mShowFilter.onDestroy();
        }
        if (pagFilter != null) {
            pagFilter.onDestroy();
        }
        if (mDoodleFilter != null) {
            mDoodleFilter.onDestroy();
        }
        if (transitionFilter != null) {
            transitionFilter.onDestroy();
        }
        if (mParticlePAGPlayer != null) {
            mParticlePAGPlayer.release();
        }
        if (mTranPAGPlayer != null) {
            mTranPAGPlayer.release();
        }
        deleteFrameBuffer();
    }

    @Override
    public void onDrawFrame() {
    }

    /**
     * 把滤镜从主渲染剔除，移到单元的渲染里面。
     * 图片层的滤镜转移到imageplayer里面，视频层的滤镜由videoDrawer接管
     */
    @Override
    public void onDrawFrame(ActionStatus status, int position) {
//        LogUtils.v("MediaDrawer", "transiStatus:" + transiStatus + ",position:" + position);
        if (transiStatus == TransitionFilter.PreviewStatus.NONE) {
            transiStatus = TransitionFilter.PreviewStatus.NORMAL;
            drawNormal(position);
            return;
        }
        if (transiStatus == TransitionFilter.PreviewStatus.PREVIEW) {
            drawNormal(position);
            if (status != ActionStatus.ENTER) {
                transiStatus = TransitionFilter.PreviewStatus.NORMAL;
            }
            return;
        }

        if (mediaItemIndex != 0 && status == ActionStatus.ENTER && transitionFilter != null || status == ActionStatus.AlWAY_TRAN) {
            LogUtils.v("MediaDrawer", "drawTransition...");
            drawTransition(position);
            return;
        }

        drawNormal(position);
    }

    /**
     * 正常渲染方式
     */
    private void drawNormal(int position) {
        lastTeture = NORMAL;
        mDoodleFilter.setTextureId(inputTexture);
        /**
         * 如果有镜像，则只反向原图，doodle不应受影响
         */
        if (mMediaMatrix != null && mMediaMatrix.isMirror()) {
            mShowFilter.setTextureId(inputTexture);
            mShowFilter.setMatrix(flipMirrorOm);
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            GLES20.glViewport(0, 0, viewWidth, viewHeight);
            mShowFilter.draw();
            EasyGlUtils.unBindFrameBuffer();
            mDoodleFilter.setTextureId(fTexture[0]);
        }
        mDoodleFilter.drawGroup(position);
        mShowFilter.setMatrix(flipOm);
        mShowFilter.setTextureId(mDoodleFilter.getOutputTexture());
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mShowFilter.draw();
        if (imageDrawer != null) {
            imageDrawer.draw();
        }
        EasyGlUtils.unBindFrameBuffer();
        GLES20.glViewport(isPreviewRotate ? 0 : offsetX, isPreviewRotate ? 0 : offsetY, viewWidth, viewHeight);
        mShowFilter.setTextureId(fTexture[1]);
        mShowFilter.setMatrix(normalOm);
//        if (mMediaMatrix != null && mMediaMatrix.isMirror()) {
//            mShowFilter.setMatrix(norMirrorOm);
//        }
        mShowFilter.draw();
        drawPagParticle();
    }

    /**
     * 转场时的预览
     * //这里后期核查是否有主题用到这些渲染，因为从主题里面迁移出来的
     * //            if (lastActionRender != null) {
     * //                lastActionRender.drawLast();
     * //            }
     * actionRender.drawFrameIndex();
     * //imageplayer的drawprepare需要在mediadrawer之前
     */
    private void drawTransition(int position) {
        LogUtils.v(TAG, "drawTransition");
        lastTeture = TRANSITION;
        transitionFilter.setPreviewTextureId(fTexture[0]);
        transitionFilter.setTextureId(fTexture[1]);

        /**
         * 字体涂鸦不参与镜像
         */
        mDoodleFilter.setTextureId(inputTexture);
        if (mMediaMatrix != null && mMediaMatrix.isMirror()) {
            mShowFilter.setTextureId(inputTexture);
            mShowFilter.setMatrix(flipMirrorOm);
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[2]);
            GLES20.glViewport(0, 0, viewWidth, viewHeight);
            mShowFilter.draw();
            EasyGlUtils.unBindFrameBuffer();
            mDoodleFilter.setTextureId(fTexture[2]);
        }
        mDoodleFilter.drawGroup(position);
        mShowFilter.setTextureId(mDoodleFilter.getOutputTexture());
        mShowFilter.setMatrix(flipOm);
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mShowFilter.draw();
        if (imageDrawer != null) {
            imageDrawer.draw();
        }
        EasyGlUtils.unBindFrameBuffer();
        //为了后续的粒子，这里不直接绘画，把transitionFilter的绘画内容转到mShowFilter进行绘画
        if (pagParticle != null && pagParticle instanceof PAGOneBgParticle && !(transitionFilter instanceof PAGTransitionFilter)) {
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[2]);
            transitionFilter.setMatrix(normalOm);
            transitionFilter.draw();
            EasyGlUtils.unBindFrameBuffer();
            transitionFilter.setMatrix(flipOm);
            mShowFilter.setTextureId(fTexture[2]);
            GLES20.glViewport(isPreviewRotate ? 0 : offsetX, isPreviewRotate ? 0 : offsetY, viewWidth, viewHeight);
            mShowFilter.draw();
        } else {
            //正常的转场流程
            GLES20.glViewport(isPreviewRotate ? 0 : offsetX, isPreviewRotate ? 0 : offsetY, viewWidth, viewHeight);
//            transitionFilter.setMatrix(flipOm);
            //绘制内部fade渐变过程
            transitionFilter.draw();
        }
        //如果转场是pag转场
        if (transitionFilter instanceof PAGTransitionFilter) {
            GLES20.glViewport(isPreviewRotate ? 0 : offsetX, isPreviewRotate ? 0 : offsetY, viewWidth, viewHeight);
            //同步播放时长为转场时长
//            ((PAGTransitionFilter) transitionFilter).getPAGFile().setDuration(ConstantMediaSize.TRANSITION_DURATION);
            pagTranDuration = ((PAGTransitionFilter) transitionFilter).getPAGFile().duration() / 1000;
            if (position - timeTran < 0) {
                timeTran = position;
            }
            progress = (position - timeTran) % pagTranDuration * 1f / pagTranDuration;
            if ((float) (position - timeTran) / pagTranDuration >= 1f) {
                progress = 1f;
            }
//            LogUtils.v("MediaDrawer", "progress:" + progress + ",position" + position
//                    + ",duration:" + pagTranDuration + ",playTime:" + (position - timeTran));
            if (mTranPAGPlayer != null) {
                mTranPAGPlayer.setProgress(progress);
                mTranPAGPlayer.flush();
            }
            pagFilter.draw();
        }
        drawPagParticle();
    }


    /**
     * 片段切换时数据更新
     *
     * @param mediaItem
     * @param index
     */
    public void drawPrepare(MediaItem mediaItem, int index, long currentPostion) {
        setMediaMatrix(mediaItem.getMediaMatrix());
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
//        if (transiStatus == TransitionFilter.PreviewStatus.PREVIEW) {
//            GLES20.glClearColor(0, 0, 0, 1.0f);
//            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//            transiStatus = TransitionFilter.PreviewStatus.NORMAL;
//            if (transitionFilter instanceof WholeZoomTransitionFilter) {
//                drawBufferFrame();
//            }
//        } else {
//            drawBufferFrame();
//        }
        drawBufferFrame();
        EasyGlUtils.unBindFrameBuffer();
        this.mediaItemIndex = index;
//        if (mediaItem.getTransitionFilter() == null) {
//            transitionFilter = TransitionFactory.initFilters(TransitionType.NONE);
//        }
        //添加转场的回收和赋值
        if (mediaItem.getTransitionFilter() != null) {
            transitionFilter = mediaItem.getTransitionFilter();
            transitionFilter.create();
            transitionFilter.onSizeChanged(viewWidth, viewHeight);
        }
        if (transitionFilter != null) {
            transitionFilter.updateProgress(0);
            transitionFilter.resetProperty();
        }
        //如果是pag转场，则初始化pag资源
        if (transitionFilter instanceof PAGTransitionFilter) {
            checkPagTranPlay(viewWidth, viewHeight);
            if (((PAGTransitionFilter) transitionFilter).getPAGFile() == null) {
                LogUtils.e("MediaDrawer", "pagFile is null!!!");
            } else {
                mediaItem.setPagDuration(((PAGTransitionFilter) transitionFilter).getPAGFile().duration() / 1000);
            }
            ((PAGTransitionFilter) transitionFilter).setWholeWh(wholeWidth, wholeHeight);
            transitionFilter.setPreviewTextureId(fTexture[0]);
            transitionFilter.setTextureId(fTexture[1]);
            timeTran = currentPostion;
            mTranPAGPlayer.setComposition(((PAGTransitionFilter) transitionFilter).getPAGFile());
            ((PAGTransitionFilter) transitionFilter).pagTransitionPrepare();
            //设置两个纹理
            if (PAGLocalTwoTransitionFilter.class.isAssignableFrom(transitionFilter.getClass())) {
                ((PAGTransitionFilter) transitionFilter).pagTransitionDraw();
            }
        }
    }

    /**
     * /**
     * pag的粒子模板放到最上层
     * 更滑粒子系统
     */

    public void setParticlesDrawerWhenPlaying(ParticleDrawerManager particlesDrawer, PAGNoBgParticle pagNoBgParticle) {
        if (this.particlesDrawer != null) {
            this.particlesDrawer.onDestroy();
        }
        if (pagNoBgParticle == null) {
            this.particlesDrawer = particlesDrawer;
            particlesDrawer.onSurfaceCreated();
            particlesDrawer.onSurfaceChanged(0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
            pagParticle = null;
            return;
        }
        //设置pag素材粒子
        this.pagParticle = pagNoBgParticle;
        themePagFile = null;
        checkPagParticlePlay(viewWidth, viewHeight);
        pagParticle.onSurfaceChanged(offsetX, offsetY, viewWidth, viewHeight, wholeWidth, wholeHeight);
        mParticlePAGPlayer.setComposition(pagParticle.getPAGFile());
        pagParticle.pagFilePrepare();
    }

    /**
     * 设置transitionfilter的状态，正常状态，预览（transition页面），过滤transition预览（片段编辑）
     *
     * @param status
     */
    public void setPreviewStatus(@TransitionFilter.PreviewStatus int status) {
        transiStatus = status;
    }


    /**
     * 设置主题的转场
     *
     * @param transitionType
     */
    public void setThemeTransition(TransitionType transitionType) {
        if (themePagFile == pagParticle) {
            pagParticle = null;
        }
        if (transitionType != TransitionType.NONE) {
            transitionFilter = TransitionFactory.initFilters(transitionType);
            transitionFilter.create();
            transitionFilter.onSizeChanged(viewWidth, viewHeight);
            if (transitionFilter instanceof PAGTransitionFilter) {
                checkPagTranPlay(viewWidth, viewHeight);
                if (((PAGTransitionFilter) transitionFilter).getPAGFile() == null) {
                    LogUtils.e("MediaDrawer", "pagFile is null!!!");
                }
                ((PAGTransitionFilter) transitionFilter).setWholeWh(wholeWidth, wholeHeight);
                transitionFilter.setPreviewTextureId(fTexture[0]);
                transitionFilter.setTextureId(fTexture[1]);
                mTranPAGPlayer.setComposition(((PAGTransitionFilter) transitionFilter).getPAGFile());
                ((PAGTransitionFilter) transitionFilter).pagTransitionPrepare();
                //设置两个纹理
                if (PAGLocalTwoTransitionFilter.class.isAssignableFrom(transitionFilter.getClass())) {
                    ((PAGTransitionFilter) transitionFilter).pagTransitionDraw();
                }
            }
        } else {
            transitionFilter = null;
        }
    }

    /**
     * 获取当前层次的最后一层纹理
     *
     * @return
     */
    private int getLastTexure() {
        switch (lastTeture) {
            case NORMAL:
                return mShowFilter.getTextureId();
            case TRANSITION:
                if (transitionFilter instanceof PAGTransitionFilter) {
                    //如果是带有背景的的，则直接取问题，如果没有带纹理的，则离屏渲染一个当前片段和pag转场的绘画
//                    if (transitionFilter instanceof PAGLocalTwoTransitionFilter || transitionFilter instanceof PAGDownTwoTransitionFilter) {
////                        return pagFilter.getTextureId();
//                        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[4]);
//                        pagFilter.draw();
//                        return fTexture[4];
//                    }
                    //转场透明的
                    EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[4]);
                    GLES20.glViewport(0, 0, viewWidth, viewHeight);
                    transitionFilter.setMatrix(normalOm);
                    pagFilter.setMatrix(flipOm);
                    transitionFilter.draw();
                    pagFilter.draw();
                    EasyGlUtils.unBindFrameBuffer();
                    transitionFilter.setMatrix(flipOm);
                    pagFilter.setMatrix(normalOm);
                    return fTexture[4];
                } else {
                    return mShowFilter.getTextureId();
                }
            case PARTICLE:
                return pagParticleFilter.getTextureId();
        }
        return mShowFilter.getTextureId();
    }

    /**
     * 切换主题做的操作
     */
    public void resetIndex() {
        this.mediaItemIndex = 0;
    }

    /**
     * 挂载的pag数据
     */
    private void drawPagParticle() {
        particlesDrawer.setMatrix(flipOm);
        particlesDrawer.onDrawFrame();
        if (pagParticle != null) {
            pagParticleDuration = pagParticle.getPAGFile().duration();
            if (timeParticle == 0) {
                timeParticle = System.currentTimeMillis();
            }
            long playTime = (System.currentTimeMillis() - timeParticle) * 1000;
            float progress = playTime % pagParticleDuration * 1.0f / pagParticleDuration;
            LogUtils.v("MediaDrawer", "progress:" + progress);
            mParticlePAGPlayer.setProgress(progress);
            if (pagParticle instanceof PAGOneBgParticle) {
                ((PAGOneBgParticle) pagParticle).updateBgTeture(getLastTexure());
            }
            GLES20.glViewport(isPreviewRotate ? 0 : offsetX, isPreviewRotate ? 0 : offsetY, viewWidth, viewHeight);
            mParticlePAGPlayer.flush();
            pagParticleFilter.draw();
        }
    }


    @IntDef({NORMAL, TRANSITION, PARTICLE})
    /**
     * 因为pag文件存在遮挡型的背景图，所以需要取最后一张的问题赋值到当前背景
     *目前拓展到正常渲染，转场渲染，粒子渲染pag文件。如果有其他的pag文件加入再进行排序
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawEndTeture {
        int NORMAL = 0;
        int TRANSITION = 1;
        int PARTICLE = 2;
    }


}
