package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam4;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 欧南节4
 */
public class Onam4Action4 extends BaseBlurThemeTemplate {


    public Onam4Action4(int totalTime, int width, int height) {
        super(totalTime, width, height, true,  true);
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
        //橘色花
        widgets[0] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[0][0], getWidgetsMeta()[0][1]);
        //文字
        widgets[1] = buildNewScaleInOutTemplateAnimate(totalTime, getWidgetsMeta()[1][0], getWidgetsMeta()[1][1]);
      }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //橘色花
                    {0f, 0.5f, 1.2f, 1.2f},
                    //文字
                    {0f, 0.5f, 2.5f, 2.5f},
            };
        }
        return widgetMeta;
    }


    /**
     * 烟花效果
     */
    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);

        IParticleRender particleRender = new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                .setListBitmaps(new ArrayList<>(mimaps.subList(mimaps.size()-3, mimaps.size())))
                .setParticleCount(6)
                //默认4f.setParticleSizeDelta(4f)
                .build();
        particleRender.onSurfaceCreated();
        particleRenders = Collections.singletonList(particleRender);
    }

}
