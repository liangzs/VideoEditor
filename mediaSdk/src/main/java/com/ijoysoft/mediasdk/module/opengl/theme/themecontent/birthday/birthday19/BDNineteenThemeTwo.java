package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday19;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayShakeAnimation;

import java.util.List;

/**
 * 左y轴翻转进，左下角出z轴翻转出
 */
public class BDNineteenThemeTwo extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private int width, height;
    private final int endConorOffset = -10;

    public BDNineteenThemeTwo(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_ENTER_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).setZView(BDNineteenThemeManager.Z_VIEW).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(BDNineteenThemeManager.Z_VIEW).setCoordinate(0, BDNineteenThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 0, 360).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(BDNineteenThemeManager.Z_VIEW).setWidthHeight(width, height).setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).build();
        this.width = width;
        this.height = height;
    }


    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime, 0, DEFAULT_OUT_TIME);
        widgetOne.init(null, null, null, width, height);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetTwo.setZView(-2.5f);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setFade(0, 1f).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(1, 0f).setZView(-2.5f).build());

        widgetThree = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME);
        widgetThree.setZView(-2.5f);
        widgetThree.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(-2.5f).setFade(0, 1f).build());
        widgetThree.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setFade(1, 0f).setZView(-2.5f).build());
    }

    @Override
    protected void drawFramePre() {
        if (widgetOne.getTexture() != 0 && widgetOne.getTexture() != -1) {
            widgetOne.drawFrame();
        }
    }

    @Override
    public void drawWiget() {
        widgetTwo.drawFrame();
        widgetThree.drawFrame();
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 5f : (width == height ? 5f : 5f);
        float centerX = width < height ? -0.8f : (width == height ? -0.8f : -0.8f);
        float centerY = width < height ? -0.7f : (width == height ? -0.7f : -0.7f);
        widgetTwo.init(mimaps.get(0), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
        widgetTwo.setStayAction(new StayShakeAnimation(3000, width, height, -0.8f, -0.7f, 10, -2.5f));

        scaleX = width < height ? 2.5f : (width == height ? 3f : 4f);
        centerX = width < height ? -0.65f : (width == height ? -0.7f : -0.65f);
        centerY = width < height ? 0.92f : (width == height ? 0.85f : 0.8f);
        widgetThree.init(mimaps.get(1), width, height);
        widgetThree.adjustScaling(width, height,
                mimaps.get(1).getWidth(), mimaps.get(1).getHeight(), centerX, centerY, scaleX, scaleX);
    }


    @Override
    public void setPreTeture(List<Integer> listTexture, List<Integer> listConors, List<float[]> listPos, List<GPUImageFilter> listAfilter) {
        if (ObjectUtils.isEmpty(listTexture) || ObjectUtils.isEmpty(listConors) || ObjectUtils.isEmpty(listPos) || ObjectUtils.isEmpty(listAfilter)) {
            return;
        }
        widgetOne.setOriginTexture(listTexture.get(0));
        widgetOne.setVertex(listPos.get(0));
        widgetOne.setFilter(listAfilter.get(0));
        widgetOne.setAfilterVertex(listPos.get(0));
        int conor = listConors.get(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(totalTime).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(BDNineteenThemeManager.Z_VIEW).build());
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

    @Override
    public void onDestroy() {
        GLES20.glDeleteProgram(mProgram);
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}
