package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam5;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节5
 * TODO: 缺粒子
 */
public class Onam5Action2 extends BaseBlurThemeTemplate {



    public Onam5Action2(int totalTime, int width, int height) {
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
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //花
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f},
                    //花
                    {0f, Float.MAX_VALUE, 0.8f, 1.6f},
            };
        }
        return widgetMeta;
    }


//    /**
//     * 烟花效果
//     */
//    @Override
//    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
//        super.init(bitmap, tempBit, mimaps, width, height);
//
//        IParticleRender particleRender = new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
//                .setListBitmaps(new ArrayList<>(mimaps.subList(mimaps.size()-3, mimaps.size())))
//                .setParticleCount(6)
//                //默认4f.setParticleSizeDelta(4f)
//                .build();
//        particleRender.onSurfaceCreated();
//        particleRenders = Collections.singletonList(particleRender);
//    }
}
