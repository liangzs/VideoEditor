package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Collections;
import java.util.List;

/**
 * 欧南节3
 */
public class Onam3Action3 extends BaseBlurThemeTemplate {


    public Onam3Action3(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }


    @Override
    protected BaseEvaluate initEnterAnimation() {

        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(-2.5f, 0f, 0, 0).
                setOrientation(AnimateInfo.ORIENTATION.LEFT).setScale(1.1f, 1.1f));
    }

    @Override
    protected BaseEvaluate initOutAnimation() {

        return new EnterEvaluateEaseOut(new AnimationBuilder(DEFAULT_ENTER_TIME).setCoordinate(0, 0f, 0f, -2.5f).
                setOrientation(AnimateInfo.ORIENTATION.BOTTOM).setScale(1.1f, 1.1f));
    }


    @Override
    public void initWidget() {
        super.initWidget();
        //横幅x轴拉伸后恢复
        widgets[0] = new BaseThemeExample(totalTime, DEFAULT_ENTER_TIME, DEFAULT_STAY_TIME, DEFAULT_OUT_TIME, true);
        widgets[0].setEnterAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME)
                .setIsNoZaxis(true).setScale(0.0001f, 1f).setZView(0)
                .setOnlyScaleX(true)
                .build());
        widgets[0].getEnterAnimation().setIsSubAreaWidget(true, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);
        widgets[0].setOutAnimation(new AnimationBuilder(DEFAULT_ENTER_TIME).setIsNoZaxis(true).setFade(1f, 0f).build());
        widgets[0].setZView(0);
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        Bitmap particle = mimaps.get(mimaps.size() - 1);
        IParticleRender particleRender =  new ParticleBuilder(ParticleType.FALLING, Collections.singletonList(particle))
                .setParticleCount(8)
                .setPointSize(6, 10)
                .build();
        particleRender.onSurfaceCreated();
        particleRenders = Collections.singletonList(particleRender);
    }

    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    {0f, 0.8f, 1.09f, 2.18f},
            };
        }
        return widgetsMeta;
    }

    float[][][] rotateWidgetMeta = new float[][][]{{
            {0f, -1f, 0f, 0.8f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }


}
