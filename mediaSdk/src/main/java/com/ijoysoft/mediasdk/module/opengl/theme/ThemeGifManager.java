package com.ijoysoft.mediasdk.module.opengl.theme;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.EasyGlUtils;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.BGInfo;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifTransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFactory;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;
import com.ijoysoft.mediasdk.module.opengl.transition.WholeZoomTransitionFilter;

/**
 * 采用Gig素材转场，兼容gif转场
 * 图像的位移，检测是否离屏渲染中是否嵌套了离屏渲染
 */
public class ThemeGifManager extends BaseThemeManager {
    protected TransitionFilter transitionFilter;
    private int width, height;
    private int screenWidth, screenHeight;
    private int[] fFrame = new int[1];
    // 两张纹理
    private int fTextureSize = 2;
    private int[] fTexture = new int[fTextureSize];
    protected boolean isPreviewTransition;
    // 是否为纯色背景
    private BGInfo mBGInfo;

    private GifTransitionFilter gifFilter;//利用gif做转场，记得从gif的0开始
    private IAction lastActionRender;

    /**
     * 本地gif
     *
     * @param gifRes
     */
    public ThemeGifManager(int gifRes) {
        createGifFilter("", gifRes);
    }

    /**
     * 下载gif
     *
     * @param gifPath
     */
    public ThemeGifManager(String gifPath) {
        createGifFilter(gifPath, 0);
    }

    private void createGifFilter(String gifPath, int gifRes) {
        gifFilter = new GifTransitionFilter();
        gifFilter.initGif(gifPath, gifRes);
        transitionFilter = TransitionFactory.initFilters(TransitionType.FADE_IN);
    }

    public void onDestroyFragment() {
    }

    public void onDestroy() {
        if (actionRender != null) {
            actionRender.onDestroy();
        }
        if (transitionFilter != null) {
            transitionFilter.onDestroy();
        }
        if (gifFilter != null) {
            gifFilter.onDestroy();
        }
        deleteFrameBuffer();
    }

//    @Override
//    public void previewTransitionFilter(boolean isPreview) {
//        this.isPreviewTransition = isPreview;
//    }

    public void setIsPureColor(BGInfo bgInfo) {
        this.mBGInfo = bgInfo;
    }


    /**
     * 当当前片段即将结束时，切入下一个场景的临时temp，当当前场景结束时，把temp上的坐标值赋值给下个场景
     * 在调用层判断是否为最后一个片段
     * transitionFilter后续加上片段切换时，转场也做切换动作
     *
     * @param mediaItem
     * @param index
     */
    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        if (mediaDrawer != null) {
//            mediaDrawer.setFlipVertical(true);
            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            if (isPreviewTransition) {
                GLES20.glClearColor(0, 0, 0, 1.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                isPreviewTransition = false;
                if (transitionFilter != null && transitionFilter instanceof WholeZoomTransitionFilter) {
                    mediaDrawer.drawBufferFrame();
                }
            } else {
                mediaDrawer.drawBufferFrame();
            }
            EasyGlUtils.unBindFrameBuffer();
//            mediaDrawer.setFlipVertical(false);
        }
        gifFilter.resetData();
        if (actionRender != null) {
            LogUtils.d(getClass().getSimpleName(), "lastActionRender = actionRender");
            lastActionRender = actionRender;
        }
        actionRender = drawPrepareIndex(mediaItem, index, width, height);
        if (actionRender instanceof BaseBlurThemeExample) {
            ((BaseBlurThemeExample) actionRender).setIsPureColor(mBGInfo,width,height);
        }
        actionRender.init(mediaItem, width, height);
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }
        //添加转场的回收和赋值
        if (mediaItem.getTransitionFilter() != null && mediaItem.getTransitionFilter().getTransitionType() != TransitionType.NONE) {
            transitionFilter = mediaItem.getTransitionFilter();
            transitionFilter.create();
            transitionFilter.onSizeChanged(width, height);
        }
        if (transitionFilter != null) {
            transitionFilter.updateProgress(0);
        }
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[1]);
        actionRender.drawFrame();
        EasyGlUtils.unBindFrameBuffer();
    }

    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        return null;
    }

    public IAction getAction() {
        return actionRender;
    }

    @Override
    public void draFrameExtra() {
        actionRender.drawLast();
    }

    @Override
    public void previewAfilter(MediaItem mediaItem) {
        if (actionRender == null) {
            return;
        }
        if (mediaItem != null && mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }
        actionRender.drawFramePreview();
    }


    @Override
    public void ratioChange() {
        if (transitionFilter != null) {
            transitionFilter.changeRatio();
        }

    }

    public IAction getNextAction(MediaItem mediaItem, int index) {
        IAction temp = null;
        return temp;
    }

    /**
     * 两场景进行切换时，对动画进行赋值
     *
     * @param actionRender
     */
    public void setActionRender(IAction actionRender) {
        if (this.actionRender != null) {
            this.actionRender.onDestroy();
        }
        this.actionRender = actionRender;
    }

    public IAction getActionRender() {
        return actionRender;
    }


    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        if (gifFilter != null) {
            gifFilter.create();
        }
        if (transitionFilter != null) {
            transitionFilter.create();
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        createFBo(width, height);
        if (transitionFilter != null) {
            transitionFilter.onSizeChanged(width, height);
        }
        gifFilter.setSize(width, height);
    }

    private void createFBo(int width, int height) {
        // 创建离屏渲染
        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, fFrame, 0);
        // // 创建离屏纹理
        genTextures(width, height);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    // 生成Textures
    private void genTextures(int width, int height) {
        GLES20.glGenTextures(fTextureSize, fTexture, 0);
        if (fTexture[0] == -1 || fTexture[1] == -1) {
            deleteFrameBuffer();
            GLES20.glGenTextures(fTextureSize, fTexture, 0);
        }
        for (int i = 0; i < fTextureSize; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_REPLACE); //
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


    @Override
    public void onDrawFrame() {
        if (actionRender.getStatus() == ActionStatus.ENTER) {
            actionRender.drawFrameIndex();
            if (transitionFilter != null) {//自定义转场
                transitionFilter.setPreviewTextureId(fTexture[0]);
                transitionFilter.setTextureId(fTexture[1]);
                transitionFilter.draw();
                gifFilter.draw();
            }
            if (lastActionRender != null) {
                lastActionRender.drawLast();
            }
        } else {
            actionRender.drawFrame();
            if (actionRender.getStatus() == ActionStatus.OUT && lastActionRender != null) {
                LogUtils.d(getClass().getSimpleName(), "lastActionRender.onDestroy()");
                lastActionRender.onDestroy();
                lastActionRender = null;
            }
        }
    }

    public void drawGifFrame() {
        if (actionRender.getStatus() == ActionStatus.ENTER) {
            gifFilter.draw();
        }
    }

    public void setGifTexture(int texture) {
//        gifFilter.setTextureId(texture);
    }

    @Override
    public boolean checkSupportTransition() {
        return true;
    }


}
