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

import java.util.Collections;
import java.util.List;

/**
 * 欧南节3
 */
public class Onam3Action1 extends BaseBlurThemeTemplate {

    public Onam3Action1(int totalTime, int width, int height) {
        super(totalTime, width, height, true);
        notCoverWidgetsStartIndex = 1;
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




    /**
     * 动画设置
     */
    @Override
    public void initWidget() {
        super.initWidget();
        //四朵花
        for (int i = 0; i < 5; i++) {
            widgets[i] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[i][0], getWidgetsMeta()[i][1]);
        }
        widgets[5] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        init(bitmap, width, height);


        //素材位置信息元数据
        final float[][] widgetsMeta = getWidgetsMeta();
        Bitmap circle = mimaps.get(0);
        for (int i = 0; i < 5; i++) {
            widgets[i].init(circle, width, height);
            if (width < height) {
                widgets[i].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                        widgetsMeta[i][0],
                        widgetsMeta[i][1],
                        widgetsMeta[i][2], widgetsMeta[i][2]);
            } else {
                widgets[i].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                        widgetsMeta[i][0],
                        widgetsMeta[i][1],
                        widgetsMeta[i][3], widgetsMeta[i][3]);
            }
        }

        circle = mimaps.get(1);
        widgets[5].init(circle, width, height);
        if (width < height) {
            widgets[5].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                    widgetsMeta[5][0],
                    widgetsMeta[5][1],
                    widgetsMeta[5][2], widgetsMeta[5][2]);
        } else {
            widgets[5].adjustScaling(width, height, circle.getWidth(), circle.getHeight(),
                    widgetsMeta[5][0],
                    widgetsMeta[5][1],
                    widgetsMeta[5][3], widgetsMeta[5][3]);
        }

        circle = mimaps.get(2);
        ParticleDefaultDrawer particleDefaultDrawer = (ParticleDefaultDrawer)
                new ParticleBuilder(ParticleType.FALLING, Collections.singletonList(circle))
                .setParticleCount(8)
                .setPointSize(6, 10)
                        .build();
        particleDefaultDrawer.onSurfaceCreated();
        particleRenders = Collections.singletonList(particleDefaultDrawer);
    }


    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //顶中间花
                    {0f, -0.8f, 3.6f, 3.6f},
                    //两边花
                    {-0.85f, -0.8f, 5f, 5f},
                    {0.85f, -0.8f, 5f, 5f},
                    {-0.45f, -0.8f, 5f, 5f},
                    {0.45f, -0.8f, 5f, 5f},
                    //标题
                    {0f, 0.6f, 1f, 2f},
            };
        }
        return widgetsMeta;
    }

}
