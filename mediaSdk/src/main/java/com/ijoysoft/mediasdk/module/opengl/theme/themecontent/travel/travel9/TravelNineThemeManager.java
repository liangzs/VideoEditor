package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel9;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

import java.util.ArrayList;
import java.util.List;

/**
 * imageplayer中处理时，取消进出场的交割方案，换成单元集合方式，一个集合动态包含1-3个单元。
 */
public class TravelNineThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;
    private List<Integer> listTexture;
    private List<Integer> listconor;
    private List<float[]> listPos;
    private List<GPUImageFilter> listAfilters;
    /**
     * 创建缓存对象，离屏buffer
     */

    private int width, height;

    public static float Z_VIEW = -4.5f;

    //由于bitmap的处理比较延迟，所以在对象初始化的时候把这些数据准备好，以防用到时卡动画
    public TravelNineThemeManager() {
        bgImageFilter = new ImageOriginFilter();
        listTexture = new ArrayList<>();
        listPos = new ArrayList<>();
        listconor = new ArrayList<>();
        listAfilters = new ArrayList<>();
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
     * 三人成虎，三人成行，三个女人一条街
     *
     * @param mediaItem
     * @param index
     */
    public void drawPrepare(MediaItem mediaItem, int index) {
//        index = 5;
        if (actionRender != null) {
            switch (index % 3) {
                case 0:
                    if (!listTexture.isEmpty()) {
                        int[] array = new int[listTexture.size()];
                        for (int i = 0; i < listTexture.size(); i++) {
                            array[i] = listTexture.get(i);
                        }
                        GLES20.glDeleteTextures(1, array, 0);
                    }
                    listTexture.clear();
                    listPos.clear();
                    listconor.clear();
                    break;
                case 1:
                case 2:
                    if (ObjectUtils.isEmpty(actionRender.getPos())) {
                        return;
                    }
                    listTexture.add(actionRender.getTexture());
                    listconor.add(actionRender.getConor());
                    listPos.add(actionRender.getPos());
                    break;
            }
            actionRender.onDestroy();
        }

        switch (index % 6) {
            case 0:
                actionRender = new TravelNineThemeOne((int) mediaItem.getDuration(), width, height);
                break;
            case 1:
                actionRender = new TravelNineThemeTwo((int) mediaItem.getDuration(), width, height);
                break;
            case 2:
                actionRender = new TravelNineThemeThree((int) mediaItem.getDuration(), width, height);
                break;
            case 3:
                actionRender = new TravelNineThemeFour((int) mediaItem.getDuration(), width, height);
                break;
            case 4:
                actionRender = new TravelNineThemeFive((int) mediaItem.getDuration(), width, height);
                break;
            case 5:
                actionRender = new TravelNineThemeSix((int) mediaItem.getDuration(), width, height);
                break;
        }
        actionRender.init(mediaItem, width, height);
        if (mediaItem.getAfilter() != null) {
            actionRender.setFilter(mediaItem.getAfilter());
        }

    }

    @Override
    public void setPreAfilter(List<MediaItem> list, int index) {
        switch (index % 3) {
            case 0:
                listAfilters.clear();
                break;
            case 1:
            case 2:
                listAfilters.add(list.get(index - 1).getAfilter());
                break;
        }
        actionRender.setPreTeture(listTexture, listconor, listPos, listAfilters);
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
        Z_VIEW = -5.5f;
        switch (ConstantMediaSize.ratioType) {
            case _9_16:
            case _3_4:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_nine_bg916" + ConstantMediaSize.SUFFIX));
                Z_VIEW = -3.5f;
                break;
            case _1_1:
                Z_VIEW = -4f;
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_nine_bg11" + ConstantMediaSize.SUFFIX));
                break;
            case _16_9:
            case _4_3:
                Z_VIEW = -4f;
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_nine_bg169" + ConstantMediaSize.SUFFIX));
                break;
            default:
                bgImageFilter.setBackgroundTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/travel_nine_bg916" + ConstantMediaSize.SUFFIX));
                Z_VIEW = -4.5f;
                break;
        }
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

    @Override
    public IAction getNextAction(MediaItem mediaItem, int index) {
        return null;
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
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        bgImageFilter.draw();
        actionRender.drawFrame();
    }


}
