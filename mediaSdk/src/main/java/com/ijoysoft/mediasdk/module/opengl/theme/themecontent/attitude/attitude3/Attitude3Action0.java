package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.particle.StarDrawer;
import com.ijoysoft.mediasdk.module.opengl.particle.StarSystem;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 态度3
 */
public class Attitude3Action0 extends BaseBlurThemeTemplate {


    public Attitude3Action0(int totalTime, int width, int height) {
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
        //底部阴影
        widgets[0] = buildNewFadeInOutTemplateAnimate();
        //顶部蓝色效果
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        //标题
        widgets[2] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //底部阴影
                    {0f, 0.603125f, 1f, 1f, 1f},
                    //顶部蓝色效果
                    {0f, -1f, 0.5f, 1f, 1f},
                    //标题
                    {0f, 0.7f, 1.1f, 2.2f, 1.1f},
            };
        }
        return widgetMeta;
    }





    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        List<Bitmap> pSrc = new ArrayList<>(mimaps.subList(3, 6));
        //爱心闪光效果
        IParticleRender heart =
                new ParticleBuilder().setParticleType(ParticleType.STAR_HEART)
                        .setListBitmaps(pSrc)
                        .build();
        heart.onSurfaceCreated();

//        ((StarDrawer) heart).setOnSurfaceChangedListener(new StarDrawer.ChangeStarGraphListener() {
//            @Override
//            public void onRatioChange(int width, int height, StarSystem starSystem) {
//                if (width < height) {
//                    starSystem.setPositionParameter(0f, 0f, 1f);
//                } else if (height < width) {
//                    starSystem.setPositionParameter(0f, -0.4f, 0.6f);
//                } else {
//                    starSystem.setPositionParameter(0f, -0.4f, 0.6f);
//                }
//            }
//
//        });
        if (width < height) {
            ((StarDrawer) heart).getParticleSystem().setPositionParameter(0f, 0f, 1f);
        } else if (height < width) {
            ((StarDrawer) heart).getParticleSystem().setPositionParameter(0f, -0.3f, 0.8f);
        } else {
            ((StarDrawer) heart).getParticleSystem().setPositionParameter(0f, -0.3f, 0.8f);
        }


        //飞鸟效果
        IParticleRender bird = new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                .setListBitmaps(Collections.singletonList(mimaps.get(mimaps.size() - 1)))
                .setParticleCount(4)
                .setParticleSizeRatio(0.05f)
                .build();
        bird.onSurfaceCreated();



        particleRenders = Arrays.asList(heart, bird);
    }


//    @Override
//    public void onSizeChanged(int width, int height) {
//        super.onSizeChanged(width, height);
//        if (particleRenders != null) {
//            particleRenders.get(0).onSurfaceChanged(0, 0, width, height, width, height);
//        }
//    }


}
