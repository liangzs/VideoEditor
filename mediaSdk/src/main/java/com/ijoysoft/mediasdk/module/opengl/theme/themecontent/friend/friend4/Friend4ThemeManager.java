package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend4;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

public class Friend4ThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;
    private int width, height;
    public static float zView = 0f;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public Friend4ThemeManager() {
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
        actionRender = new Friend4Action((int) mediaItem.getDuration(), width, height, index);
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
        zView = 0f;
//        switch (ConstantMediaSize.ratioType) {
//            case _16_9:
//            case _4_3:
//                bgImageFilter.setBackgroundTexture(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.bd_one_theme_bg169));
//                break;
//            case _3_4:
//            case _9_16:
//                bgImageFilter.setBackgroundTexture(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.bd_one_theme_bg916));
//                break;
//            case _1_1:
//                bgImageFilter.setBackgroundTexture(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.bd_one_theme_bg11));
//                break;
//            default:
        bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend4_bg_9_16" + ConstantMediaSize.SUFFIX));
//                break;
//        }
    }

    public IAction getNextAction(MediaItem mediaItem, int index) {
        IAction temp;
        temp = new Friend4Action((int) mediaItem.getDuration(), width, height, index);
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
