package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 态度4
 */
public class Attitude4Action0 extends BaseBlurThemeTemplate {
    public Attitude4Action0(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
        setCoverWidgetsCount(1, 1);
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
        widgets[0] = buildNoneAnimationWidget();
        //标题
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
    }





    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        List<Bitmap> pSrc = new ArrayList<>(mimaps.subList(2, 6));
        //粒子效果
        IParticleRender heart =
                new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(pSrc)
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.02f)
                        .build();
        heart.onSurfaceCreated();
        //飞鸟效果
        IParticleRender bird = new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                .setListBitmaps(Collections.singletonList(mimaps.get(mimaps.size() - 1)))
                .setParticleCount(4)
                .setParticleSizeRatio(0.05f)
                .build();
        bird.onSurfaceCreated();



        particleRenders = Arrays.asList(heart, bird);
    }


    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //边框
                    {0f, 0, 1f, 1f, 1f},
                    //标题
                    {0f, Float.MAX_VALUE, 1.2f, 2.4f, 1.2f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 1) {
            widgets[1].setVertex(transitionRatioPosArray(widgets[1].getCube(), 0f, -0.1f));
        }
    }
}
