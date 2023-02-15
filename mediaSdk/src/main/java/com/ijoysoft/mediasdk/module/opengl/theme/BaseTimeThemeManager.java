package com.ijoysoft.mediasdk.module.opengl.theme;

import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.util.Pair;

import androidx.annotation.CallSuper;

import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.NoFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.PureColorFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ThemePagFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseTimeBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseTimeThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmbeddedFilterEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeDrawerContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemePagParticleContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeVideoExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import org.libpag.PAGFile;
import org.libpag.PAGPlayer;
import org.libpag.PAGSurface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 * 无非就是上下左右，四个角方向平移，缩放，旋转：中心点，四个顶角旋转，再叠加动感模糊效果过渡，和加速减速效果
 * 悬停过程可以是微缩放，平移等
 */
public abstract class BaseTimeThemeManager implements ThemeIRender {
    public final int ANIMATE_TIME_S = 300;
    public final int ANIMATE_TIME_M = 400;
    public final int ANIMATE_TIME_L = 500;
    public final int ANIMATE_TIME_XL = 600;
    protected int width, height;
    protected BaseTimeThemeExample actionRender;
    protected List<WidgetTimeExample> widgetRenders;
    protected List<TimeThemeDrawerContainer> renders;
    protected Map<Integer, ArrayList<BaseEvaluate>> map;
    protected ArrayList<BaseEvaluate> currentEvaluates;
    protected ArrayList<BaseEvaluate> lastEvaluates;
    protected int recyleTime;
    protected PureColorFilter pureColorFilter;

    protected ThemePagParticleDrawerProxy particleProxy;

    protected int currentDuration;

    /**
     * 边框类型的素材
     */
    protected ImageOriginFilter borderImageFilter = null;

    /**
     * gif绘制
     * 集合
     */
    protected List<GifOriginFilter> globalizedGifOriginFilters = null;

    //pag控件展示
    protected List<ThemePagFilter> mThemePagFilters;

    public BaseTimeThemeManager() {
        recyleTime = computeThemeCycleTime();
        if (blurBackground()) {
            actionRender = new BaseTimeBlurThemeExample();
        } else {
            actionRender = new BaseTimeThemeExample();
        }
        widgetRenders = new ArrayList<>();
        renders = new ArrayList<>();
        map = new ArrayMap<>();
        if (!ObjectUtils.isEmpty(getPureColor())) {
            pureColorFilter = new PureColorFilter();
            int[] value = ColorUtil.hex2Rgb(getPureColor());
            float[] rgba = new float[4];
            rgba[0] = ((float) value[0] / 255f);
            rgba[1] = ((float) value[1] / 255f);
            rgba[2] = ((float) value[2] / 255f);
            rgba[3] = ((float) value[3] / 255f);
            pureColorFilter.setIsPureColor(rgba);
        }
        if (!ObjectUtils.isEmpty(customFragment())) {
            actionRender.setFragment(customFragment());
        }
    }

    protected String getPureColor() {
        return "";
    }

    /**
     * 设置控件资源
     *
     * @param widgetMipmaps
     */
    public void setWidgetMipmaps(List<Bitmap> widgetMipmaps) {
        if (ObjectUtils.isEmpty(widgetMipmaps)) {
            return;
        }
        if (customCreateWidgetWrapper(widgetMipmaps)) {
            for (int i = 0; i < widgetRenders.size(); i++) {
                intWidget(i, widgetRenders.get(i));
            }
            return;
        }

        widgetRenders.clear();
        ThemeWidgetInfo widgetInfo;
        for (int i = 0; i < widgetMipmaps.size(); i++) {
            widgetInfo = createWidgetByIndex(i);
            if (widgetInfo != null) {
                widgetInfo.setBitmap(widgetMipmaps.get(i));
                createWidgetTimeExample(widgetInfo);
            }
        }
    }

    /**
     * 添加控件
     *
     * @param info 如果只有start和end，action设置到out里面
     * @return
     */
    protected WidgetTimeExample createWidgetTimeExample(ThemeWidgetInfo info) {
        WidgetTimeExample widgetTimeExample = new WidgetTimeExample(info);
        widgetRenders.add(widgetTimeExample);
        return widgetTimeExample;
    }

    /**
     * 手动创建控件等
     *
     * @return true 不执行默认创建控件的方式
     * @return false 继续执行默认创建控件的方式
     */
    protected boolean customCreateWidget(List<Bitmap> widgetMipmaps) {
        if (widgetRenders != null) {
            for (int i = 0; i < widgetRenders.size(); i++) {
                widgetRenders.get(i).onDestroy();
            }
            widgetRenders.clear();
        }
        return false;
    }

    private boolean customCreateWidgetWrapper(List<Bitmap> widgetMipmaps) {
        if (widgetRenders != null) {
            for (int i = 0; i < widgetRenders.size(); i++) {
                widgetRenders.get(i).onDestroy();
            }
            widgetRenders.clear();
        }
        return customCreateWidget(widgetMipmaps);
    }

    /**
     * 创建时间轴的gif
     * 如果有粒子系统，记得创建完之后要把粒子系统放在上面
     *
     * @param gifMipmaps    素材
     * @param timeLineStart 非全局起始坐标
     */
    protected void customCreateGifWidget(List<List<GifDecoder.GifFrame>> gifMipmaps, int timeLineStart) {

    }


    protected abstract ThemeWidgetInfo createWidgetByIndex(int index);

    /**
     * @param index widgetrenders 中的index 不是mimpa 中的index
     */
    protected abstract void intWidget(int index, WidgetTimeExample widgetTimeExample);

    protected boolean existPag() {
        return false;
    }

    /**
     * 是否模糊背景
     *
     * @return
     */
    protected boolean blurBackground() {
        return false;
    }

    /**
     * 特定主题是否自定义BaseTimeThemeExample的fragment
     *
     * @return
     */
    protected String customFragment() {
        return null;
    }


    /**
     * 动态创建动画组合,根据片段的时长均分动画，或者自定义时间，只要和duration相同即可
     *
     * @param index
     * @return
     */
    protected abstract ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration);

    /**
     * 创建其他基于素材的控件
     *
     * @param index     顺序
     * @param mediaItem 媒体元素
     */
    protected void createExtraWithBitmap(int index, MediaItem mediaItem) {

    }

    protected abstract int createActions();

    /**
     * 一个循环主题的时间
     *
     * @return
     */
    protected int computeThemeCycleTime() {
        return createActions() * BaseTimePreTreatment.DURATION;
    }

    /**
     * 依据片段切换时间
     *
     * @return
     */
    protected int computeClipTime() {
        return recyleTime;
    }

    @Override
    public void onDestroy() {
        if (actionRender != null) {
            actionRender.onDestroy();
        }

        if (borderImageFilter != null) {
            borderImageFilter.onDestroy();
        }
        for (WidgetTimeExample example : widgetRenders) {
            example.onDestroy();
        }
        for (TimeThemeDrawerContainer particleRender : renders) {
            particleRender.onDestroy();
        }
        if (pureColorFilter != null) {
            pureColorFilter.onDestroy();
        }
        for (List<BaseEvaluate> list : map.values()) {
            for (BaseEvaluate evaluate : list) {
                if (evaluate instanceof EmbeddedFilterEvaluate) {
                    EmbeddedFilterEvaluate evaluate1 = (EmbeddedFilterEvaluate) evaluate;
                    if (evaluate1.getEmbeddedFilter() != null) {
                        evaluate1.getEmbeddedFilter().onDestroy();
                    }
                }
            }
        }
        /**
         * gif生命周期-销毁
         */
        if (!ObjectUtils.isEmpty(globalizedGifOriginFilters)) {
            for (GifOriginFilter gifOriginFilter : globalizedGifOriginFilters) {
                gifOriginFilter.onDestroy();
            }
            globalizedGifOriginFilters = null;
        }
        if (particleProxy != null) {
            particleProxy.onDestroy();
        }

        if (mThemePagFilters != null) {
            for (ThemePagFilter themePagFilter : mThemePagFilters) {
                themePagFilter.onDestroy();
            }
        }

    }


    @Override
    public void initBackgroundTexture() {

    }

    @Override
    public void drawPrepare(GlobalDrawer mediaDrawer, MediaItem mediaItem, int index) {
        int key = index % createActions();
        if (!map.containsKey(key)) {
            map.put(key, createAnimateEvaluate(key, mediaItem.getDuration()));
            createExtraWithBitmap(index, mediaItem);
        }
        lastEvaluates = currentEvaluates;
        currentEvaluates = map.get(index % createActions());
        actionRender.drawPrepare(mediaItem);
        if (isTextureInside()) {
            actionRender.adjustImageScalingInside(width, height);
        } else {
            actionRender.adjustImageScalingCrop(width, height);
        }

        actionRender.setEvaluates(currentEvaluates);
//        LogUtils.v("bug check", String.format("currentEvaluatesIndex: %d", index));
        if (lastEvaluates != null) {
            resetEvaluate(lastEvaluates);
        }
        if (ObjectUtils.isEmpty(globalizedGifOriginFilters) && !ObjectUtils.isEmpty(mediaItem.getDynamicMitmaps())) {
            onGifDrawerCreated(mediaItem, index);
            //gif绘制 生命周期 创建
            if (!ObjectUtils.isEmpty(globalizedGifOriginFilters)) {
                for (GifOriginFilter gifOriginFilter : globalizedGifOriginFilters) {
                    gifOriginFilter.onCreate();
                }
            }
            //生命周期几乎相同，直接放在这一块代码里：
            customCreateGifWidget(mediaItem.getDynamicMitmaps(), ObjectUtils.isEmpty(globalizedGifOriginFilters) ? 0 : globalizedGifOriginFilters.size());
        }
        if (globalizedGifOriginFilters != null) {
            for (GifOriginFilter gifOriginFilter : globalizedGifOriginFilters) {
                gifOriginFilter.onSizeChanged(width, height);
            }
        }
        onGifSizeChanged(index, width, height);
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        } else {
            actionRender.setFilter(new GPUImageFilter());
        }

        /**
         * 检测创建
         */
        if (!ObjectUtils.isEmpty(mediaItem.getThemePags()) && ObjectUtils.isEmpty(mThemePagFilters) && createThemePags() != null) {
            createThemeFilters(createThemePags(), mediaItem.getThemePags());
        }
    }

    /**
     * 创建themepag主题
     */
    private void createThemeFilters(List<ThemeWidgetInfo> list, List<PAGFile> pagFiles) {
        mThemePagFilters = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (pagFiles.get(i) == null) {
                continue;
            }
            ThemePagFilter themePagFilter = new ThemePagFilter(list.get(i));
            themePagFilter.setPagFile(pagFiles.get(i));
            themePagFilter.create();
            themePagFilter.onSizeChanged(width, height);
            mThemePagFilters.add(themePagFilter);
            dealThemePag(i, themePagFilter);
        }
    }

    /**
     * 自定义处理pag文件
     *
     * @param index
     */
    protected void dealThemePag(int index, ThemePagFilter themePagFilter) {

    }

    /**
     * gif绘制准备
     * 素材准备
     *
     * @param mediaItem 图片源，内涵gif素材源
     * @param index     顺序
     */
    protected void onGifDrawerCreated(MediaItem mediaItem, int index) {

    }

    /**
     * 屏幕比例变化时gif作出响应
     *
     * @param width  屏宽
     * @param height 屏高
     */
    protected void onGifSizeChanged(int index, int width, int height) {
//        if (mThemePagFilters != null) {
//            for (ThemePagFilter thempag : mThemePagFilters) {
//                thempag.onDestroy();
//            }
//            mThemePagFilters.clear();
//        }
    }


    protected void resetEvaluate(List<BaseEvaluate> list) {
        for (BaseEvaluate baseEvaluate : list) {
            baseEvaluate.prepare();
        }
    }

    @Override
    public void setActionRender(IAction actionRender) {

    }

    @Override
    public IAction getNextAction(MediaItem mediaItem, int index) {
        if (mediaItem.isVideo()) {
            return new TimeVideoExample();
        }
        return null;
    }

    @Override
    public IAction getAction() {
        return actionRender;
    }

    @Override
    public void draFrameExtra() {
        for (WidgetTimeExample widgetTimeExample : widgetRenders) {
            widgetTimeExample.reset();
        }
    }

    public boolean isTextureInside() {
        return false;
    }

    @Override
    public void previewAfilter(MediaItem mediaItem) {
        if (actionRender == null) {
            return;
        }
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }
        actionRender.drawFramePreview();
    }


    @Override
    public void previewMediaIem() {

    }

    @Override
    public void setPreAfilter(List<MediaItem> list, int index) {

    }

    @Override
    public void ratioChange() {

    }

    @Override
    public void seekTo(int currentDuration) {

    }

    @Override
    public void onDestroyFragment() {

    }

    @Override
    public boolean checkSupportTransition() {
        return false;
    }

    @Override
    public void onSurfaceCreated() {
        actionRender.create();
        if (borderImageFilter != null) {
            borderImageFilter.create();
        }
//        for (WidgetTimeExample widgetTimeExample : widgetRenders) {
//            widgetTimeExample.create();
//        }
        for (TimeThemeDrawerContainer particleRender : renders) {
            particleRender.onSurfaceCreated();
        }
        if (pureColorFilter != null) {
            pureColorFilter.create();
        }
    }


    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        //对显示区域重新赋值
        this.width = width;
        this.height = height;
        actionRender.onSizeChanged(width, height);
        if (borderImageFilter != null) {
            borderImageFilter.onSizeChanged(width, height);
        }
        for (int i = 0; i < widgetRenders.size(); i++) {
            widgetRenders.get(i).onSizeChanged(width, height);
            intWidget(i, widgetRenders.get(i));
            widgetRenders.get(i).init();
        }
        for (TimeThemeDrawerContainer particleRender : renders) {
            particleRender.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        }


        LogUtils.i("", "recyleTime:" + recyleTime);
        if (pureColorFilter != null) {
            pureColorFilter.onSizeChanged(width, height);
        }


        for (List<BaseEvaluate> list : map.values()) {
            for (BaseEvaluate evaluate : list) {
                if (evaluate instanceof EmbeddedFilterEvaluate) {
                    EmbeddedFilterEvaluate evaluate1 = (EmbeddedFilterEvaluate) evaluate;
                    if (evaluate1.getEmbeddedFilter() != null) {
                        evaluate1.getEmbeddedFilter().onSizeChanged(width, height);
                    }
                }
                if (evaluate instanceof TimeThemeEvaluate && ((TimeThemeEvaluate) evaluate).getExtraRender() != null) {
                    ((TimeThemeEvaluate) evaluate).onRenderDrawFrame();
                }
            }
        }
        map.clear();
        ratioChange();
        //gif响应比例变化
        if (globalizedGifOriginFilters != null) {
            for (int i = 0; i < globalizedGifOriginFilters.size(); i++) {
                globalizedGifOriginFilters.get(i).onSizeChanged(width, height);
            }
        }
    }

    @Override
    public void onDrawFrame(int currenPostion) {
        if (pureColorFilter != null) {
            pureColorFilter.draw();
        }
        actionRender.drawFrame();
        if (borderImageFilter != null) {
            borderImageFilter.draw();
        }//        LogUtils.i("", "currenPostion % recyleTime:" + currenPostion % recyleTime);
        for (WidgetTimeExample widgetTimeExample : widgetRenders) {
            widgetTimeExample.onDrawFrame(currenPostion % recyleTime);
        }
        for (TimeThemeDrawerContainer particleRender : renders) {
            if (particleRender instanceof TimeThemePagParticleContainer) {
                ((TimeThemePagParticleContainer) particleRender).onDrawFrame(currenPostion % recyleTime, actionRender.getTextureId());
            } else {
                particleRender.onDrawFrame(currenPostion % recyleTime);
            }
        }
        /**
         * gif生命周期绘制
         */
        if (!ObjectUtils.isEmpty(globalizedGifOriginFilters)) {
            for (GifOriginFilter gifOriginFilter : globalizedGifOriginFilters) {
                gifOriginFilter.draw();
            }
        }
        if (mThemePagFilters != null) {
            for (ThemePagFilter themePagFilter : mThemePagFilters) {
                themePagFilter.drawFrame(currenPostion % computeClipTime());
            }
        }
    }

    @Override
    public void onDrawFrame() {

    }

    public void setBaseTimeCusFragmeng(IBaseTimeCusFragmeng baseTimeCusFragmeng) {
        if (actionRender != null) {
            actionRender.setBaseTimeCusFragmeng(baseTimeCusFragmeng);
        }
    }

    public BaseTimeThemeExample getActionRender() {
        return actionRender;
    }


    /**
     * 获取片段中步骤的时间
     *
     * @param duration 片段总时间
     * @param frames   默认设置下，当前步骤的帧数
     * @return 步骤时间
     */
    protected static int getDuration(long duration, int frames, int totalFrames) {
        if (duration % totalFrames == 0) {
            return (int) (duration * frames / totalFrames);
        } else {
            return (int) Math.round((duration * ((double) frames)) / ((double) totalFrames));
        }
    }

    @Override
    public void onFinish() {
        for (WidgetTimeExample widgetTimeExample : widgetRenders) {
            widgetTimeExample.reset();
        }
    }

    @Override
    public void drawVideoFrame(int videoTeture) {
        if (actionRender != null) {
            actionRender.drawVideoFrame(videoTeture);
        }
    }

    /**
     * 检测是否有视频，如果有视频，提前初始化视频渲染的资源
     *
     * @return
     */
    public void setExistVideo(boolean videoDraw) {
        actionRender.setVideoDraw(videoDraw);
    }

    /**
     * 是否有pag文件
     *
     * @return
     */
    protected List<ThemeWidgetInfo> createThemePags() {
        return null;
    }

}
