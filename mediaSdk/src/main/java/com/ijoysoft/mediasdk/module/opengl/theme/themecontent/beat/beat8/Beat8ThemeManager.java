package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat8;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.ColorUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;

import java.util.Arrays;

/**
 * 图片层的背景渲染，逻辑从imageplayer中抽出
 * 目的是把背景封装，暴露纹理接口，即外部调用的时候只需传入纹理id便可
 * <p>
 * *****这里重点记录一下，actionRender目前是没有做回收，android9的手机中对纹理进行回收之后，会出现纯黑，
 * 所以这里暂时不回收让系统自己回收
 */
public class Beat8ThemeManager extends BaseThemeManager {
    private ImageOriginFilter bgImageFilter;

    /**
     * 创建缓存对象，离屏buffer
     */

    private int width, height;

    public Beat8ThemeManager() {
        bgImageFilter = new ImageOriginFilter();
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
        if (actionRender != null) {
            actionRender.onDestroy();
        }
        if (bgImageFilter != null) {
            bgImageFilter.onDestroy();
        }
    }


    public void setIsPureColor() {
        String hexColor = "#FF1F170C";
        int[] value = ColorUtil.hex2Rgb(hexColor);
        if (ObjectUtils.isEmpty(value)) {
            return;
        }
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
        switch (index % 5) {
            case 0:
                actionRender = new Beat8ThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new Beat8ThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new Beat8ThemeThree((int) mediaItem.getDuration(), width, height, true);
                break;
            case 3:
                actionRender = new Beat8ThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new Beat8ThemeFive((int) mediaItem.getDuration(), true);
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
//        bgImageFilter.draw();
        actionRender.drawLast();
        bgImageFilter.draw();
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
                temp = new Beat8ThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                temp = new Beat8ThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                temp = new Beat8ThemeThree((int) mediaItem.getDuration(), width, height, true);
                break;
            case 3:
                temp = new Beat8ThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                temp = new Beat8ThemeFive((int) mediaItem.getDuration(), true);
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
//        bgImageFilter.setBackgroundTexture(resources, R.mipmap.vt_six_theme_bg);
        Bitmap bitmap = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_eight_border");
        bgImageFilter.setBackgroundTexture(bitmap);
        bgImageFilter.setVertex(adjustScaling(width, height, bitmap.getWidth(), bitmap.getHeight(), -0.7f, -0.8f, 1f, 1f));
    }

    /**
     * 不变形顶点
     */
    private float[] adjustScaling(int showWidth, int showHeight, int framewidth, int frameheight, float centerX, float centerY, float scalex, float scaley) {
        //图片宽高相对渲染宽高缩小的程度
        float ratio1 = (float) showWidth / framewidth;
        float ratio2 = (float) showHeight / frameheight;
        //哪一个轴缩放的程度最小，就说明等比例拉伸后该轴长先达到渲染(宽/高)的长度
        float ratioMax = Math.min(ratio1, ratio2);
        // 居中后图片显示的大小
        /**
         * @author hayring将该图片等比拉伸至刚才测量的值，即某一方向上的长度先达到与渲染（长/宽）相等
         */
        int imageWidthNew = Math.round(framewidth * ratioMax);
        int imageHeightNew = Math.round(frameheight * ratioMax);
        //频繁打印的日志放至-verbose级别中
//        LogUtils.v("adjustScaling", "outputWidth:" + (float) showWidth + ",outputHeight:" + (float) showHeight + ",imageWidthNew:"
//                + imageWidthNew + ",imageHeightNew:" + imageHeightNew);
        // 图片被拉伸的比例,以及显示比例时的偏移量
        float ratioWidth = imageWidthNew / (float) showWidth;
        float ratioHeight = imageHeightNew / (float) showHeight;
        // 根据拉伸比例还原顶点
        float[] cube = new float[]{centerX - ratioWidth / scalex, centerY + ratioHeight / scaley, centerX - ratioWidth / scalex, centerY - ratioHeight / scaley,
                centerX + ratioWidth / scalex, centerY + ratioHeight / scaley, centerX + ratioWidth / scalex, centerY - ratioHeight / scaley};
        //频繁打印的日志放至-verbose级别中
        LogUtils.v("adjustScaling", "cube:" + Arrays.toString(cube));
        return cube;
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

    }

    /**
     * 渲染尾部片段，同时上下两个同时展示
     */
    public void drawEndOffset() {
    }
}
