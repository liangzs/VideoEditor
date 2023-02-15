package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.particle.RandomParticlesDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * 兄妹节5
 */
public class Rakshabandhan5Action5 extends BaseBlurThemeTemplate {


    public Rakshabandhan5Action5(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
    }

    @Override
    protected BaseEvaluate initStayAction() {
        StayScaleAnimation res = new StayScaleAnimation(DEFAULT_STAY_TIME, false, 1, 0.1f, 1.1f);
        res.setZView(0);
        return res;
    }



    @Override
    protected float initZView() {
        return 0;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        //顶部线条
        widgets[0] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);
        //底部线条
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[1][0], getWidgetsMeta()[1][1]);

    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //上下
                    {0f, -0.8f, 1f, 1f},
                    {0f, 0.8f, 1f, 1f},
            };
        }
        return widgetMeta;
    }

    float[][][] rotateWidgetMeta = new float[][][]{{
            //旋转圈圈
            {0f, -0.8f, 0f, 0.333f},
            {0f, 0.8f, 0f, 0.333f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }


    /**
     * 随机粒子
     */
    RandomParticlesDrawer randomParticlesDrawer;


    @Override
    public void drawWiget() {
        super.drawWiget();
        randomParticlesDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        List<Bitmap> bitmaps = new ArrayList<>(6);
        bitmaps.addAll(mimaps.subList(3, 9));
        randomParticlesDrawer =  (RandomParticlesDrawer) new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                .setListBitmaps(bitmaps)
                .setParticleCount(10)
                .setParticleSizeRatio(0.01f)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        randomParticlesDrawer.onDestroy();
    }




}
