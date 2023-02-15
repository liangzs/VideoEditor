package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday4;

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
public class HDFourThemeManager extends BaseThemeManager {
    /**
     * 创建缓存对象，离屏buffer
     */

    private int width, height;
    private ImageOriginFilter bgImageFilter;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public HDFourThemeManager() {
        super();
//        pureColorFilter = new PureColorFilter();
        bgImageFilter = new ImageOriginFilter();
    }


    public void onDestroy() {
//        if (pureColorFilter != null) {
//            pureColorFilter.onDestroy();
//        }
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
//        index = 1;
        switch (index % 6) {
            case 0:
                actionRender = new HDFourThemeOne((int) mediaItem.getDuration());
                break;
            case 1:
                actionRender = new HDFourThemeTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                actionRender = new HDFourThemeThree((int) mediaItem.getDuration(), width, height);
                break;
            case 3:
                actionRender = new HDFourThemeFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                actionRender = new HDFourThemeFive((int) mediaItem.getDuration(), width, height);
                break;
            case 5:
                actionRender = new HDFourThemeSix((int) mediaItem.getDuration(), width, height);
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
        bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_four_theme_bg" + ConstantMediaSize.SUFFIX));
//        switch (ConstantMediaSize.ratioType) {
//            case _9_16:
//            case _3_4:
//                bgImageFilter.setBackgroundTexture(MediaSdk.getInstance().getResouce(), R.mipmap.hd_eleven_theme916_bg);
//                break;
//            case _1_1:
//                bgImageFilter.setBackgroundTexture(MediaSdk.getInstance().getResouce(), R.mipmap.hd_eleven_theme11_bg);
//                break;
//            case _16_9:
//            case _4_3:
//                bgImageFilter.setBackgroundTexture(MediaSdk.getInstance().getResouce(), R.mipmap.hd_eleven_theme169_bg);
//                break;
//        }
    }

    public BaseThemeExample getNextAction(MediaItem mediaItem, int index) {
        BaseThemeExample temp = null;
//        index = 1;
        switch (index % 6) {
            case 0:
                temp = new HDFourThemeOne((int) mediaItem.getDuration());
                break;
            case 1:
                temp = new HDFourThemeTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                temp = new HDFourThemeThree((int) mediaItem.getDuration(), width, height);
                break;
            case 3:
                temp = new HDFourThemeFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                temp = new HDFourThemeFive((int) mediaItem.getDuration(), width, height);
                break;
            case 5:
                temp = new HDFourThemeSix((int) mediaItem.getDuration(), width, height);
                break;
        }
        temp.init(mediaItem,width, height);
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
    public void initBackgroundTexture () {
        ratioChange();
    }


    @Override
    public void onSurfaceCreated() {
//        pureColorFilter.create();
        bgImageFilter.create();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//        pureColorFilter.setSize(width, height);
        bgImageFilter.onSizeChanged(width, height);
        //对显示区域重新赋值
        LogUtils.i("render", "onSizeChanged....");
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        pureColorFilter.draw();
        bgImageFilter.draw();
        actionRender.drawFrame();
    }


}
