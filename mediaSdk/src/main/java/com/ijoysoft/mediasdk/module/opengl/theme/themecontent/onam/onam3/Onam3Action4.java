package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDefaultDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EnterEvaluateEaseOut;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 欧南节1
 */
public class Onam3Action4 extends BaseBlurThemeTemplate {


    public Onam3Action4(int totalTime, int width, int height) {
        super(totalTime, width, height,  true);
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
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);
        //素材位置信息元数据
        float[][] widgetsMeta = getWidgetsMeta();
        Bitmap circle = mimaps.get(0);
        for (int i = 0; i < 7; i++) {
            widgets[i].init(circle, width, height);
            if (width < height) {
                widgets[i].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                        widgetsMeta[i][0],
                        widgetsMeta[i][1],//(float) (widgetsMeta[i][1] * (((double)height/width) / SOURCE_RATIO)),
                        widgetsMeta[i][2], widgetsMeta[i][2]);
            } else {
                widgets[i].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                        widgetsMeta[i][0],
                        widgetsMeta[i][1],
                        widgetsMeta[i][3], widgetsMeta[i][3]);
            }
        }
        circle = mimaps.get(1);
        for (int i = 7; i < 14; i++) {
            widgets[i].init(circle, width, height);
            if (width < height) {
                widgets[i].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                        widgetsMeta[i][0],
                        widgetsMeta[i][1],//(float) (widgetsMeta[i][1] * (((double)height/width) / SOURCE_RATIO)),
                        widgetsMeta[i][2], widgetsMeta[i][2]);
            } else {
                widgets[i].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                        widgetsMeta[i][0],
                        widgetsMeta[i][1],
                        widgetsMeta[i][3], widgetsMeta[i][3]);
            }
        }

        ParticleDefaultDrawer particleDefaultDrawer = (ParticleDefaultDrawer)
                new ParticleBuilder(ParticleType.FALLING,
                        Arrays.asList(
                                mimaps.get(2), mimaps.get(3)
                        )
                        )
                        .setParticleCount(8)
                        .setPointSize(6, 10)
                        .build();
        particleDefaultDrawer.onSurfaceCreated();
        particleRenders = Collections.singletonList(particleDefaultDrawer);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        for (int i = 0; i < 7; i++) {
            widgets[i] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.LEFT);
        }
        for (int i = 7; i < 14; i++) {
            widgets[i] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.RIGHT);
        }
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[14][4];
            float base = -1f + (1f/7f);
            float delta = 2f/7f;
            float y = 0.95f;
            for (int i = 0; i < 7; i++) {
                widgetMeta[i][0] = base + delta * i;
                widgetMeta[i][1] = y;
                widgetMeta[i][2] = 7f;
                widgetMeta[i][3] = 7f;
            }
            y = -y;
            for (int i = 7; i < 14; i++) {
                widgetMeta[i][0] = widgetMeta[i-7][0];
                widgetMeta[i][1] = y;
                widgetMeta[i][2] = 7f;
                widgetMeta[i][3] = 7f;
            }
        }
        return widgetMeta;
    }







}
