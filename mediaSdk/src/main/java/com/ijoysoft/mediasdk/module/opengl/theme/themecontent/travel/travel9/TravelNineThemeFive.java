package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel9;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEaseOutElastic;

import java.util.List;

/**
 * 左下角z轴翻滚进，右y轴翻转出
 */
public class TravelNineThemeFive extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private int width, height;
    private final int endConorOffset = -10;

    private BaseThemeExample widgetTwo;

    public TravelNineThemeFive(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 3000, DEFAULT_OUT_TIME);
        stayAction = new AnimationBuilder(3000).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).setZView(TravelNineThemeManager.Z_VIEW).build();
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, TravelNineThemeManager.Z_VIEW, 0, 0).setZView(TravelNineThemeManager.Z_VIEW).
                setConors(AnimateInfo.ROTATION.X_ANXIS_RE, 0, 360).setBgConor(endConorOffset).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setWidthHeight(width, height).setZView(TravelNineThemeManager.Z_VIEW).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, endConorOffset, endConorOffset).build();
        this.width = width;
        this.height = height;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, totalTime, 0, DEFAULT_OUT_TIME);
        widgetOne.init(null, null, null, width, height);

        widgetTwo = new BaseThemeExample(totalTime, 3000, 0, DEFAULT_ENTER_TIME, true);
        widgetTwo.setZView(0);
        widgetTwo.setEnterAnimation(new EnterEaseOutElastic(new AnimationBuilder(3000).setIsNoZaxis(true).setZView(0).
                setCoordinate(0f, -1, 0, 0).setOrientation(AnimateInfo.ORIENTATION.TOP)));
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setIsNoZaxis(true).setZView(0).
                setCoordinate(0, 0, 0, -1).build());
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
        float centerX = width < height ? 0.65f : (width == height ? 0.7f : 0.65f);
        float centerY = width < height ? -0.7f : (width == height ? -0.65f : -0.6f);
        float scaleX = width < height ? 4 : (width == height ? 4 : 4);
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
                setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(TravelNineThemeManager.Z_VIEW).build());
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
