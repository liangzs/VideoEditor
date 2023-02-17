package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love16;

import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
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
public class LoveSixteenThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;
    private int width, height;
    public static float LOVESIXTEEN_ZVIEW = -2.7f;
    private final int ACTION_COUNT = 5;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public LoveSixteenThemeManager() {
        super();
        bgImageFilter = new ImageOriginFilter();
    }


    public void onDestroy() {
        if (actionRender != null) {
            actionRender.onDestroy();
        }
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
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
        switch (index % ACTION_COUNT) {
            case 0:
                actionRender = new LoveSixteenThemeOne((int) mediaItem.getDuration(), width, height);
                break;
            case 1:
                actionRender = new LoveSixteenThemeTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                actionRender = new LoveSixteenThemeThree((int) mediaItem.getDuration());
                break;
            case 3:
                actionRender = new LoveSixteenThemeFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                actionRender = new LoveSixteenThemeFive((int) mediaItem.getDuration(), width, height);
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

    @Override
    public void ratioChange() {
        switch (ConstantMediaSize.ratioType) {
            case _16_9:
            case _4_3:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_bg"));
                break;
            case _3_4:
            case _9_16:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_bg"));
                break;
            case _1_1:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_bg"));
                break;
            default:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love16_bg"));
                break;
        }
    }

    public BaseThemeExample getNextAction(MediaItem mediaItem, int index) {
        BaseThemeExample temp = null;
        switch (index % ACTION_COUNT) {
            case 0:
                temp = new LoveSixteenThemeOne((int) mediaItem.getDuration(), width, height);
                break;
            case 1:
                temp = new LoveSixteenThemeTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                temp = new LoveSixteenThemeThree((int) mediaItem.getDuration());
                break;
            case 3:
                temp = new LoveSixteenThemeFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                temp = new LoveSixteenThemeFive((int) mediaItem.getDuration(), width, height);
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
    public void onSurfaceCreated() {
        bgImageFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        bgImageFilter.onSizeChanged(width, height);
        //对显示区域重新赋值
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame() {
        bgImageFilter.draw();
        actionRender.drawFrame();
    }

}