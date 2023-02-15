package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding12;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 婚礼12
 */
public class Wedding12Action extends BaseBlurThemeTemplate {


    public Wedding12Action(int totalTime, int width, int height, int index) {
        super(totalTime, width, height, true, true );
        this.index = index % 4;
        setCoverWidgetsCount(1, 1);
        initWidgetAfterSuper();
    }

    /**
     * 当前action顺序
     */
    int index;



    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }

    /**
     * 在获取到index后再初始化控件
     */
    public void initWidgetAfterSuper() {
        super.initWidget();
        widgets[0] = buildNoneAnimationWidget();
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.TOP);
        widgets[2] = buildNewFadeInOutTemplateAnimate();
        widgets[3] = buildNewFadeInOutTemplateAnimate();
        widgets[4] = buildNewFadeInOutTemplateAnimate();
        if (index != 2) {
            widgets[5] = buildNewFadeInOutTemplateAnimate();
        }

    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
    }

    /**
     * 空间元数据
     */
    private static final float[][][] WIDGETS_META = new float[][][] {
            {
                //边框
                    {0f, 0f, 1f, 1f, 1f},
                    //玫瑰
                    {Float.MAX_VALUE , 0.7f, 4f, 3f, 4f},
                    //标题
                    {0f, 0.45f, 1.2f, 2f, 1.4f},
                    //粉色效果
                    {-0.75f, 0.75f, 1f, 1f, 1f},
                    //绿色
                    {0.75f, 0f, 1f, 1f, 1f},
                    //黄色
                    {-0.75f, -0.75f, 1f, 1f, 1f},
            },
            {
                    //边框
                    {0f, 0f, 1f, 1f, 1f},
                    //玫瑰
                    {Float.MAX_VALUE , 0.7f, 4f, 3f, 4f},
                    //标题
                    {0f, 0.45f, 1.5f, 2.2f, 1.8f},
                    //橙效果
                    {0.9f, -0.25f, 1f, 1f, 1f},
                    //绿色
                    {-0.9f, 0f, 1f, 1f, 1f},
                    //黄色
                    {0f, -0.8f, 1f, 1f, 1f},
            },
            {
                    //边框
                    {0f, 0f, 1f, 1f, 1f},
                    //玫瑰
                    {Float.MAX_VALUE , 0.7f, 4f, 3f, 4f},
                    //标题
                    {0f, 0.45f, 1.2f, 2f, 1.2f},
                    //橙效果
                    {-0.9f, -0.1f, 1f, 1f, 1f},
                    //黄色
                    {0.9f, 0.25f, 1f, 1f, 1f},
            },
            {
                    //边框
                    {0f, 0f, 1f, 1f, 1f},
                    //玫瑰
                    {Float.MAX_VALUE , 0.7f, 4f, 3f, 4f},
                    //标题
                    {0f, 0.45f, 1.2f, 2f, 1.2f},
                    //粉效果
                    {0.9f, 0.5f, 1f, 1f, 1f},
                    //蓝色
                    {0.25f, -0.75f, 1f, 1f, 1f},
                    //黄色
                    {-0.9f, 0.05f, 1f, 1f, 1f},
            },
    };

//    private static final float[][] TITLE_META = {
//            {0.5f, 0.45f, 0.5f, 0.5f},
//            {0.5f, 0.45f, 0.5f, 0.5f},
//            {0.5f, 0.45f, 0.5f, 0.5f},
//    };





    @Override
    protected float[][] getWidgetsMeta() {
//        if (width < height) {
//            WIDGETS_META[index][2][1] = TITLE_META[0][index];
//        } else if (height < width) {
//            WIDGETS_META[index][2][1] = TITLE_META[1][index];
//        } else {
//            WIDGETS_META[index][2][1] = TITLE_META[2][index];
//        }
        return WIDGETS_META[index];
    }




}
