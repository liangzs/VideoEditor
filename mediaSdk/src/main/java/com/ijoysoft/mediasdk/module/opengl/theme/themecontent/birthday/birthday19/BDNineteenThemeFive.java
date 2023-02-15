package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday19;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

import java.util.List;

/**
 * 左下角z轴翻滚进，右y轴翻转出
 */
public class BDNineteenThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private int width, height;
    private final int endConorOffset = -10;
    private final int endConor = 350;

    public BDNineteenThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).setZView(BDNineteenThemeManager.Z_VIEW).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, BDNineteenThemeManager.Z_VIEW, 0, 0).setZView(BDNineteenThemeManager.Z_VIEW).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setWidthHeight(width, height).setZView(BDNineteenThemeManager.Z_VIEW).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, endConor, endConor).build();
        this.width = width;
        this.height = height;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime, 0, DEFAULT_OUT_TIME);
        widgetOne.init(null, null, null, width, height);

        widgetTwo = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, 2500, DEFAULT_OUT_TIME, true);
        widgetTwo.setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 1.0f, 0, 0).setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).
                setCoordinate(0, 0.0f, 0, 1.5f).setIsWidget(true).setIsNoZaxis(true).build());
        widgetTwo.setZView(0);

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
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        float scaleX = width < height ? 1.2f : (width == height ? 1.5f : 2);
        float centerX = 0;
        float centerY = width < height ? 0.85f : (width == height ? 0.78f : 0.72f);
        widgetTwo.init(mimaps.get(0), width, height);
        widgetTwo.adjustScaling(width, height,
                mimaps.get(0).getWidth(), mimaps.get(0).getHeight(), centerX, centerY, scaleX, scaleX);
    }


    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
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
    }
}
