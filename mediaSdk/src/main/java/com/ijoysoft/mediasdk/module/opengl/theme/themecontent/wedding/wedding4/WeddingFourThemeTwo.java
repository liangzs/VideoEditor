package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

import java.util.List;

/**
 * 左y轴翻转进，左下角出z轴翻转出
 */
public class WeddingFourThemeTwo extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private int width, height;
    private final int endConorOffset = -10;

    public WeddingFourThemeTwo(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_ENTER_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).setZView(WeddingFourThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(WeddingFourThemeManager.Z_VIEW).setCoordinate(0, WeddingFourThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 0, 360).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(WeddingFourThemeManager.Z_VIEW).setWidthHeight(width, height).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).build();
        this.width = width;
        this.height = height;
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime, 0, DEFAULT_OUT_TIME);
        widgetOne.init(null, null, null, width, height);
    }

    @Override
    protected void drawFramePre() {
        if (widgetOne.getTexture() != 0 && widgetOne.getTexture() != -1) {
            widgetOne.drawFrame();
        }
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
    }


    @Override
    public void setPreTeture(List<Integer> listTexture, List<Integer> listConors, List<float[]> listPos, List<GPUImageFilter> listAfilter) {
        if (ObjectUtils.isEmpty(listTexture) || ObjectUtils.isEmpty(listConors) || ObjectUtils.isEmpty(listPos) || ObjectUtils.isEmpty(listAfilter)) {
            return;
        }
        if (listTexture.get(0) == 0 || listTexture.get(0) == -1) {
            return;
        }
        widgetOne.setOriginTexture(listTexture.get(0));
        widgetOne.setVertex(listPos.get(0));
        widgetOne.setFilter(listAfilter.get(0));
        widgetOne.setAfilterVertex(listPos.get(0));
        int conor = listConors.get(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(totalTime).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(WeddingFourThemeManager.Z_VIEW).build());
    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }


    @Override
    public int getConor() {
        return endConorOffset;
    }

    @Override
    public float[] getPos() {
        return cube;
    }

//    @Override
//    public void onDestroy() {
//        GLES20.glDeleteProgram(mProgram);
//    }
}
