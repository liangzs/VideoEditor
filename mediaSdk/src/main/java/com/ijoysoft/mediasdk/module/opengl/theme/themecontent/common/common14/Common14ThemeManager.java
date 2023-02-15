package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common14;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.IBaseTimeCusFragmeng;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseTimeThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.ActionBlurType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustom;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseCustomNoOffset;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseInMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutCubic;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutMultiple;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EaseOutPow;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.Easings;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.WaveOut;

import java.util.ArrayList;

/**
 * 片段铺满展示，不展示模糊背景
 * 动态组合动画，完成片段的展示。片段切换时，一个切换动画组合，二是切换片段纹理
 */
public class Common14ThemeManager extends BaseTimeThemeManager implements IBaseTimeCusFragmeng {
    private final String CUS_LOCATION = " uniform float wavePrograss;//0-0.1f  \n";
    private final String CUS_METHOD = "vec2 offset(float progress, float x) {\n" +
            //3.14*5=15.7075
            "  float shifty = 0.03*(progress)*sin(6.28*(progress+x));\n" +
            "  return vec2(0, shifty);\n" +
            "}\n";
    private final String CUS_BODY = "coord=textureCoordinate+offset(wavePrograss,textureCoordinate.x);\n";
    private int waveLocation;

    public Common14ThemeManager() {
    }


    @Override
    protected ThemeWidgetInfo createWidgetByIndex(int index) {
        return null;
    }

    @Override
    protected void intWidget(int index, WidgetTimeExample widgetTimeExample) {

    }


    @Override
    protected boolean blurBackground() {
        return true;
    }

    @Override
    public boolean isTextureInside() {
        return true;
    }


    /**
     * 添加 自定义fragment
     *
     * @return
     */
    @Override
    protected String customFragment() {
        setBaseTimeCusFragmeng(this);
        StringBuffer sb = new StringBuffer();
        sb.append(BaseTimeThemeExample.FRAGMENT_HEAD).append(CUS_LOCATION)
                .append(CUS_METHOD).append(BaseTimeThemeExample.FRAGMENT_MAIN)
                .append(CUS_BODY).append(BaseTimeThemeExample.FRAGMENT_BODY)
                .append(BaseTimeThemeExample.FRAGMENT_END_BLACKET);
        LogUtils.v("", sb.toString());
        return sb.toString();
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return switch (index % getMipmapsCount()) {
     * //左右移动
     * case 0:
     * case 1:
     * return 2000;
     * default:
     * return 1600;
     * }
     */
    @Override
    protected ArrayList<BaseEvaluate> createAnimateEvaluate(int index, long duration) {
        ArrayList<BaseEvaluate> list = new ArrayList<>();
        switch (index % createActions()) {
            case 0:
                //模糊缩小，等待，放大出
                list.add(new AnimationBuilder((int) (640), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(5, 0, true)
                        .setScale(1.7f, 1.0f)
                        .build());
                list.add(new AnimationBuilder((int) (duration - 1280), width, height, true).build());
                list.add(new AnimationBuilder((int) (640), width, height, true)
                        .setScaleAction(new EaseInMultiple(8))
                        .setRatioBlurRange(0, 5, false)
                        .setScale(1.0f, 1.7f)
                        .build());
                break;
            case 1:
            case 3:
                //缩小波浪进，停顿,右下滑出
                list.add(new AnimationBuilder((int) (800), width, height, true)
                        .setScaleAction(new EaseOutCubic())
                        .setRatioBlurRange(2, 0, true)
                        .setScale(1.7f, 1.1f)
                        .setCusAction(new EaseOutCubic())
                        .setCusStartEnd(-1, 0)
                        .build());
                list.add(new AnimationBuilder((int) (duration - 1600), width, height, true).build());
                list.add(new AnimationBuilder((int) (800), width, height, true)
                        .setMoveAction(new EaseInCubic(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 0, 10)
                        .setCoordinate(0, 0, 1.0f, 1.0f)
                        .setCusStartEnd(0, 1)
                        .build());
                break;
            case 2:
            case 4:
                //左上进，波浪，放大
                list.add(new AnimationBuilder((int) (1000), width, height, true)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 10, 0)
                        .setCoordinate(-1.0f, -1.0f, 0, 0)
                        .setCusAction(new EaseInMultiple(3))
                        .setCusStartEnd(-1, 0)
                        .build());
                list.add(new AnimationBuilder((int) (duration - 1800), width, height, true)
                        .build());
                list.add(new AnimationBuilder((int) (800), width, height, true)
                        .setScaleAction(new EaseInMultiple(5))
                        .setRatioBlurRange(0, 2, false)
                        .setScale(1.0f, 1.7f)
                        .build());
                break;
            case 5:
                //下掉弹簧。放大
                list.add(new AnimationBuilder((int) (1200), width, height, true)
                        .setMoveAction(new EaseOutCubic(true, false, false))
                        .setMoveBlurRange(ActionBlurType.MOVE_BLUR, 3, 0)
                        .setCoordinate(0, -1.0f, 0, 0)
                        .setScale(0.8f, 0.8f)
                        .setIsSprintX(true)
                        .setMoveActionX(new EaseCustomNoOffset(x -> Easings.easeSpringTwice(x)))
                        .build());
                list.add(new AnimationBuilder((int) (duration - 1800), width, height, true).build());
                list.add(new AnimationBuilder((int) (600), width, height, true)
                        .setMoveAction(new EaseInCubic(false, true, false))
                        .setScale(0.8f, 1.7f)
                        .setMoveBlurRange(ActionBlurType.RATIO_BLUR, 0, 1)
                        .build());
                break;
        }
        return list;
    }

    @Override
    protected int createActions() {
        return 6;
    }


    @Override
    public void initLocation(int programe) {
        waveLocation = GLES20.glGetUniformLocation(programe, "wavePrograss");
    }

    @Override
    public void drawLocation(BaseEvaluate baseEvaluate) {
//        LogUtils.v("", "baseEvaluate.getCustomValue():" + baseEvaluate.getCustomValue());
        GLES20.glUniform1f(waveLocation, baseEvaluate.getCustomValue());
    }
}
