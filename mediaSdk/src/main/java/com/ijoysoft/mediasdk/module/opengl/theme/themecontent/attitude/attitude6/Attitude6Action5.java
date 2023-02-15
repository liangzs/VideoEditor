package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude6;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.particle.StarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 态度6
 */
public class Attitude6Action5 extends BaseBlurThemeTemplate {


    public Attitude6Action5(int totalTime, int width, int height) {
        super(totalTime, width, height, true, true);
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
        widgets[0] = buildNewFadeInOutTemplateAnimate();
        widgets[1] = buildNewCoordinateTemplateAnimate(AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
    }

    /**
     * 空间元数据
     */
    float[][] widgetsMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetsMeta == null) {
            widgetsMeta = new float[][]{
                    //蓝色效果
                    {0.75f, 0.75f, 0.3f, 0.6f, 0.6f},
                    //标题
                    {0f, 0.75f, 1f, 2f, 1f},
            };
        }
        return widgetsMeta;
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
        } else if (height < width) {
            ((StarDrawer) heart).getParticleSystem().setPositionParameter(0f, -0.2f, 0.8f);
        } else {
            ((StarDrawer) heart).getParticleSystem().setPositionParameter(0f, -0.1f, 0.9f);
        }
        particleRenders = Collections.singletonList(heart);
    }


}
