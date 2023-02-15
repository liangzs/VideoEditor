package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam5;

import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

/**
 * 欧南节5
 * TODO: 缺粒子
 */
public class Onam5Action4 extends BaseBlurThemeTemplate {


    public Onam5Action4(int totalTime, int width, int height) {
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
        //边框
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);//tag和文字
        widgets[1] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.BOTTOM, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[2] = relayWidget(widgets[1]);
        widgets[3] = relayWidget(widgets[1]);

        //两行文字依赖tag进行定位
        initRelative();
        relative[2] = 1;
        relative[3] = 1;
        //四个角落四束花
        widgets[4] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.TOP);
        widgets[5] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.TOP);
        widgets[6] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.LEFT, AnimateInfo.ORIENTATION.BOTTOM);
        widgets[7] = buildNewCoordinateTemplateAnimate(totalTime, AnimateInfo.ORIENTATION.RIGHT, AnimateInfo.ORIENTATION.BOTTOM);

    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //边框
                    {0f, 0f, 1f, 1f},
                    //tag和文字
                    {0f, Float.MAX_VALUE, 1.4f, 2.8f},
                    {0f, 0f, 2.5f, 2.5f},
                    {0f, 0.15f, 3f, 3f},
                    //四个角落四束花
                    {Float.MIN_VALUE, Float.MIN_VALUE, 1.25f, 2.5f},
                    {Float.MAX_VALUE, Float.MIN_VALUE, 1.25f, 2.5f},
                    {Float.MIN_VALUE, Float.MAX_VALUE, 1.25f, 2.5f},
                    {Float.MAX_VALUE, Float.MAX_VALUE, 1.25f, 2.5f},
            };
        }
        return widgetMeta;
    }

//
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
