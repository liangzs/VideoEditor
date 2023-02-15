package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday12;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * 合成超过5张出空白的问题解决了，时长用tempdruation，在mediaclipper中的startPhotoCodec也用tempdruation
 */
public class BDTwelveThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;
    // 是否为纯色背景

    /**
     * 创建缓存对象，离屏buffer
     */

    private int width, height, offsetX, offsetY;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public BDTwelveThemeManager() {
        super();
        bgImageFilter = new ImageOriginFilter();
    }


    public void onDestroy() {
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
        if(actionRender!=null){
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
                actionRender = new BDTwelveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new BDTwelveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new BDTwelveThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new BDTwelveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new BDTwelveThemeFive((int) mediaItem.getDuration(), true);
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
//        index = 4;
        IAction temp = null;
        switch (index % 5) {
            case 0:
                temp = new BDTwelveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                temp = new BDTwelveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                temp = new BDTwelveThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                temp = new BDTwelveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                temp = new BDTwelveThemeFive((int) mediaItem.getDuration(), true);
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
    public void initBackgroundTexture () {
        bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_twelve_theme_bg" + ConstantMediaSize.SUFFIX));
    }


    @Override
    public void onSurfaceCreated() {
        bgImageFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        bgImageFilter.onSizeChanged(width, height);
        //对显示区域重新赋值
        LogUtils.i("render", "onSizeChanged....");
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

    }

    @Override
    public void onDrawFrame() {
        bgImageFilter.draw();
        actionRender.drawFrame();
        //是否滤镜展示
    }


}
