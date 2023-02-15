package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love15;

import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * 图片层的背景渲染，逻辑从imageplayer中抽出
 * 目的是把背景封装，暴露纹理接口，即外部调用的时候只需传入纹理id便可
 * <p>
 * *****这里重点记录一下，actionRender目前是没有做回收，android9的手机中对纹理进行回收之后，会出现纯黑，
 * 所以这里暂时不回收让系统自己回收
 */
public class LoveFifteenThemeManager extends BaseThemeManager {

    private int width, height;


    public void onDestroy() {
        if (actionRender != null) {
            actionRender.onDestroy();
        }
    }

    public void setIsPureColor() {
        String hexColor = "#CEB768";
        int[] value = ColorUtil.hex2Rgb(hexColor);
        float rgba[] = new float[4];
        rgba[0] = ((float) value[0] / 255f);
        rgba[1] = ((float) value[1] / 255f);
        rgba[2] = ((float) value[2] / 255f);
        rgba[3] = ((float) value[3] / 255f);
        pureColorFilter.setIsPureColor(rgba);
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
        switch (index % 7) {
            case 0:
                actionRender = new LoveFifteenThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new LoveFifteenThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new LoveFifteenThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new LoveFifteenThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new LoveFifteenThemeFive((int) mediaItem.getDuration(), true);
                break;
            case 5:
                actionRender = new LoveFifteenThemeSix((int) mediaItem.getDuration(), true);
                break;
            case 6:
                actionRender = new LoveFifteenThemeSeven((int) mediaItem.getDuration(), true);
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
//        bgImageFilter.draw();
        actionRender.drawLast();
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

    }

    public IAction getNextAction(MediaItem mediaItem, int index) {
        IAction temp = null;
        switch (index % 7) {
            case 0:
                temp = new LoveFifteenThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                temp = new LoveFifteenThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                temp = new LoveFifteenThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                temp = new LoveFifteenThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                temp = new LoveFifteenThemeFive((int) mediaItem.getDuration(), true);
                break;
            case 5:
                temp = new LoveFifteenThemeSix((int) mediaItem.getDuration(), true);
                break;
            case 6:
                temp = new LoveFifteenThemeSeven((int) mediaItem.getDuration(), true);
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
//        bgImageFilter.setBackgroundTexture(resources, R.mipmap.vt_six_theme_bg);
    }


    @Override
    public void onSurfaceCreated() {
//        bgImageFilter.create();
        pureColorFilter.create();

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//        bgImageFilter.onSizeChanged(width, height);
        pureColorFilter.setSize(width, height);
        //对显示区域重新赋值
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame() {
        pureColorFilter.draw();
        actionRender.drawFrame();
    }

    /**
     * 渲染尾部片段，同时上下两个同时展示
     */
    public void drawEndOffset() {
    }
}
