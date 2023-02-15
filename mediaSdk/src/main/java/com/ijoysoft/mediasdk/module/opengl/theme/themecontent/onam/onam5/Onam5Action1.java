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
public class Onam5Action1 extends BaseBlurThemeTemplate {

    public Onam5Action1(int totalTime, int width, int height) {
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
        //链条
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.TOP, AnimateInfo.ORIENTATION.TOP);
        //花
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);
        //tag
        widgets[3] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        widgets[4] = relayWidget(widgets[3]);
        widgets[5] = relayWidget(widgets[3]);

        //两行文字依赖tag进行定位
        initRelative();
        relative[4] = 3;
        relative[5] = 3;
    }


    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f},
                    //链条
                    {0f, Float.MIN_VALUE, 1f, 1f},
                    //花
                    {0f, Float.MIN_VALUE, 4f, 8f},
                    //tag
                    {0f, Float.MAX_VALUE, 1f, 1f},


                    //相对定位的两行文字
                    {0f, 0f, 2.5f, 2.5f},
                    {0f, 0.15f, 3f, 3f},
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
