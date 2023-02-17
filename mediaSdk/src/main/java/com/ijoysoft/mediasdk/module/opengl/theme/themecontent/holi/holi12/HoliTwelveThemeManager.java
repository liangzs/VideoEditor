package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi12;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * 图片层的背景渲染，逻辑从imageplayer中抽出
 * 目的是把背景封装，暴露纹理接口，即外部调用的时候只需传入纹理id便可
 * <p>
 * *****这里重点记录一下，actionRender目前是没有做回收，android9的手机中对纹理进行回收之后，会出现纯黑，
 * 所以这里暂时不回收让系统自己回收
 */
public class HoliTwelveThemeManager extends BaseThemeManager {

    private int width, height;
    private ImageOriginFilter bgImageFilter;

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
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
        if (actionRender != null) {
            actionRender.onDestroy();
        }
    }


    public void setIsPureColor() {
        String hexColor = "#FFEB53F4";
        int[] value = ColorUtil.hex2Rgb(hexColor);
        if (ObjectUtils.isEmpty(value)) {
            return;
        }
        rgba[0] = ((float) value[0] / 255f);
        rgba[1] = ((float) value[1] / 255f);
        rgba[2] = ((float) value[2] / 255f);
        rgba[3] = ((float) value[3] / 255f);
        pureColorFilter.setIsPureColor(rgba);
        bgImageFilter = new ImageOriginFilter();
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
        switch (index % 5) {
            case 0:
                actionRender = new HoliTwelveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new HoliTwelveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new HoliTwelveThemeThree((int) mediaItem.getDuration(), width, height, true);
                break;
            case 3:
                actionRender = new HoliTwelveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new HoliTwelveThemeFive((int) mediaItem.getDuration(), true);
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
        switch (index % 5) {
            case 0:
                temp = new HoliTwelveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                temp = new HoliTwelveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                temp = new HoliTwelveThemeThree((int) mediaItem.getDuration(), width, height, true);
                break;
            case 3:
                temp = new HoliTwelveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                temp = new HoliTwelveThemeFive((int) mediaItem.getDuration(), true);
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
        bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_twelve_border"));
    }


    @Override
    public void onSurfaceCreated() {
        pureColorFilter.create();
        bgImageFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        pureColorFilter.setSize(width, height);
        //对显示区域重新赋值
        this.width = width;
        this.height = height;
        bgImageFilter.onSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame() {
        pureColorFilter.draw();
        actionRender.drawFrame();
        bgImageFilter.draw();
    }

    /**
     * 渲染尾部片段，同时上下两个同时展示
     */
    public void drawEndOffset() {
    }
}