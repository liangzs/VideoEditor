package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5;

import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

public class GreetingFiveThemeManager extends BaseThemeManager {

    /**
     * 创建缓存对象，离屏buffer
     */

    private int width, height;


    @Override
    public void onDestroyFragment() {
        if (actionRender != null) {
            actionRender.onDestroy();
        }
    }
    public void onDestroy() {
        if (pureColorFilter != null) {
            pureColorFilter.onDestroy();
        }
        if (actionRender != null) {
            actionRender.onDestroy();
        }
    }


    public void setIsPureColor() {
        String hexColor = "#FF204374";
        int[] value = ColorUtil.hex2Rgb(hexColor);
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
//        index = 3;
        switch (index % 4) {
            case 0:
                actionRender = new GreetFiveThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                actionRender = new GreetFiveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new GreetFiveThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                actionRender = new GreetFiveThemeFour((int) mediaItem.getDuration(), true, width, height);
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
//        index = 3;
        IAction temp;
        switch (index % 4) {
            case 0:
                temp = new GreetFiveThemeOne((int) mediaItem.getDuration(), true);
                break;
            case 1:
                temp = new GreetFiveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                temp = new GreetFiveThemeThree((int) mediaItem.getDuration(), true, width, height);
                break;
            case 3:
                temp = new GreetFiveThemeFour((int) mediaItem.getDuration(), true, width, height);
                break;
            default:
                temp = new GreetFiveThemeOne((int) mediaItem.getDuration(), true);
        }
        temp.init(mediaItem, width, height);
        if (mediaItem.getAfilter() != null) {
            temp.setFilter(mediaItem.getAfilter());
        }
        return temp;
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
        pureColorFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        //对显示区域重新赋值
        this.width = width;
        this.height = height;
        pureColorFilter.setSize(width, height);

    }

    @Override
    public void onDrawFrame() {
//        pureColorFilter.draw();
        actionRender.drawFrame();
    }


}
