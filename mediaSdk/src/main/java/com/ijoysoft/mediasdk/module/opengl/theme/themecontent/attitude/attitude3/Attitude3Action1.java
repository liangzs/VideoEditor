package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Collections;
import java.util.List;

/**
 * 态度3
 */
public class Attitude3Action1 extends BaseBlurThemeTemplate {

    public Attitude3Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        notCoverWidgetsStartIndex = 1;
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
        //底部阴影
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //青色光效
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.TOP);
        //蓝色光效
        widgets[2] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);

        initRelative();
        //爱心emoji
        widgets[3] = relayWidget(widgets[1]);
        relative[3] = 1;

        //自信emoji
        widgets[4] = relayWidget(widgets[2]);
        relative[4] = 2;

    }


    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //底部阴影
                    {0f, 0.603125f, 1f, 1f},
                    //青色光效
                    {-0.7f, -0.75f, 1.2f, 1.2f},
                    //蓝色光效
                    {0.7f, 0.8f, 0.6f, 1.2f},
                    //爱心emoji
                    {0f, -0.05f, 2.5f, 2.5f},
                    //自信emoji
                    {0f, -0.05f, 2.5f, 2.5f},
            };
        }
        return widgetMeta;
    }



}
