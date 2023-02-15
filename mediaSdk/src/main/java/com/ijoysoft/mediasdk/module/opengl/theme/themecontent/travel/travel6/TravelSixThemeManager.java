package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel6;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * 图片层的背景渲染，逻辑从imageplayer中抽出
 * 目的是把背景封装，暴露纹理接口，即外部调用的时候只需传入纹理id便可
 * <p>
 * *****这里重点记录一下，actionRender目前是没有做回收，android9的手机中对纹理进行回收之后，会出现纯黑，
 * 所以这里暂时不回收让系统自己回收
 */
public class TravelSixThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;
    // 是否为纯色背景

    /**
     * 创建缓存对象，离屏buffer
     */

    private int width, height;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public TravelSixThemeManager() {
        super();
        bgImageFilter = new ImageOriginFilter();
    }


    public void onDestroy() {
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
//        if (pureColorFilter != null) {
//            pureColorFilter.onDestroy();
//        }
        if (actionRender != null) {
            actionRender.onDestroy();
        }
    }


    /**
     * 当当前片段即将结束时，切入下一个场景的临时temp，当当前场景结束时，把temp上的坐标值赋值给下个场景
     * 在调用层判断是否为最后一个片段
     *
     * @param mediaItem
     * @param index
     */
    public void drawPrepare(MediaItem mediaItem, int index) {
        if (actionRender != null) {
            actionRender.onDestroy();
        }
//        index = 4;
        switch (index % 5) {
            case 0:
                actionRender = new TravelSixNineThemeOne((int) mediaItem.getDuration());
                break;
            case 1:
                actionRender = new TravelSixNineThemeTwo((int) mediaItem.getDuration());
                break;
            case 2:
                actionRender = new TravelSixNineThemeThree((int) mediaItem.getDuration());
                break;
            case 3:
                actionRender = new TravelSixNineThemeFour((int) mediaItem.getDuration());
                break;
            case 4:
                actionRender = new TravelSixNineThemeFive((int) mediaItem.getDuration());

                break;
        }
        actionRender.init(mediaItem, width, height);
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }
    }

    public IAction getAction() {
        return actionRender;
    }

    @Override
    public void draFrameExtra() {

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


    public BaseThemeExample getNextAction(MediaItem mediaItem, int index) {
        BaseThemeExample temp = null;
//        index = 4;
        switch (index % 5) {
            case 0:
                temp = new TravelSixNineThemeOne((int) mediaItem.getDuration());
                break;
            case 1:
                temp = new TravelSixNineThemeTwo((int) mediaItem.getDuration());
                break;
            case 2:
                temp = new TravelSixNineThemeThree((int) mediaItem.getDuration());
                break;
            case 3:
                temp = new TravelSixNineThemeFour((int) mediaItem.getDuration());
                break;
            case 4:
                temp = new TravelSixNineThemeFive((int) mediaItem.getDuration());
                break;
        }
        temp.init(mediaItem, width, height);
        if (mediaItem.getAfilter() != null) {
            temp.setFilter(mediaItem.getAfilter());
        }
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

    /**
     * 设置背景
     */
    public void initBackgroundTexture() {
        ratioChange();
    }

    @Override
    public void ratioChange() {
        switch (ConstantMediaSize.ratioType) {
            case _9_16:
            case _3_4:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_bg916" + ConstantMediaSize.SUFFIX));
                break;
            case _1_1:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_bg11" + ConstantMediaSize.SUFFIX));
                break;
            case _16_9:
            case _4_3:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_bg169" + ConstantMediaSize.SUFFIX));
                break;
            default:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_six_bg916" + ConstantMediaSize.SUFFIX));
                break;
        }
        DEFAUT_ZVIEW=-2.3f;
    }


    @Override
    public void onSurfaceCreated() {
        bgImageFilter.create();
//        pureColorFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        bgImageFilter.onSizeChanged(width, height);
//        pureColorFilter.setSize(width, height);
        //对显示区域重新赋值
        LogUtils.i("render", "onSizeChanged....");
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame() {
        bgImageFilter.draw();
//        pureColorFilter.draw();
        actionRender.drawFrame();
//        particlesDrawer.onDrawFrame();
    }

    /**
     * 渲染尾部片段，同时上下两个同时展示
     */
    public void drawEndOffset() {
//        setMatrix(actionRender.draw());
//        Log.i("draw", "matrix:" + Arrays.toString(getMatrix()));
//        draw();
    }
}
