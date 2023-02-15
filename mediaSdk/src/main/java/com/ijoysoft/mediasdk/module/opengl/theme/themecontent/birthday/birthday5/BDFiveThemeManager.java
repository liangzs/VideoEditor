package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5;

import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * 合成超过5张出空白的问题解决了，时长用tempdruation，在mediaclipper中的startPhotoCodec也用tempdruation
 */
public class BDFiveThemeManager extends BaseThemeManager {


    private int width, height, offsetX, offsetY;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public BDFiveThemeManager() {
    }

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
        if(actionRender!=null){
            actionRender.onDestroy();
        }
    }


    public void setIsPureColor() {
        String hexColor = "#FF2d406f";
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
//        index = 4;
        switch (index % 5) {
            case 0:
                actionRender = new BDFiveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new BDFiveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new BDFiveThemeThree((int) mediaItem.getDuration(), width, height, true);
                break;
            case 3:
                actionRender = new BDFiveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new BDFiveThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        actionRender.init(mediaItem,width, height);
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
//        index = 4;
        IAction temp = null;
        switch (index % 5) {
            case 0:
                temp = new BDFiveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                temp = new BDFiveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                temp = new BDFiveThemeThree((int) mediaItem.getDuration(), width, height, true);
                break;
            case 3:
                temp = new BDFiveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                temp = new BDFiveThemeFive((int) mediaItem.getDuration(), true);
                break;
        }
        temp.init(mediaItem,width, height);
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
        pureColorFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//        bgImageFilter.onSizeChanged(width, height);
        pureColorFilter.setSize(width, height);
        //对显示区域重新赋值
        LogUtils.i("render", "onSizeChanged....");
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

    }

    @Override
    public void onDrawFrame() {
        pureColorFilter.draw();
        actionRender.drawFrame();
        //是否滤镜展示
    }


}
