package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.particle.StarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 态度4
 */
public class Attitude4Action3 extends BaseBlurThemeTemplate {
    public Attitude4Action3(int totalTime, int width, int height) {
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
        //爱心闪光效果
        IParticleRender heart =
                new ParticleBuilder().setParticleType(ParticleType.STAR_HEART)
                        .setListBitmaps(pSrc)
                        .build();
        heart.onSurfaceCreated();


        if (width < height) {
            ((StarDrawer) heart).getParticleSystem().setPositionParameter(0f, 0f, 1f);
        } else {
            ((StarDrawer) heart).getParticleSystem().setPositionParameter(0f, -0.3f, 0.7f);
        }

        //飘动爱心效果
        IParticleRender bird = new ParticleBuilder().setParticleType(ParticleType.MOVING)
                .setListBitmaps(new ArrayList<>(mimaps.subList(6, 9)))
                .setParticleCount(16)
                .setParticleSizeRatio(0.02f)
                .setPositionUpdater((particle, offset, length) -> {
                    //往上飘
                    particle[offset + 1] -= 0.01f;
                })
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
                    {0f, Float.MAX_VALUE, 1.4f, 2.8f, 1.4f},
            };
        }
        return widgetsMeta;
    }

    @Override
    protected void fixNotCoverWidgetsPositionManually(int i) {
        if (i == 1) {
            widgets[1].setVertex(transitionRatioPosArray(widgets[1].getCube(), 0f, 0.1f));
        }
    }

}
