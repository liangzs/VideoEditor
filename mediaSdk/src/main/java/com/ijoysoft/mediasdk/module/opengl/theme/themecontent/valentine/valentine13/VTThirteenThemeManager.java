package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine13;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
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
public class VTThirteenThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;

    /**
     * 创建缓存对象，离屏buffer
     */

    private int width, height;
    //粒子系统

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public VTThirteenThemeManager() {
        super();
        bgImageFilter = new ImageOriginFilter();
    }


    public void onDestroy() {
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
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
//        actionRender = new BirthDayTwoThemeOne((int) mediaItem.getDuration(),true);
//        index = 4;
        if (actionRender != null) {
            actionRender.onDestroy();
        }
        switch (index % 5) {
            case 0:
                actionRender = new VTThirteenThemeOne((int) mediaItem.getDuration());
                break;
            case 1:
                actionRender = new VTThirteenThemeTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                actionRender = new VTThirteenThemeThree((int) mediaItem.getDuration(), width, height);
                break;
            case 3:
                actionRender = new VTThirteenThemeFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                actionRender = new VTThirteenThemeFive((int) mediaItem.getDuration(), width, height);
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
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_thirteen_theme_169_bg" + ConstantMediaSize.SUFFIX));
                break;
            default:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_thirteen_theme_bg" + ConstantMediaSize.SUFFIX));

                break;
        }
    }

    public BaseThemeExample getNextAction(MediaItem mediaItem, int index) {
        BaseThemeExample temp = null;
//        index = 4;
        switch (index % 5) {
            case 0:
                temp = new VTThirteenThemeOne((int) mediaItem.getDuration());
                break;
            case 1:
                temp = new VTThirteenThemeTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                temp = new VTThirteenThemeThree((int) mediaItem.getDuration(), width, height);
                break;
            case 3:
                temp = new VTThirteenThemeFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                temp = new VTThirteenThemeFive((int) mediaItem.getDuration(), width, height);
                break;
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
        LogUtils.i("render", "onSizeChanged....");
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        bgImageFilter.draw();
        actionRender.drawFrame();
    }


}
