package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel9;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;

import java.util.List;

/**
 * 右上角x轴翻滚进，x反相下方出
 * one和two承载前两文件展示移动
 */
public class TravelNineThemeThree extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private BaseThemeExample widgetThree;
    private int width, height;

    public TravelNineThemeThree(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 0, DEFAULT_OUT_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setZView(TravelNineThemeManager.Z_VIEW).setCoordinate(TravelNineThemeManager.Z_VIEW, -TravelNineThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, 0, 360).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setZView(TravelNineThemeManager.Z_VIEW).setCoordinate(0, 0f, 1.5f * TravelNineThemeManager.Z_VIEW, 0f).build();
        setZView(TravelNineThemeManager.Z_VIEW);
        this.width = width;
        this.height = height;

        widgetThree = new BaseThemeExample(totalTime, 3000, 0, DEFAULT_ENTER_TIME, true);
        widgetThree.setEnterAnimation(new AnimationBuilder(3000).setIsNoZaxis(true)
                .setZView(0).setCoordinate(-1, 1, 0, 0).build());
        widgetThree.setZView(0);
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 3800, 0, DEFAULT_OUT_TIME);
        widgetTwo = new BaseThemeExample(totalTime, 3400, 0, DEFAULT_OUT_TIME);
        widgetOne.init(null, null, null, width, height);
        widgetTwo.init(null, null, null, width, height);
        widgetOne.setZView(TravelNineThemeManager.Z_VIEW);
        widgetTwo.setZView(TravelNineThemeManager.Z_VIEW);
    }

    @Override
    protected void drawFramePre() {
        if (widgetOne.getTexture() != 0 && widgetOne.getTexture() != -1) {
            widgetOne.drawFrame();
        }
        if (widgetTwo.getTexture() != 0 && widgetTwo.getTexture() != -1) {
            widgetTwo.drawFrame();
        }
    }

    @Override
    public void drawWiget() {
        widgetThree.drawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        widgetThree.init(mimaps.get(0), width, height);
        float scaleW = width < height ? 4f : (width == height ? 5 : 5);
        float centerx = 0.6f;
        float centery = width < height ? -0.7f : -0.6f;
        widgetThree.adjustScaling(width, height, mimaps.get(0).getWidth(),
                mimaps.get(0).getHeight(), centerx, centery, scaleW, scaleW);
    }


    @Override
    public void setPreTeture(List<Integer> listTexture, List<Integer> listConors, List<float[]> listPos, List<GPUImageFilter> listAfilter) {
        if (ObjectUtils.isEmpty(listTexture) || listTexture.size() < 2) {
            return;
        }
        if (ObjectUtils.isEmpty(listConors) || listConors.size() < 2) {
            return;
        }
        if (ObjectUtils.isEmpty(listPos) || listPos.size() < 2) {
            return;
        }
        if (ObjectUtils.isEmpty(listAfilter) || listAfilter.size() < 2) {
            return;
        }
        widgetOne.setOriginTexture(listTexture.get(0));
        widgetOne.setVertex(listPos.get(0));
        widgetOne.setFilter(listAfilter.get(0));
        widgetOne.setAfilterVertex(listPos.get(0));
        int conor = listConors.get(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(3800).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(TravelNineThemeManager.Z_VIEW).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 1.5f * TravelNineThemeManager.Z_VIEW, 0f)
                .setWidthHeight(width, height).setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(TravelNineThemeManager.Z_VIEW).build());

        //two
        widgetTwo.setOriginTexture(listTexture.get(1));
        widgetTwo.setVertex(listPos.get(1));
        widgetTwo.setFilter(listAfilter.get(1));
        widgetTwo.setAfilterVertex(listPos.get(1));
        conor = listConors.get(1);
        widgetTwo.setEnterAnimation(new AnimationBuilder(3400).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(TravelNineThemeManager.Z_VIEW).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 1.5f * TravelNineThemeManager.Z_VIEW, 0f)
                .setWidthHeight(width, height).setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(TravelNineThemeManager.Z_VIEW).build());

    }

    @Override
    public void adjustImageScaling(int width, int height) {
        adjustImageScalingStretch(width, height);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        widgetOne.onDestroy();
        widgetTwo.onDestroy();
        widgetThree.onDestroy();
    }
}