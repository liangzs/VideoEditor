package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan2;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

/**
 * 兄妹节1
 */
public class Rakshabandhan2ThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;
    private int width, height;
    public static float BD_ONE_ZVIEW = DEFAUT_ZVIEW;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public Rakshabandhan2ThemeManager() {
        super();
        bgImageFilter = new ImageOriginFilter();
    }


    @Override
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
    @Override
    public void drawPrepare(MediaItem mediaItem, int index) {
        if (actionRender != null) {
            actionRender.onDestroy();
        }
//        index=0;
        switch (index % 5) {
            case 0:
                actionRender = new RBTwoActionOne((int) mediaItem.getDuration(), width, height);
                break;
            case 1:
                actionRender = new RBTwoActionTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                actionRender = new RBTwoActionThree((int) mediaItem.getDuration(), width, height);
                break;
            case 3:
                actionRender = new RBTwoActionFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                actionRender = new RBTwoActionFive((int) mediaItem.getDuration(), width, height);
                break;
        }
        actionRender.init(mediaItem, width, height);
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }
    }

    @Override
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
//        bgImageFilter.setBackgroundTexture(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.rb_bg));
        bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan2_bg" + ConstantMediaSize.SUFFIX));

    }

    @Override
    public IAction getNextAction(MediaItem mediaItem, int index) {
        IAction temp;
//        index = 0;
        switch (index % 5) {
            case 1:
                temp = new RBTwoActionTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                temp = new RBTwoActionThree((int) mediaItem.getDuration(), width, height);
                break;
            case 3:
                temp = new RBTwoActionFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                temp = new RBTwoActionFive((int) mediaItem.getDuration(), width, height);
                break;
            default:
                temp = new RBTwoActionOne((int) mediaItem.getDuration(), width, height);
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
    @Override
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
    @Override
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
