package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude3;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.particle.StarDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.RepeatAroundFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 态度3
 */
public class Attitude3Action4 extends BaseBlurThemeTemplate {


    public Attitude3Action4(int totalTime, int width, int height) {
        super(totalTime, width, height, true,  true);
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
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //粉色效果
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        //蓝色效果
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);
        //标题
        widgets[3] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //底部阴影
                    {0f, 0.603125f, 1f, 1f, 1f},
                    //粉色效果
                    {-1f, -0.5f, 0.6f, 1.2f, 1.2f},
                    //蓝色效果
                    {1f, 0.25f, 0.5f, 1f, 1f},
                    //标题
                    {0f, 0.65f, 1.4f, 2.8f, 1.3f},
            };
        }
        return widgetMeta;
    }


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        List<Bitmap> pSrc = new ArrayList<>(mimaps.subList(4, 7));
        //爱心闪光效果
        IParticleRender heart =
                new ParticleBuilder().setParticleType(ParticleType.STAR_HEART)
                        .setListBitmaps(pSrc)
                        .build();
        heart.onSurfaceCreated();

        //图案定位
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


}
