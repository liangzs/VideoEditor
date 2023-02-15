package com.ijoysoft.mediasdk.module.playControl;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

import java.util.List;

/**
 * 用handlerThread提换timer实现暂停，恢复，销毁
 * 亦或用rxsingle实现timer功能
 *
 * @since 2022年11月4日10:33:48
 * 后续需要把themeManager中初始化主控件的纹理抽出来，放到ImageRichRender中
 * 对外提供处理过的纹理（比如背景模糊等)，给到主题类（thememanager)或者转场类(mediaDrawer)
 */
public class ImageRichRender implements IRender {
    private static final String TAG = "ImagePlayer";
//    /**
//     * 创建缓存对象，离屏buffer
//     */
//    private int[] fFrame = new int[1];
//    // 两张纹理
//    private int fTextureSize = 1;
//    private int[] fTexture = new int[fTextureSize];

    //    private int currentDuration;
//    private int currentPosition;//总时长的当前时间进度
    private IMediaCallback imageCallback;
    //    private boolean isPlaying;
    private int transiPreTime;

    // 记录原始图片的纹理id,在create后存储纹理ID
    private MediaItem currentMediaItem;
    private List<MediaItem> mediaItems;
    private List<Bitmap> widgetMimaps;
    private int mWidth, mHeight, canvasWidth, canvasHeight;
    private boolean isPreviewTransition;
    private boolean isPreview;
    //    private ThemeIRender themeBackgroundManager;
//    private IAction themeNextFilter;
//    //粒子系统
//    private ParticleDrawerManager particlesDrawer;
    private boolean changeRatio;
    private int currentIndex;
    private boolean seekRunMore;
    private boolean isClipPreview;

    public ImageRichRender(IMediaCallback imageCallback) {
        this.imageCallback = imageCallback;


    }

    @Override
    public void onSurfaceCreated() {
//        currentDuration = 0;
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        Log.i(TAG, "onSurfaceChanged...");
        mWidth = width;
        mHeight = height;
        this.canvasWidth = screenWidth;
        this.canvasHeight = screenHeight;
//        createFBo(width, height);

    }


//    private void createFBo(int width, int height) {
//        deleteFrameBuffer();
//        GLES20.glGenFramebuffers(1, fFrame, 0);
//        genTextures(width, height);
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//    }
//
//    // 生成Textures
//    private void genTextures(int width, int height) {
//        GLES20.glGenTextures(fTextureSize, fTexture, 0);
//        if (fTexture[0] == -1) {
//            GLES20.glGenTextures(fTextureSize, fTexture, 0);
//        }
//        for (int i = 0; i < fTextureSize; i++) {
//            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i]);
//            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA,
//                    GLES20.GL_UNSIGNED_BYTE, null);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        }
//    }
//
//    private void deleteFrameBuffer() {
//        GLES20.glDeleteFramebuffers(1, fFrame, 0);
//        /* 9.0以上的手机问题，纹理不进行回收了！ */
//        GLES20.glDeleteTextures(fTextureSize, fTexture, 0);
//        for (int i = 0; i < fTextureSize; i++) {
//            fTexture[i] = -1;
//        }
//    }

    /**
     * 对图片的originAfiter进行初始化准备工作
     * 记得在queuEvent中进行执行
     */
    public boolean playPrepare(GlobalDrawer mediaDrawer) {

        return true;
    }


    /**
     * 根据currentDuration来是否使用transitionfilter
     * 在imageplayer的ondrawFrame中，内含了EasyGlUtils.bindFrameTexture 离屏渲染，
     * 如果在themeBackgroundManager涉及离屏渲染的，尽量放到prepare中，
     * 而不应该EasyGlUtils.bindFrameTexture内嵌内含了EasyGlUtils.bindFrameTexture的操作，导致图像数据便宜
     */
    @Override
    public void onDrawFrame() {
    }

    /**
     * {{@link #onDrawFrame(ActionStatus, int)}}
     *
     * @return
     */
    @Override
    public ActionStatus onDrawFrameStatus() {
        // 黑色背景
//        GLES20.glClearColor(0.114f, 0.114f, 0.114f, 1.0f);
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//        // 显示区背景
////        pureColorFilter.draw();
//        if (ObjectUtils.isEmpty(themeBackgroundManager.getAction())) {
//            return ActionStatus.STAY;
//        }
//        //如果是时间轴主题，就直接绘画
//        if (isTimeTheme()) {
//            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
//            ((BaseTimeThemeManager) themeBackgroundManager).onDrawFrame(currentPosition);
//            particlesDrawer.onDrawFrame();
//            EasyGlUtils.unBindFrameBuffer();
//            return themeBackgroundManager.getAction().getStatus();
//        }
//        //先画个纯色显示区域
//        if (themeBackgroundManager.getAction().getStatus() == ActionStatus.OUT
//                && (currentIndex < mediaItems.size()) && !ThemeHelper.isIgnore() && !isClipPreview) {//到当前片段的尾部500ms时候进行切换
//            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
//            themeBackgroundManager.onDrawFrame();
//            if (themeNextFilter != null) {
//                themeNextFilter.drawFrame();
//            }
//            themeBackgroundManager.draFrameExtra();
//            particlesDrawer.onDrawFrame();
//            EasyGlUtils.unBindFrameBuffer();
//        } else {
//            EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
//            themeBackgroundManager.onDrawFrame();//是否滤镜变化
//            themeBackgroundManager.draFrameExtra();
//            particlesDrawer.onDrawFrame();
//            EasyGlUtils.unBindFrameBuffer();
//
//        }
//        if (themeBackgroundManager instanceof ThemeOpenglManager) {
//            return themeBackgroundManager.getAction().getStatus();
//        }
        return ActionStatus.STAY;

    }

    public void setDataSource(List<MediaItem> list, MediaItem mediaItem) {
        this.mediaItems = list;
        currentMediaItem = mediaItem;
        currentIndex = list.indexOf(mediaItem);
    }

    /**
     * 设置挂载在时间轴的控件
     *
     * @param mimaps
     */
    public void setWidgetMimaps(List<Bitmap> mimaps) {
        this.widgetMimaps = mimaps;
    }

    /**
     * 预览滤镜,原方案只渲染主题bitamp，现方案渲染整个区域
     */
    public void previewFilter(MediaItem currentMediaItem) {
        isPreview = true;
//        if (themeBackgroundManager != null) {
//            themeBackgroundManager.previewAfilter(currentMediaItem);
//        }
    }


    public void setPreview(boolean preview) {
        isPreview = preview;
//        currentDuration = 0;
    }

    /**
     * 单个片段预览
     */
    public void previewMediaitem(MediaItem currentMediaItem) {
//        currentDuration = themeBackgroundManager.getAction().getEnterTime();//这里设定了片段的stay状态展示
//        themeNextFilter = null;
        isPreview = false;
    }


    private MediaItem getPreMediaItem() {
        if (ObjectUtils.isEmpty(mediaItems)) {
            return null;
        }
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
     * 检查是否有滤镜
     *
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
     *
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


//    /**
//     * 分辨率改变，需要重新加载比例资源
//     * mediaItem中挂载的mipbitmaps要重现提换加载
//     * 背景资源需要重新提换
//     * 对于有些特殊的
//     */
//    public void setRatio() {
//        changeRatio = true;
//        if (themeNextFilter != null) {
//            themeNextFilter.onDestroy();
//            themeNextFilter = null;
//        }
//        playPrepare(null);
//        //恢复原来进度
//        int tempDuration = currentIndex == 0 ? currentDuration : currentDuration + ConstantMediaSize.END_OFFSET;
//        themeBackgroundManager.seekTo(tempDuration);
//        //检测当前片段是否是出场状态，如果是出场状态，则再进行计算下一片段的进场时间
//        if (themeNextFilter != null && themeBackgroundManager.getAction().getStatus() == ActionStatus.OUT) {
//            tempDuration = (int) (currentMediaItem.getDuration() - currentDuration);
//            themeNextFilter.seek(tempDuration);
//        }
//
//    }

    /**
     * 控制渲染状态
     */
    public void playPause() {
//        isPlaying = false;
//        LogUtils.v("ImagePlayer", ":playPause,isPlaying:" + isPlaying);
    }

//
//    public boolean isPlaying() {
//        return isPlaying;
//    }

    /**
     * 恢复播放
     */
    public void playResume() {
//        isPlaying = true;
//        handler.removeMessages(RENDER_CODE);
//        LogUtils.v("ImagePlayer", ":playResume,isPlaying:" + isPlaying);
//        startRender();
    }

    public void playComplete() {
        imageCallback.render();
//        isPlaying = false;
        imageCallback.onComplet();
    }

    /**
     * 跳转到当前播放文件指定位置，duration默认是4s
     * 这里需给theme主题设置新的进度条,这里判断如果涉及了片段的切换，则需要对齐进行再次初始化操作
     * seekRunMore防止seek操作过多时，glthread执行太多任务导致渲染栈挤兑
     *
     * @param seek
     */
    /**
     * 跳转到当前播放文件指定位置，duration默认是4s
     * 这里需给theme主题设置新的进度条,这里判断如果涉及了片段的切换，则需要对齐进行再次初始化操作
     *
     * @param seek
     */
    public void seekTo(int index, int seek, boolean isRender, boolean force, GLSurfaceView glSurfaceView) {
//        currentDuration = seek;
//        if (ObjectUtils.isEmpty(themeBackgroundManager)) {
//            return;
//        }
//        LogUtils.i(TAG, "seekTo->currentIndex:" + currentIndex + ",index:" + index + ",currentDuration:" + currentDuration + ",force:" + force);
//        if (currentIndex != index || force) {
//            if (!force && seekRunMore) {
//                return;
//            }
//            seekRunMore = true;
//            currentIndex = index;
//            currentMediaItem = mediaItems.get(currentIndex);
//            glSurfaceView.queueEvent(new Runnable() {
//                @Override
//                public void run() {
//                    //先清理
//                    if (themeBackgroundManager != null) {
//                        themeBackgroundManager.onDestroyFragment();
//                    }
//                    if (themeNextFilter != null) {
//                        themeNextFilter.onDestroy();
//                    }
//                    themeNextFilter = null;
//                    LogUtils.i(TAG, "seekTo->playPrepare");
//                    playPrepare(null);
//                    themeBackgroundManager.initBackgroundTexture();
//                    if (force) {
//                        int innerDuration = currentDuration;
//                        for (MediaItem mediaItem : mediaItems) {
//                            if (mediaItem.getTempDuration() != 0 && mediaItem.getTempDuration() <= innerDuration) {
//                                innerDuration -= mediaItem.getTempDuration();
//                            } else if (mediaItem.getDuration() <= innerDuration) {
//                                innerDuration -= mediaItem.getDuration();
//                            } else {
//                                break;
//                            }
//                        }
////                        currentDuration = themeBackgroundManager.getAction().getEnterTime();
//                        if (isTimeTheme()) {
//                            themeBackgroundManager.draFrameExtra();
//                            ((BaseTimeThemeExample) themeBackgroundManager.getAction()).drawCertainTimePreview(innerDuration);
//                        } else {
//                            themeBackgroundManager.getAction().drawFramePreview();
//                        }
//                    }
//                    seekRunMore = false;
//                }
//            });
//        }
//
//        int tempDuration = index == 0 ? currentDuration : currentDuration + ConstantMediaSize.END_OFFSET;
//        themeBackgroundManager.seekTo(tempDuration);
//        //检测当前片段是否是出场状态，如果是出场状态，则再进行计算下一片段的进场时间
//        if (themeNextFilter != null && themeBackgroundManager.getAction().getStatus() == ActionStatus.OUT) {
//            tempDuration = (int) (currentMediaItem.getDuration() - currentDuration);
//            themeNextFilter.seek(tempDuration);
//        }
//
//        if (isRender) {
//            imageCallback.render();
//        }
    }


    public void onDestroy() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            handler.removeCallbacksAndMessages(null);
//            handlerThread.quitSafely();
//        }
//        if (themeBackgroundManager != null) {
//            themeBackgroundManager.onDestroy();
//        }
//        if (particlesDrawer != null) {
//            particlesDrawer.onDestroy();
//        }
//        deleteFrameBuffer();
    }

    /**
     * 获取当前位置点，默认是时长是4秒
     *
     * @return
     */

//    public int getOutputTexture() {
//        return fTexture[0];
//    }
//
//    public void setIsPureColor(BGInfo bgInfo) {
//        if (themeBackgroundManager instanceof ThemeOpenglManager) {
//            ((ThemeOpenglManager) themeBackgroundManager).setIsPureColor(bgInfo);
//        }
//    }

    /**
     * 开始播放，或者切换播放的时候，记得设置来切换图片源
     *
     * @param currentMediaItem
     */
    public void setCurrentMediaItem(MediaItem currentMediaItem) {
        this.currentMediaItem = currentMediaItem;
        this.currentIndex = mediaItems.indexOf(currentMediaItem);
    }

//    /**
//     * 合成的时候，进行更新
//     *
//     * @param currentDuration
//     */
//    public void setCurrentDuration(int currentDuration) {
//        this.currentDuration = currentDuration;
//    }
//
//    /**
//     * 当前总市场的时间进度
//     *
//     * @param currenPosition
//     */
//    public void setCurrenPosition(int currenPosition) {
//        this.currentPosition = currenPosition;
//    }

    /**
     * 是否是预览转场效果,如果是预览转场效果，转场播放结束后设置isplaying为false
     * 后面再评估这些存在的必要性
     */
    public void isPreviewTransition(boolean isPreviewTransition) {
        this.isPreviewTransition = isPreviewTransition;
    }

    /**
     * 更换主题
     */
    public void changeTheme(MediaConfig mediaConfig) {
//        themeBackgroundManager.onDestroy();
//        particlesDrawer.onDestroy();
//        if (themeNextFilter != null) {
//            themeNextFilter.onDestroy();
//            themeNextFilter = null;
//        }
//        createFBo(mWidth, mHeight);
//        themeBackgroundManager = ThemeHelper.createThemeManager();
//        //控件挂载时间轴
//        if (isTimeTheme()) {
//            ((BaseTimeThemeManager) themeBackgroundManager).setWidgetMipmaps(widgetMimaps);
//        }
//        particlesDrawer = ThemeHelper.createParticles();
//        particlesDrawer.onSurfaceCreated();
//        particlesDrawer.onSurfaceChanged(0, 0, mWidth, mHeight, canvasWidth, canvasHeight);
//        themeBackgroundManager.onSurfaceCreated();
//        themeBackgroundManager.onSurfaceChanged(0, 0, mWidth, mHeight, canvasWidth, canvasHeight);
//        themeBackgroundManager.initBackgroundTexture();
//        if (themeBackgroundManager instanceof ThemeOpenglManager) {
//            ((ThemeOpenglManager) themeBackgroundManager).setIsPureColor(mediaConfig.getBGInfo());
//        }
    }

    /**
     * 是否为片段编辑
     *
     * @param isClipPreview
     */
    public void isClipView(boolean isClipPreview) {
        this.isClipPreview = isClipPreview;
    }


//    public void onFinish() {
//        if (themeBackgroundManager != null) {
//            themeBackgroundManager.onFinish();
//        }
//    }

}
