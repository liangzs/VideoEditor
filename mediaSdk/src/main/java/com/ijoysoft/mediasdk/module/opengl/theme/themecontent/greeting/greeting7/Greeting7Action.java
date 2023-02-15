package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting7;

import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 问候7
 */
public class Greeting7Action extends BaseBlurThemeTemplate {


    public Greeting7Action(int totalTime, int width, int height, int index) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
        this.index = index % MOD;
        initWidgetAfter();
    }

    /**
     * 当前顺序
     */
    int index;

    /**
     * 不同种类数量
     */
    private static final int MOD = 5;



    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    public void initWidgetAfter() {
        widgets[0] = buildNoneAnimationWidget();
        switch (index) {
            case 0 :
                widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
                widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.TOP);
                break;
            case 1:
                widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.TOP);
                widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
                break;
            case 2:
                widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
                widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
                break;
            case 3:
                widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
                widgets[2] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
                break;
            case 4:
                widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
                widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);
                break;
            default:throw new IllegalStateException("index could not be out of 0-4");
        }
    }

    /**
     * 空间元数据
     */
    private static final float[][] widgetsMeta = new float[][]{
            //边框
            {0f, 0f, 1f, 1f, 1f},
            //标题1
            {0f, 0.53f, 1.13f, 2.3f, 1.5f},
            //标题2
            {0f, Float.MAX_VALUE, 2f, 5f, 3f},
    };



    @Override
    protected float[][] getWidgetsMeta() {
        if (width < height) {
            if (RatioType.FloatCompare.considerEqual((float) width / (float) height, RatioType.CONST_9_16)) {
                widgetsMeta[1][1] = 0.53f;
            } else {
                widgetsMeta[1][1] = 0.43f;
            }
        } else if (height < width) {
            if (RatioType.FloatCompare.considerEqual((float) width / (float) height, RatioType.CONST_16_9)) {
                widgetsMeta[1][1] = 0.3f;
            } else {
                widgetsMeta[1][1] = 0.35f;
            }
        } else {
            widgetsMeta[1][1] = 0.378f;
        }
        return widgetsMeta;
    }


    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 2) {
            if (RatioType.getNotNoneRatioType() == RatioType._16_9) {
                widgets[2].setVertex(transitionRatioPosArray(widgets[2].getCube(), 0f, -1.3f));
            } else if (RatioType.getNotNoneRatioType() == RatioType._4_3) {
                widgets[2].setVertex(transitionRatioPosArray(widgets[2].getCube(), 0f, -1.9f));
            }
        }
    }
}
