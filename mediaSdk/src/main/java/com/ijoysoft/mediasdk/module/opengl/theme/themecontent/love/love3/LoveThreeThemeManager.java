package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love3;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * @ author       lfj
 * @ date         2020/10/20
 * @ description
 */
public class LoveThreeThemeManager extends BaseThemeManager {

    /**
     * 创建缓存对象，离屏buffer
     */

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
//        index = 0;
        switch (index % 5) {
            case 0:
                actionRender = new LoveThreeThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new LoveThreeThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                actionRender = new LoveThreeThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new LoveThreeThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                actionRender = new LoveThreeThemeFive((int) mediaItem.getDuration(), true, width, height);
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

    }

    public IAction getNextAction(MediaItem mediaItem, int index) {
//        index = 0;
        IAction temp = null;
        switch (index % 5) {
            case 0:
                temp = new LoveThreeThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                temp = new LoveThreeThemeTwo((int) mediaItem.getDuration(), true, width, height);
                break;
            case 2:
                temp = new LoveThreeThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                temp = new LoveThreeThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            case 4:
                temp = new LoveThreeThemeFive((int) mediaItem.getDuration(), true, width, height);
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
//        bgImageFilter.setBackgroundTexture(resources, R.mipmap.them_birthday_bg);
    }


    @Override
    public void onSurfaceCreated() {
//        bgImageFilter.create();
//        pureColorFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//        bgImageFilter.onSizeChanged(width, height);
//        pureColorFilter.setSize(width, height);
        //对显示区域重新赋值
        LogUtils.i("render", "onSizeChanged....");
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame() {
//        if (isPureBg) {
//            pureColorFilter.draw();
//        } else {
//            bgImageFilter.draw();
//        }
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