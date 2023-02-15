package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding5;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * @ author       lfj
 * @ date         2020/10/24
 * @ description
 */
public class WeddingFiveThemeManager extends BaseThemeManager {

    private int width, height;


    public void onDestroy() {
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
//        index = 3;
        switch (index % 4) {
            case 0:
                actionRender = new WedFiveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new WedFiveThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new WedFiveThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new WedFiveThemeFour((int) mediaItem.getDuration(), true, width, height);
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
//        index = 3;
        switch (index % 4) {
            case 0:
                temp = new WedFiveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                temp = new WedFiveThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                temp = new WedFiveThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                temp = new WedFiveThemeFour((int) mediaItem.getDuration(), true, width, height);
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
    }


    @Override
    public void onSurfaceCreated() {
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        //对显示区域重新赋值
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame() {
        actionRender.drawFrame();
    }

    /**
     * 渲染尾部片段，同时上下两个同时展示
     */
    public void drawEndOffset() {
    }

}
