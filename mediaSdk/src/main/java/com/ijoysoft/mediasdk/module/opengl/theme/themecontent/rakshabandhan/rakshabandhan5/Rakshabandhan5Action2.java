package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan5;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.particle.RandomFadeDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Collections;
import java.util.List;

/**
 * 兄妹节5
 */
public class Rakshabandhan5Action2 extends BaseBlurThemeTemplate {



    public Rakshabandhan5Action2(int totalTime, int width, int height) {
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
        //底部极光
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);

    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //顶部线条
                    {0f, -0.8f, 1f, 1f},
                    //底部极光
                    {0f, 0.657f, 1f, 1f},
            };
        }
        return widgetMeta;
    }

    float[][][] rotateWidgetMeta = new float[][][]{{
            //旋转圈圈
            {0f, -0.8f, 0f, 0.333f},
    }};

    @Override
    protected float[][][] getRotateWidgetsMeta() {
        return rotateWidgetMeta;
    }




    @Override
    public void drawFramePreview() {
        super.drawFramePreview();
        for (BaseThemeExample widget : widgets) {
            widget.drawFramePreview();
        }
    }


    /**
     * 烟花效果
     */
    RandomFadeDrawer randomFadeDrawer;



    @Override
    public void drawWiget() {
        super.drawWiget();
        randomFadeDrawer.onDrawFrame();
    }

    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        randomFadeDrawer = (RandomFadeDrawer) new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                .setListBitmaps(Collections.singletonList(mimaps.get(mimaps.size() - 1)))
                .setParticleCount(4)
                .setParticleSizeRatio(0.05f)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        randomFadeDrawer.onDestroy();
    }
}
