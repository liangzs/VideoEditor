package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.RepeatAroundFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.List;

/**
 * 生日26
 */
public class Birthday26Action2 extends BaseBlurThemeTemplate {




    public Birthday26Action2(int totalTime, int width, int height) {
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
        widgets[2] = buildNewFadeInOutTemplateAnimate();
        widgets[3] = buildNewScaleInOutTemplateAnimateAtSelfCenter(3);

    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;



    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //暗色效果
                    {0f, Float.MAX_VALUE, 1f, 1f},
                    //紫色效果
                    {-1f, -0.1f, 0.7f, 1.4f},
                    //黄色效果
                    {0.25f, -1f, 0.6f, 1.2f},
                    //标题
                    {0f, 0.65f, 1.1f, 2.2f}
            };
        }
        return widgetsMeta;
    }



}
