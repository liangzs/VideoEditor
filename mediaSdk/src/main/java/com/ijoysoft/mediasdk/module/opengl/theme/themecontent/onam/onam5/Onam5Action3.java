package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Collections;
import java.util.List;

/**
 * 欧南节5
 */
public class Onam5Action3 extends BaseBlurThemeTemplate {


    public Onam5Action3(int totalTime, int width, int height) {
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
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //花
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[2] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.BOTTOM);
        //文字
        widgets[3] = buildNewScaleInOutTemplateAnimateAtSelfCenter(3);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f},
                    //两边的花
                    {Float.MIN_VALUE, Float.MIN_VALUE, 0.5f, 1f},
                    {Float.MAX_VALUE, Float.MIN_VALUE, 0.5f, 1f},
                    //文字
                    {0f, 0.75f, 1.8f, 3.6f},
            };
        }
        return widgetMeta;
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //飘落效果
        IParticleRender particleRender =
                new ParticleBuilder(ParticleType.FALLING, Collections.singletonList(mimaps.get(mimaps.size() - 1)))
                        .setParticleCount(8)
                        .setPointSize(6, 10)
                        .build();
        particleRender.onSurfaceCreated();
        particleRenders = Collections.singletonList(particleRender);
    }




}
