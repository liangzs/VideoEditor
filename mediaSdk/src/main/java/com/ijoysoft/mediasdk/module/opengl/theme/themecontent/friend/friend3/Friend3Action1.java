package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend3;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 朋友3
 */
public class Friend3Action1 extends BaseBlurThemeTemplate {


    public Friend3Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }




    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        widgets[0] = buildNewFadeInOutTemplateAnimate();
        widgets[1] = buildNewFadeInOutTemplateAnimate();

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //红
                    {0.8f, 0.4f, 0.3f, 0.3f, 0.3f},
                    //蓝
                    {-0.9f, -0.5f, 0.3f, 0.3f, 0.3f},
                    //标题
                    {0f, 0.7f, 1.2f, 2.4f, 1.2f},
            };
        }
        return widgetsMeta;
    }


    /**
     * 动画顺着标题文字基线移动
     * 宽高变化时动画坐标变化，保证基线斜率相同
     * @param width 宽
     * @param height 高
     */
    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);

        float deltaY = (width * 16f * 0.1515f) / (height * 9f);

        widgets[2] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        //进入动画
        AnimationBuilder animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(2f, -deltaY, 0f, 0f);
        widgets[2].setEnterAnimation(animationBuilder.build());
        animationBuilder = new AnimationBuilder(DEFAULT_ENTER_TIME);
        animationBuilder.setIsNoZaxis(true);
        animationBuilder.setCoordinate(0f, 0f, -2f, deltaY);
        widgets[2].setOutAnimation(animationBuilder.build());
        widgets[2].setZView(zView);
    }

}
