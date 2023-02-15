package com.ijoysoft.mediasdk.module.opengl.theme;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer;
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.filter.ThemePagFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import org.libpag.PAGFile;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 20220617转场迁走
 */
public class ThemeOpenglManager extends BaseThemeManager {

    protected int width, height, screenWidth, screenHeight;
    protected IAction lastActionRender;
    protected int currentIndex;


    /**
     * gif绘制
     * 集合
     */
    protected List<GifOriginFilter> globalizedGifOriginFilters = null;
    //pag控件展示
    protected List<ThemePagFilter> mThemePagFilters;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public ThemeOpenglManager() {

    }

    @Override
    public void onDestroyFragment() {
    }

    @Override
    public void onDestroy() {
        if (actionRender != null) {
            actionRender.onDestroy();
        }
        if (lastActionRender != null && lastActionRender != actionRender) {
            actionRender.onDestroy();
            lastActionRender = null;
        }
        //gif生命周期-销毁
        if (!ObjectUtils.isEmpty(globalizedGifOriginFilters)) {
            for (GifOriginFilter gifOriginFilter : globalizedGifOriginFilters) {
                gifOriginFilter.onDestroy();
            }
            globalizedGifOriginFilters = null;
        }
        if (mThemePagFilters != null) {
            for (ThemePagFilter themePagFilter : mThemePagFilters) {
                themePagFilter.onDestroy();
            }
        }
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
        if (actionRender != null) {
            LogUtils.d(getClass().getSimpleName(), "lastActionRender = actionRender");
            if (lastActionRender != null) {
                lastActionRender.onDestroy();
                lastActionRender = null;
            }
            lastActionRender = actionRender;
        }
        currentIndex = index;
        actionRender = drawPrepareIndex(mediaItem, index, width, height);
        if (actionRender instanceof BaseBlurThemeExample) {
            if (mediaItem.isVideo()) {
                ((BaseBlurThemeExample) actionRender).setVideoWH(mediaItem.getWidth(), mediaItem.getHeight());
            }
            ((BaseBlurThemeExample) actionRender).setIsPureColor(mBGInfo, width, height);
        }
        actionRender.init(mediaItem, width, height);
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }
        if (ObjectUtils.isEmpty(globalizedGifOriginFilters) && !ObjectUtils.isEmpty(mediaItem.getDynamicMitmaps())) {
            //ObjectUtils.isEmpty(globalizedGifOriginFilters) 所以这里只会走一次
            onGifDrawerCreated(mediaItem, index);
            //gif绘制 生命周期 创建
            if (!ObjectUtils.isEmpty(globalizedGifOriginFilters)) {
                for (GifOriginFilter gifOriginFilter : globalizedGifOriginFilters) {
                    gifOriginFilter.onCreate();
                }
            }
        }
        onGifSizeChanged(index, width, height);

        /**
         * 检测创建
         */
        if (!ObjectUtils.isEmpty(mediaItem.getThemePags()) && ObjectUtils.isEmpty(mThemePagFilters) && createThemePags() != null) {
            createThemeFilters(createThemePags(), mediaItem.getThemePags());
        }
    }

    /**
     * 是否有pag文件
     *
     * @return
     */
    protected List<ThemeWidgetInfo> createThemePags() {
        return null;
    }


    /**
     * 创建themepag主题
     */
    private void createThemeFilters(List<ThemeWidgetInfo> list, List<PAGFile> pagFiles) {
        mThemePagFilters = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
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
     * 创建时间轴的gif
     * 如果有粒子系统，记得创建完之后要把粒子系统放在上面
     *
     * @param gifMipmaps    素材
     * @param timeLineStart 非全局起始坐标
     */
    protected void customCreateGifWidget(List<List<GifDecoder.GifFrame>> gifMipmaps, int timeLineStart) {

    }


    /**
     * 屏幕比例变化时gif作出响应
     *
     * @param width  屏宽
     * @param height 屏高
     */
    protected void onGifSizeChanged(int index, int width, int height) {
        if (globalizedGifOriginFilters != null) {
            for (GifOriginFilter gifOriginFilter : globalizedGifOriginFilters) {
                gifOriginFilter.onSizeChanged(width, height);
            }
        }
    }

    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        return null;
    }

    @Override
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

    }

    @Override
    public IAction getNextAction(MediaItem mediaItem, int index) {
        IAction temp = null;
        return temp;
    }

    /**
     * 两场景进行切换时，对动画进行赋值
     *
     * @param actionRender
     */
    @Override
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


    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        //gif响应比例变化
        if (globalizedGifOriginFilters != null) {
            for (int i = 0; i < globalizedGifOriginFilters.size(); i++) {
                onGifSizeChanged(i, width, height);
            }
        }
    }


    @Override
    public void onDrawFrame() {
        actionRender.drawFrame();
        if (actionRender.getStatus() == ActionStatus.OUT && lastActionRender != null) {
            LogUtils.d(getClass().getSimpleName(), "lastActionRender.onDestroy()");
            lastActionRender.onDestroy();
            lastActionRender = null;
        }
        onDrawFrameGif();
    }

    /**
     * gif生命周期绘制
     */
    public void onDrawFrameGif() {
        if (!ObjectUtils.isEmpty(globalizedGifOriginFilters)) {
            for (int i = 0; i < globalizedGifOriginFilters.size(); i++) {
                if (checkGifshow(i)) {
                    continue;
                }
                globalizedGifOriginFilters.get(i).draw();
            }
        }
    }

    @Override
    public void onDrawFrame(int position) {
        if (mThemePagFilters != null) {
            if (drawCustFramePag()) {
                return;
            }
            for (ThemePagFilter themePagFilter : mThemePagFilters) {
                themePagFilter.drawFrame(position);
            }
        }
    }

    /**
     * 是否自定义渲染pag文件
     *
     * @return
     */
    protected boolean drawCustFramePag() {
        return false;
    }

    protected boolean checkGifshow(int gifIndex) {
        return false;
    }

    @Override
    public boolean checkSupportTransition() {
        return true;
    }


    /**
     * 主题基础包，用于反射创建实例
     */
    public static final String THEME_BASE_PACKAGE = "com.ijoysoft.mediasdk.module.opengl.theme.themecontent.";


    /**
     * @author hayring
     * 反射式Opengl主题管理器
     */
    public static abstract class ReflectOpenglThemeManager extends ThemeOpenglManager {


        /**
         * 必须返回一个static final 的List
         *
         * @return static final list
         */
        public abstract List<Class<? extends ImageOriginFilter>> getFinalActionClasses();

        /**
         * 设置步骤处理类
         *
         * @return IAction action实例
         */
        @Override
        public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
            Constructor<? extends ImageOriginFilter> constructor = null;
            try {
                Class<? extends ImageOriginFilter> clazz = getFinalActionClasses().get(index % getFinalActionClasses().size());
                LogUtils.i("drawPrepareIndex", "" + clazz);
                constructor = clazz.getConstructor(int.class, int.class, int.class);
                IAction iAction = (IAction) constructor.newInstance((int) mediaItem.getDuration(), width, height);
                //频繁打印的日志放至-verbose级别中
                LogUtils.v(getClass().getSimpleName(), "create instance reflecting successfully: " + constructor.getName());
                return iAction;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.i("drawPrepareIndex", "" + constructor);
                assert constructor != null;
                LogUtils.e(getClass().getSimpleName(), "create instance reflecting failed: " + constructor.getName());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 当前片段状态
     *
     * @return
     */
    public ActionStatus getActionStatus() {
        if (actionRender == null || actionRender.getStatus() == null) {
            return ActionStatus.STAY;
        }
        return actionRender.getStatus();
    }
}




