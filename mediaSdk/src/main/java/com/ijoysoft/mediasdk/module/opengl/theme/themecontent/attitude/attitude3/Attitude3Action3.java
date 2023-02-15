package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude3;

import android.graphics.Bitmap;

import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseBlurThemeTemplate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.RepeatAroundFilter;
import com.ijoysoft.mediasdk.module.opengl.theme.action.stayaction.StayScaleAnimation;

import java.util.Collections;
import java.util.List;

/**
 * 态度3
 */
public class Attitude3Action3 extends BaseBlurThemeTemplate {


    public Attitude3Action3(int totalTime, int width, int height) {
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
        widgets[0] = buildNewFadeInOutTemplateAnimate(totalTime);
        //粉色效果
        widgets[1] = buildNewScaleInOutTemplateAnimateAtSelfCenter(1);
        //黄色效果
        widgets[2] = buildNewScaleInOutTemplateAnimateAtSelfCenter(2);
    }

    float[][] widgetMeta;

    @Override
    protected float[][] getWidgetsMeta() {
        if (widgetMeta == null) {
            widgetMeta = new float[][]{
                    //底部阴影
                    {0f, 0.603125f, 1f, 1f},
                    //粉色效果
                    {1f, -1f, 0.5f, 1f},
                    //黄色效果
                    {-1f, 0f, 0.6f, 1.2f}
            };
        }
        return widgetMeta;
    }



    /**
     * 图片环绕渲染
     */
    RepeatAroundFilter repeatAroundFilter;


    @Override
    public void init(Bitmap bitmap, Bitmap tempBit, List<Bitmap> mimaps, int width, int height) {
        super.init(bitmap, tempBit, mimaps, width, height);
        //频谱
        repeatAroundFilter = new RepeatAroundFilter();
        repeatAroundFilter.init(mimaps.get(3), width, height, AnimateInfo.ORIENTATION.BOTTOM);
        repeatAroundFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM);
    }

    @Override
    protected void drawAfterWidget() {
        repeatAroundFilter.draw();
    }

}
