package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding4;

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
public class WeddingFourThemeSix extends BaseThemeExample {
    private BaseThemeExample widgetOne;
    private BaseThemeExample widgetTwo;
    private int width, height;

    public WeddingFourThemeSix(int totalTime, int width, int height) {
        super(totalTime, DEFAULT_ENTER_TIME, 1800, DEFAULT_ENTER_TIME);
        enterAnimation = new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(WeddingFourThemeManager.Z_VIEW, -WeddingFourThemeManager.Z_VIEW, 0, 0).
                setConors(AnimateInfo.ROTATION.Y_ANXIS, 0, 360).setZView(WeddingFourThemeManager.Z_VIEW).setWidthHeight(width, height).build();
        outAnimation = new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 1.5f * WeddingFourThemeManager.Z_VIEW).setZView(WeddingFourThemeManager.Z_VIEW).build();
        setZView(WeddingFourThemeManager.Z_VIEW);
        this.width = width;
        this.height = height;
    }

    @Override
    public void initWidget() {
        widgetOne = new BaseThemeExample(totalTime, 3800, 0, DEFAULT_OUT_TIME);
        widgetTwo = new BaseThemeExample(totalTime, 3400, 0, DEFAULT_OUT_TIME);
        widgetOne.init(null, null, null, width, height);
        widgetTwo.init(null, null, null, width, height);

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
        if (listTexture.get(0) == 0 || listTexture.get(0) == -1) {
            return;
        }
        widgetOne.setOriginTexture(listTexture.get(0));
        widgetOne.setVertex(listPos.get(0));
        widgetOne.setFilter(listAfilter.get(0));
        widgetOne.setAfilterVertex(listPos.get(0));
        int conor = listConors.get(0);
        widgetOne.setEnterAnimation(new AnimationBuilder(3800).setWidthHeight(width, height).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(WeddingFourThemeManager.Z_VIEW).build());
        widgetOne.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 1.5f * WeddingFourThemeManager.Z_VIEW)
                .setWidthHeight(width, height).setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(WeddingFourThemeManager.Z_VIEW).build());
        widgetTwo.setOriginTexture(listTexture.get(1));
        widgetTwo.setVertex(listPos.get(1));
        widgetTwo.setFilter(listAfilter.get(1));
        widgetTwo.setAfilterVertex(listPos.get(1));
        conor = listConors.get(1);
        widgetTwo.setEnterAnimation(new AnimationBuilder(3400).setWidthHeight(width, height).setZView(WeddingFourThemeManager.Z_VIEW).
                setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).build());
        widgetTwo.setOutAnimation(new AnimationBuilder(DEFAULT_OUT_TIME).setCoordinate(0, 0f, 0f, 1.5f * WeddingFourThemeManager.Z_VIEW)
                .setWidthHeight(width, height).setConors(AnimateInfo.ROTATION.Z_ANXIS, conor, conor).setZView(WeddingFourThemeManager.Z_VIEW).build());
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
    }
}
